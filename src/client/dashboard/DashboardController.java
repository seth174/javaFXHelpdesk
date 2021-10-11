package client.dashboard;

import buttonCalls.ClientButtons;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import main.Driver;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController extends ClientButtons implements Initializable {
    private static Stage myStage = Driver.getStage();
    @FXML
    private Button buttonProfile;
    @FXML
    private Button buttonTickets;
    @FXML
    private Button buttonContacts;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
