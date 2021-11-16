package helpDeskTeamManager.addOrganization;

import buttonCalls.ButtonCalls;
import error.Error;
import dao.DatabaseManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import main.Driver;
import models.Person;

import java.net.URL;
import java.util.ResourceBundle;

public class AddOrganizationController extends ButtonCalls implements Initializable {
    @FXML
    private TextField organizationName;

    private DatabaseManager dbm = Driver.getDbm();
    private Person person = dbm.findPersonByID(Driver.getEmployeeID());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> organizationName.requestFocus());

        organizationName.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER)
                submit();
        });
    }

    public void submit()
    {
        if(check())
        {
            dbm.insert(organizationName.getText(), person.getOrganization());
            Error.error("Organization Created Successfully");
            dbm.insertQueue(organizationName.getText() + " Queue", dbm.findByName(organizationName.getText()));
            person.getOrganization().invalidateQueues();
            person.getOrganization().invalidateChildren();

        }
        dbm.commit();
    }

    public boolean check()
    {

        if(organizationName.getText().trim().equals(""))
        {
            Error.error("Please write a name");
            dbm.commit();
            return false;
        }
        if(dbm.findByName(organizationName.getText()) != null)
        {
            Error.error("Organization name already exist!\nPick another");
            dbm.commit();
            return false;
        }
        return true;
    }
}
