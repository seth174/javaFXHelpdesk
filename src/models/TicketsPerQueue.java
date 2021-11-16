package models;

import dao.TicketsPerQueueDAO;

public class TicketsPerQueue {
    private TicketsPerQueueDAO dao;
    private int ticketsPerQueueID;
    private Ticket ticket;
    private Queue queue;

    public TicketsPerQueue(TicketsPerQueueDAO dao, int ticketsPerQueueID, Ticket ticket, Queue queue)
    {
        this.dao = dao;
        this.ticketsPerQueueID = ticketsPerQueueID;
        this.ticket = ticket;
        this.queue = queue;
    }

    public int getTicketsPerQueueID(){ return ticketsPerQueueID; }

    public Ticket getTicket(){ return ticket; }

    public Queue getQueue() { return queue; }
}
