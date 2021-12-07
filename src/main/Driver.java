package main;

import dao.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Driver extends Application {
    private static Stage stage;
    private static DatabaseManager dbm;
    private static Integer employeeID;

    public static void main(String[] args)
    {
        DatabaseManager dbm1 = new DatabaseManager();
        dbm1.insertFakeData();
        dbm1.commit();
        dbm = dbm1;
        dbm.commit();

        launch(args);
    }



    @Override
    public void start(Stage primaryStage) throws Exception{
        setStage(primaryStage);
        stage.setOnCloseRequest(e -> {
            dbm.close();
            stage.close();
        });
        try{
            Parent root1 = FXMLLoader.load(getClass().getResource("/login/Login.fxml"));
            primaryStage.setTitle("Login");
            primaryStage.setScene(new Scene(root1, 315, 232));
            primaryStage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static void setStage(Stage stage)
    {
        Driver.stage = stage;
    }

    public static Stage getStage()
    {
        return Driver.stage;
    }

    public static DatabaseManager getDbm(){ return dbm; }

    public static int getEmployeeID(){ return employeeID;}

    public static void setEmployeeID(Integer employeeID1){ employeeID = employeeID1; }


}
