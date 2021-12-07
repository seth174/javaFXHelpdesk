package helpDeskTeamManager.viewQueue;

import buttonCalls.ButtonCalls;
import dao.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.Driver;
import models.Person;
import models.Queue;
import models.QueuePerPerson;

import java.net.URL;
import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;

public class ViewController extends ButtonCalls implements Initializable {
    private DatabaseManager dbm = Driver.getDbm();
    private Person person = dbm.findPersonByID(Driver.getEmployeeID());
    @FXML
    private MenuButton menuButton;
    @FXML
    private Text userText;
    @FXML
    private VBox vBox;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Collection<Person> users = person.getOrganization().getOrganizationPeople();
        for(Person p: users)
        {
            MenuItem email = new MenuItem(p.getEmail());
            menuButton.getItems().add(email);
            email.setOnAction(e -> {
                updateScreen(email);
            });
        }
        dbm.commit();
    }

    public void updateScreen(MenuItem item)
    {
        clear();
        Person selected = dbm.findByEmail(item.getText().toLowerCase(Locale.ROOT));
        dbm.commit();
        updateUserInfo(selected);
        updateQueue(selected);
    }

    public void updateUserInfo(Person p)
    {
        userText.setText("User: " + p.getEmail());
    }

    public void updateQueue(Person p)
    {
        Collection<QueuePerPerson> queues = p.getQueuePerPerson();
        for(QueuePerPerson qp: queues)
        {
            if(!qp.getQueue().getDeleted())
            {
                Text t = new Text(qp.getQueue().getName());
                t.getStyleClass().add("WhiteText");
                t.getStyleClass().add("BigText");
                vBox.getChildren().add(t);
            }

        }
        dbm.commit();
    }

    public void clear()
    {
        vBox.getChildren().clear();
    }

}
