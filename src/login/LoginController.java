package login;

import dao.DatabaseManager;
import error.Error;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import main.Driver;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import models.Person;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button login;
    @FXML
    private Button help;
    private static Stage stage = Driver.getStage();
    private static DatabaseManager dbm = Driver.getDbm();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stage.setOnShown(e -> {
            username.requestFocus();
        });
        username.setOnKeyPressed( event -> {
            if( event.getCode() == KeyCode.ENTER ) {
                login();
            }
        } );
        password.setOnKeyPressed( event -> {
            if( event.getCode() == KeyCode.ENTER ) {
                login();
            }
        } );
    }

    public void login()
    {
        String email = username.getText();
        String loginPassword = password.getText();

        Person person = dbm.getPerson(email, loginPassword);

        dbm.commit();

        if(person == null)
        {
            Error.error("Username or Password is incorrect");
            return;
        }

        Driver.setEmployeeID(person.getEmployeeID());
        System.out.println(person.getOrganization().getOrganizationID());
        try{
            Parent root1 = null;
            if(person.getLevel() == 1)
                root1 = FXMLLoader.load(getClass().getResource("/client/dashboard/dashboard.fxml"));
            else
                root1 = FXMLLoader.load(getClass().getResource("/helpDeskTeam/tickets/ticketMainPage.fxml"));
            stage.setScene(new Scene(root1));
            stage.setTitle("Ticket Home Page");
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static Stage getStage()
    {
        return stage;
    }


}
