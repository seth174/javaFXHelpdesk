package helpDeskTeamManager.addTicketStatus;

import buttonCalls.ButtonCalls;
import dao.DatabaseManager;
import error.Error;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import main.Driver;
import models.Person;

import java.net.URL;
import java.util.ResourceBundle;

public class AddTicketStatus extends ButtonCalls implements Initializable {
    @FXML
    private TextField ticketStatusName;
    private DatabaseManager dbm = Driver.getDbm();
    private Person person = dbm.findPersonByID(Driver.getEmployeeID());

    public void submit()
    {
        if(check())
        {
            dbm.insertTicketStatus(ticketStatusName.getText(), person.getOrganization());
            Error.error("Added Ticket Status successfully ");
            dbm.commit();
        }
    }

    public boolean check()
    {
        if(ticketStatusName.getText().equals(""))
        {
            Error.error("Please enter a name");
            dbm.commit();
            return false;
        }
        else if(dbm.findStatusByName(ticketStatusName.getText(), person.getOrganization()) != null)
        {
            Error.error("Ticket Status already exist");
            dbm.commit();
            return false;
        }
        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ticketStatusName.setOnKeyPressed( e -> {
            if(e.getCode() == KeyCode.ENTER)
            {
                submit();
            }
        });
    }
}
