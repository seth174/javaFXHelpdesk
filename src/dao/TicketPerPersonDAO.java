package dao;

import models.Organization;
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
            sb.append("select tp.ticketID, tp.employeeID");
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
            rs.close();

            Ticket ticket = dbm.findTicketByID(ticketID);
            Person person = dbm.findPersonByID(employeeID);

            TicketPerPerson ticketPerPerson = new TicketPerPerson(this, ticketPerPersonID, ticket, person);
            cache.put(ticketPerPersonID, ticketPerPerson);

            return ticketPerPerson;
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


}
