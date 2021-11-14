package sharedCode.editProfile;

import error.Error;
import dao.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.Driver;

import java.net.URL;
import java.util.ResourceBundle;

public class EditProfile implements Initializable {
    @FXML
    private Text editValue;
    @FXML
    private TextField editValuesTextField;
    @FXML
    private Button submitButton;
    private static DatabaseManager dbm = Driver.getDbm();

    public void setText(String text)
    {
        editValue.setText(text);
    }

    public void submit()
    {
        Stage window = (Stage)submitButton.getScene().getWindow();
        if(editValuesTextField.getText().equals(""))
        {
            Error.error("Please enter a value");
            return;
        }

        update();

        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public void close()
    {
        Stage window = (Stage)submitButton.getScene().getWindow();
        window.close();
    }


    public void update()
    {
        if(editValue.getText().equals("email"))
        {
            if(dbm.findByEmail(editValuesTextField.getText()) != null)
            {
                Error.error("That email is already linked to another account");
                return;
            }
            dbm.updateUserInfo(null, null,
                    editValuesTextField.getText(), null, Driver.getEmployeeID());
        }
        else if(editValue.getText().equals("first name"))
        {
            dbm.updateUserInfo(editValuesTextField.getText(), null,
                    null, null, Driver.getEmployeeID());
        }
        else if(editValue.getText().equals("last name"))
        {
            dbm.updateUserInfo(null, editValuesTextField.getText(),
                    null, null, Driver.getEmployeeID());
        }
        else if(editValue.getText().equals("phone number"))
        {
            dbm.updateUserInfo(null, null,
                    null, editValuesTextField.getText(), Driver.getEmployeeID());
        }
        dbm.commit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        editValuesTextField.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER)
                submit();
        });
    }
}
