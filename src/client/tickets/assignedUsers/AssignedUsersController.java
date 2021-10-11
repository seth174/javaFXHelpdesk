package client.tickets.assignedUsers;

import buttonCalls.ClientButtons;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AssignedUsersController extends ClientButtons implements Initializable {
    @FXML
    private Button closeButton;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void closeAssignedUsers()
    {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
