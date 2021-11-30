package helpDeskTeamManager.addTicketPriority;

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

public class AddTicketPriority extends ButtonCalls implements Initializable {
    @FXML
    private TextField ticketPriorityName;
    private DatabaseManager dbm = Driver.getDbm();
    private Person person = dbm.findPersonByID(Driver.getEmployeeID());

    public void submit()
    {
        if(check())
        {
            dbm.insertTickePriority(ticketPriorityName.getText(), person.getOrganization());
            Error.error("Added Ticket Status successfully ");
            dbm.commit();
        }
    }

    public boolean check()
    {
        if(ticketPriorityName.getText().equals(""))
        {
            Error.error("Please enter a name");
            dbm.commit();
            return false;
        }
        else if(dbm.findPriorityByName(ticketPriorityName.getText(), person.getOrganization()) != null)
        {
            Error.error("Ticket Status already exist");
            dbm.commit();
            return false;
        }
        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ticketPriorityName.setOnKeyPressed( e -> {
            if(e.getCode() == KeyCode.ENTER)
            {
                submit();
            }
        });
    }
}
