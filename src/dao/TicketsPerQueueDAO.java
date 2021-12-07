package dao;

import models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TicketsPerQueueDAO {
    private Connection conn;
    private DatabaseManager dbm;
    private Map<Integer, TicketsPerQueue> cache;

    public TicketsPerQueueDAO(Connection conn, DatabaseManager dbm) {
        this.conn = conn;
        this.dbm = dbm;
        this.cache = new HashMap<>();
    }

    public static void create(Connection conn) throws SQLException{
        StringBuilder sb = new StringBuilder();
        sb.append("create table TicketsPerQueue(");
        sb.append("  ticketID integer not null,");
        sb.append("  TicketsPerQueueID integer not null,");
        sb.append("  QueueID integer not null,");
        sb.append("  primary key (TicketsPerQueueID),");
        sb.append("  Foreign key (QueueID) references Queue,");
        sb.append("  Foreign key (ticketID) references Ticket");
        sb.append(")");

        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sb.toString());
    }

    public TicketsPerQueue find(int ticketsPerQueueID)
    {
//        if (cache.containsKey(ticketsPerQueueID)) {
//            System.out.println("inside tickets per queue cach");
//            return cache.get(ticketsPerQueueID);
//        }

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select tq.ticketID, tq.QueueID");
            sb.append("  from TicketsPerQueue tq");
            sb.append("  where tq.TicketsPerQueueID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, ticketsPerQueueID);
            ResultSet rs = pstmt.executeQuery();

            // return null if student doesn't exist
            if (!rs.next())
                return null;

            int ticketID = rs.getInt("ticketID");
            int queueID = rs.getInt("QueueID");

            Ticket ticket = dbm.findTicketByID(ticketID);
            Queue queue = dbm.findQueueByID(queueID);

            rs.close();

            TicketsPerQueue ticketsPerQueue = new TicketsPerQueue(this, ticketsPerQueueID, ticket, queue);
            cache.put(ticketsPerQueueID, ticketsPerQueue);

            return ticketsPerQueue;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding ticketsPerQueue", e);
        }
    }

    public TicketsPerQueue find(Queue queue, Ticket ticket)
    {

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select tq.TicketsPerQueueID");
            sb.append("  from TicketsPerQueue tq");
            sb.append("  where tq.QueueID = ? and tq.ticketID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, queue.getQueueID());
            pstmt.setInt(2, ticket.getTicketID());
            ResultSet rs = pstmt.executeQuery();

            // return null if student doesn't exist
            if (!rs.next())
                return null;

            int ticketsPerQueueID = rs.getInt("TicketsPerQueueID");

            rs.close();

            return find(ticketsPerQueueID);
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding ticketsPerQueue with ticket and queue", e);
        }
    }

    public TicketsPerQueue find(Ticket ticket, Organization organization)
    {

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select tq.TicketsPerQueueID");
            sb.append("  from TicketsPerQueue tq");
            sb.append("  LEFT JOIN QUEUE q ON q.QUEUEID = tq.queueId");
            sb.append("  where tq.ticketID = ? AND q.ORGANIZATIONID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, ticket.getTicketID());
            pstmt.setInt(2, organization.getOrganizationID());
            ResultSet rs = pstmt.executeQuery();

            // return null if student doesn't exist
            if (!rs.next())
                return null;

            int ticketsPerQueueID = rs.getInt("TicketsPerQueueID");

            rs.close();

            return find(ticketsPerQueueID);
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding ticketsPerQueue with ticket and org", e);
        }
    }

    public Collection<Ticket> getTicketsPerQueue(int queueID)
    {
        try {
            Collection<Ticket> ticketsPerQueues = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            sb.append("select tq.ticketID");
            sb.append("  from TicketsPerQueue tq");
            sb.append("  where tq.queueID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, queueID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                int ticketID = rs.getInt("ticketID");
                ticketsPerQueues.add(dbm.findTicketByID(ticketID));
            }
            rs.close();

            return ticketsPerQueues;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding tickets per queue queue", e);
        }
    }

    public Collection<Ticket> getOnlyTicketsPerQueue(int queueID)
    {
        try {
            Collection<Ticket> ticketsPerQueues = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            sb.append("select tq.ticketID");
            sb.append("  from TicketsPerQueue tq");
            sb.append("  where tq.queueID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, queueID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                int ticketID = rs.getInt("ticketID");
                ticketsPerQueues.add(dbm.findTicketByID(ticketID));
            }
            rs.close();

            return ticketsPerQueues;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding tickets per queue queue", e);
        }
    }

    public Collection<TicketsPerQueue> getTicketsPerQueueTickets(int ticketID)
    {
        try {
            Collection<TicketsPerQueue> ticketsPerQueues = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            sb.append("select tq.TicketsPerQueueID");
            sb.append("  from TicketsPerQueue tq");
            sb.append("  where tq.ticketID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, ticketID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int ticketsPerQueueID = rs.getInt("TicketsPerQueueID");
                ticketsPerQueues.add(find(ticketsPerQueueID));
            }
            rs.close();

            return ticketsPerQueues;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding tickets per queue queue", e);
        }
    }

    private int getNewID()
    {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select max(tq.TicketsPerQueueID) as id");
            sb.append("  from TicketsPerQueue tq");
            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            ResultSet rs = pstmt.executeQuery();

            //No ticket exist
            if (!rs.next())
                return 1;
            int max = rs.getInt("id");
            return max + 1;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding max TicketsPerQueueID", e);
        }
    }

    public TicketsPerQueue insert(Ticket ticket, Queue queue) {
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("insert into TicketsPerQueue(TicketsPerQueueID, ticketID, QueueID)");
            sb.append("  values (?, ?, ?)");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            int id = getNewID();
            pstmt.setInt(1, id);
            pstmt.setInt(2, ticket.getTicketID());
            pstmt.setInt(3, queue.getQueueID());
            pstmt.executeUpdate();

            TicketsPerQueue ticketsPerQueue = new TicketsPerQueue(this, id, ticket, queue);
            cache.put(id, ticketsPerQueue);

            // Tell the user and queue that it will have to recalculate its queuePerPerson list
            ticket.invalidateTicketsPerQueue();
            queue.invalidateTicketsPerQueues();

            return ticketsPerQueue;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error inserting ticketsPerQueue", e);
        }
    }

    public Organization findOtherTicketOrganization(int ticketID, Organization organization)
    {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT o.NAME");
            sb.append(" from TicketsPerQueue tpq");
            sb.append(" LEFT JOIN Queue q ON q.QUEUEID = tpq.queueid");
            sb.append(" LEFT JOIN ORGANIZATION o ON q.ORGANIZATIONID = o.ORGANIZATIONID");
            sb.append(" WHERE tpq.ticketID = ? AND o.organizationid != ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, ticketID);
            pstmt.setInt(2, organization.getOrganizationID());
            ResultSet rs = pstmt.executeQuery();

            // return null if student doesn't exist
            if (!rs.next())
                return null;

            String organizationName = rs.getString("NAME");

            rs.close();

            return dbm.findByName(organizationName);
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding ticketsPerQueue", e);
        }
    }

    public void updateTicketPerQueue(Queue newQueue, TicketsPerQueue ticketsPerQueue)
    {
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("Update TicketsPerQueue");
            sb.append(" set QueueID = ?");
            sb.append(" where TicketsPerQueueID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, newQueue.getQueueID());
            pstmt.setInt(2, ticketsPerQueue.getTicketsPerQueueID());

            pstmt.executeUpdate();

            ticketsPerQueue.getQueue().invalidateTicketsPerQueues();
            TicketsPerQueue ticketsPerQueueNew = new TicketsPerQueue(this, ticketsPerQueue.getTicketsPerQueueID(),
                    ticketsPerQueue.getTicket(), newQueue);
            cache.put(ticketsPerQueue.getTicketsPerQueueID(), ticketsPerQueueNew);
            ticketsPerQueueNew.getTicket().invalidateTicketsPerQueue();
            ticketsPerQueue.getQueue().invalidateTicketsPerQueues();

        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error updating ticketsPerQueue", e);
        }
    }

}
