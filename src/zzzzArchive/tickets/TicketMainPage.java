package zzzzArchive.tickets;

import buttonCalls.ButtonCalls;
import dao.DatabaseManager;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.Driver;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TicketMainPage extends ButtonCalls implements Initializable {
    private static Stage stage = Driver.getStage();
    private static DatabaseManager dbm = Driver.getDbm();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void loadOldTickets()
    {
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/zzzzArchive/tickets/oldTicketsPage.fxml"));
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
