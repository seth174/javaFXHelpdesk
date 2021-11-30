package helpDeskTeam.tickets;

import buttonCalls.ButtonCalls;
import dao.DatabaseManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.Driver;
import models.*;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TicketPage extends ButtonCalls implements Initializable {
    @FXML
    private DatePicker datePicker;
    @FXML
    private Text titleText;
    @FXML
    private Text descriptionText;
    @FXML
    private TextField datePickerTime;
    @FXML
    private GridPane infoGridPane;
    @FXML
    private GridPane newMessages;
    @FXML
    private TextArea textArea;
    @FXML
    private ButtonBar buttonBar;
    @FXML
    private MenuButton menuButtonStatus;
    @FXML
    private MenuButton menuButtonPriority;
    private static int ticketID;
    private DatabaseManager dbm = new DatabaseManager();
    private Ticket ticket = dbm.findTicketByID(ticketID);
    private Person person = dbm.findPersonByID(Driver.getEmployeeID());


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

        loadTitle();
        loadDescription();

        Organization otherOrg = dbm.findOtherTicketOrganization(ticket.getTicketID(), person.getOrganization());
        loadPriorities(otherOrg);
        loadStatuses(otherOrg);
        menuButtonPriority.setText(ticket.getTicketPriority().getPriority());
        menuButtonStatus.setText(ticket.getTicketStatus().getTicketStatus());

        newMessages.setVgap(30);

        loadMessages();
    }

    public void loadTitle()
    {
        titleText.setText(ticket.getTicketTitle());
    }

    public void loadDescription()
    {
        descriptionText.setText(ticket.getTicketDescription());
    }

    public void postMessage()
    {
        if(textArea.getText().equals(""))
            return;
        Timestamp datePosted = new Timestamp(System.currentTimeMillis());
        dbm.insert(ticket, null, person, textArea.getText(), datePosted);
        dbm.commit();
        postMessage(textArea.getText(), null, datePosted, person);
        textArea.clear();
    }


    public VBox postMessage(String message, VBox messageVbox, Timestamp datePosted, Person personPosted)
    {
        if(message.equals(""))
            return null;

        VBox vBox;
        if(messageVbox == null)
        {
            vBox = new VBox();
            vBox.setSpacing(10);
        }
        else
        {
            vBox = messageVbox;
        }

        Text text = new Text(message);

        Button respond = new Button("Respond");
        TextArea textArea2 = new TextArea();
        respondButton(respond, vBox, textArea2);

        Text infoText = new Text(personPosted.getEmail() + " Posted at " + datePosted);
        vBox.getChildren().addAll(text, infoText, respond);
        if(messageVbox == null)
        {
            if(textArea.getText().equals(""))
            {
                newMessages.addColumn(0, vBox);
            }
            else{
                textArea.clear();
                newMessages.getChildren().clear();
                loadMessages();
            }
        }
        return vBox;
    }

    public void respondButton(Button button, VBox vBox, TextArea textArea1){
        button.setOnAction(e -> {
            if(button.getText().equals("Respond"))
            {
                button.setText("Post");
                textArea1.setPrefWidth(500);
                textArea1.setPrefHeight(50);
                vBox.getChildren().remove(button);
                vBox.getChildren().add(textArea1);
                vBox.getChildren().add(button);
            }
            else
            {
                postResponse(textArea1, button, vBox);
            }
        });
    }

    public void postResponse(TextArea textAreaResponse, Button button, VBox vBox){
        if(textAreaResponse.getText().equals(""))
            return;

        vBox.getChildren().remove(textAreaResponse);
        vBox.getChildren().remove(button);

        Text infoText = (Text)vBox.getChildren().get(vBox.getChildren().size() - 1);
        Text message = (Text)vBox.getChildren().get(vBox.getChildren().size() - 2);
        String search = "((\\d)+(-)?)+\\s((\\d)*:)*(\\d)*\\.(\\d)*";
        Pattern pattern = Pattern.compile(search);
        Matcher matcher = pattern.matcher(infoText.getText());
        Timestamp timestamp = null;

        if(matcher.find())
        {
            timestamp = Timestamp.valueOf(matcher.group());
        }

        Message replyMessage = dbm.findMessage(message.getText(), timestamp, ticket);
        Timestamp datePosted = new Timestamp(System.currentTimeMillis());
        postMessage(textAreaResponse.getText(), vBox, datePosted, person);
        dbm.insert(ticket, replyMessage, person, textAreaResponse.getText(), datePosted);
        dbm.commit();

    }

    public void loadMessages()
    {
        Collection<Message> messages = ticket.getMessages();
        for(Message m: messages)
        {
            VBox messageVbox = postMessage(m.getMessage(), null, m.getDatePosted(), m.getPerson());
            Collection<Message> replyMessages = m.getReplyMessages();

            if(replyMessages.size() > 0)
            {
                System.out.println("yessir");
                loadReplyMessages(messageVbox, (Message)m.getReplyMessages().toArray()[0]);
            }
        }
    }

    public void loadReplyMessages(VBox vBox, Message m)
    {
        vBox.getChildren().remove(vBox.getChildren().size() - 1);
        vBox = postMessage(m.getMessage(), vBox, m.getDatePosted(), m.getPerson());
        if(m.getReplyMessages().size() > 0 )
            loadReplyMessages(vBox, (Message)m.getReplyMessages().toArray()[0]);
    }

    public static void setTicketID(int ticketID){
        TicketPage.ticketID = ticketID;
    }

    public void loadPriorities(Organization organization)
    {
        Organization personOrganization = person.getOrganization();
        Collection<TicketPriority> priorities = null;
        //this will need work
        if(personOrganization == organization)
        {
            priorities = organization.getTicketPriorities();
        }
        else if(personOrganization.getParentOrganization() == organization)
        {
            priorities = organization.getTicketPriorities();
        }
        else
        {
            priorities = personOrganization.getTicketPriorities();
        }
        if(priorities.size() > 0)
        {
            for(TicketPriority tp : priorities)
            {
                MenuItem priority = new MenuItem(tp.getPriority());
                menuButtonPriority.getItems().add(priority);
            }

        }
        dbm.commit();
    }

    public void loadStatuses(Organization organization)
    {
        Organization personOrganization = person.getOrganization();
        Collection<TicketStatus> statuses = null;
        //this will need work
        if(personOrganization == organization)
        {
            statuses = organization.getTicketStatuses();
        }
        else if(personOrganization.getParentOrganization() == organization)
        {
            statuses = organization.getTicketStatuses();
        }
        else
        {
            statuses = personOrganization.getTicketStatuses();
        }
        if(statuses.size() > 0)
        {
            for(TicketStatus ts : statuses)
            {
                MenuItem status = new MenuItem(ts.getTicketStatus());
                menuButtonStatus.getItems().add(status);
            }

        }
        dbm.commit();
    }




}
