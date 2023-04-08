package lk.ijse.dep10.game.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class StartMenuViewController {

    @FXML
    private Button btnQuitGame;

    @FXML
    private Button btnStartGame;

    @FXML
    private Button btnViewHighScores;

    @FXML
    private TextField txtName;

    @FXML
    void btnQuitGameOnAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void btnStartGameOnAction(ActionEvent event) {

    }

    @FXML
    void btnViewHighScoresOnAction(ActionEvent event) {
        try {
            URL fxmlFile = getClass().getResource("/view/ScoreView.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlFile);
            AnchorPane root = fxmlLoader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("High Scores");
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(btnViewHighScores.getScene().getWindow());
            stage.show();
            stage.centerOnScreen();
            stage.setResizable(false);

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR,"Failed to load ScoreView.fxml, try again...!").showAndWait();
            throw new RuntimeException(e);
        }
    }

}

