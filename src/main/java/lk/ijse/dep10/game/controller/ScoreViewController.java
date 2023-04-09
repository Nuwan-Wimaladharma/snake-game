package lk.ijse.dep10.game.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.dep10.game.db.DBConnection;
import lk.ijse.dep10.game.model.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScoreViewController {

    @FXML
    private TableView<Player> tblScores;
    public void initialize(){
        tblScores.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblScores.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblScores.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("score"));

        loadScores();
    }
    private void loadScores(){
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Scores ORDER BY player_score DESC ");
            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList<Player> scoreList = tblScores.getItems();
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String playerName = resultSet.getString("player_name");
                int playerScore = resultSet.getInt("player_score");
                Player player = new Player(id, playerName, playerScore);
                scoreList.add(player);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Failed to load scores, try again...!").showAndWait();
            throw new RuntimeException(e);
        }
    }
}

