package lk.ijse.dep10.game.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;

public class GameOverViewController {

    public Label lblScore;
    @FXML
    private Button btnPlayAgain;

    @FXML
    private Button btnQuitGame;
    private int finalScore;
    public void getScore(int score){
        finalScore = score;
    }
    public void initialize(){
        Platform.runLater(() -> {
            lblScore.setText("Your Score : " + finalScore);
        });
    }

    @FXML
    void btnPlayAgainOnAction(ActionEvent event) {
        Stage closeStage = (Stage) btnPlayAgain.getScene().getWindow();
        closeStage.close();
    }

    @FXML
    void btnQuitGameOnAction(ActionEvent event) {
        Platform.exit();
    }

}

