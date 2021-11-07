package helpDeskTeamManager.addQueue;

import buttonCalls.HelpDeskTeamManagerButtons;
import dao.DatabaseManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import main.Driver;

import java.net.URL;
import java.util.ResourceBundle;

public class QueueController extends HelpDeskTeamManagerButtons implements Initializable  {
    @FXML
    private TextField name;
    private DatabaseManager dbm = Driver.getDbm();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> name.requestFocus());
    }

    public void close()
    {
        loadManageQueue();
    }

    public void insertQueue()
    {

    }
}
