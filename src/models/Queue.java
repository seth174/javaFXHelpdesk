package models;

import dao.QueueDAO;

import java.util.Collection;

public class Queue {
    private QueueDAO dao;
    private int queueID;
    private String name;
    private Organization organization;
    private boolean deleted;

    private Collection<QueuePerPerson> queuePerPerson;
    private Collection<TicketsPerQueue> ticketsPerQueues;

    public Queue(QueueDAO dao, int queueID, String name, Organization organization, boolean deleted)
    {
        this.dao = dao;
        this.queueID = queueID;
        this.name = name;
        this.organization = organization;
        this.deleted = deleted;
    }

    public int getQueueID() {return queueID;}

    public String getName() {return name;}

    public boolean getDeleted() {return deleted;}

    public Collection<QueuePerPerson> getQueuePerPerson()
    {
        if (queuePerPerson == null) {
            queuePerPerson = dao.getQueuePerPersonQueue(queueID);
        }
        return queuePerPerson;
    }

    public Collection<TicketsPerQueue> getTicketsPerQueues()
    {
        if(ticketsPerQueues == null){
            ticketsPerQueues = dao.getTicketsPerQueue(queueID);
        }
        return ticketsPerQueues;
    }

    public Organization getOrganizationID(){
        return organization;
    }

    public void invalidateQueuePerPerson()
    {
        queuePerPerson = null;
    }

    public void invalidateTicketsPerQueues()
    {
        ticketsPerQueues = null;
    }

}
