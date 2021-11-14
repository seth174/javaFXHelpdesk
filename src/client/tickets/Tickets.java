package client.tickets;

import zzzzArchive.ClientButtons;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.Driver;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Tickets extends ClientButtons implements Initializable {

    private static Stage stage = Driver.getStage();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


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


}
