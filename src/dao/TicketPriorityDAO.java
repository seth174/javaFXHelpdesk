package dao;

import models.Organization;
import models.Ticket;
import models.TicketPriority;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TicketPriorityDAO {
    private Connection conn;
    private DatabaseManager dbm;
    private Map<Integer, TicketPriority> cache;

    public TicketPriorityDAO(Connection conn, DatabaseManager dbm) {
        this.conn = conn;
        this.dbm = dbm;
        this.cache = new HashMap<>();
    }

    public static void create(Connection conn) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("create table ticketPriority(");
        sb.append("  ticketPriorityID integer not null,");
        sb.append("  ticketPriority varchar(255) not null,");
        sb.append("  organizationID integer not null,");
        sb.append("  Foreign key (organizationID) references Organization,");
        sb.append("  primary key (ticketPriorityID)");
        sb.append(")");

        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sb.toString());
    }

    public TicketPriority find(int ticketPriorityID) {
        if (cache.containsKey(ticketPriorityID)) {
            return cache.get(ticketPriorityID);
        }

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select tpr.ticketPriority, tpr.organizationID");
            sb.append("  from ticketPriority tpr");
            sb.append("  where tpr.ticketPriorityID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, ticketPriorityID);
            ResultSet rs = pstmt.executeQuery();

            // return null if student doesn't exist
            if (!rs.next())
                return null;

            String ticketPriorityString = rs.getString("ticketPriority");
            int organizationID = rs.getInt("organizationID");
            rs.close();

            Organization organization = dbm.findOrganization(organizationID);
            TicketPriority ticketPriority = new TicketPriority(this, ticketPriorityID, ticketPriorityString, organization);
            cache.put(ticketPriorityID, ticketPriority);

            return ticketPriority;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding ticketPriority", e);
        }
    }

    public TicketPriority findByName(String name, Organization organization) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select tpr.ticketPriorityID");
            sb.append("  from ticketPriority tpr");
            sb.append("  where tpr.ticketPriority = ? and tpr.organizationID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setString(1, name);
            pstmt.setInt(2, organization.getOrganizationID());
            ResultSet rs = pstmt.executeQuery();

            // return null if Ticket Priority doesn't exist
            if (!rs.next())
                return null;

            int ticketPriorityID = rs.getInt("ticketPriorityID");
            rs.close();

            TicketPriority ticketPriority = find(ticketPriorityID);
            cache.put(ticketPriorityID, ticketPriority);

            return ticketPriority;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding ticketPriority by name", e);
        }
    }

    public Collection<Ticket> getTicketsByPriority(int ticketPriorityID)
    {
        return dbm.getTicketsByPriority(ticketPriorityID);
    }

    public TicketPriority insert(String ticketPriorityString, Organization organization) {
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("insert into ticketPriority(ticketPriorityID, ticketPriority, organizationID)");
            sb.append("  values (?, ?, ?)");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            int ticketPriorityID = getNewID();
            pstmt.setInt(1, ticketPriorityID);
            pstmt.setString(2, ticketPriorityString);
            pstmt.setInt(3, organization.getOrganizationID());
            pstmt.executeUpdate();

            TicketPriority ticketPriority = new TicketPriority(this, ticketPriorityID, ticketPriorityString, organization);
            cache.put(ticketPriorityID, ticketPriority);

            return ticketPriority;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error inserting new ticketPriority", e);
        }
    }

    public Collection<TicketPriority> getTicketPriorities(int organizationID)
    {
        try {
            Collection<TicketPriority> ticketsByPriority = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            sb.append("select t.ticketPriorityID");
            sb.append("  from ticketPriority t");
            sb.append("  where t.organizationID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, organizationID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int ticketPriorityID = rs.getInt("ticketPriorityID");
                ticketsByPriority.add(find(ticketPriorityID));
            }
            rs.close();

            return ticketsByPriority;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error getting ticket priorities", e);
        }
    }

    private int getNewID()
    {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select max(t.ticketPriorityID) as id");
            sb.append("  from ticketPriority t");
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
