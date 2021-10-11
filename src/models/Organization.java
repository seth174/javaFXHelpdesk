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
    private Collection<Ticket> organizationTickets;

    public Organization(OrganizationDAO dao, String name, int organizationID, Organization parentOrganization)
    {
        this.dao = dao;
        this.name = name;
        this.organizationID = organizationID;
        this.parentOrganization = parentOrganization;
    }

    public String getName() {return name;}

    public int getOrganizationID() {return organizationID;}

    public Organization getParentOrganizationID() {return parentOrganization;}

    public Collection<Person> getOrganizationPeople()
    {
        if(organizationPeople == null)
        {
            organizationPeople = dao.getOrganizationPeople(organizationID);
        }
        return organizationPeople;
    }

    public Collection<Ticket> getOrganizationTickets()
    {
        if(organizationTickets == null)
        {
            organizationTickets = dao.getOrganizationTickets(organizationID);
        }
        return organizationTickets;
    }

    public Collection<Organization> getOrganizationsChildren()
    {
        if(organizationsChildren == null)
        {
            organizationsChildren = dao.getOrganizationsChildren(organizationID);
        }
        return organizationsChildren;
    }

    public void invalidatePeople() {
        organizationPeople = null;
    }

    public void invalidateChildren(){
        organizationsChildren = null;
    }

    public void invalidateTickets(){
        organizationTickets = null;
    }


}
