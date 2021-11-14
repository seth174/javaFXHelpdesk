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

import java.net.URL;
import java.util.ResourceBundle;

public class AddOrganizationController extends ButtonCalls implements Initializable {
    @FXML
    private TextField organizationName;

    private DatabaseManager dbm = Driver.getDbm();

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
            dbm.insert(organizationName.getText(), dbm.findPersonByID(Driver.getEmployeeID()).getOrganization());
            Error.error("Organization Created Successfully");
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
