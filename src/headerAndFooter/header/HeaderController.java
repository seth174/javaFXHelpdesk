package headerAndFooter.header;

import buttonCalls.ButtonCalls;
import dao.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import main.Driver;

import java.net.URL;
import java.util.ResourceBundle;

public class HeaderController extends ButtonCalls implements Initializable {

    @FXML
    private ButtonBar buttonBar;
    @FXML
    private Button tickets;
    @FXML
    private Button contacts;
    @FXML
    private Button profile;
    private DatabaseManager dbm = Driver.getDbm();
    private Button add;
    private Button manageTeamQueue;
    private Button stats;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(dbm.findPersonByID(Driver.getEmployeeID()).getLevel() == 3)
        {
            add = new Button("Add");
            add.getStyleClass().add("Button");

            manageTeamQueue = new Button("Manage Team Queue");
            manageTeamQueue.getStyleClass().add("Button");

            stats = new Button("Statistics");
            stats.getStyleClass().add("Button");

            buttonBar.getButtons().add(add);
            buttonBar.getButtons().add(manageTeamQueue);
            buttonBar.getButtons().add(stats);

            manageTeamQueue.setOnAction(e -> loadManageQueue());
            add.setOnAction(e -> loadAdd());
        }
        dbm.commit();

        loadButtonPressed();
    }

    public void loadButtonPressed()
    {
        switch (ButtonCalls.getButtonPressed()){
            case "Tickets":
                tickets.getStyleClass().add("ButtonPressed");
                break;
            case "Contacts":
                contacts.getStyleClass().add("ButtonPressed");
                break;
            case "Profile":
                profile.getStyleClass().add("ButtonPressed");
                break;
            case "Add":
                add.getStyleClass().add("ButtonPressed");
                break;
            case "ManageTeamQueue":
                manageTeamQueue.getStyleClass().add("ButtonPressed");
                break;
            case "Stats":
                stats.getStyleClass().add("ButtonPressed");
                break;
            default:
                break;
        }
    }
}
