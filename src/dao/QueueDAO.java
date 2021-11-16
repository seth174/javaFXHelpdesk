package dao;

import models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class QueueDAO {
    private Connection conn;
    private DatabaseManager dbm;
    private Map<Integer, Queue> cache;

    public QueueDAO(Connection conn, DatabaseManager dbm) {
        this.conn = conn;
        this.dbm = dbm;
        this.cache = new HashMap<>();
    }

    public static void create(Connection conn) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("create table Queue(");
        sb.append("  QueueID integer not null,");
        sb.append("  name varchar (256) not null,");
        sb.append("  organizationID integer not null,");
        sb.append("  deleted integer not null,");
        sb.append("  Foreign key(organizationID) references organization,");
        sb.append("  primary key (QueueID)");
        sb.append(")");

        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sb.toString());
    }

    public Queue find(int queueID) {
        if (cache.containsKey(queueID)) {
            return cache.get(queueID);
        }

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select q.name, organizationID, q.deleted");
            sb.append("  from Queue q");
            sb.append("  where q.QueueID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, queueID);
            ResultSet rs = pstmt.executeQuery();

            // return null if student doesn't exist
            if (!rs.next())
                return null;

            String name = rs.getString("name");
            int organizationID = rs.getInt("organizationID");
            boolean deleted = rs.getInt("deleted") != 0;

            Organization organization = dbm.findOrganization(organizationID);
            rs.close();

            Queue queue = new Queue(this, queueID, name, organization, deleted);
            cache.put(queueID, queue);

            return queue;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding queue", e);
        }
    }

    public Queue findByName(String name, Organization organization) {

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select q.QueueID");
            sb.append("  from Queue q");
            sb.append("  where lower(name) = ? and organizationID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setString(1, name.toLowerCase());
            pstmt.setInt(2, organization.getOrganizationID());
            ResultSet rs = pstmt.executeQuery();

            // return null if queue doesn't exist
            if (!rs.next())
                return null;

            int queueID = rs.getInt("QueueID");
            rs.close();

            return find(queueID);
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding queue by name", e);
        }
    }

    public Queue insert(String name, Organization organization) {
        try {
            // make sure that the name is currently unused
            Queue q = findByName(name, organization);
            if (q != null) {
                if(q.getOrganizationID() == organization)
                    return null;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("insert into Queue(QueueID, name, organizationID, deleted)");
            sb.append("  values (?, ?, ?, ?)");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());

            int queueID = getNewQueueID();
            pstmt.setInt(1, queueID);
            pstmt.setString(2, name);
            pstmt.setInt(3, organization.getOrganizationID());
            pstmt.setInt(4, 0);
            pstmt.executeUpdate();

            Queue queue = new Queue(this, queueID, name, organization, false);
            cache.put(queueID, queue);

            return queue;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error inserting new queue", e);
        }
    }


    public Collection<QueuePerPerson> getQueuePerPersonQueue(int queueID)
    {
        return dbm.getQueuePerPersonQueue(queueID);
    }

    public Collection<Ticket> getTicketsInQueue(int queueID)
    {
        return dbm.getTicketsInQueue(queueID);
    }

    private int getNewQueueID()
    {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select max(q.QueueID) as id");
            sb.append("  from Queue q");
            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            ResultSet rs = pstmt.executeQuery();

            //No people exist
            if (!rs.next())
                return 1;
            int max = rs.getInt("id");
            return max + 1;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding max queue id", e);
        }
    }

    public Collection<Queue> getOrganizationQueues(int organizationID)
    {
        try {
            Collection<Queue> organizationQueues = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            sb.append("select q.QueueID");
            sb.append("  from Queue q");
            sb.append("  where q.organizationID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, organizationID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int queueID = rs.getInt("QueueID");
                organizationQueues.add(dbm.findQueueByID(queueID));
            }
            rs.close();

            return organizationQueues;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error getting organizationQueues", e);
        }
    }

    public Queue updateDeleted(int queueID)
    {
        try {
            Queue queue = find(queueID);

            StringBuilder sb = new StringBuilder();
            sb.append("update Queue");
            if(queue.getDeleted())
                sb.append(" set deleted = 0");
            else
                sb.append(" set deleted = 1");
            sb.append(" where QueueID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, queueID);

            pstmt.executeUpdate();

            cache.clear();
            queue.getOrganizationID().invalidateQueues();

            return find(queueID);
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error updating queue", e);
        }
    }

    public Collection<TicketsPerQueue> getTicketsPerQueue(int queueID)
    {
        return dbm.getTicketsPerQueue(queueID);
    }
}
