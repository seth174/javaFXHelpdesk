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
    private Organization organization;
    private Queue queue;
    private Person personCreated;
    private Person personClosed;

    private Collection<Message> messages;
    private Collection<TicketPerPerson> ticketPerPerson;
    private Collection<TimePerPerson> timePerPerson;

    public Ticket(TicketDAO dao, int ticketID, String ticketTitle, String ticketDescription, TicketPriority ticketPriority,
                  TicketStatus ticketStatus, Date dateCreated, Date dateClosed, Organization organization,
                  Queue queue, Person personCreated, Person personClosed)
    {
        this.dao = dao;
        this.ticketID = ticketID;
        this.ticketTitle = ticketTitle;
        this.ticketDescription = ticketDescription;
        this.ticketPriority = ticketPriority;
        this.ticketStatus = ticketStatus;
        this.dateCreated = dateCreated;
        this.dateClosed = dateClosed;
        this.organization = organization;
        this.queue = queue;
        this.personCreated = personCreated;
        this.personClosed = personClosed;
    }

    public int getTicketID() {return ticketID;}

    public String getTicketTitle() {return ticketTitle;}

    public String getTicketDescription() {return ticketDescription;}

    public TicketPriority getTicketPriority() {return ticketPriority;}

    public TicketStatus getTicketStatus() {return ticketStatus;}

    public Date getDateCreated() {return dateCreated;}

    public Date getDateClosed() {return dateClosed;}

    public Organization getOrganization() {return organization;}

    public Queue getQueue() {return queue;}

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

}