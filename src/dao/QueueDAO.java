package dao;

import models.Organization;
import models.Queue;
import models.QueuePerPerson;
import models.Ticket;

import java.sql.*;
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
            sb.append("select q.name");
            sb.append("  from Queue q");
            sb.append("  where q.QueueID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, queueID);
            ResultSet rs = pstmt.executeQuery();

            // return null if student doesn't exist
            if (!rs.next())
                return null;

            String name = rs.getString("name");
            rs.close();

            Queue queue = new Queue(this, queueID, name);
            cache.put(queueID, queue);

            return queue;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding queue", e);
        }
    }

    public Queue findByName(String name) {

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select q.QueueID");
            sb.append("  from Queue q");
            sb.append("  where q.name = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            // return null if queue doesn't exist
            if (!rs.next())
                return null;

            int queueID = rs.getInt("name");
            rs.close();

            return find(queueID);
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding queue by name", e);
        }
    }

    public Queue insert(String name) {
        try {
            // make sure that the name is currently unused
            if (findByName(name) != null) {
                return null;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("insert into Queue(QueueID, name)");
            sb.append("  values (?, ?)");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());

            int queueID = getNewQueueID();
            pstmt.setInt(1, queueID);
            pstmt.setString(2, name);
            pstmt.executeUpdate();

            Queue queue = new Queue(this, queueID, name);
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
}
