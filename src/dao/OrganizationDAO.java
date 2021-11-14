package dao;

import models.Organization;
import models.Person;
import models.Queue;
import models.Ticket;

import java.sql.*;
import java.util.*;


public class OrganizationDAO {
    private Connection conn;
    private DatabaseManager dbm;
    private Map<Integer, Organization> cache;

    public OrganizationDAO(Connection conn, DatabaseManager dbm) {
        this.conn = conn;
        this.dbm = dbm;
        this.cache = new HashMap<>();
    }

    public Organization find(int organizationID)
    {
        if (cache.containsKey(organizationID)) {
            return cache.get(organizationID);
        }

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select o.name, o.parentOrganizationID");
            sb.append("  from Organization o");
            sb.append("  where o.organizationID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, organizationID);
            ResultSet rs = pstmt.executeQuery();

            // return null if organization doesn't exist
            if (!rs.next())
                return null;

            String name = rs.getString("name");
            int parentOrganizationID = rs.getInt("parentOrganizationID");
            rs.close();

            Organization parentOrganization = find(parentOrganizationID);

            Organization organization = new Organization(this, name, organizationID, parentOrganization);
            cache.put(organizationID, organization);

            return organization;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding organization", e);
        }
    }

    public Organization find(String name)
    {

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select o.organizationID");
            sb.append("  from Organization o");
            sb.append("  where o.name = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if(!rs.next())
                return null;

            int organizationID = rs.getInt("organizationID");

            return find(organizationID);
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding organization", e);
        }
    }

    public Organization insert(String name, Organization parentOrganization) {
        try {

            if(find(name.toLowerCase(Locale.ROOT)) != null)
                return find(name);

            StringBuilder sb = new StringBuilder();
            sb.append("insert into organization(name, organizationID, parentOrganizationID)");
            sb.append("  values (?, ?, ?)");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setString(1, name);
            int organizationID = getNewOrganizationID();
            pstmt.setInt(2, organizationID);
            if (parentOrganization == null) {
                pstmt.setNull(3, Types.INTEGER);
            } else {
                pstmt.setInt(3, parentOrganization.getOrganizationID());
            }
            pstmt.executeUpdate();

            Organization organization = new Organization(this, name, organizationID, parentOrganization);
            cache.put(organizationID, organization);

            // Tell the parent org that it will have to recalculate its children list
            if(parentOrganization != null)
            {
                parentOrganization.invalidateChildren();
            }
            return organization;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error inserting new organization", e);
        }
    }

    public Collection<Person> getOrganizationPeople(int organizationID)
    {
        return dbm.getPeopleByOrganizationID(organizationID);
    }

    public static void create(Connection conn) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("create table organization(");
        sb.append("  name varchar(255) not null,");
        sb.append("  organizationID integer not null,");
        sb.append("  parentOrganizationID integer,");
        sb.append("  primary key (organizationID),");
        sb.append("  Foreign key(parentOrganizationID) references organization");
        sb.append(")");

        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sb.toString());
    }

    public Collection<Organization> getOrganizationsChildren(int parentOrganizationID)
    {
        try {
            Collection<Organization> organizations = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            sb.append("select o.organizationID");
            sb.append("  from Organization o");
            sb.append("  where o.parentOrganizationID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, parentOrganizationID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int organizationID = rs.getInt("organizationID");
                organizations.add(find(organizationID));
            }
            rs.close();

            return organizations;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error getting organization children", e);
        }
    }

    private int getNewOrganizationID()
    {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select max(o.organizationID) as id");
            sb.append("  from organization o");
            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            ResultSet rs = pstmt.executeQuery();

            //No organizations exist
            if (!rs.next())
                return 1;
            int max = rs.getInt("id");
            return max + 1;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding max organization id", e);
        }
    }

    public Collection<Ticket> getOrganizationTickets(int organizationID)
    {
        return dbm.getOrganizationTickets(organizationID);
    }

    public Collection<Queue> getOrganizationQueues(int organizationID)
    {
        return dbm.getOrganizationQueues(organizationID);
    }
}
