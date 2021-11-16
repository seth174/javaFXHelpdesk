package helpDeskTeamManager.addUsers;

import buttonCalls.ButtonCalls;
import dao.OrganizationDAO;
import error.Error;
import autoCompleteTextField.AutoCompleteTextField;
import dao.DatabaseManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import main.Driver;
import models.Organization;
import models.Person;
import models.Queue;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

public class AddUsersController extends ButtonCalls implements Initializable {
    @FXML
    private AutoCompleteTextField autoComplete;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField email;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField password;

    Stage stage = Driver.getStage();

    private boolean var = false;

    DatabaseManager dbm = Driver.getDbm();
    Person person = dbm.findPersonByID(Driver.getEmployeeID());
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       setComboBox();
       setEnterRequest();

        comboBox.setButtonCell(new ListCell(){

            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item==null || var){
                    // styled like -fx-prompt-text-fill:
                    setStyle("-fx-text-fill: white");
                    if(!empty || item != null)
                        setText(item.toString());
                    else
                        setText("Type of User");
                }
                else {
                    setStyle("-fx-text-fill: -fx-text-inner-color");
                    setText(item.toString());
                    var = true; //after first time through, it will no longer update the combo box items
                }
            }

        });


        Collection<Organization> orgs =  dbm.getOrganizationsChildren(person.getOrganization().getOrganizationID());
        Collection<String> orgName = new ArrayList<>();
        orgName.add(person.getOrganization().getName());
        for(Organization o: orgs)
        {
            orgName.add(o.getName());
        }
        autoComplete.getEntries().addAll(orgName);

        dbm.commit();

        Platform.runLater(() -> firstName.requestFocus());
    }

    public void createUser()
    {
        if(checkInput())
        {
            Organization org = dbm.findByName(autoComplete.getText());
            int level = 1;
            if(comboBox.getValue().equals("HelpDeskWorker"))
                level = 2;
            else if(comboBox.getValue().equals("SuperUser"))
                level = 3;
            dbm.insert(firstName.getText(), lastName.getText(), email.getText(), password.getText(),
                    phoneNumber.getText(), org, level);
            Error.error("User created successfully");
            Queue queue = dbm.insertQueue(email.getText() + " Tickets", org);
            dbm.insert(dbm.findByEmail(email.getText()), queue);
            Queue queue2 = dbm.findQueueByName(org.getName() + " Queue", org);
            dbm.insert(dbm.findByEmail(email.getText()), queue2);
            clear();

        }
        dbm.commit();
    }

    public boolean checkInput()
    {
        if(firstName.getText().strip().equals(""))
        {
            Error.error("Please enter a first name");
            return false;
        }
        else if(lastName.getText().strip().equals(""))
        {
            Error.error("Please enter a last name");
            return false;
        }
        else if(email.getText().strip().equals(""))
        {
            Error.error("Please enter an email");
            return false;
        }
        else if(dbm.findByEmail(email.getText()) != null)
        {
            Error.error("That email already exist");
            return false;
        }
        else if(phoneNumber.getText().strip().equals(""))
        {
            Error.error("Please enter a phoneNumber");
            return false;
        }
        else if(password.getText().strip().equals(""))
        {
            Error.error("Please enter a password");
            return false;
        }
        else if(autoComplete.getText().strip().equals(""))
        {
            Error.error("Please enter an organization");
            return false;
        }
        else if(dbm.findByName(autoComplete.getText().strip()) == null)
        {
            Error.error("The organization does not exist");
            return false;
        }
        else if(comboBox.getValue() == null)
        {
            Error.error("Please enter a type of user");
            return false;
        }
        return true;
    }

    public void clear()
    {
        autoComplete.setText(null);
        firstName.setText(null);
        lastName.setText(null);
        email.setText(null);
        phoneNumber.setText(null);
        password.setText(null);
        setComboBox();
    }

    public void setComboBox()
    {
        var = false;
        comboBox.getItems().clear();
        comboBox.getItems().addAll("Client", "HelpDeskWorker", "SuperUser");
    }

    public void setEnterRequest()
    {
        firstName.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER)
                createUser();
        });
        lastName.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER)
                createUser();
        });
        password.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER)
                createUser();
        });
        autoComplete.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER)
                createUser();
        });
        phoneNumber.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER)
                createUser();
        });
        email.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER)
                createUser();
        });
    }
}
