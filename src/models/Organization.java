package models;

import dao.OrganizationDAO;

import java.util.Collection;

public class Organization {
    private OrganizationDAO dao;
    private String name;
    private int organizationID;
    private Organization parentOrganization;

    private Collection<Person> organizationPeople;
    private Collection<Organization> organizationsChildren;
    private Collection<Queue> organizationQueues;
    private Collection<TicketPriority> ticketPriorities;
    private Collection<TicketStatus> ticketStatuses;

    public Organization(OrganizationDAO dao, String name, int organizationID, Organization parentOrganization)
    {
        this.dao = dao;
        this.name = name;
        this.organizationID = organizationID;
        this.parentOrganization = parentOrganization;
    }

    public String getName() {return name;}

    public int getOrganizationID() {return organizationID;}

    public Organization getParentOrganization() {return parentOrganization;}

    public Collection<Person> getOrganizationPeople()
    {
        if(organizationPeople == null)
        {
            organizationPeople = dao.getOrganizationPeople(organizationID);
        }
        return organizationPeople;
    }

    public Collection<TicketPriority> getTicketPriorities()
    {
        if(ticketPriorities == null)
        {
            ticketPriorities = dao.getTicketPriorities(organizationID);
        }
        return ticketPriorities;
    }

    public Collection<Organization> getOrganizationsChildren()
    {
        if(organizationsChildren == null)
        {
            organizationsChildren = dao.getOrganizationsChildren(organizationID);
        }
        return organizationsChildren;
    }

    public Collection<Queue> getOrganizationQueues()
    {
        if(organizationQueues == null)
        {
            organizationQueues = dao.getOrganizationQueues(organizationID);
        }
        return organizationQueues;
    }

    public Collection<TicketStatus> getTicketStatuses()
    {
        if(ticketStatuses == null)
        {
            ticketStatuses = dao.getTicketStatuses(organizationID);
        }
        return ticketStatuses;
    }

    public void invalidatePeople() {
        organizationPeople = null;
    }

    public void invalidateChildren(){
        organizationsChildren = null;
    }

    public void invalidateQueues() { organizationQueues = null; }

    public void invalidatePriorities() { ticketPriorities = null; }

    public void invalidateStatuses() { ticketStatuses = null;}


}
