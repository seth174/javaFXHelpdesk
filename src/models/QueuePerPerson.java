package models;

import dao.QueuePerPersonDAO;

public class QueuePerPerson {
    private QueuePerPersonDAO dao;
    private int queuePerPersonID;
    private Person person;
    private Queue queue;

    public QueuePerPerson(QueuePerPersonDAO dao, int queuePerPersonID, Person person, Queue queue)
    {
        this.dao = dao;
        this.queuePerPersonID = queuePerPersonID;
        this.person = person;
        this.queue = queue;
    }

    public int getQueuePerPersonID() {return queuePerPersonID;}

    public Person getEmployeeID() {return person;}

    public Queue getQueue() {return queue;}
}
