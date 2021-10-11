package models;

import dao.QueueDAO;

import java.util.Collection;

public class Queue {
    private QueueDAO dao;
    private int queueID;
    private String name;

    private Collection<QueuePerPerson> queuePerPerson;
    private Collection<Ticket> ticketsInQueue;

    public Queue(QueueDAO dao, int queueID, String name)
    {
        this.dao = dao;
        this.queueID = queueID;
        this.name = name;
    }

    public int getQueueID() {return queueID;}

    public String getName() {return name;}

    public Collection<QueuePerPerson> getQueuePerPerson()
    {
        if (queuePerPerson == null) {
            queuePerPerson = dao.getQueuePerPersonQueue(queueID);
        }
        return queuePerPerson;
    }

    public Collection<Ticket> getTicketsInQueue()
    {
        if (ticketsInQueue == null) {
            ticketsInQueue = dao.getTicketsInQueue(queueID);
        }
        return ticketsInQueue;
    }

    public void invalidateQueuePerPerson()
    {
        queuePerPerson = null;
    }

    public void invalidateTicketsInQueue()
    {
        ticketsInQueue = null;
    }
}
