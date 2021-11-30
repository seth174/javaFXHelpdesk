package helpDeskTeam.tickets;

import autoCompleteComboBox.ComboBoxAutoComplete;
import buttonCalls.ButtonCalls;
import dao.DatabaseManager;
import error.Error;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import main.Driver;
import models.*;
import models.Queue;

import java.net.URL;
import java.util.*;

public class CreateTickets extends ButtonCalls implements Initializable {

    private DatabaseManager dbm = Driver.getDbm();
    private Person person = dbm.findPersonByID(Driver.getEmployeeID());
    @FXML
    private ButtonBar buttonBar;
    @FXML
    private MenuButton organization;
    @FXML
    private ComboBox<String> priority;
    @FXML
    private ComboBox<String> status;
    @FXML
    private TextField title;
    @FXML
    private TextArea description;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(dbm.findPersonByID(Driver.getEmployeeID()).getLevel() == 3)
        {
            Button add = new Button("Add");
            add.getStyleClass().add("Button");

            Button manageTeamQueue = new Button("Manage Team Queue");
            manageTeamQueue.getStyleClass().add("Button");

            Button stats = new Button("Statistics");
            stats.getStyleClass().add("Button");

            buttonBar.getButtons().add(add);
            buttonBar.getButtons().add(manageTeamQueue);
            buttonBar.getButtons().add(stats);

            manageTeamQueue.setOnAction(e -> loadManageQueue());
            add.setOnAction(e -> loadAdd());
        }

        Collection<Organization> organizations = person.getOrganization().getOrganizationsChildren();
        organizations.add(person.getOrganization());
        organizations.add(person.getOrganization().getParentOrganization());

        Comparator<MenuItem> menuItemComparator = Comparator.comparing(MenuItem::getText);
        TreeSet<MenuItem> organizationTreeSet = new TreeSet<>(menuItemComparator);
        for(Organization o : organizations)
        {
            MenuItem item = new MenuItem(o.getName());
            organizationTreeSet.add(item);

            item.setOnAction(e -> {
                Organization organization1 = dbm.findByName(o.getName());
                loadPriorities(organization1);
                organization.setText(organization1.getName());
                loadStatuses(organization1);

            });
        }

        organization.getItems().addAll(organizationTreeSet);

        dbm.commit();

    }

    public void loadPriorities(Organization organization)
    {
        Organization personOrganization = person.getOrganization();
        Collection<TicketPriority> priorities = null;
        //this will need work
        if(personOrganization == organization)
        {
            priorities = organization.getTicketPriorities();
        }
        else if(personOrganization.getParentOrganization() == organization)
        {
            priorities = organization.getTicketPriorities();
        }
        else
        {
            priorities = personOrganization.getTicketPriorities();
        }
        TreeSet<String> ticketPriorities = new TreeSet<>();
        if(priorities != null)
        {
            for(TicketPriority tp : priorities)
            {
                ticketPriorities.add(tp.getPriority());
            }

            priority.getItems().clear();
            priority.getItems().addAll(ticketPriorities);
        }

        new ComboBoxAutoComplete<>(priority);
        dbm.commit();
    }

    public void loadStatuses(Organization organization)
    {
        Organization personOrganization = person.getOrganization();
        Collection<TicketStatus> statuses = null;
        //this will need work
        if(personOrganization == organization)
        {
            statuses = organization.getTicketStatuses();
        }
        else if(personOrganization.getParentOrganization() == organization)
        {
            statuses = organization.getTicketStatuses();
        }
        else
        {
            statuses = personOrganization.getTicketStatuses();
        }
        TreeSet<String> ticketStatus = new TreeSet<>();
        if(statuses.size() > 0)
        {
            for(TicketStatus ts : statuses)
            {
                ticketStatus.add(ts.getTicketStatus());
            }

            status.getItems().clear();
            status.getItems().addAll(ticketStatus);
        }

        new ComboBoxAutoComplete<>(status);
        dbm.commit();
    }

    public void create()
    {
        if(check())
        {
            String description1 = description.getText();
            String title1 = title.getText();
            Organization organization1 = dbm.findByName(organization.getText());
            TicketPriority priority1;
            priority1 = dbm.findPriorityByName(priority.getValue(), organization1);
            TicketStatus ticketStatus = dbm.findStatusByName(status.getValue(), organization1);

            Ticket ticket = dbm.insert(title1, description1, priority1, ticketStatus, organization1, person);

            addQueues(ticket);

            dbm.commit();

            Error.error("Ticket Created");
        }
    }

    public void addQueues(Ticket ticket)
    {
        Queue queue1 = dbm.findQueueByName(person.getOrganization().getName() + " Queue", person.getOrganization());
        dbm.insert(ticket, queue1);

        Organization org = dbm.findByName(organization.getText());
        Queue queue2 = dbm.findQueueByName(org.getName() + " Queue", org);
        dbm.insert(ticket, queue2);
    }

    public boolean check()
    {
        if(organization.getText() == null)
        {
            Error.error("Please select which organization you would like to submit to");
            return false;
        }
        else if(priority.getValue() == null)
        {
            Error.error("Please select the priority of the ticket");
            return false;
        }
        else if(status.getValue().equals(""))
        {
            Error.error("Please select the status of the ticket");
            return false;
        }
        else if(title.getText().equals(""))
        {
            Error.error("Please enter a title");
            return false;
        }
        else if(description.getText().equals(""))
        {
            Error.error("Please enter a description");
            return false;
        }
        return true;
    }

}
