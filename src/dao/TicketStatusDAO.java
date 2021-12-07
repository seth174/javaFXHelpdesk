package dao;

import models.Organization;
import models.Ticket;
import models.TicketStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TicketStatusDAO {
    private Connection conn;
    private DatabaseManager dbm;
    private Map<Integer, TicketStatus> cache;

    public TicketStatusDAO(Connection conn, DatabaseManager dbm) {
        this.conn = conn;
        this.dbm = dbm;
        this.cache = new HashMap<>();
    }

    public static void create(Connection conn) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("create table TicketStatus(");
        sb.append("  TicketStatusID integer not null,");
        sb.append("  ticketStatus varchar(255) not null,");
        sb.append("  organizationID integer not null,");
        sb.append("  Foreign key (organizationID) references Organization,");
        sb.append("  primary key (TicketStatusID)");
        sb.append(")");

        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sb.toString());
    }

    public TicketStatus find(int ticketStatusID) {
        if (cache.containsKey(ticketStatusID)) {
            return cache.get(ticketStatusID);
        }

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select ts.ticketStatus, ts.organizationID");
            sb.append("  from TicketStatus ts");
            sb.append("  where ts.TicketStatusID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, ticketStatusID);
            ResultSet rs = pstmt.executeQuery();

            // return null if student doesn't exist
            if (!rs.next())
                return null;

            String status = rs.getString("ticketStatus");
            int organizationID = rs.getInt("organizationID");
            rs.close();

            Organization organization = dbm.findOrganization(organizationID);
            TicketStatus ticketStatus = new TicketStatus(this, ticketStatusID, status, organization);
            cache.put(ticketStatusID, ticketStatus);

            return ticketStatus;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding ticket status", e);
        }
    }

    public Collection<Ticket> getTicketsByStatus(int ticketStatusID)
    {
        return dbm.getTicketsByStatus(ticketStatusID);
    }

    public TicketStatus insert(String ticketStatusName, Organization organization) {
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("insert into TicketStatus(TicketStatusID, ticketStatus, organizationID)");
            sb.append("  values (?, ?, ?)");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            int ticketStatusID = getNewID();
            pstmt.setInt(1, ticketStatusID);
            pstmt.setString(2, ticketStatusName);
            pstmt.setInt(3, organization.getOrganizationID());
            pstmt.executeUpdate();

            TicketStatus ticketStatus = new TicketStatus(this, ticketStatusID, ticketStatusName, organization);
            cache.put(ticketStatusID, ticketStatus);

            return ticketStatus;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error inserting new ticket status", e);
        }
    }

    public TicketStatus findStatusByName(String name, Organization organization){
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select TicketStatusID");
            sb.append("  from TicketStatus ");
            sb.append("  where ticketStatus = ? and organizationID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setString(1, name);
            pstmt.setInt(2, organization.getOrganizationID());
            ResultSet rs = pstmt.executeQuery();

            // return null if Ticket Priority doesn't exist
            if (!rs.next())
                return null;

            int ticketStatusID = rs.getInt("TicketStatusID");
            rs.close();

            TicketStatus ticketStatus = find(ticketStatusID);
            cache.put(ticketStatusID, ticketStatus);

            return ticketStatus;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding ticket status by name", e);
        }
    }

    public Collection<TicketStatus> getTicketStatuses(int organizationID)
    {
        try {
            Collection<TicketStatus> ticketStatuses = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            sb.append("select t.TicketStatusID");
            sb.append("  from TicketStatus t");
            sb.append("  where t.organizationID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, organizationID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int ticketStatusID = rs.getInt("TicketStatusID");
                ticketStatuses.add(find(ticketStatusID));
            }
            rs.close();

            return ticketStatuses;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error getting ticket statuses", e);
        }
    }

    public TicketStatus findByName(String name, Organization organization) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select tps.TicketStatusID");
            sb.append("  from TicketStatus tps");
            sb.append("  where tps.ticketStatus = ? and tps.organizationID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setString(1, name);
            pstmt.setInt(2, organization.getOrganizationID());
            ResultSet rs = pstmt.executeQuery();

            // return null if Ticket Priority doesn't exist
            if (!rs.next())
                return null;

            int ticketStatusID = rs.getInt("TicketStatusID");
            rs.close();

            TicketStatus ticketStatus = find(ticketStatusID);
            cache.put(ticketStatusID, ticketStatus);

            return ticketStatus;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding ticket status by name", e);
        }
    }

    private int getNewID()
    {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select max(t.TicketStatusID) as id");
            sb.append("  from TicketStatus t");
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
