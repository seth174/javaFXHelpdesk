package helpDeskTeamManager.addUsersToQueue;

import autoCompleteComboBox.ComboBoxAutoComplete;
import buttonCalls.ButtonCalls;
import dao.DatabaseManager;
import error.Error;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import main.Driver;
import models.Person;
import models.Queue;

import java.net.URL;
import java.util.*;

public class AddUsersToQueue extends ButtonCalls implements Initializable {
    @FXML
    private ComboBox<String> userComboBox;
    @FXML
    private ComboBox<String> queueComboBox;
    private DatabaseManager dbm = Driver.getDbm();
    private Person person = dbm.findPersonByID(Driver.getEmployeeID());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Collection<Person> people = person.getOrganization().getOrganizationPeople();
        TreeSet<String> names = new TreeSet<>();
        people.forEach(e -> names.add(e.getEmail().toLowerCase(Locale.ROOT)));
        userComboBox.getItems().addAll(names);

        Collection<Queue> queues = person.getOrganization().getOrganizationQueues();
        TreeSet<String> queueNames = new TreeSet<>();
        queues.forEach(e -> {
            if(!e.getDeleted())
                queueNames.add(e.getName().toLowerCase(Locale.ROOT));
        });
        queueComboBox.getItems().addAll(queueNames);

        new ComboBoxAutoComplete(queueComboBox);
        new ComboBoxAutoComplete(userComboBox);

        dbm.commit();
    }

    public void add()
    {
        if(check())
        {
            Person p = dbm.findPersonByID(dbm.findByEmail(userComboBox.getValue()).getEmployeeID());
            Queue q = dbm.findQueueByName(queueComboBox.getValue(), p.getOrganization());
            dbm.insert(p, q);
            p.invalidateQueuePerPerson();
            q.invalidateQueuePerPerson();
            dbm.commit();
            Error.error("Successfully added the Queue to the user");
        }
    }

    public boolean check()
    {
        if(queueComboBox.getValue() == null)
        {
            Error.error("Please select a Queue");
            return false;
        }
        else if(userComboBox.getValue() == null)
        {
            Error.error("Please select a user");
            return false;
        }
        else{
            Person p = dbm.findPersonByID(dbm.findByEmail(userComboBox.getValue()).getEmployeeID());
            Queue q = dbm.findQueueByName(queueComboBox.getValue(), p.getOrganization());
            if(dbm.findQueuePerPerson(p, q) != null)
            {
                Error.error("That person is already assigned that queue");
                dbm.commit();
                return false;
            }
            dbm.commit();
            return true;
        }
    }
}
