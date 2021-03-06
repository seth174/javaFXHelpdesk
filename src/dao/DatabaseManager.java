package dao;
import java.sql.*;
import java.util.Collection;
import java.util.Properties;

import models.*;
import org.apache.derby.jdbc.ClientDriver;

public class DatabaseManager {
    private Driver driver;
    private Connection conn;
    private MessageDAO messageDAO;
    private OrganizationDAO organizationDAO;
    private PersonDAO personDAO;
    private QueueDAO queueDAO;
    private QueuePerPersonDAO queuePerPersonDAO;
    private TicketDAO ticketDAO;
    private TicketPerPersonDAO ticketPerPersonDAO;
    private TicketPriorityDAO ticketPriorityDAO;
    private TicketStatusDAO ticketStatusDAO;
    private TimePerPersonDAO timePerPersonDAO;
    private TicketsPerQueueDAO ticketsPerQueueDAO;

    private final String url = "jdbc:derby://localhost/bin/db/HelpSpotDB";

    public DatabaseManager() {
        driver = new ClientDriver();
        Properties prop = new Properties();
        prop.put("create", "false");

        // try to connect to an existing database
        try {
            conn = driver.connect(url, prop);
            conn.setAutoCommit(false);
            System.out.println("connected");
        } catch (SQLException e) {
            // database doesn't exist, so try creating it
            try {
                System.out.println("Creating");
                prop.put("create", "true");
                conn = driver.connect(url, prop);
                conn.setAutoCommit(false);
                create(conn);
            } catch (SQLException e2) {
                throw new RuntimeException("cannot connect to database", e2);
            }
        }

        personDAO = new PersonDAO(conn, this);
        messageDAO = new MessageDAO(conn, this);
        organizationDAO = new OrganizationDAO(conn, this);
        queueDAO = new QueueDAO(conn, this);
        queuePerPersonDAO = new QueuePerPersonDAO(conn, this);
        ticketDAO = new TicketDAO(conn, this);
        ticketPerPersonDAO = new TicketPerPersonDAO(conn, this);
        ticketPriorityDAO = new TicketPriorityDAO(conn, this);
        ticketStatusDAO = new TicketStatusDAO(conn, this);
        timePerPersonDAO = new TimePerPersonDAO(conn, this);
        ticketsPerQueueDAO = new TicketsPerQueueDAO(conn, this);
    }

    public void create(Connection conn) throws SQLException
    {
        OrganizationDAO.create(conn);
        PersonDAO.create(conn);
        QueueDAO.create(conn);
        QueuePerPersonDAO.create(conn);
        TicketStatusDAO.create(conn);
        TicketPriorityDAO.create(conn);
        TicketDAO.create(conn);
        TicketPerPersonDAO.create(conn);
        TimePerPersonDAO.create(conn);
        MessageDAO.create(conn);
        TicketsPerQueueDAO.create(conn);
        conn.commit();
    }

    public Collection<Person> getPeopleByOrganizationID(int organizationID)
    {
        return personDAO.getPeopleByOrganizationID(organizationID);
    }

    public Organization findOrganization(int organizationID)
    {
        return organizationDAO.find(organizationID);
    }

    public void commit() {
        try {
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("cannot commit database", e);
        }
    }

    public void cleanup() {
        try {
            conn.rollback();
            conn.close();
        } catch (SQLException e) {
            System.out.println("fatal error: cannot cleanup connection");
        }
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException("cannot close database connection", e);
        }

