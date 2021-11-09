package helpDeskTeamManager.deleteQueue;

import autoCompleteTextField.AutoCompleteTextField;
import buttonCalls.HelpDeskTeamManagerButtons;
import dao.DatabaseManager;
import error.Error;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import main.Driver;
import models.Organization;
import models.Person;
import models.Queue;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

public class DeleteQueue extends HelpDeskTeamManagerButtons implements Initializable {

    private DatabaseManager dbm = Driver.getDbm();
    private Person person = dbm.findPersonByID(Driver.getEmployeeID());
    @FXML
    private AutoCompleteTextField textField;

    public void delete()
    {
        if(check())
        {
            int queueID = dbm.findQueueByName(textField.getText()).getQueueID();
            dbm.updateDeleted(queueID);
            dbm.commit();
            Error.error("Successfully deleted Queue");
        }
    }
    public void close()
    {
        loadManageQueue();
    }

    public boolean check()
    {
        if(textField.getText().equals(""))
        {
            Error.error("Please enter a Queue");
            return false;
        }
        else if(dbm.findQueueByName(textField.getText()).getDeleted())
        {
            Error.error("This Queue has been deleted");
            return false;
        }
        else if(dbm.findQueueByName(textField.getText()) == null)
        {
            Error.error("Queue does not exist");
            return false;
        }
        else if(dbm.findQueueByName(textField.getText()).getTicketsInQueue() != null)
        {
            //Add method to move tickets if queue has tickets
        }
        dbm.commit();
        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Collection<Queue> queues =  dbm.getOrganizationQueues(person.getOrganization().getOrganizationID());
        Collection<String> queueName = new ArrayList<>();
        for(Queue q: queues)
        {
            if(!q.getDeleted())
            {
                queueName.add(q.getName());
            }
        }
        textField.getEntries().addAll(queueName);

        dbm.commit();

        Platform.runLater(() -> textField.requestFocus());
    }
}
