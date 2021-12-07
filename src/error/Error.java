package error;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Error {
    @FXML
    private Text text;

    public void close()
    {
        Stage window = (Stage)text.getScene().getWindow();
        window.close();
    }

    public void setError(String error)
    {
        text.setText(error);
        text.setTextAlignment(TextAlignment.CENTER);
    }

    public static void error(String text2)
    {
        FXMLLoader loader = new FXMLLoader(
                Error.class.getResource(
                        "/error/error.fxml"
                )
        );
        Stage stage = new Stage();
        try {
            stage.setScene(
                    new Scene(loader.load())
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        Error controller = loader.getController();
        controller.setError(text2);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

}
