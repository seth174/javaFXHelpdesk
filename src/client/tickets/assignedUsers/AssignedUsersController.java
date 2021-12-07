package client.tickets.assignedUsers;

import buttonCalls.ButtonCalls;
import dao.DatabaseManager;
import helpDeskTeam.tickets.TicketPage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import main.Driver;
import models.*;
import zzzzArchive.ClientButtons;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.TreeSet;

public class AssignedUsersController extends ButtonCalls implements Initializable {
    @FXML
    private Button closeButton;
    @FXML
    private VBox organization1;
    @FXML
    private VBox organization2;
    @FXML
    private Text organization1Text;
    @FXML
    private Text organization2Text;

    private DatabaseManager dbm = Driver.getDbm();
    private Person person = dbm.findPersonByID(Driver.getEmployeeID());
    private Ticket ticket = dbm.findTicketByID(TicketPage.getTicket());
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadUsers(person.getOrganization(), organization1, true, organization1Text);
        Organization organization = dbm.findOtherTicketOrganization(ticket.getTicketID(), person.getOrganization());
        loadUsers(organization, organization2, true, organization2Text);
    }

    public void closeAssignedUsers()
    {
        loadTicketPage();
    }

    public void loadUsers(Organization organization, VBox vBox, boolean delete, Text orgText)
    {
        orgText.setText(organization.getName() + " Users:");
        Collection<TicketPerPerson> organization1Users = ticket.getTicketPerPerson();
        System.out.println("FWGR - " + organization.getName());
        for(TicketPerPerson tp: organization1Users)
        {
            if(tp.getPerson().getOrganization() != organization)
            {
                System.out.println(organization.getName());
                System.out.println(tp.getPerson().getEmail());
                continue;
            }
            System.out.println("new text: " + tp.getPerson().getEmail());
            HBox hBox = new HBox();
            Text text = new Text(tp.getPerson().getEmail());
            hBox.getChildren().add(text);

            if(delete)
            {
                Button button = new Button("Remove");
                hBox.getChildren().add(button);
                button.setOnAction(e -> {
                    vBox.getChildren().remove(hBox);
                    remove(text);
                });
            }

            vBox.getChildren().add(hBox);
        }

        dbm.commit();
    }

    public void remove(Text email)
    {
        Person person1 = dbm.findByEmail(email.getText());
        dbm.updateSubscription(person1, ticket);
        if(!dbm.findSubscribers(ticket, person1.getOrganization()))
        {
            System.out.println("Yesssss");
            updateQueue(person1.getOrganization());
        }
        person1.invalidateTicketsPerPerson();
        dbm.commit();
    }

    public void updateQueue(Organization org)
    {
        Queue queue = dbm.findQueueByName(org.getName() + " Queue", org);
        TicketsPerQueue ticketsPerQueue =dbm.find(ticket, org);
        dbm.updateTicketPerQueue(ticketsPerQueue, queue);
        dbm.commit();
    }

    public void loadAddUsers(Organization organization)
    {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/tickets/assignedUsers/addUsers.fxml"));
            window.setScene(new Scene(loader.load()));
            AddUsers addUsers = loader.getController();
            addUsers.loadComboBox(organization);
            addUsers.setTicket(ticket);
            addUsers.setOrganization(organization);
            window.setTitle("Add Users");
            window.showAndWait();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public void addOne()
    {
        loadAddUsers(dbm.findByName(organization1Text.getText().substring(0, organization1Text.getText().length() - 7)));
        dbm.commit();
    }

    public void addTwo()
    {
        loadAddUsers(dbm.findByName(organization2Text.getText().substring(0, organization2Text.getText().length() - 7)));
        dbm.commit();
    }
}
