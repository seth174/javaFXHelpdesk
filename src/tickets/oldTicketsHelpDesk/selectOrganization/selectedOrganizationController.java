package tickets.oldTicketsHelpDesk.selectOrganization;

import autoCompleteComboBox.ComboBoxAutoComplete;
import buttonCalls.ButtonCalls;
import dao.DatabaseManager;
import error.Error;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import main.Driver;
import models.Organization;
import models.Person;
import tickets.oldTicketsHelpDesk.organizationOldTickets.OrganizationOldTickets;


import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.TreeSet;

public class selectedOrganizationController extends ButtonCalls implements Initializable {

    private DatabaseManager dbm = Driver.getDbm();
    private Person person = dbm.findPersonByID(Driver.getEmployeeID());
    @FXML
    private ComboBox<String> comboBox;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadOrganizations();
    }

    public void select()
    {
        if(comboBox.getValue() == null)
        {
            Error.error("Please select an organization");
            return;
        }

        try
        {

            Stage stage = (Stage) comboBox.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tickets/oldTicketsHelpDesk/organizationOldTickets/organizationOldTickets.fxml"));
            stage.setScene(new Scene(loader.load()));
            stage.setFullScreen(true);
            OrganizationOldTickets organizationOldTickets = loader.getController();
            Organization organization = dbm.findByName(comboBox.getValue());
            dbm.commit();
            organizationOldTickets.loadTickets(organization);
            stage.setTitle("Old Tickets");

        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public void loadOrganizations()
    {
        Collection<Organization> organizations = person.getOrganization().getOrganizationsChildren();
        TreeSet<String> organizationsNames = new TreeSet<>();
        for(Organization o: organizations)
        {
            organizationsNames.add(o.getName());
        }

        comboBox.getItems().addAll(organizationsNames);

        new ComboBoxAutoComplete<>(comboBox);

        dbm.commit();
    }

    public void close()
    {
        loadOldTickets();
    }
}
