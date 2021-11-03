package dao;

import models.Person;
import models.Ticket;
import models.TicketPerPerson;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TicketPerPersonDAO {
    private Connection conn;
    private DatabaseManager dbm;
    private Map<Integer, TicketPerPerson> cache;

    public TicketPerPersonDAO(Connection conn, DatabaseManager dbm) {
        this.conn = conn;
        this.dbm = dbm;
        this.cache = new HashMap<>();
    }

    public static void create(Connection conn) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("create table ticketPerPerson(");
        sb.append("  ticketID integer not null,");
        sb.append("  ticketPerPersonID integer not null,");
        sb.append("  employeeID integer not null,");
        sb.append("  Subscribed integer not null,");
        sb.append("  primary key (ticketPerPersonID),");
        sb.append("  Foreign key (employeeID) references Person,");
        sb.append("  Foreign key (ticketID) references Ticket");
        sb.append(")");

        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sb.toString());
    }

    public Collection<TicketPerPerson> getTicketPerPerson(int employeeID) {
        try {
            Collection<TicketPerPerson> ticketPerPerson = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            sb.append("select tp.ticketID, tp.ticketPerPersonID");
            sb.append("  from ticketPerPerson tp");
            sb.append("  where tp.employeeID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, employeeID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int ticketPerPersonID = rs.getInt("ticketPerPersonID");
                ticketPerPerson.add(find(ticketPerPersonID));
            }
            rs.close();

            return ticketPerPerson;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error getting ticket per person", e);
        }
    }

    public TicketPerPerson find(int ticketPerPersonID) {
        if (cache.containsKey(ticketPerPersonID)) {
            return cache.get(ticketPerPersonID);
        }

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select tp.ticketID, tp.employeeID, t.Subscribed");
            sb.append("  from ticketPerPerson tp");
            sb.append("  where tp.ticketPerPersonID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, ticketPerPersonID);
            ResultSet rs = pstmt.executeQuery();

            // return null if student doesn't exist
            if (!rs.next())
                return null;

            int ticketID = rs.getInt("ticketID");
            int employeeID = rs.getInt("employeeID");
            int subscribedNum = rs.getInt("Subscribed");
            rs.close();

            boolean subscribed = subscribedNum == 1;
            Ticket ticket = dbm.findTicketByID(ticketID);
            Person person = dbm.findPersonByID(employeeID);

            TicketPerPerson ticketPerPerson = new TicketPerPerson(this, ticketPerPersonID, ticket, person, subscribed);
            cache.put(ticketPerPersonID, ticketPerPerson);

            return ticketPerPerson;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding one ticketPerPerson", e);
        }
    }

    public TicketPerPerson find(Ticket ticket, Person person) {

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select tp.ticketPerPersonID");
            sb.append("  from ticketPerPerson tp");
            sb.append("  where tp.ticketID = ? AND tp.employeeID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, ticket.getTicketID());
            pstmt.setInt(2, person.getEmployeeID());
            ResultSet rs = pstmt.executeQuery();

            // return null if student doesn't exist
            if (!rs.next())
                return null;

            int ticketPerPersonID = rs.getInt("ticketPerPersonID");
            rs.close();

            return find(ticketPerPersonID);
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding one ticketPerPerson", e);
        }
    }

    public Collection<TicketPerPerson> getTicketPerPersonTicket(int ticketID)
    {
        try {
            Collection<TicketPerPerson> ticketPerPerson = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            sb.append("select tp.ticketPerPersonID");
            sb.append("  from ticketPerPerson tp");
            sb.append("  where tp.ticketID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, ticketID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int ticketPerPersonID = rs.getInt("ticketPerPersonID");
                ticketPerPerson.add(find(ticketPerPersonID));
            }
            rs.close();

            return ticketPerPerson;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error getting ticket per person", e);
        }
    }

    public TicketPerPerson insert(Ticket ticket, Person person) {
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("insert into ticketPerPerson(ticketID, ticketPerPersonID, employeeID, Subscribed)");
            sb.append("  values (?, ?, ?, ?)");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            int ticketPerPersonID = getNewTicketPerPersonID();
            pstmt.setInt(1, ticket.getTicketID());
            pstmt.setInt(2, ticketPerPersonID);
            pstmt.setInt(3, person.getEmployeeID());
            pstmt.setInt(4, 1);
            pstmt.executeUpdate();

            TicketPerPerson tpr = new TicketPerPerson(this, ticketPerPersonID, ticket, person, true);
            cache.put(ticketPerPersonID, tpr);

            // Tell the Dept that it will have to recalculate its majors list
            ticket.invalidateTicketPerPerson();
            person.invalidateTicketsPerPerson();

            return tpr;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error inserting new ticketPerPerson", e);
        }
    }

    public void toggleTicketSubscription(Person person, Ticket ticket) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("update ticketPerPerson");
            sb.append("  set Subscribed = ?");
            sb.append("  where ticketPerPersonID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            TicketPerPerson ticketPerPerson = find(ticket, person);
            if (ticketPerPerson == null) {
                return;
            }
            if(ticketPerPerson.getSubscribed())
            {
                pstmt.setInt(1, 0);
            }
            else
            {
                pstmt.setInt(1, 1);
            }

            pstmt.setInt(2, ticketPerPerson.getTicketPerPersonID());
            pstmt.executeUpdate();

            person.invalidateTicketsPerPerson();
            ticket.invalidateTicketPerPerson();
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error changing ticketsPerPerson", e);
        }
    }

    private int getNewTicketPerPersonID()
    {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select max(t.ticketPerPersonID) as id");
            sb.append("  from ticketPerPerson t");
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

}
