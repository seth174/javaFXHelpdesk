package models;

import dao.TimePerPersonDAO;

import java.sql.Date;

public class TimePerPerson {
    private TimePerPersonDAO dao;
    private int timePerPersonID;
    private Person person;
    private Ticket ticket;
    private double time;
    private Date date;

    public TimePerPerson(TimePerPersonDAO dao, int timePerPersonID, Person person, Ticket ticket, double time,
                         Date date)
    {
        this.dao = dao;
        this.timePerPersonID = timePerPersonID;
        this.person = person;
        this.ticket = ticket;
        this.time = time;
        this.date = date;
    }

    public Person getPerson() {return person;}

    public int getTimePerPersonID() {return timePerPersonID;}

    public Ticket getTicket() {return ticket;}

    public double getTime() {return time;}

    public Date getDate(){ return date; }
}
