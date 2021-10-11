package helpDeskTeam.tickets;

import buttonCalls.HelpDeskTeamButtons;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class selectedOrganizationController extends HelpDeskTeamButtons implements Initializable {
    @FXML
    private Button closeButton;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void loadOrganizationOldTickets()
    {

    }

    public void close()
    {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
