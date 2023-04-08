package lk.ijse.dep10.game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class AppInitializer extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            URL fxmlFile = getClass().getResource("/view/StartMenuView.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlFile);
            AnchorPane root = fxmlLoader.load();

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Snake Game");
            primaryStage.show();
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR,"Failed to load StartMenuView.fxml, try again...!").showAndWait();
            throw new RuntimeException(e);
        }


    }
}
