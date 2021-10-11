package dao;

import models.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PersonDAO {
    private Connection conn;
    private DatabaseManager dbm;
    private Map<Integer, Person> cache;

    public PersonDAO(Connection conn, DatabaseManager dbm) {
        this.conn = conn;
        this.dbm = dbm;
        this.cache = new HashMap<>();
    }

    public static void create(Connection conn) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("create table Person(");
        sb.append("  firstName varchar(255) not null,");
        sb.append("  lastName varchar(255) not null,");
        sb.append("  email varchar(255) not null,");
        sb.append("  password varchar(255) not null,");
        sb.append("  phoneNumber integer not null,");
        sb.append("  organizationID integer not null,");
        sb.append("  employeeID integer not null,");
        sb.append("  level integer not null,");
        sb.append("  primary key (employeeID),");
        sb.append("  Foreign key (organizationID) references organization");
        sb.append(")");

        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sb.toString());
    }

    public Collection<Person> getPeopleByOrganizationID(int organizationID)
    {
        try {
            Collection<Person> people = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            sb.append("select p.employeeID");
            sb.append("  from Person p");
            sb.append("  where p.organizationID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, organizationID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int employeeID = rs.getInt("employeeID");
                people.add(find(employeeID));
            }
            rs.close();

            return people;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error getting people by organization", e);
        }
    }

    public Person find(int employeeID) {
        if (cache.containsKey(employeeID)) {
            return cache.get(employeeID);
        }

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select p.firstName, p.lastName, p.email, p.password, p.phoneNumber, " +
                    "p.organizationID, p.level");
            sb.append("  from Person p");
            sb.append("  where p.employeeID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, employeeID);
            ResultSet rs = pstmt.executeQuery();

            // return null if course doesn't exist
            if (!rs.next())
                return null;

            String firstName = rs.getString("firstName");
            String lastName = rs.getString("lastName");
            String email = rs.getString("email");
            String password = rs.getString("password");
            int phoneNumber = rs.getInt("phoneNumber");
            int organizationID = rs.getInt("organizationID");
            int level = rs.getInt("level");
            rs.close();

            Organization organization = dbm.findOrganization(organizationID);

            Person person = new Person(this, firstName, lastName, email, password, phoneNumber, organization, employeeID, level);
            cache.put(employeeID, person);

            return person;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding person", e);
        }
    }

    public Person findByEmail(String email) {

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select p.employeeID");
            sb.append("  from Person p");
            sb.append("  where p.email = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            // return null if person doesn't exist
            if (!rs.next())
                return null;

            int employeeID = rs.getInt("employeeID");
            rs.close();

            return find(employeeID);
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding person by email", e);
        }
    }

    public Person insert(String firstName, String lastName, String email, String password, int phoneNumber,
                         Organization organization, int level) {
        try {
            // make sure that the email is currently unused
            if (findByEmail(email) != null) {
                return null;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("insert into Person(firstName, lastName, email, password, phoneNumber, organizationID, " +
                    "employeeID, level)");
            sb.append("  values (?, ?, ?, ?, ?, ?, ?, ?)");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.setInt(5, phoneNumber);
            pstmt.setInt(6, organization.getOrganizationID());
            int employeeID = getNewEmployeeID();
            pstmt.setInt(7, employeeID);
            pstmt.setInt(8, level);
            pstmt.executeUpdate();

            Person person = new Person(this, firstName, lastName, email, password, phoneNumber, organization,
                    employeeID, level);
            cache.put(employeeID, person);

            organization.invalidatePeople();

            return person;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error inserting new person", e);
        }
    }

    public Collection<TicketPerPerson> getTicketPerPerson(int employeeID)
    {
        return dbm.getTicketPerPerson(employeeID);
    }

    public Collection<QueuePerPerson> getQueuePerPerson(int employeeID)
    {
        return dbm.getQueuePerPerson(employeeID);
    }

    public Collection<TimePerPerson> getTimePerPerson(int employeeID)
    {
        return dbm.getTimePerPerson(employeeID);
    }

    public Collection<Message> getMessages(int employeeID)
    {
        return dbm.getMessages(employeeID);
    }

    private int getNewEmployeeID()
    {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select max(p.employeeID) as id");
            sb.append("  from Person p");
            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            ResultSet rs = pstmt.executeQuery();

            //No people exist
            if (!rs.next())
                return 1;
            int max = rs.getInt("id");
            return max + 1;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding max employee id", e);
        }
    }

}
