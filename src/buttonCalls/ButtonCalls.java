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
        if(level == 1 || level == 2)
        {
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
        else if(level == 3)
        {
            try
            {
                Parent root1 = FXMLLoader.load(getClass().getResource("/helpDeskTeamManager/profile/Profile.fxml"));
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

    public void loadContacts()
    {
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/client/contacts/contacts.fxml"));
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
                System.out.println("HERERE");
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
            Parent root1 = FXMLLoader.load(getClass().getResource("/client/tickets/ticketPage.fxml"));
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
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/helpDeskTeam/tickets/selectOrganization.fxml"));
            window.setScene(new Scene(root1));
            window.setTitle("Select Organization");
            window.showAndWait();
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


}
