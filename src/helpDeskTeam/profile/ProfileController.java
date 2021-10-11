package helpDeskTeam.profile;

import buttonCalls.ClientButtons;
import buttonCalls.HelpDeskTeamButtons;
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

public class ProfileController extends HelpDeskTeamButtons implements Initializable {

    private static Stage stage = Driver.getStage();
    @FXML
    private Button buttonProfile;
    @FXML
    private Button buttonTickets;
    @FXML
    private Button buttonContacts;
    @FXML
    private Button editName;
    @FXML
    private Button editEmail;
    @FXML
    private Button editPhone;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

}
