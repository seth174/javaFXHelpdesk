package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Driver extends Application {
    private static Stage stage;
    public static final String backgroundButton = "-fx-background-color: #5B84B1FF; -fx-background-radius: 15;";
    public static final String backgroundButtonPressed = "-fx-background-color: #050A30; -fx-background-radius: 15;";
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        setStage(primaryStage);
        try{
            Parent root1 = FXMLLoader.load(getClass().getResource("/login/Login.fxml"));
            primaryStage.setTitle("Login");
            primaryStage.setScene(new Scene(root1, 315, 232));
            primaryStage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void startMenu(Stage frame) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(8);
        grid.setAlignment(Pos.CENTER);

        Label label = new Label("Welcome to Euchre by Seth");
        GridPane.setConstraints(label, 0, 0);

        Button play = new Button("Play");
        Button stats = new Button("Stats");
        Button settings = new Button("Settings");

        VBox options = new VBox(20);
        options.getChildren().addAll(play, stats, settings);
        GridPane.setConstraints(options, 0, 1);
        options.setAlignment(Pos.CENTER);

        grid.getChildren().addAll(label, options);

        Scene startMenu = new Scene(grid);

        frame.setHeight(200);
        frame.setWidth(200);
        frame.setTitle("Hello");

        frame.setScene(startMenu);

        frame.show();
    }

    public static void setStage(Stage stage)
    {
        Driver.stage = stage;
    }

    public static Stage getStage()
    {
        return Driver.stage;
    }

    public static void loadButton(Button button)
    {
        button.setStyle(Driver.backgroundButton);
        button.setOnMouseEntered(e -> button.setStyle(Driver.backgroundButtonPressed));
        button.setOnMouseExited(e -> button.setStyle(Driver.backgroundButton));
    }
}
