package models;

import dao.TicketPerPersonDAO;

public class TicketPerPerson {
    private TicketPerPersonDAO dao;
    private int ticketPerPersonID;
    private Ticket ticket;
    private Person person;
    private boolean subscribed;

    public TicketPerPerson(TicketPerPersonDAO dao, int ticketPerPersonID, Ticket ticket, Person person,
                           boolean subscribed)
    {
        this.dao = dao;
        this.ticketPerPersonID = ticketPerPersonID;
        this.ticket = ticket;
        this.person = person;
        this.subscribed = subscribed;
    }

    public int getTicketPerPersonID() {return ticketPerPersonID;}

    public Ticket getTicket() {return ticket;}

    public Person getPerson() {return person;}

    public boolean getSubscribed() {return subscribed;}
}
