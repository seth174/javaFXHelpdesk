package helpDeskTeam.tickets;

import buttonCalls.ButtonCalls;
import dao.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Driver;
import models.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.ResourceBundle;

public class TicketMainPage extends ButtonCalls implements Initializable {
    private static Stage stage = Driver.getStage();
    @FXML
    private ButtonBar buttonBar;
    @FXML
    private VBox vBox;
    private DatabaseManager dbm = Driver.getDbm();
    private Person person = dbm.findPersonByID(Driver.getEmployeeID());
    private HashMap<Integer, Integer> ticketPosition;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(dbm.findPersonByID(Driver.getEmployeeID()).getLevel() == 3)
        {
            Button add = new Button("Add");
            add.getStyleClass().add("Button");

            Button manageTeamQueue = new Button("Manage Team Queue");
            manageTeamQueue.getStyleClass().add("Button");

            Button stats = new Button("Statistics");
            stats.getStyleClass().add("Button");

            buttonBar.getButtons().add(add);
            buttonBar.getButtons().add(manageTeamQueue);
            buttonBar.getButtons().add(stats);

            manageTeamQueue.setOnAction(e -> loadManageQueue());
            add.setOnAction(e -> loadAdd());
        }

        ticketPosition = new HashMap<>();

        loadQueues();
    }

    public void loadQueues()
    {
        Collection<QueuePerPerson> qp  = person.getQueuePerPerson();
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
        titledPane.setText(q.getName());
        return titledPane;
    }

    public void loadTickets(TitledPane titledPane, Queue q)
    {
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
        //Need to fix this
        Collection<TicketsPerQueue> tickets = q.getTicketsPerQueues();
        int counter = 1;
        for(TicketsPerQueue tq: tickets)
        {
            Ticket t = tq.getTicket();
            //this one needs help
            Text organization1 = new Text(t.getTicketTitle());
            Text title1 = new Text(t.getTicketTitle());
            Text priority1 = new Text(String.valueOf(t.getTicketPriority().getPriority()));
            Text status1 = new Text(t.getTicketStatus().getTicketStatus());
            Text id1 = new Text(String.valueOf(t.getTicketID()));

            organization1.setOnMouseClicked(e -> {System.out.println("hi");});



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

            ticketPosition.put(counter, t.getTicketID());

            counter += 1;
        }

        titledPaneGridPane.setOnMouseClicked(e -> {
            Bounds boundsInScreen = titledPaneGridPane.localToScreen(titledPaneGridPane.getBoundsInLocal());
            System.out.println("MIN " + boundsInScreen.getMinY());
            System.out.println("MAX " + boundsInScreen.getMaxY());
            System.out.println("Size" + (boundsInScreen.getMaxY() - boundsInScreen.getMinY()));
            System.out.println("LOCATION " + e.getY());
            System.out.println("Guess " + ((int)(e.getY() - 10) / 35));
            System.out.println();
            int guess = ((int)(e.getY() - 10) / 35);
            System.out.println("row column " + guess + " has a ticket id of " +ticketPosition.get(guess));
        });
    }


}
