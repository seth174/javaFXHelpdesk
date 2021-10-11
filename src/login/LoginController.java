package login;

import buttonCalls.HelpDeskTeamManagerButtons;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import main.Driver;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Button login;
    @FXML
    private Button help;
    private static Stage stage = Driver.getStage();

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
        try{
            //Parent root1 = FXMLLoader.load(getClass().getResource("/helpDeskTeamManager/tickets/ticketMainPage.fxml"));
            Parent root1 = FXMLLoader.load(getClass().getResource("/client/dashboard/dashboard.fxml"));
            stage.setScene(new Scene(root1));
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
