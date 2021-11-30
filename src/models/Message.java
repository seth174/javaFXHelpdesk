package models;

import dao.MessageDAO;

import java.sql.Timestamp;
import java.util.Collection;

public class Message {
    private MessageDAO dao;
    private int messageID;
    private Ticket ticket;
    private String message;
    private Timestamp datePosted;
    private Message messageReplyToID;
    private Person person;

    //Maybe add cache of reply messages
    private Collection<Message> replyMessages;
    public Message(MessageDAO dao, int messageID, Ticket ticket, String message, Timestamp datePosted, Message messageReplyToID, Person person)
    {
        this.dao = dao;
        this.messageID = messageID;
        this.ticket = ticket;
        this.message = message;
        this.datePosted = datePosted;
        this.messageReplyToID = messageReplyToID;
        this.person = person;
    }

    public int getMessageID() {return messageID;}

    public Ticket getTicketID() {return ticket;}

    public String getMessage() {return message;}

    public Timestamp getDatePosted() {return datePosted;}

    public Message getTicketReplyTo() {return messageReplyToID;}

    public Person getPerson() {return person;}

    public Collection<Message> getReplyMessages()
    {
        if(replyMessages == null)
        {
            replyMessages = dao.getReplyMessages(messageID);
        }
        return replyMessages;
    }

    public void invalidateReplyMessages(){
        replyMessages = null;
    }
}
