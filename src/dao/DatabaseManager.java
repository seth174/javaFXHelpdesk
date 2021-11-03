package dao;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;

import models.*;
import org.apache.derby.jdbc.ClientDriver;
import org.apache.derby.jdbc.EmbeddedDriver;

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

    private final String url = "jdbc:derby://localhost:1527/db/HelpSpotDB";

    public DatabaseManager() {
        driver = new ClientDriver();
        Properties prop = new Properties();
        prop.put("create", "false");

        // try to connect to an existing database
        try {
            conn = driver.connect(url, prop);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            // database doesn't exist, so try creating it
            try {
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

    public Collection<TicketPerPerson> getTicketPerPerson(int employeeID)
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

    public TicketStatus findTicketStatusByID(int ticketStatusID)
    {
        return ticketStatusDAO.find(ticketStatusID);
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

    public Message insert(Ticket ticket, Message messageReplyTo, Person person, String messageContent)
    {
        return messageDAO.insert(ticket, messageReplyTo, person, messageContent);
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

    public Queue insert(String name){
        return queueDAO.insert(name);
    }

    public QueuePerPerson insert(Person person, Queue queue)
    {
        return queuePerPersonDAO.insert(person, queue);
    }

    public Ticket insert(String ticketTitle, String ticketDescription, TicketPriority ticketPriority,
                         TicketStatus ticketStatus, Organization organization, Queue queue, Person personCreated)
    {
        return ticketDAO.insert(ticketTitle, ticketDescription, ticketPriority, ticketStatus, organization,
                queue, personCreated);
    }

    public TicketPerPerson insert(Ticket ticket, Person person){
        return ticketPerPersonDAO.insert(ticket, person);
    }

    public TicketStatus insertTicketStatus(String ticketStatusName)
    {
        return ticketStatusDAO.insert(ticketStatusName);
    }

    public TicketPriority insertTickePriority(int ticketPriorityNumber)
    {
        return ticketPriorityDAO.insert(ticketPriorityNumber);
    }

    public TimePerPerson insert(Ticket ticket, Person person, double time)
    {
        return timePerPersonDAO.insert(ticket, person, time);
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

    public void insertFakeOrganization()
    {
        System.out.println("HERE");
        Organization helpSpot =  organizationDAO.insert("helpSpot", null);
        Organization dePauwHelpDesk = organizationDAO.insert("DePauwHelpDesk", helpSpot);
        organizationDAO.insert("CS Department", dePauwHelpDesk);
        organizationDAO.insert("Math Department", dePauwHelpDesk);
        organizationDAO.insert("English Department", dePauwHelpDesk);
        Organization bigTech = organizationDAO.insert("BigTech", helpSpot);
        organizationDAO.insert("Google", bigTech);
        organizationDAO.insert("Microsoft", bigTech);
        organizationDAO.insert("FaceBook", bigTech);
        organizationDAO.insert("Uber", bigTech);
        commit();
    }

    public void fakePeople()
    {
        insert("Seth", "Fagen", "Sethfagen_2022@depauw.edu", "1234", "2385777",
         findByName("helpSpot"),  3);
        insert("Steven", "Boegarts", "stevenBoegarts@depauw.edu", "1235", "254",
                findByName("CS Department"),  1);
        insert("Scott", "Thede", "sThede@depauw.edu", "1236", "2543",
                findByName("CS Department"),  1);
        insert("Khadija", "Stewart", "Khadija@depauw.edu", "1237", "654",
                findByName("CS Department"),  1);
        insert("HelpDesk", "DePauw", "helpdesk@depauw.edu", "1238", "734",
                findByName("DePauwHelpDesk"),  2);
        insert("1", "1", "1", "1", "1", findByName("helpSpot"), 3);
        commit();
    }

    public void insertPriorities()
    {
        insertTickePriority(0);
        insertTickePriority(1);
        insertTickePriority(2);
        insertTickePriority(3);
        insertTickePriority(4);
        insertTickePriority(5);
        commit();
    }

    public void insertStatus(){
        insertTicketStatus("Awaiting Feedback");
        insertTicketStatus("Response Provided");
        insertTicketStatus("Closed");
        insertTicketStatus("On hold");
        commit();
    }


}
