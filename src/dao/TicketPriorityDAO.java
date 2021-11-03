package dao;

import models.Ticket;
import models.TicketPriority;

import java.sql.*;
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
        sb.append("  ticketPriority integer not null,");
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
            sb.append("select tpr.ticketPriority");
            sb.append("  from ticketPriority tpr");
            sb.append("  where tpr.ticketPriorityID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, ticketPriorityID);
            ResultSet rs = pstmt.executeQuery();

            // return null if student doesn't exist
            if (!rs.next())
                return null;

            int ticketPriorityNumber = rs.getInt("ticketPriority");
            rs.close();

            TicketPriority ticketPriority = new TicketPriority(this, ticketPriorityID, ticketPriorityNumber);
            cache.put(ticketPriorityID, ticketPriority);

            return ticketPriority;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding ticketPriority", e);
        }
    }

    public Collection<Ticket> getTicketsByPriority(int ticketPriorityID)
    {
        return dbm.getTicketsByPriority(ticketPriorityID);
    }

    public TicketPriority insert(int ticketPriorityNumber) {
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("insert into ticketPriority(ticketPriorityID, ticketPriority)");
            sb.append("  values (?, ?)");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            int ticketPriorityID = getNewID();
            pstmt.setInt(1, ticketPriorityID);
            pstmt.setInt(2, ticketPriorityNumber);
            pstmt.executeUpdate();

            TicketPriority ticketPriority = new TicketPriority(this, ticketPriorityID, ticketPriorityNumber);
            cache.put(ticketPriorityID, ticketPriority);

            return ticketPriority;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error inserting new ticketPriority", e);
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
