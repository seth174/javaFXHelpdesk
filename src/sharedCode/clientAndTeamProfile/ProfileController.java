package sharedCode.clientAndTeamProfile;

import buttonCalls.ButtonCalls;
import dao.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Driver;
import models.Person;
import sharedCode.editProfile.EditProfile;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController extends ButtonCalls implements Initializable {
    @FXML
    private Text firstName;
    @FXML
    private Text lastName;
    @FXML
    private Text email;
    @FXML
    private Text phoneNumber;
    @FXML
    private Text organization;

    private static DatabaseManager dbm = Driver.getDbm();
    private static Stage stage = Driver.getStage();
    private Person person;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        person = dbm.findPersonByID(Driver.getEmployeeID());
        loadData();
        System.out.println("Inside intitaliza");
        dbm.commit();

    }

    public void loadData()
    {
        firstName.setText(person.getFirstName());
        lastName.setText(person.getLastName());
        email.setText(person.getEmail());
        phoneNumber.setText(person.getPhoneNumber());
        organization.setText(person.getOrganization().getName());
    }

    public void editEmail()
    {
        edit("email");
    }

    public void editFirstName()
    {
        edit("first name");
    }

    public void editLastName()
    {
        edit("last name");
    }

    public void editPhoneNumber()
    {
        edit("phone number");
    }

    public void edit(String text)
    {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/sharedCode/editProfile/editProfile.fxml"
                )
        );
        Stage stage2 = new Stage();
        try {
            stage2.setScene(
                    new Scene(loader.load())
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage2.setOnCloseRequest(e -> {
            refreshData();
        });

        EditProfile editprofile = loader.getController();
        editprofile.setText(text);

        stage2.initModality(Modality.APPLICATION_MODAL);
        stage2.showAndWait();
    }

    public void invalidate()
    {
        person = dbm.findPersonByID(Driver.getEmployeeID());
        System.out.println(person.getFirstName());
        dbm.commit();
    }

    public void refresh()
    {
        try
        {
            Parent root1 = FXMLLoader.load(getClass().getResource("/helpDeskTeamManager/editProfile/editProfile.fxml"));
            stage.setScene(new Scene(root1));
            stage.setFullScreen(true);
            stage.show();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public void refreshData()
    {
        invalidate();
        loadData();
    }

}
