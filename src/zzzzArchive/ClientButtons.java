package zzzzArchive;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Driver;

import java.io.IOException;

public abstract class ClientButtons {
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
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/zzzzArchive/tickets.fxml"));
            stage.setScene(new Scene(root1));
            stage.setFullScreen(true);
            stage.show();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public void loadCurrentTickets()
    {
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/zzzzArchive/tickets.fxml"));
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

}
