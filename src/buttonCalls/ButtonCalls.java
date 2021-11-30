package buttonCalls;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Driver;

import java.io.IOException;

public abstract class ButtonCalls {
    private int level = Driver.getDbm().findPersonByID(Driver.getEmployeeID()).getLevel();
    private static Stage stage = Driver.getStage();
    
    public void loadProfile()  {
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/sharedCode/clientAndTeamProfile/Profile.fxml"));
            stage.setScene(new Scene(root1));
            stage.setFullScreen(true);
            stage.show();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }

    }

    public void loadContacts()
    {
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/sharedCode/contacts/contacts.fxml"));
            stage.setScene(new Scene(root1));
            stage.setFullScreen(true);
            stage.show();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public void loadTickets()
    {
        if(level == 1) {
            try {
                Parent root1 = FXMLLoader.load(getClass().getResource("/client/tickets/tickets.fxml"));
                stage.setScene(new Scene(root1));
                stage.setFullScreen(true);
                stage.show();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        else{
            try {
                Parent root1 = FXMLLoader.load(getClass().getResource("/helpdeskTeam/tickets/ticketMainPage.fxml"));
                stage.setScene(new Scene(root1));
                stage.setFullScreen(true);
                stage.show();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
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
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/helpDeskTeam/tickets/ticketPage.fxml"));
            stage.setScene(new Scene(root1));
            stage.setFullScreen(true);
            stage.show();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
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
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/helpDeskTeamManager/add/add.fxml"));
            stage.setScene(new Scene(root1));
            stage.setFullScreen(true);
            stage.show();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public void loadManageQueue()
    {
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/helpDeskTeamManager/manageQueue/manageQueue.fxml"));
            stage.setScene(new Scene(root1));
            stage.setFullScreen(true);
            stage.show();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public void loadAddOrganization(){
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/helpDeskTeamManager/addOrganization/addOrganization.fxml"));
            stage.setScene(new Scene(root1));
            stage.setFullScreen(true);
            stage.show();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public void loadCreateUsers()
    {
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/helpDeskTeamManager/addUsers/addUsers.fxml"));
            stage.setScene(new Scene(root1));
            stage.setFullScreen(true);
            stage.show();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public void loadViewQueue()
    {
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/helpDeskTeamManager/viewQueue/viewQueue.fxml"));
            stage.setScene(new Scene(root1));
            stage.setFullScreen(true);
            stage.show();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public void loadAddQueue()
    {
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/helpDeskTeamManager/addQueue/addQueue.fxml"));
            stage.setScene(new Scene(root1));
            stage.setFullScreen(true);
            stage.show();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public void loadDeleteQueue()
    {
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/helpDeskTeamManager/deleteQueue/deleteQueue.fxml"));
            stage.setScene(new Scene(root1));
            stage.setFullScreen(true);
            stage.show();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public void loadAddUsersToQueue()
    {
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/helpDeskTeamManager/addUsersToQueue/addUsersToQueue.fxml"));
            stage.setScene(new Scene(root1));
            stage.setFullScreen(true);
            stage.show();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public void createNewTicket()
    {
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/helpDeskTeam/tickets/createTickets.fxml"));
            stage.setScene(new Scene(root1));
            stage.setFullScreen(true);
            stage.show();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public void loadAddTicketStatus()
    {
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/helpDeskTeamManager/addTicketStatus/addTicketStatus.fxml"));
            stage.setScene(new Scene(root1));
            stage.setFullScreen(true);
            stage.show();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public void loadAddTicketPriority()
    {
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/helpDeskTeamManager/addTicketPriority/AddTicketPriority.fxml"));
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
