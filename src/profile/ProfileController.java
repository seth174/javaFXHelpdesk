package profile;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import main.Driver;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
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
        Driver.loadButton(buttonTickets);
        Driver.loadButton(buttonContacts);
        Driver.loadButton(editEmail);
        Driver.loadButton(editName);
        Driver.loadButton(editPhone);
    }
}
