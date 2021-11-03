package dao;

import models.Ticket;
import models.TicketStatus;

import java.sql.*;
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
            sb.append("select ts.ticketStatus");
            sb.append("  from TicketStatus ts");
            sb.append("  where ts.TicketStatusID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, ticketStatusID);
            ResultSet rs = pstmt.executeQuery();

            // return null if student doesn't exist
            if (!rs.next())
                return null;

            String status = rs.getString("ticketStatus");
            rs.close();

            TicketStatus ticketStatus = new TicketStatus(this, ticketStatusID, status);
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

    public TicketStatus insert(String ticketStatusName) {
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("insert into TicketStatus(TicketStatusID, ticketStatus)");
            sb.append("  values (?, ?)");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            int ticketStatusID = getNewID();
            pstmt.setInt(1, ticketStatusID);
            pstmt.setString(2, ticketStatusName);
            pstmt.executeUpdate();

            TicketStatus ticketStatus = new TicketStatus(this, ticketStatusID, ticketStatusName);
            cache.put(ticketStatusID, ticketStatus);

            return ticketStatus;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error inserting new ticket status", e);
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
