package client.tickets.assignedUsers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AddUsers {
    @FXML
    private Button closeButton;
    public void closeAddUsers()
    {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
