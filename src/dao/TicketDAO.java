package dao;

import models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TicketDAO {
    private Connection conn;
    private DatabaseManager dbm;
    private Map<Integer, Ticket> cache;

    public TicketDAO(Connection conn, DatabaseManager dbm) {
        this.conn = conn;
        this.dbm = dbm;
        this.cache = new HashMap<>();
    }

    public static void create(Connection conn) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("create table ticket(");
        sb.append("  ticketID integer not null,");
        sb.append("  ticketTitle varchar(255) not null,");
        sb.append("  ticketDescription varchar (4000) not null,");
        sb.append("  ticketPriority integer not null,");
        sb.append("  ticketStatus integer not null,");
        sb.append("  dateCreated Date not null,");
        sb.append("  dateClosed Date,");
        sb.append("  EmployeeIDClosed integer,");
        sb.append("  EmployeeIDCreated integer not null,");

        sb.append("  primary key (ticketID),");
        sb.append("  Foreign key (EmployeeIDClosed) references person,");
        sb.append("  Foreign key (EmployeeIDCreated) references person,");
        sb.append("  Foreign key (ticketStatus) references ticketStatus,");
        sb.append("  Foreign key (ticketPriority) references ticketPriority");
        sb.append(")");

        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sb.toString());
    }

    public Ticket find(int ticketID)
    {
        if (cache.containsKey(ticketID)) {
            return cache.get(ticketID);
        }

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select t.ticketTitle, t.ticketDescription, t.ticketPriority, t.ticketStatus, t.dateCreated, " +
                    "t.dateClosed, t.EmployeeIDClosed, t.EmployeeIDCreated");
            sb.append("  from ticket T");
            sb.append("  where T.ticketID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, ticketID);
            ResultSet rs = pstmt.executeQuery();

            // return null if ticket doesn't exist
            if (!rs.next())
                return null;

            String ticketTitle = rs.getString("ticketTitle");
            String ticketDescription = rs.getString("ticketDescription");
            int ticketPriorityNumber = rs.getInt("ticketPriority");
            int ticketStatusID = rs.getInt("ticketStatus");
            Date dateCreated = rs.getDate("dateCreated");
            Date dateClosed = rs.getDate("dateClosed");
            int employeeIDClosed = rs.getInt("EmployeeIDClosed");
            int employeeIDCreated = rs.getInt("EmployeeIDCreated");
            rs.close();

            Person employeeCreated = dbm.findPersonByID(employeeIDCreated);
            Person employeeClosed = dbm.findPersonByID(employeeIDClosed);
            TicketPriority ticketPriority = dbm.findTicketPriorityByID(ticketPriorityNumber);
            TicketStatus ticketStatus = dbm.findTicketStatusByID(ticketStatusID);


            Ticket ticket = new Ticket(this, ticketID, ticketTitle, ticketDescription, ticketPriority,
                    ticketStatus, dateCreated, dateClosed, employeeCreated, employeeClosed);
            cache.put(ticketID, ticket);

            return ticket;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding ticket", e);
        }
    }

    public Ticket insert(String ticketTitle, String ticketDescription, TicketPriority ticketPriority,
                         TicketStatus ticketStatus, Person personCreated) {
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("insert into ticket(ticketID, ticketTitle, ticketDescription, ticketPriority, ticketStatus, " +
                    "dateCreated, dateClosed, EmployeeIDClosed, EmployeeIDCreated)");
            sb.append("  values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            int ticketID = getNewTicketID();
            pstmt.setInt(1, ticketID);
            pstmt.setString(2, ticketTitle);
            pstmt.setString(3, ticketDescription);
            pstmt.setInt(4, ticketPriority.getId());
            pstmt.setInt(5, ticketStatus.getTicketStatusID());
            java.sql.Date date= new java.sql.Date(System.currentTimeMillis());
            pstmt.setDate(6, date);
            pstmt.setNull(7, Types.DATE);
            pstmt.setNull(8, Types.INTEGER);
            pstmt.setInt(9, personCreated.getEmployeeID());
            pstmt.executeUpdate();

            Ticket ticket = new Ticket(this, ticketID, ticketTitle, ticketDescription, ticketPriority,
                    ticketStatus, date, null, personCreated, null);
            cache.put(ticketID, ticket);

            // Tell everyone that it will have to recalculate its list of tickets
            ticketPriority.invalidate();
            ticketStatus.invalidate();
            personCreated.invalidateTicketsPerPerson();

            return ticket;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error inserting new ticket", e);
        }
    }

    public Collection<Ticket> getTicketsInQueue(int queueID)
    {
        try {
            Collection<Ticket> ticketsInQueue = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            sb.append("select t.ticketID");
            sb.append("  from ticket t");
            sb.append("  where t.queueID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, queueID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int ticketID = rs.getInt("ticketID");
                ticketsInQueue.add(find(ticketID));
            }
            rs.close();

            return ticketsInQueue;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error getting ticketsInQueue", e);
        }
    }

    public Collection<Ticket> getTicketsByPriority(int ticketPriorityID)
    {
        try {
            Collection<Ticket> ticketsByPriority = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            sb.append("select t.ticketID");
            sb.append("  from ticket t");
            sb.append("  where t.ticketPriority = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, ticketPriorityID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int ticketID = rs.getInt("ticketID");
                ticketsByPriority.add(find(ticketID));
            }
            rs.close();

            return ticketsByPriority;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error getting tickets by priority", e);
        }
    }

    public Collection<Ticket> getTicketsByStatus(int ticketStatusID)
    {
        try {
            Collection<Ticket> ticketsByStatus = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            sb.append("select t.ticketID");
            sb.append("  from ticket t");
            sb.append("  where t.ticketStatus = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, ticketStatusID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int ticketID = rs.getInt("ticketID");
                ticketsByStatus.add(find(ticketID));
            }
            rs.close();

            return ticketsByStatus;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error getting tickets by status", e);
        }
    }

    public Collection<Ticket> getOrganizationTickets(int organizationID)
    {
        try {
            Collection<Ticket> organizationTickets = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            sb.append("select t.ticketID");
            sb.append("  from ticket t");
            sb.append("  where t.organizationID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, organizationID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int ticketID = rs.getInt("ticketID");
                organizationTickets.add(find(ticketID));
            }
            rs.close();

            return organizationTickets;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error getting tickets by organization", e);
        }
    }

    public Collection<Message> getMessagesPerTicket(int ticketID)
    {
        return dbm.getMessagesPerTicket(ticketID);
    }

    public Collection<TicketPerPerson> getTicketPerPersonTickets(int ticketID)
    {
        return dbm.getTicketPerPersonTicket(ticketID);
    }

    public Collection<TimePerPerson> getTimePerPersonTicket(int ticketID)
    {
        return dbm.getTimePerPersonTicket(ticketID);
    }

    private int getNewTicketID()
    {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select max(t.ticketID) as id");
            sb.append("  from ticket t");
            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            ResultSet rs = pstmt.executeQuery();

            //No ticket exist
            if (!rs.next())
                return 1;
            int max = rs.getInt("id");
            return max + 1;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding max ticket id", e);
        }
    }

    public Collection<TicketsPerQueue> getTicketsPerQueue(int ticketID)
    {
        return dbm.getTicketsPerQueueTickets(ticketID);
    }
}
