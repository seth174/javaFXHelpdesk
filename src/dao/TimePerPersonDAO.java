package dao;

import models.Person;
import models.Ticket;
import models.TimePerPerson;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TimePerPersonDAO {
    private Connection conn;
    private DatabaseManager dbm;
    private Map<Integer, TimePerPerson> cache;

    public TimePerPersonDAO(Connection conn, DatabaseManager dbm) {
        this.conn = conn;
        this.dbm = dbm;
        this.cache = new HashMap<>();
    }

    public static void create(Connection conn) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("create table timePerPerson(");
        sb.append("  timePerPersonid integer not null,");
        sb.append("  ticketID integer not null,");
        sb.append("  employeeID integer not null,");
        sb.append("  time DOUBLE not null,");
        sb.append("  entryDate Date not null,");
        sb.append("  primary key (timePerPersonid),");
        sb.append("  Foreign key (employeeID) references Person,");
        sb.append("  Foreign key (ticketID) references Ticket");
        sb.append(")");

        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sb.toString());
    }

    public Collection<TimePerPerson> getTimePerPerson(int employeeID) {
        try {
            Collection<TimePerPerson> timePerPerson = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            sb.append("select tpp.timePerPersonid");
            sb.append("  from timePerPerson tpp");
            sb.append("  where tpp.employeeID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, employeeID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int timePerPersonid = rs.getInt("timePerPersonid");
                timePerPerson.add(find(timePerPersonid));
            }
            rs.close();

            return timePerPerson;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error getting time per person", e);
        }
    }

    public TimePerPerson find(int timePerPersonID) {
        if (cache.containsKey(timePerPersonID)) {
            return cache.get(timePerPersonID);
        }

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select tpp.ticketID, tpp.employeeID, tpp.time, tpp.entryDate");
            sb.append("  from timePerPerson tpp");
            sb.append("  where tpp.timePerPersonid = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, timePerPersonID);
            ResultSet rs = pstmt.executeQuery();

            // return null if Time per person doesn't exist
            if (!rs.next())
                return null;

            int ticketID = rs.getInt("ticketID");
            int employeeID = rs.getInt("employeeID");
            double time = rs.getDouble("time");
            Date date = rs.getDate("entryDate");

            rs.close();

            Person person = dbm.findPersonByID(employeeID);
            Ticket ticket = dbm.findTicketByID(ticketID);

            TimePerPerson timePerPerson = new TimePerPerson(this, timePerPersonID, person, ticket, time, date);
            cache.put(timePerPersonID, timePerPerson);

            return timePerPerson;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding time per person", e);
        }
    }

    public Collection<TimePerPerson> getTimePerPersonTicket(int ticketID)
    {
        try {
            Collection<TimePerPerson> timePerPerson = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            sb.append("select tpp.timePerPersonid");
            sb.append("  from timePerPerson tpp");
            sb.append("  where tpp.ticketID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, ticketID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int timePerPersonid = rs.getInt("timePerPersonid");
                timePerPerson.add(find(timePerPersonid));
            }
            rs.close();

            return timePerPerson;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error getting time per person Ticket", e);
        }
    }

    public TimePerPerson insert(Ticket ticket, Person person, double time, Date date) {
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("insert into timePerPerson(timePerPersonid, ticketID, employeeID, time, entryDate)");
            sb.append("  values (?, ?, ?, ?, ?)");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            int timePerPersonID= getNewID();
            pstmt.setInt(1, timePerPersonID);
            pstmt.setInt(2, ticket.getTicketID());
            pstmt.setInt(3, person.getEmployeeID());
            pstmt.setDouble(4, time);
            pstmt.setDate(5, date);
            pstmt.executeUpdate();

            TimePerPerson timePerPerson = new TimePerPerson(this, timePerPersonID, person, ticket, time, date);
            cache.put(timePerPersonID, timePerPerson);

            // Tell the Dept that it will have to recalculate its majors list
            person.invalidateTimePerPerson();
            ticket.invalidateTimePerPerson();

            return timePerPerson;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error inserting new time per person", e);
        }
    }

    public void updateTime(int timePerPersonid, double time, Person person, Ticket ticket) {
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("update timePerPerson");
            sb.append("  set time = ?");
            sb.append("  where  timePerPersonid = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setDouble(1, time);
            pstmt.setInt(2, timePerPersonid);
            pstmt.executeUpdate();

            invalidateCache();
            person.invalidateTimePerPerson();
            ticket.invalidateTimePerPerson();

        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error updating new time per person", e);
        }
    }

    public TimePerPerson findTimePerPerson(Date date, Ticket ticket, Person person)
    {
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("select tpp.timePerPersonid");
            sb.append("  from timePerPerson tpp");
            sb.append("  where tpp.ticketID = ? and tpp.employeeID= ? and tpp.entryDate = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, ticket.getTicketID());
            pstmt.setInt(2, person.getEmployeeID());
            pstmt.setDate(3, date);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next())
                return null;

            int timePerPersonID = rs.getInt("timePerPersonid");

            return find(timePerPersonID);
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error getting time per person per Ticket", e);
        }
    }

    private int getNewID()
    {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select max(t.timePerPersonid) as id");
            sb.append("  from timePerPerson t");
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

    public void invalidateCache()
    {
        cache.clear();
    }

}