        // Now shutdown the embedded database system -- this is Derby-specific
        try {
            Properties prop = new Properties();
            prop.put("shutdown", "true");
            conn = driver.connect(url, prop);
        } catch (SQLException e) {
            // This is supposed to throw an exception...
            System.out.println("Derby has shut down successfully");
        }
    }

    public Collection<Ticket> getTicketPerPerson(int employeeID)
    {
        return ticketPerPersonDAO.getTicketPerPerson(employeeID);
    }

    public Ticket findTicketByID(int ticketID)
    {
        return ticketDAO.find(ticketID);
    }

    public Person findPersonByID(int employeeID)
    {
        return personDAO.find(employeeID);
    }

    public Queue findQueueByID(int queueID)
    {
        return queueDAO.find(queueID);
    }

    public TicketPriority findTicketPriorityByID(int ticketPriorityID)
    {
        return ticketPriorityDAO.find(ticketPriorityID);
    }

    public TicketPriority findPriorityByName(String name, Organization organization){
        return ticketPriorityDAO.findByName(name, organization);
    }

    public TicketStatus findStatusByName(String name, Organization organization)
    {
        return ticketStatusDAO.findStatusByName(name, organization);
    }

    public TicketStatus findTicketStatusByID(int ticketStatusID)
    {
        return ticketStatusDAO.find(ticketStatusID);
    }

    public Organization findOtherTicketOrganization(int ticketID, Organization currentOrganization)
    {
        return ticketsPerQueueDAO.findOtherTicketOrganization(ticketID, currentOrganization);
    }
    public Collection<QueuePerPerson> getQueuePerPerson(int employeeID)
    {
        return queuePerPersonDAO.getQueuePerPerson(employeeID);
    }

    public Collection<TimePerPerson> getTimePerPerson(int employeeID)
    {
        return timePerPersonDAO.getTimePerPerson(employeeID);
    }

    public Collection<Message> getMessages(int employeeID)
    {
        return messageDAO.getMessages(employeeID);
    }

    public Message findMessageByID(int messageID)
    {
        return messageDAO.find(messageID);
    }

    public Collection<QueuePerPerson> getQueuePerPersonQueue(int queueID)
    {
        return queuePerPersonDAO.getQueuePerPersonQueue(queueID);
    }

    public Collection<Ticket> getTicketsInQueue(int queueID)
    {
        return ticketDAO.getTicketsInQueue(queueID);
    }

    public Collection<Message> getMessagesPerTicket(int ticketID)
    {
        return messageDAO.getMessagesPerTicket(ticketID);
    }

    public Collection<TicketPerPerson> getTicketPerPersonTicket(int ticketID)
    {
        return ticketPerPersonDAO.getTicketPerPersonTicket(ticketID);
    }

    public void updateTicketStatus(TicketStatus oldStatus, TicketStatus newStatus, Ticket ticket)
    {
        ticketDAO.updateTicketStatus(oldStatus, newStatus, ticket);
    }

    public Collection<Ticket> getOnlyTicketsPerQueue(int queueID)
    {
        return ticketsPerQueueDAO.getOnlyTicketsPerQueue(queueID);
    }

    public Collection<TimePerPerson> getTimePerPersonTicket(int ticketID)
    {
        return timePerPersonDAO.getTimePerPersonTicket(ticketID);
    }

    public Collection<Ticket> getTicketsByPriority(int ticketPriorityID)
    {
        return ticketDAO.getTicketsByPriority(ticketPriorityID);
    }

    public Collection<Ticket> getTicketsByStatus(int ticketStatusID)
    {
        return ticketDAO.getTicketsByStatus(ticketStatusID);
    }

    public Collection<Ticket> getOrganizationTickets(int organizationID)
    {
        return ticketDAO.getOrganizationTickets(organizationID);
    }

    public Collection<Ticket> getTicketsPerQueue(int queueID)
    {
        return ticketsPerQueueDAO.getTicketsPerQueue(queueID);
    }

    public Collection<TicketsPerQueue> getTicketsPerQueueTickets(int ticketID)
    {
        return ticketsPerQueueDAO.getTicketsPerQueueTickets(ticketID);
    }

    public Collection<TicketPriority> getTicketPriorities(int organizationID)
    {
        return ticketPriorityDAO.getTicketPriorities(organizationID);
    }

    public Collection<TicketStatus> getTicketStatuses(int organizationID)
    {
        return ticketStatusDAO.getTicketStatuses(organizationID);
    }

    public Message insert(Ticket ticket, Message messageReplyTo, Person person, String messageContent, Timestamp timestamp)
    {
        return messageDAO.insert(ticket, messageReplyTo, person, messageContent, timestamp);
    }

    public Organization insert(String name, Organization parentOrganization)
    {
        return organizationDAO.insert(name, parentOrganization);
    }

    public Person insert(String firstName, String lastName, String email, String password, String phoneNumber,
                         Organization organization, int level)
    {
        return personDAO.insert(firstName, lastName, email, password, phoneNumber, organization, level);
    }

    public Queue insertQueue(String name, Organization organization){
        return queueDAO.insert(name, organization);
    }

    public QueuePerPerson insert(Person person, Queue queue)
    {
        return queuePerPersonDAO.insert(person, queue);
    }

    public Ticket insert(String ticketTitle, String ticketDescription, TicketPriority ticketPriority,
                         TicketStatus ticketStatus, Organization organization, Person personCreated)
    {
        return ticketDAO.insert(ticketTitle, ticketDescription, ticketPriority, ticketStatus, personCreated);
    }

    public TicketPerPerson insert(Ticket ticket, Person person){
        return ticketPerPersonDAO.insert(ticket, person);
    }

    public TicketStatus insertTicketStatus(String ticketStatusName, Organization organization)
    {
        return ticketStatusDAO.insert(ticketStatusName, organization);
    }

    public TicketPriority insertTickePriority(String ticketPriorityNumber, Organization organization)
    {
        return ticketPriorityDAO.insert(ticketPriorityNumber, organization);
    }

    public TimePerPerson insert(Ticket ticket, Person person, double time, Date date)
    {
        return timePerPersonDAO.insert(ticket, person, time, date);
    }

    public TicketsPerQueue insert(Ticket ticket, Queue queue)
    {
        return ticketsPerQueueDAO.insert(ticket, queue);
    }

    public Organization findByName(String name)
    {
        return organizationDAO.find(name);
    }

    public void updateSubscription(Person person, Ticket ticket)
    {
        ticketPerPersonDAO.toggleTicketSubscription(person, ticket);
    }

    public void updateUserInfo(String firstName, String lastName, String email, String phoneNumber, int employeeID) {
        personDAO.updateUserInfo(firstName, lastName, email, phoneNumber, employeeID);
    }

    public Person getPerson(String email, String password)
    {
        return personDAO.findPerson(email, password);
    }

    public Collection<Organization> getOrganizationsChildren(int parentOrganizationID)
    {
        return organizationDAO.getOrganizationsChildren(parentOrganizationID);
    }

    public Person findByEmail(String email)
    {
        return personDAO.findByEmail(email);
    }

    public Queue findQueueByName(String name, Organization organization){
        return queueDAO.findByName(name, organization);
    }

    public Message findMessage(String text, Timestamp time, Ticket ticket)
    {
        return messageDAO.findMessage(text, time, ticket);
    }

    public TimePerPerson findTimePerPerson(Date date, Ticket ticket, Person person)
    {
        return timePerPersonDAO.findTimePerPerson(date, ticket, person);
    }

    public TicketPerPerson find(int ticketPerPersonID)
    {
        return ticketPerPersonDAO.find(ticketPerPersonID);
    }

    public TicketPerPerson find(Ticket ticket, Person person)
    {
        return ticketPerPersonDAO.find(ticket, person);
    }

    public TicketsPerQueue find(Ticket ticket, Organization organization)
    {
        return ticketsPerQueueDAO.find(ticket, organization);
    }

    public TicketsPerQueue find(Queue queue, Ticket ticket)
    {
        return ticketsPerQueueDAO.find(queue, ticket);
    }

    public void updateTicketPerQueue(TicketsPerQueue ticketsPerQueue, Queue newQueue)
    {
        ticketsPerQueueDAO.updateTicketPerQueue(newQueue, ticketsPerQueue);
    }

    public void closeTicket(Ticket ticket, Person person)
    {
        ticketDAO.closeTicket(ticket, person);
    }

    public void updateTicketPriority(TicketPriority oldPriority, TicketPriority newPriority, Ticket ticket)
    {
        ticketDAO.updateTicketPriority(oldPriority, newPriority, ticket);
    }

    public boolean findSubscribers(Ticket ticket, Organization organization)
    {
        return ticketPerPersonDAO.findSubscribers(ticket, organization);
    }

    public Collection<Queue> getOrganizationQueues(int organizationID)
    {
        return queueDAO.getOrganizationQueues(organizationID);
    }

    public Collection<Ticket> getOldTicketPerPerson(int employeeID){
        return ticketPerPersonDAO.getOldTicketPerPerson(employeeID);
    }

    public QueuePerPerson findQueuePerPerson(Person person, Queue queue){
        return queuePerPersonDAO.find(person, queue);
    }

    public void updateTimePerPerson(int timePerPersonid, double time, Person person, Ticket ticket) {
        timePerPersonDAO.updateTime(timePerPersonid, time, person, ticket);
    }

    public Queue updateDeleted(int queueID)
    {
        return queueDAO.updateDeleted(queueID);
    }

    public void insertFakeData()
    {
        organizationDAO.insert("fake", null);
        insert("fake", "fake", "fake", "fake", "fake",
                findByName("fake"),  3);
    }


}
