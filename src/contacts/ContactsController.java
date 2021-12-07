package contacts;


import buttonCalls.ButtonCalls;
import dao.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Driver;
import models.Organization;
import models.Person;
import error.Error;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

public class ContactsController extends ButtonCalls implements Initializable {
    private static Stage stage = Driver.getStage();
    private static DatabaseManager dbm = Driver.getDbm();
    private Collection<Organization> organizations = new ArrayList<>();
    @FXML
    private MenuButton menuButton;
    @FXML
    private VBox leftVBox;
    @FXML
    private VBox middleVBox;
    @FXML
    private VBox rightVBox;
    @FXML
    private ButtonBar buttonBar;
    private Person person = dbm.findPersonByID(Driver.getEmployeeID());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        organizations.add(person.getOrganization());
        if(dbm.findPersonByID(Driver.getEmployeeID()).getOrganization().getParentOrganization() != null)
            organizations.add(dbm.findPersonByID(Driver.getEmployeeID()).getOrganization().getParentOrganization());
        Collection<Organization> children = person.getOrganization().getOrganizationsChildren();
        organizations.addAll(children);
        dbm.commit();

        for(Organization o: organizations)
        {
            MenuItem item = new MenuItem(o.getName());
            menuButton.getItems().add(item);
            item.setOnAction(e -> {
                clear();
                display(o.getName());
            });
        }

        menuButton.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.A)
            {
                System.out.println("Key Pressed");
            }
        });

    }

    public void clear()
    {
        leftVBox.getChildren().clear();
        middleVBox.getChildren().clear();
        rightVBox.getChildren().clear();
    }

    public void display(String organizationName)
    {
        if(organizationName == null)
            return;
        Organization org =  dbm.findByName(organizationName);

        Collection<Person> people = dbm.getPeopleByOrganizationID(org.getOrganizationID());

        dbm.commit();

        updateScreen(people);

        dbm.commit();

    }

    public void addText(String t1, String t2, String t3, String styleClass)
    {
        Text text1 = new Text(t1);
        Text text2 = new Text(t2);
        Text text3 = new Text(t3);

        text1.getStyleClass().add(styleClass);
        text2.getStyleClass().add(styleClass);
        text3.getStyleClass().add(styleClass);

        leftVBox.getChildren().add(text1);
        middleVBox.getChildren().add(text2);
        rightVBox.getChildren().add(text3);
    }

    public void updateScreen(Collection<Person> people)
    {
        addText("Name", "Email", "Phone Number", "BigText");
        for(Person p : people)
        {
            addText(p.getFirstName() + " " + p.getLastName(), p.getEmail(), p.getPhoneNumber(), "MediumText");
        }
    }

    public void addPeople()
    {
        Person person = Driver.getDbm().findPersonByID(Driver.getEmployeeID());
        if(Driver.getDbm().findPersonByID(Driver.getEmployeeID()).getLevel() == 1 ||
                Driver.getDbm().findPersonByID(Driver.getEmployeeID()).getLevel() == 2)
        {
            Error.error("Contact " + person.getOrganization().getParentOrganization().getName()+
                    "\n" + "to add people to your organization ");
        }
        else
        {
            loadCreateUsers();
        }
    }

    public void addOrganization()
    {
        Person person = Driver.getDbm().findPersonByID(Driver.getEmployeeID());
        if(person.getLevel() == 1 ||
                person.getLevel() == 2)
        {
            Error.error("Contact " + person.getOrganization().getParentOrganization().getName()+
                    "\n" + "to add another organization");
        }
        else
        {
            loadAddOrganization();
        }
    }

}
