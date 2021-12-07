package buttonCalls;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Driver;

import java.io.IOException;

public abstract class ButtonCalls {
    private int level = Driver.getDbm().findPersonByID(Driver.getEmployeeID()).getLevel();
    private static Stage stage = Driver.getStage();
    private static String buttonPressed = "None";

    public void load(String fxml, String stageTitle, String buttonPressed)
    {
        ButtonCalls.buttonPressed = buttonPressed;
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
        load("/sharedCode/clientAndTeamProfile/Profile.fxml", "Profile", "Profile");
    }

    public void loadContacts()
    {
        load("/sharedCode/contacts/contacts.fxml", "Contacts", "Contacts");
    }

    public void loadTickets()
    {
        load("/helpdeskTeam/tickets/ticketMainPage.fxml", "Tickets", "Tickets");
    }

    public void loadAssignedUsers()
    {
        load("/client/tickets/assignedUsers/assignedUsers.fxml", "Assigned Users", "Tickets");
    }

    public void loadTicketPage()
    {
        load("/helpDeskTeam/tickets/ticketPage.fxml", "Tickets", "Tickets");
    }

    public void loadOldTickets()
    {
        load("/client/tickets/oldTickets.fxml", "Old Tickets", "Tickets");
    }

    public void loadOrganizationTickets()
    {
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/helpDeskTeam/tickets/selectOrganization.fxml"));
            stage.setScene(new Scene(root1));
            stage.setTitle("Organization Old Tickets");
            stage.setFullScreen(true);
            stage.show();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public void loadMyOldTickets()
    {
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/helpDeskTeam/tickets/myOldTickets.fxml"));
            stage.setScene(new Scene(root1));
            stage.setTitle("My Old Tickets");
            stage.setFullScreen(true);
            stage.show();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
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
        load("/helpDeskTeamManager/addUsers/addUsers.fxml", "Create Users", "Contacts");
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
        load("/helpDeskTeam/tickets/createTickets.fxml", "Create New Ticket", "Tickets");
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
