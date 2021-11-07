package buttonCalls;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Driver;

import java.io.IOException;

public class HelpDeskTeamManagerButtons  {
    private static Stage stage = Driver.getStage();
    public void loadProfile()  {
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

    public void loadContacts()
    {
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/helpDeskTeamManager/contacts/contacts.fxml"));
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
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/helpDeskTeamManager/tickets/ticketMainPage.fxml"));
            stage.setScene(new Scene(root1));
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
            Parent root1 = FXMLLoader.load(getClass().getResource("/helpDeskTeamManager/tickets/myOldTickets.fxml"));
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

    public void loadAddUsers()
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
}
