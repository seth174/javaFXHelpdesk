package zzzzArchive;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.Driver;

import java.io.IOException;

public abstract class HelpDeskTeamButtons {
    private static Stage stage = Driver.getStage();
    public void loadProfile()  {
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/helpDeskTeam/editProfile/Profile.fxml"));
            stage.setScene(new Scene(root1));
            stage.setTitle("Profile");
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
            Parent root1 = FXMLLoader.load(getClass().getResource("/helpDeskTeam/contacts/contacts.fxml"));
            stage.setScene(new Scene(root1));
            stage.setTitle("Contacts");
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
            Parent root1 = FXMLLoader.load(getClass().getResource("/tickets/ticketMainPage/ticketMainPage.fxml"));
            stage.setScene(new Scene(root1));
            stage.setTitle("Tickets");
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
            Parent root1 = FXMLLoader.load(getClass().getResource("/tickets/oldTicketsHelpDesk/oldTicketPage/oldTicketsPage.fxml"));
            stage.setScene(new Scene(root1));
            stage.setTitle("Old Tickets Select");
            stage.setFullScreen(true);
            stage.show();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }



}
