package buttonCalls;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.stage.Stage;
import main.Driver;

import java.io.IOException;

public abstract class ButtonCalls {
    private int level = Driver.getDbm().findPersonByID(Driver.getEmployeeID()).getLevel();
    private static Stage stage = Driver.getStage();
    private static String buttonPressed = "None";

    public void load(String fxml, String stageTitle, String buttonPressed)
    {
        setButtonPressed(buttonPressed);
        Parent page = null;
        try {
            page = FXMLLoader.load(getClass().getResource(fxml), null, new JavaFXBuilderFactory());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        stage.setTitle(stageTitle);
        stage.getScene().setRoot(page);
    }
    public void loadProfile()  {
        load("/profile/profile/Profile.fxml", "Profile", "Profile");
    }

    public void loadContacts()
    {
        load("/contacts/contacts.fxml", "Contacts", "Contacts");
    }

    public void loadTickets()
    {
        load("/tickets/ticketMainPage/ticketMainPage.fxml", "Tickets", "Tickets");
    }

    public void loadAssignedUsers()
    {
        load("/tickets/assignedUsers/assignedUsers.fxml", "Assigned Users", "Tickets");
    }

    public void loadTicketPage()
    {
        load("/tickets/ticketPage/ticketPage.fxml", "Tickets", "Tickets");
    }

    public void loadOldTickets()
    {
        if(Driver.getDbm().findPersonByID(Driver.getEmployeeID()).getLevel() != 1)
        {
            load("/tickets/oldTicketsHelpDesk/oldTicketPage/oldTicketsPage.fxml", "Old Tickets", "Tickets");
        }
        else
        {
            load("/tickets/oldTickets/oldTickets.fxml", "Old Tickets", "Tickets");
        }
    }

    public void loadOldTicketsHelpDesk()
    {
        load("/tickets/oldTickets/oldTickets.fxml", "Old Tickets", "Tickets");
    }

    public void loadSelectOrganization()
    {
        load("/tickets/oldTicketsHelpDesk/selectOrganization/selectOrganization.fxml", "Select Organization", "Tickets");
    }

    public void loadAdd()
    {
        load("/helpDeskTeamManager/add/add.fxml", "Add", "Add");
    }

    public void loadManageQueue()
    {
        load("/helpDeskTeamManager/manageQueue/manageQueue.fxml", "Manage Queue", "ManageTeamQueue");
    }

    public void loadAddOrganization(){
        load("/helpDeskTeamManager/addOrganization/addOrganization.fxml", "Organization", "Add");
    }

    public void loadCreateUsers()
    {
        load("/helpDeskTeamManager/addUsers/addUsers.fxml", "Create Users", "Add");
    }

    public void loadViewQueue()
    {
        load("/helpDeskTeamManager/viewQueue/viewQueue.fxml", "View Queue", "ManageTeamQueue");
    }

    public void loadAddQueue()
    {
        load("/helpDeskTeamManager/addQueue/addQueue.fxml", "Add Queue", "ManageTeamQueue");
    }

    public void loadDeleteQueue()
    {
        load("/helpDeskTeamManager/deleteQueue/deleteQueue.fxml", "Delete Queue", "ManageTeamQueue");
    }

    public void loadAddUsersToQueue()
    {
        load("/helpDeskTeamManager/addUsersToQueue/addUsersToQueue.fxml", "Add Users To Queue", "ManageTeamQueue");
    }

    public void createNewTicket()
    {
        load("/tickets/createTickets/createTickets.fxml", "Create New Ticket", "Tickets");
    }

    public void loadAddTicketStatus()
    {
        load("/helpDeskTeamManager/addTicketStatus/addTicketStatus.fxml", "Add Ticket Status", "Add");
    }

    public void loadAddTicketPriority()
    {
        load("/helpDeskTeamManager/addTicketPriority/AddTicketPriority.fxml", "Add Ticket Priority", "Add");
    }

    public void logout()
    {
        load("/login/login.fxml", "login", "none");
    }

    public static void setButtonPressed(String buttonPressed)
    {
        ButtonCalls.buttonPressed = buttonPressed;
    }

    public static String getButtonPressed(){ return buttonPressed; }
}
