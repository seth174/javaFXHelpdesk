package tickets.ticketPage;

import buttonCalls.ButtonCalls;
import dao.DatabaseManager;
import error.Error;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.Driver;
import models.*;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.Date;
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
    private GridPane newMessages;
    @FXML
    private TextArea textArea;
    @FXML
    private MenuButton menuButtonStatus;
    @FXML
    private MenuButton menuButtonPriority;
    @FXML
    private MenuButton menuButtonQueue;
    private static int ticketID;
    private DatabaseManager dbm = new DatabaseManager();
    private Ticket ticket = dbm.findTicketByID(ticketID);
    private Person person = dbm.findPersonByID(Driver.getEmployeeID());


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loadTitle();
        loadDescription();

        loadQueue(person.getOrganization());

        Organization otherOrg = dbm.findOtherTicketOrganization(ticket.getTicketID(), person.getOrganization());
        loadPriorities(otherOrg);
        loadStatuses(otherOrg);
        menuButtonPriority.setText(ticket.getTicketPriority().getPriority());
        menuButtonStatus.setText(ticket.getTicketStatus().getTicketStatus());

        newMessages.setVgap(30);

        loadMessages();

        datePicker.setOnAction( e -> loadTime());
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

        Text text = new Text(message);
        text.getStyleClass().add("text");

        Button respond = new Button("Respond");
        respond.getStyleClass().add("Button");
        respond.getStyleClass().add("SmallButton");
        TextArea textArea2 = new TextArea();

        Text infoText = new Text(personPosted.getEmail() + " Posted at " + datePosted);

        VBox vBox;
        if(messageVbox == null)
        {
            vBox = new VBox();
            vBox.setSpacing(10);
        }
        else
        {
            vBox = messageVbox;
            VBox.setMargin(text, new Insets(15, 50, 10, 50));
            VBox.setMargin(infoText, new Insets(5, 50, 5, 50));
            VBox.setMargin(respond, new Insets(15, 50, 5, 50));
        }

        vBox.getChildren().addAll(text, infoText, respond);
        respondButton(respond, vBox, textArea2);
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
        Collection<TicketPriority> priorities;
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

                priority.setOnAction( e -> {
                    Organization ticketPriorityOrg;
                    if(personOrganization == organization)
                    {
                        ticketPriorityOrg = organization;
                    }
                    else if(personOrganization.getParentOrganization() == organization)
                    {
                        ticketPriorityOrg = organization;
                    }
                    else
                    {
                        ticketPriorityOrg = personOrganization;
                    }
                    TicketPriority oldPriority =  dbm.findPriorityByName(menuButtonPriority.getText(), ticketPriorityOrg);
                    TicketPriority newPriority = tp;

                    dbm.updateTicketPriority(oldPriority, newPriority, ticket);

                    menuButtonPriority.setText(tp.getPriority());

                    TicketsPerQueue queue = dbm.find(ticket, ticketPriorityOrg);
                    queue.getQueue().invalidateTicketsPerQueues();

                    dbm.commit();

                });
            }

        }
        dbm.commit();
    }

    public void loadStatuses(Organization organization)
    {
        Organization personOrganization = person.getOrganization();
        Collection<TicketStatus> statuses;
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
                status.setOnAction( e -> {
                    Organization ticketStatusOrg;
                    if(personOrganization == organization)
                    {
                        ticketStatusOrg = organization;
                    }
                    else if(personOrganization.getParentOrganization() == organization)
                    {
                        ticketStatusOrg = organization;
                    }
                    else
                    {
                        ticketStatusOrg = personOrganization;
                    }
                    TicketStatus oldStatus =  dbm.findStatusByName(menuButtonStatus.getText(), ticketStatusOrg);
                    TicketStatus newStatus = ts;

                    dbm.updateTicketStatus(oldStatus, newStatus, ticket);

                    menuButtonStatus.setText(ts.getTicketStatus());

                    TicketsPerQueue queue = dbm.find(ticket, ticketStatusOrg);
                    queue.getQueue().invalidateTicketsPerQueues();

                    if(ts.getTicketStatus().equalsIgnoreCase("Closed")){
                        updateQueues(ticket, person.getOrganization());
                        updateTicketInfo();
                    }

                    dbm.commit();
                });
                menuButtonStatus.getItems().add(status);
            }

        }
        dbm.commit();
    }

    public void updateTicketInfo()
    {
        dbm.closeTicket(ticket, person);
        dbm.commit();
    }

    public void updateQueues(Ticket ticket, Organization organization)
    {
        TicketsPerQueue ticketsPerQueue = dbm.find(ticket, organization);
        Queue newQueue = dbm.findQueueByName("Closed", organization);
        dbm.updateTicketPerQueue(ticketsPerQueue, newQueue);

        Organization otherOrganization = dbm.findOtherTicketOrganization(ticket.getTicketID(), organization);
        TicketsPerQueue ticketsPerQueue2 = dbm.find(ticket, otherOrganization);
        Queue newQueue2 = dbm.findQueueByName("Closed", otherOrganization);
        dbm.updateTicketPerQueue(ticketsPerQueue2, newQueue2);
        dbm.commit();
    }

    public void submitTime()
    {
        if(!checkTime())
        {
            return;
        }
        Date date = Date.valueOf(datePicker.getValue());
        double time = Double.parseDouble(datePickerTime.getText());

        if(dbm.findTimePerPerson(date, ticket, person) == null)
        {
            dbm.insert(ticket, person, time, date);
        }
        else
        {
            int timePerPersonID = dbm.findTimePerPerson(date, ticket, person).getTimePerPersonID();
            dbm.updateTimePerPerson(timePerPersonID, time, person, ticket);
        }
        dbm.commit();
        Error.error("successfully added time");
    }

    public boolean checkTime()
    {
        if(datePicker.getValue() == null || datePicker.getEditor().getText().equals(""))
        {
            Error.error("Please choose a date");
            return false;
        }
        else if(!datePicker.getEditor().getText().matches("((\\d)(\\d)?)/((\\d)(\\d)?)/((\\d)(\\d)(\\d)(\\d))"))
        {
            Error.error("Please enter a valid date format");
            return false;
        }
        else if(datePickerTime.getText().equals(""))
        {
            Error.error("Please enter a time");
            return false;
        }
        else if(datePickerTime.getText().matches("[\\w]"))
        {
            Error.error("Please enter a number");
            return false;
        }
        return true;
    }

    public void loadTime()
    {
        Date date = Date.valueOf(datePicker.getValue());
        if(dbm.findTimePerPerson(date, ticket, person) == null)
        {
            datePickerTime.setText("0");
        }
        else
        {
            double time = dbm.findTimePerPerson(date, ticket, person).getTime();
            datePickerTime.setText(String.valueOf(time));
        }
        dbm.commit();
    }

    public void loadQueue(Organization organization)
    {
        TicketsPerQueue tq = dbm.find(ticket, organization);
        String queueName = tq.getQueue().getName();

        menuButtonQueue.setText(queueName);

        if(queueName.equalsIgnoreCase("Assigned") || queueName.equalsIgnoreCase("Closed"))
        {
            dbm.commit();
            return;
        }

        Collection<Queue> queues = organization.getOrganizationQueues();

        for(Queue q: queues)
        {
            if(q.getName().equalsIgnoreCase("Closed") || q.getName().equalsIgnoreCase("Assigned"))
                continue;
            MenuItem queue = new MenuItem(q.getName());
            menuButtonQueue.getItems().add(queue);

            queue.setOnAction(e -> {
                TicketsPerQueue ticketsPerQueue = dbm.find(ticket, organization);
                dbm.updateTicketPerQueue(ticketsPerQueue, q);
                menuButtonQueue.setText(q.getName());
                dbm.commit();
            });
        }

        dbm.commit();
    }

    public static int getTicket(){ return (ticketID);}

}
