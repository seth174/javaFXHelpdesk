package dao;

import models.Message;
import models.Person;
import models.Ticket;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MessageDAO {
    private Connection conn;
    private DatabaseManager dbm;
    private Map<Integer, Message> cache;

    public MessageDAO(Connection conn, DatabaseManager dbm) {
        this.conn = conn;
        this.dbm = dbm;
        this.cache = new HashMap<>();
    }

    public static void create(Connection conn) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("create table message(");
        sb.append("  messageID integer not null,");
        sb.append("  ticketID integer not null,");
        sb.append("  messageReplyToID integer,");
        sb.append("  employeeID integer not null,");
        sb.append("  datePosted timestamp not null,");
        sb.append("  message varchar(4000) not null,");

        sb.append("  primary key (messageID),");
        sb.append("  Foreign key(ticketID) references ticket,");
        sb.append("  Foreign key(messageReplyToID) references message,");
        sb.append("  Foreign key(employeeID) references Person");
        sb.append(")");

        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sb.toString());
    }

    public Collection<Message> getMessages(int employeeID)
    {
        try {
            Collection<Message> messages = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            sb.append("select m.messageID");
            sb.append("  from message m");
            sb.append("  where m.employeeID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, employeeID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int messageID = rs.getInt("messageID");
                messages.add(find(messageID));
            }
            rs.close();

            return messages;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error getting messages", e);
        }
    }

    public Message find(int messageID) {
        if (cache.containsKey(messageID)) {
            return cache.get(messageID);
        }

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select m.ticketID, m.messageReplyToID, m.employeeID, m.datePosted, m.message");
            sb.append("  from message m");
            sb.append("  where m.messageID = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, messageID);
            ResultSet rs = pstmt.executeQuery();

            // return null if message doesn't exist
            if (!rs.next())
                return null;

            int ticketID = rs.getInt("ticketID");
            int messageReplyToID = rs.getInt("messageReplyToID");
            int employeeID = rs.getInt("employeeID");
            Timestamp datePosted = rs.getTimestamp("datePosted");
            String messageContent = rs.getString("message");
            rs.close();

            Ticket ticket = dbm.findTicketByID(ticketID);
            Message messageReplyTo = find(messageReplyToID);
            Person person = dbm.findPersonByID(employeeID);

            Message message = new Message(this, messageID, ticket, messageContent, datePosted, messageReplyTo, person);
            cache.put(messageID, message);

            return message;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding message", e);
        }
    }

    public Collection<Message> getMessagesPerTicket(int ticketID)
    {
        try {
            Collection<Message> messages = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            sb.append("select m.messageID");
            sb.append("  from message m");
            sb.append("  where m.ticketID = ? and messageReplyToID is null");
            sb.append("  ORDER BY datePosted DESC");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, ticketID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int messageID = rs.getInt("messageID");
                messages.add(find(messageID));
            }
            rs.close();

            return messages;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error getting messagesPerTicket", e);
        }
    }

    public Message insert(Ticket ticket, Message messageReplyTo, Person person, String messageContent, Timestamp timestamp) {
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("insert into MESSAGE(messageID, ticketID, messageReplyToID, employeeID, datePosted, message)");
            sb.append("  values (?, ?, ?, ?, ?, ?)");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());

            int messageID = getNewMessageID();
            pstmt.setInt(1, messageID);
            pstmt.setInt(2, ticket.getTicketID());
            if (messageReplyTo == null) {
                pstmt.setNull(3, Types.INTEGER);
            } else {
                pstmt.setInt(3, messageReplyTo.getMessageID());
            }
            pstmt.setInt(4, person.getEmployeeID());
            pstmt.setTimestamp(5, timestamp);
            pstmt.setString(6, messageContent);

            pstmt.executeUpdate();

             Message message = new Message(this, messageID, ticket, messageContent, timestamp, messageReplyTo, person);
            cache.put(messageID, message);

            // Tell the ticket that it will have to recalculate its message list
            ticket.invalidateMessage();
            message.invalidateReplyMessages();

            return message;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error inserting new message", e);
        }
    }

    private int getNewMessageID()
    {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select max(m.messageID) as id");
            sb.append("  from message m");
            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            ResultSet rs = pstmt.executeQuery();

            //No messages exist
            if (!rs.next())
                return 1;
            int max = rs.getInt("id");
            return max + 1;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding max message id", e);
        }
    }

    public Collection<Message> getReplyMessages(int messageID)
    {
        try {
            Collection<Message> messages = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            sb.append("select m.messageID");
            sb.append("  from message m");
            sb.append("  where m.messageReplyToID = ?");
            sb.append("  ORDER BY datePosted");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, messageID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int replyMessageID = rs.getInt("messageID");
                Message m = find(replyMessageID);
                messages.add(m);
            }
            rs.close();

            return messages;
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error getting messagesPerTicket", e);
        }
    }

    public Message findMessage(String text, Timestamp time, Ticket ticket)
    {

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select m.messageID");
            sb.append("  from message m");
            sb.append("  where m.message = ? and m.datePosted = ? and m.ticketID = ? ");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setString(1, text);
            pstmt.setTimestamp(2, time);
            pstmt.setInt(3, ticket.getTicketID());
            ResultSet rs = pstmt.executeQuery();

            // return null if message doesn't exist
            if (!rs.next())
                return null;

            int messageID = rs.getInt("messageID");
            rs.close();

            return find(messageID);
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException("error finding message by a bunch of stuff", e);
        }
    }

}
