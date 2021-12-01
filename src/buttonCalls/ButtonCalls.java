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

    public void load(String fxml, String stageTitle)
    {
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
        load("/sharedCode/clientAndTeamProfile/Profile.fxml", "Profile");
    }

    public void loadContacts()
    {
        load("/sharedCode/contacts/contacts.fxml", "Contacts");
    }

    public void loadTickets()
    {
        load("/helpdeskTeam/tickets/ticketMainPage.fxml", "Tickets");
    }

    public void loadCurrentTickets()
    {
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/client/tickets/tickets.fxml"));
            stage.setScene(new Scene(root1));
            stage.setFullScreen(true);
            stage.show();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public void loadAssignedUsers()
    {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/client/tickets/assignedUsers/assignedUsers.fxml"));
            window.setScene(new Scene(root1));
            window.setTitle("Assigned Users");
            window.setFullScreen(true);
            window.showAndWait();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public void loadTicketPage()
    {
        load("/helpDeskTeam/tickets/ticketPage.fxml", "Tickets");
    }

    public void loadOldTickets()
    {
        if(level == 1)
        {
            try
            {
                Parent root1 = FXMLLoader.load(getClass().getResource("/client/tickets/oldTickets.fxml"));
                stage.setScene(new Scene(root1));
                stage.setFullScreen(true);
                stage.show();
            }
            catch (IOException e)
            {
                System.out.println(e);
            }
        }
        else
        {
            try
            {
                Parent root1 = FXMLLoader.load(getClass().getResource("/helpDeskteam/tickets/oldTicketsPage.fxml"));
                stage.setScene(new Scene(root1));
                stage.setFullScreen(true);
                stage.show();
            }
            catch (IOException e)
            {
                System.out.println(e);
            }
        }

    }

    public void loadAddUsers()
    {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/client/tickets/assignedUsers/addUsers.fxml"));
            window.setScene(new Scene(root1));
            window.setTitle("Add Users");
            window.showAndWait();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
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
        load("/helpDeskTeamManager/add/add.fxml", "Add");
    }

    public void loadManageQueue()
    {
        load("/helpDeskTeamManager/manageQueue/manageQueue.fxml", "Manage Queue");
    }

    public void loadAddOrganization(){
        load("/helpDeskTeamManager/addOrganization/addOrganization.fxml", "Organization");
    }

    public void loadCreateUsers()
    {
        load("/helpDeskTeamManager/addUsers/addUsers.fxml", "Create Users");
    }

    public void loadViewQueue()
    {
        load("/helpDeskTeamManager/viewQueue/viewQueue.fxml", "View Queue");
    }

    public void loadAddQueue()
    {
        load("/helpDeskTeamManager/addQueue/addQueue.fxml", "Add Queue");
    }

    public void loadDeleteQueue()
    {
        load("/helpDeskTeamManager/deleteQueue/deleteQueue.fxml", "Delete Queue");
    }

    public void loadAddUsersToQueue()
    {
        load("/helpDeskTeamManager/addUsersToQueue/addUsersToQueue.fxml", "Add Users To Queue");
    }

    public void createNewTicket()
    {
        load("/helpDeskTeam/tickets/createTickets.fxml", "Create New Ticket");
    }

    public void loadAddTicketStatus()
    {
        load("/helpDeskTeamManager/addTicketStatus/addTicketStatus.fxml", "Add Ticket Status");
    }

    public void loadAddTicketPriority()
    {
        load("/helpDeskTeamManager/addTicketPriority/AddTicketPriority.fxml", "Add Ticket Priority");
    }

    public void logout()
    {
        load("/login/login.fxml", "login");
    }

}
