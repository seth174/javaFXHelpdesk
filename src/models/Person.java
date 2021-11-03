package models;

import dao.PersonDAO;

import java.sql.Connection;
import java.util.Collection;

public class Person {
    private PersonDAO dao;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private Organization organization;
    private int employeeID;
    private int level;

    private Collection<TicketPerPerson> ticketsPerPerson;
    private Collection<QueuePerPerson> queuePerPerson;
    private Collection<TimePerPerson> timePerPerson;
    private Collection<Message> messages;

    public Person(PersonDAO dao, String firstName, String lastName, String email, String password, String phoneNumber, Organization organization,
                  int employeeID, int level)
    {
        this.dao = dao;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.organization = organization;
        this.employeeID = employeeID;
        this.level = level;
    }

    public String getFirstName() {return firstName;}

    public String getLastName() {return lastName;}

    public String getEmail() {return email;}

    public String getPassword() {return password;}

    public String getPhoneNumber() {return phoneNumber;}

    public Organization getOrganization() {return organization;}

    public int getEmployeeID() {return employeeID;}

    public int getLevel() {return level;}

    public Collection<TicketPerPerson> getTicketsPerPerson() {
        if (ticketsPerPerson == null) {
            ticketsPerPerson = dao.getTicketPerPerson(employeeID);
        }
        return ticketsPerPerson;
    }

    public Collection<QueuePerPerson> getQueuePerPerson() {
        if (queuePerPerson == null) {
            queuePerPerson = dao.getQueuePerPerson(employeeID);
        }
        return queuePerPerson;
    }

    public Collection<TimePerPerson> getTimePerPerson() {
        if (timePerPerson == null) {
            timePerPerson = dao.getTimePerPerson(employeeID);
        }
        return timePerPerson;
    }

    public Collection<Message> getMessages()
    {
        if (messages == null) {
            messages = dao.getMessages(employeeID);
        }
        return messages;
    }

    public void invalidateTicketsPerPerson()
    {
        ticketsPerPerson = null;
    }

    public void invalidateTimePerPerson()
    {
        timePerPerson = null;
    }

    public void invalidateMessages()
    {
        messages = null;
    }

    public void invalidateQueuePerPerson()
    {
        queuePerPerson = null;
    }
}
