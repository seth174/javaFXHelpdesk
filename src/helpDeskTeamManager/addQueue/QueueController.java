package helpDeskTeamManager.addQueue;

import buttonCalls.ButtonCalls;
import dao.DatabaseManager;
import error.Error;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import main.Driver;
import models.Person;

import java.net.URL;
import java.util.ResourceBundle;

public class QueueController extends ButtonCalls implements Initializable  {
    @FXML
    private TextField name;
    private DatabaseManager dbm = Driver.getDbm();
    private Person person = dbm.findPersonByID(Driver.getEmployeeID());
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> name.requestFocus());
        name.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER)
            {
                insertQueue();
            }
        });
    }

    public void close()
    {
        loadManageQueue();
    }

    public void insertQueue()
    {
        if(check())
        {
            dbm.insertQueue(name.getText(), person.getOrganization());
            Error.error("Successfully Added Queue");
            person.getOrganization().invalidateQueues();
            dbm.commit();
        }
    }

    public boolean check()
    {
        if(name.getText().equals(""))
        {
            Error.error("Please enter a name");
            return false;
        }
        else if(dbm.findQueueByName(name.getText(), person.getOrganization()) != null )
        {
            Error.error("Queue already exist");
            return false;
        }
        dbm.commit();
        return true;
    }
}
