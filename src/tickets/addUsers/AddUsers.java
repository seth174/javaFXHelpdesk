package tickets.addUsers;

import autoCompleteComboBox.ComboBoxAutoComplete;
import dao.DatabaseManager;
import error.Error;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.Driver;
import models.*;

import java.util.Collection;
import java.util.Objects;
import java.util.TreeSet;

public class AddUsers {
    private DatabaseManager dbm = Driver.getDbm();
    private Organization organization;
    private Ticket ticket;
    @FXML
    private Button closeButton;
    @FXML
    private ComboBox<String> comboBox;


    public void loadComboBox(Organization organization)
    {
        Collection<Person> people = dbm.getPeopleByOrganizationID(organization.getOrganizationID());
        TreeSet<String> peopleNames = new TreeSet<>();
        for(Person p: people)
            peopleNames.add(p.getEmail());
        comboBox.getItems().addAll(peopleNames);
        new ComboBoxAutoComplete<>(comboBox);
        dbm.commit();
    }

    public void addUser()
    {
        if(!check())
        {
            return;
        }
        String email = comboBox.getValue();
        Person person = dbm.findByEmail(email);
        if(dbm.find(ticket, person) == null)
        {
            dbm.insert(ticket, person);
        }
        else
        {
            dbm.updateSubscription(person, ticket);
        }

        person.invalidateTicketsPerPerson();
        ticket.invalidateTicketPerPerson();

        updateQueues(person.getOrganization());
        dbm.commit();

        Stage window = (Stage)closeButton.getScene().getWindow();

        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public void updateQueues(Organization organization)
    {
        Queue queue = dbm.find(ticket, organization).getQueue();
        if(!queue.getName().equalsIgnoreCase("Assigned"))
        {
            Queue oldQueueTest = dbm.find(ticket, organization).getQueue();
            Queue newQueue = dbm.findQueueByName("Assigned", organization);
            dbm.updateTicketPerQueue(dbm.find(oldQueueTest, ticket), newQueue);
        }
        dbm.commit();
    }

    public boolean check()
    {
        if(comboBox.getValue() == null)
        {
            Error.error("Please pick a name");
            return false;
        }
        else
        {
            TicketPerPerson ticketPerPerson = dbm.find(ticket, dbm.findByEmail(comboBox.getValue()));
            if(!Objects.isNull(ticketPerPerson))
            {
                if(ticketPerPerson.getSubscribed())
                {
                    Error.error("Person is already assigned");
                    return false;
                }
            }

        }
        return true;
    }

    public void closeAddUsers()
    {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void setTicket(Ticket ticket)
    {
        this.ticket = ticket;
    }

    public void setOrganization(Organization organization)
    {
        this.organization = organization;
    }

}
