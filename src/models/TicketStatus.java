package models;

import dao.TicketStatusDAO;

import java.util.Collection;

public class TicketStatus {
    private TicketStatusDAO dao;
    private int ticketStatusID;
    private String ticketStatus;

    private Collection<Ticket> tickets;

    public TicketStatus(TicketStatusDAO dao, int ticketStatusID, String ticketStatus)
    {
        this.dao = dao;
        this.ticketStatusID = ticketStatusID;
        this.ticketStatus = ticketStatus;
    }

    public int getTicketStatusID() {return ticketStatusID;}

    public String getTicketStatus() {return ticketStatus;}

    public Collection<Ticket> getTickets()
    {
        if (tickets == null) {
            tickets = dao.getTicketsByStatus(ticketStatusID);
        }
        return tickets;
    }

    public void invalidate()
    {
        tickets = null;
    }

}
