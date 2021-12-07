package models;

import dao.TicketDAO;

import java.sql.Date;
import java.util.Collection;

public class Ticket {
    private TicketDAO dao;
    private int ticketID;
    private String ticketTitle;
    private String ticketDescription;
    private TicketPriority ticketPriority;
    private TicketStatus ticketStatus;
    private Date dateCreated;
    private Date dateClosed;
    private Person personCreated;
    private Person personClosed;

    private Collection<Message> messages;
    private Collection<TicketPerPerson> ticketPerPerson;
    private Collection<TimePerPerson> timePerPerson;
    private Collection<TicketsPerQueue> ticketsPerQueue;

    public Ticket(TicketDAO dao, int ticketID, String ticketTitle, String ticketDescription, TicketPriority ticketPriority,
                  TicketStatus ticketStatus, Date dateCreated, Date dateClosed,
                  Person personCreated, Person personClosed)
    {
        this.dao = dao;
        this.ticketID = ticketID;
        this.ticketTitle = ticketTitle;
        this.ticketDescription = ticketDescription;
        this.ticketPriority = ticketPriority;
        this.ticketStatus = ticketStatus;
        this.dateCreated = dateCreated;
        this.dateClosed = dateClosed;
        this.personCreated = personCreated;
        this.personClosed = personClosed;
    }

    public int getTicketID() {return ticketID;}

    public TicketDAO getDao() {
        return dao;
    }

    public String getTicketTitle() {return ticketTitle;}

    public String getTicketDescription() {return ticketDescription;}

    public TicketPriority getTicketPriority() {return ticketPriority;}

    public TicketStatus getTicketStatus() {return ticketStatus;}

    public Date getDateCreated() {return dateCreated;}

    public Date getDateClosed() {return dateClosed;}

    public Person getPersonCreated() {return personCreated;}

    public Person getPersonClosed() {return personClosed;}

    public Collection<Message> getMessages()
    {
        if (messages == null) {
            messages = dao.getMessagesPerTicket(ticketID);
        }
        return messages;
    }

    public Collection<TicketPerPerson> getTicketPerPerson()
    {
        if (ticketPerPerson == null) {
            ticketPerPerson = dao.getTicketPerPersonTickets(ticketID);
        }
        return ticketPerPerson;
    }

    public Collection<TimePerPerson> getTimePerPersonTicket()
    {
        if (timePerPerson == null) {
            timePerPerson = dao.getTimePerPersonTicket(ticketID);
        }
        return timePerPerson;
    }

    public Collection<TicketsPerQueue> getTicketsPerQueue()
    {
        if(ticketsPerQueue == null)
            return dao.getTicketsPerQueue(ticketID);
        return ticketsPerQueue;
    }

    public void invalidateMessage()
    {
        messages = null;
    }

    public void invalidateTimePerPerson()
    {
        timePerPerson = null;
    }

    public void invalidateTicketPerPerson()
    {
        ticketPerPerson = null;
    }

    public void invalidateTicketsPerQueue() {ticketsPerQueue = null; }

}
