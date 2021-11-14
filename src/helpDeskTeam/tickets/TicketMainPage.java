package helpDeskTeam.tickets;

import buttonCalls.ButtonCalls;
import dao.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.stage.Stage;
import main.Driver;

import java.net.URL;
import java.util.ResourceBundle;

public class TicketMainPage extends ButtonCalls implements Initializable {
    private static Stage stage = Driver.getStage();
    @FXML
    private ButtonBar buttonBar;
    private DatabaseManager dbm = Driver.getDbm();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(dbm.findPersonByID(Driver.getEmployeeID()).getLevel() == 3)
        {
            Button add = new Button("Add");
            add.getStyleClass().add("Button");

            Button manageTeamQueue = new Button("Manage Team Queue");
            manageTeamQueue.getStyleClass().add("Button");

            Button stats = new Button("Statistics");
            stats.getStyleClass().add("Button");

            buttonBar.getButtons().add(add);
            buttonBar.getButtons().add(manageTeamQueue);
            buttonBar.getButtons().add(stats);

            manageTeamQueue.setOnAction(e -> loadManageQueue());
            add.setOnAction(e -> loadAdd());
        }
    }


}
