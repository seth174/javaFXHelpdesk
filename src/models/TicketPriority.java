package models;

import dao.TicketPriorityDAO;

import java.util.Collection;

public class TicketPriority {
    private TicketPriorityDAO dao;
    private int id;
    private String priority;

    private Collection<Ticket> tickets;

    public TicketPriority(TicketPriorityDAO dao, int id, String priority)
    {
        this.dao = dao;
        this.id = id;
        this.priority = priority;
    }

    public int getId() {return id;}

    public String getPriority() {return priority;}

    public Collection<Ticket> getTickets()
    {
        if (tickets == null) {
            tickets = dao.getTicketsByPriority(id);
        }
        return tickets;
    }

    public void invalidate()
    {
        tickets = null;
    }
}
