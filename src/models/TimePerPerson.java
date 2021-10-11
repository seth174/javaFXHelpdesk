package models;

import dao.TimePerPersonDAO;

public class TimePerPerson {
    private TimePerPersonDAO dao;
    private int timePerPersonID;
    private Person person;
    private Ticket ticket;
    private double time;

    public TimePerPerson(TimePerPersonDAO dao,int timePerPersonID, Person person, Ticket ticket, double time)
    {
        this.dao = dao;
        this.timePerPersonID = timePerPersonID;
        this.person = person;
        this.ticket = ticket;
        this.time = time;
    }

    public Person getPerson() {return person;}

    public Ticket getTicket() {return ticket;}

    public double getTime() {return time;}
}
