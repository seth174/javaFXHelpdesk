package client.dashboard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import main.Driver;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    private static Stage myStage = Driver.getStage();
    @FXML
    private Button buttonProfile;
    @FXML
    private Button buttonTickets;
    @FXML
    private Button buttonContacts;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Driver.loadButton(buttonProfile);
        Driver.loadButton(buttonTickets);
        Driver.loadButton(buttonContacts);
    }

    public void loadProfile()  {
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/profile/Profile.fxml"));
            myStage.setScene(new Scene(root1));
            myStage.setFullScreen(true);
            myStage.show();
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
            Parent root1 = FXMLLoader.load(getClass().getResource("/client/contacts/contacts.fxml"));
            myStage.setScene(new Scene(root1));
            myStage.setFullScreen(true);
            myStage.show();
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
            myStage.setScene(new Scene(root1));
            myStage.setFullScreen(true);
            myStage.show();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }
}
