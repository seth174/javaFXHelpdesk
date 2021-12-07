package tickets.ticketMainPage;

import buttonCalls.ButtonCalls;
import dao.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Driver;
import models.*;
import models.Queue;
import tickets.ticketPage.TicketPage;

import java.net.URL;
import java.util.*;

public class TicketMainPage extends ButtonCalls implements Initializable {
    @FXML
    private VBox vBox;
    @FXML
    private TitledPane titledPane;
    private DatabaseManager dbm = Driver.getDbm();
    private Person person = dbm.findPersonByID(Driver.getEmployeeID());
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadQueues();
    }

    public void loadQueues()
    {
        Collection<QueuePerPerson> qp  = person.getQueuePerPerson();
        loadTickets(titledPane, null);
        for(QueuePerPerson queuePerPerson: qp)
        {
            TitledPane titledPane = addQueue(queuePerPerson.getQueue());
            vBox.getChildren().add(titledPane);
            loadTickets(titledPane, queuePerPerson.getQueue());
        }
        dbm.commit();
    }

    public TitledPane addQueue(Queue q)
    {
        TitledPane titledPane = new TitledPane();
        VBox.setMargin(titledPane, new Insets(25, 25, 25, 25));
        titledPane.setText(q.getName());
        return titledPane;
    }

    public void loadTickets(TitledPane titledPane, Queue q)
    {
        HashMap<Integer, Integer> position = new HashMap<>();


        Text organization = new Text("Organization");
        Text title = new Text("Title");
        Text priority = new Text("Priority");
        Text status = new Text("Status");
        Text id = new Text("ID");
        GridPane titledPaneGridPane = new GridPane();

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(50);
        titledPaneGridPane.getColumnConstraints().add(column1);
        titledPaneGridPane.getColumnConstraints().add(column1);
        titledPaneGridPane.getColumnConstraints().add(column1);
        titledPaneGridPane.getColumnConstraints().add(column1);
        titledPaneGridPane.getColumnConstraints().add(column1);

        GridPane.setConstraints(organization, 0, 0);
        GridPane.setConstraints(title, 1, 0);
        GridPane.setConstraints(priority, 2, 0);
        GridPane.setConstraints(status, 3, 0);
        GridPane.setConstraints(id, 4, 0);
        GridPane.setMargin(organization, new Insets(5, 0, 10, 0));
        GridPane.setMargin(title, new Insets(5, 0, 10, 0));
        GridPane.setMargin(priority, new Insets(5, 0, 10, 0));
        GridPane.setMargin(status, new Insets(5, 0, 10, 0));
        GridPane.setMargin(id, new Insets(5, 0, 10, 0));

        titledPaneGridPane.getChildren().addAll(organization, title, priority, status, id);

        titledPane.setContent(titledPaneGridPane);
        Collection<Ticket> tickets;
        if(q == null)
        {
            tickets = person.getTicketsPerPerson();
        }
        else
        {
            tickets = q.getTicketsPerQueues();
        }

        int counter = 1;
        for(Ticket t: tickets)
        {

            //this one needs help

            Organization otherOrg = dbm.findOtherTicketOrganization(t.getTicketID(), person.getOrganization());
            Text organization1 = new Text();

            organization1.setText(otherOrg == null ? person.getOrganization().getName(): otherOrg.getName());

            Text title1 = new Text(t.getTicketTitle());
            Text priority1 = new Text(String.valueOf(t.getTicketPriority().getPriority()));
            Text status1 = new Text(t.getTicketStatus().getTicketStatus());
            Text id1 = new Text(String.valueOf(t.getTicketID()));

            GridPane.setConstraints(organization1, 0, counter);
            GridPane.setConstraints(title1, 1, counter);
            GridPane.setConstraints(priority1, 2, counter);
            GridPane.setConstraints(status1, 3, counter);
            GridPane.setConstraints(id1, 4, counter);

            GridPane.setMargin(organization1, new Insets(10, 0, 10, 0));
            GridPane.setMargin(title1, new Insets(10, 0, 10, 0));
            GridPane.setMargin(priority1, new Insets(10, 0, 10, 0));
            GridPane.setMargin(status1, new Insets(10, 0, 10, 0));
            GridPane.setMargin(id1, new Insets(10, 0, 10, 0));

            titledPaneGridPane.getChildren().addAll(organization1, title1, priority1, status1, id1);

            position.put(counter, t.getTicketID());

            counter += 1;
        }

        titledPaneGridPane.setOnMouseClicked(e -> {
            int guess = ((int)(e.getY() - 20) / 35);
            if(position.containsKey(guess))
            {
                TicketPage.setTicketID(position.get(guess));
                loadTicketPage();
            }
        });
    }


}
