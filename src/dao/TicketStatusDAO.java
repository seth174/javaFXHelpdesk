package dao;

import models.Organization;
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
}
