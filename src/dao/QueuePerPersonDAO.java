package dao;

import models.Person;
import models.Queue;
import models.QueuePerPerson;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class QueuePerPersonDAO {
    private Connection conn;
    private DatabaseManager dbm;
    private Map<Integer, QueuePerPerson> cache;

    public QueuePerPersonDAO(Connection conn, DatabaseManager dbm) {
        this.conn = conn;
        this.dbm = dbm;
        this.cache = new HashMap<>();
    }
    public static void create(Connection conn) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("create table queuePerPerson(");
        sb.append("  id integer not null,");
        sb.append("  employeeID integer not null,");
        sb.append("  queueID integer not null,");
        sb.append("  Primary key (id),");
        sb.append("  Foreign key (queueID) references queue,");
        sb.append("  Foreign key (employeeID) references Person");
        sb.append(")");

        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sb.toString());
    }

    public Collection<QueuePerPerson> getQueuePerPerson(int employeeID)
    {

        try {
            Collection<QueuePerPerson> queuePerPerson = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            sb.append("select qp.id");
            sb.append("  from queuePerPerson qp");
            sb.append("  where qp.employeeID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, employeeID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                queuePerPerson.add(find(id));
            }
            rs.close();

            return queuePerPerson;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding course", e);
        }
    }

    public Collection<QueuePerPerson> getQueuePerPersonQueue(int queueID)
    {

        try {
            Collection<QueuePerPerson> queuePerPerson = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            sb.append("select qp.id");
            sb.append("  from queuePerPerson qp");
            sb.append("  where qp.queueID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, queueID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                queuePerPerson.add(find(id));
            }
            rs.close();

            return queuePerPerson;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding QueuePerPerson", e);
        }
    }

    public QueuePerPerson find(int id) {
        if (cache.containsKey(id)) {
            return cache.get(id);
        }

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select qp.employeeID, qp.queueID");
            sb.append("  from queuePerPerson qp");
            sb.append("  where qp.id = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            // return null if department doesn't exist
            if (!rs.next())
                return null;

            int queueID = rs.getInt("queueID");
            int employeeID = rs.getInt("employeeID");
            rs.close();

            Queue queue = dbm.findQueueByID(queueID);
            Person person = dbm.findPersonByID(employeeID);

            QueuePerPerson queuePerPerson = new QueuePerPerson(this, id, person, queue);
            cache.put(id, queuePerPerson);

            return queuePerPerson;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding queuePerPerson", e);
        }
    }

    public QueuePerPerson insert(Person person, Queue queue) {
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("insert into queuePerPerson(id, employeeID, queueID)");
            sb.append("  values (?, ?, ?)");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            int id = getNewQueuePerPersonID();
            pstmt.setInt(1, id);
            pstmt.setInt(2, person.getEmployeeID());
            pstmt.setInt(3, queue.getQueueID());
            pstmt.executeUpdate();

            QueuePerPerson queuePerPerson = new QueuePerPerson(this, id, person, queue);
            cache.put(id, queuePerPerson);

            // Tell the user and queue that it will have to recalculate its queuePerPerson list
            person.invalidateQueuePerPerson();
            queue.invalidateQueuePerPerson();

            return queuePerPerson;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error inserting new queue per person", e);
        }
    }

    private int getNewQueuePerPersonID()
    {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select max(qp.id) as maxId");
            sb.append("  from queuePerPerson qp");
            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            ResultSet rs = pstmt.executeQuery();

            //No QueuePerPerson exist
            if (!rs.next())
                return 1;
            int max = rs.getInt("maxId");
            return max + 1;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding max queue per person id", e);
        }
    }

    public QueuePerPerson find(Person p, Queue q) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select qp.id");
            sb.append("  from queuePerPerson qp");
            sb.append("  where qp.queueID = ? and qp.employeeID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, q.getQueueID());
            pstmt.setInt(2, p.getEmployeeID());
            ResultSet rs = pstmt.executeQuery();

            // return null if department doesn't exist
            if (!rs.next())
                return null;

            int id = rs.getInt("id");
            rs.close();

            return find(id);
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding queuePerPerson with queue and person", e);
        }
    }

}
