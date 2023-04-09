package lk.ijse.dep10.game.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.dep10.game.db.DBConnection;
import lk.ijse.dep10.game.util.Direction;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static javafx.scene.paint.Color.*;

public class NewGameViewController {

    public AnchorPane root;
    public Label txtScore;
    public Label txtPlayer;
    public Canvas cnvGame;
    @FXML
    private String playerName;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 740;
    private static final int CELL_SIZE = 20;
    private static final int X_BOUND = WIDTH / CELL_SIZE;
    private static final int Y_BOUND = HEIGHT / CELL_SIZE;
    private Direction currectDirection;
    private Timeline timeline;
    private List<Point> snakeBody = new ArrayList<>();
    private List<Point> food = new ArrayList<>();
    private Point snakeHead;
    private Point currentFood;
    private GraphicsContext gc;
    private int score = 0;

    public void getPlayerName(String name){
        playerName = name;
    }
    public void initialize(){
        Platform.runLater(() -> {
            txtPlayer.setText("Player : " + playerName);
            for (int i = 0; i < 4; i++) {
                snakeBody.add(new Point(X_BOUND / 2,Y_BOUND / 2));
            }
            snakeHead = snakeBody.get(0);

            Random random = new Random();
            food.add(new Point(random.nextInt(X_BOUND),random.nextInt(Y_BOUND)));
            currentFood = food.get(food.size()-1);
            gc = cnvGame.getGraphicsContext2D();
            gc.setFill(RED);
            gc.fillOval(currentFood.x * CELL_SIZE,currentFood.y * CELL_SIZE,CELL_SIZE - 2,CELL_SIZE - 2);

            currectDirection = Direction.RIGHT;

            timeline = new Timeline(new KeyFrame(Duration.millis(120),event -> run()));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        });

    }
    public void keyCommands(){
        cnvGame.getScene().addEventFilter(KeyEvent.KEY_PRESSED,event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.UP && currectDirection != Direction.DOWN){
                currectDirection = Direction.UP;
            }
            else if (code == KeyCode.DOWN && currectDirection != Direction.UP){
                currectDirection = Direction.DOWN;
            }
            else if (code == KeyCode.RIGHT && currectDirection != Direction.LEFT){
                currectDirection = Direction.RIGHT;
            }
            else if (code == KeyCode.LEFT && currectDirection != Direction.RIGHT){
                currectDirection = Direction.LEFT;
            }
        });
    }
    public void run(){
        newSnake();
        keyCommands();
        currentFood = food.get(food.size()-1);
        gc = cnvGame.getGraphicsContext2D();
        gc.clearRect(snakeBody.get((snakeBody.size())-1).x * CELL_SIZE,snakeBody.get((snakeBody.size())-1).y * CELL_SIZE,CELL_SIZE,CELL_SIZE);
        for (int i = snakeBody.size()-1; i >= 1 ; i--) {
            snakeBody.get(i).x = snakeBody.get(i-1).x;
            snakeBody.get(i).y = snakeBody.get(i-1).y;
        }
        if (currectDirection == Direction.UP){
            snakeHead.y--;
        }
        else if (currectDirection == Direction.DOWN){
            snakeHead.y++;
        }
        else if (currectDirection == Direction.RIGHT){
            snakeHead.x++;
        }
        else if (currectDirection == Direction.LEFT){
            snakeHead.x--;
        }
        if (eatFood()){
            newFood();
        }
        if (gameOverBeyondBoundary() || gameOverByOwnBiting()){
            timeline.stop();
            addDataToDatabase();
            Stage stage = (Stage) cnvGame.getScene().getWindow();
            stage.close();
            openGameOverView();
        }
    }
    public void rootOnKeyPressed(KeyEvent keyEvent) {

    }
    private void newFood(){
        Random random = new Random();
        food.add(new Point(random.nextInt(X_BOUND),random.nextInt(Y_BOUND)));
        gc = cnvGame.getGraphicsContext2D();
        gc.setFill(RED);
        gc.fillOval(food.get(food.size()-1).x * CELL_SIZE,food.get(food.size()-1).y * CELL_SIZE,CELL_SIZE - 2,CELL_SIZE - 2);
    }

    private void newSnake(){
        gc = cnvGame.getGraphicsContext2D();
        gc.setFill(BLACK);
        gc.fillRect(snakeHead.getX() * CELL_SIZE,snakeHead.getY() * CELL_SIZE,CELL_SIZE-1,CELL_SIZE-1);

        gc.setFill(GREY);
        for (int i = 1; i < snakeBody.size(); i++) {
            gc.fillRect(snakeBody.get(i).getX() * CELL_SIZE,snakeBody.get(i).getY() * CELL_SIZE,CELL_SIZE - 1,CELL_SIZE - 1);
        }
    }
    private boolean eatFood(){
        boolean isEat = false;
        if ((snakeHead.getX() == currentFood.getX()) && (snakeHead.getY() == currentFood.getY())){
            isEat = true;
            snakeBody.add(new Point(-1,-1));
            score += 10;
            txtScore.setText("Score : " + score);
        }
        return isEat;
    }
    private boolean gameOverBeyondBoundary(){
        boolean gameOver = false;
        if (snakeHead.getX() < 0 || snakeHead.getY() < 0 || snakeHead.getX() > X_BOUND || snakeHead.getY() > Y_BOUND){
            gameOver = true;
        }
        return gameOver;
    }
    private boolean gameOverByOwnBiting(){
        boolean gameOver = false;
        for (int i = 1; i < snakeBody.size(); i++) {
            if ((snakeHead.x == snakeBody.get(i).x) && (snakeHead.y == snakeBody.get(i).y)){
                gameOver = true;
            }
        }
        return gameOver;
    }
    private void openGameOverView(){
        try {
            URL fxmlFile = getClass().getResource("/view/GameOverView.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlFile);
            AnchorPane root1 = fxmlLoader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root1);
            stage.setScene(scene);
            stage.setTitle("Game Over");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(root.getScene().getWindow());
            stage.show();
            stage.centerOnScreen();
            stage.setResizable(false);

            GameOverViewController controller = fxmlLoader.getController();
            controller.getScore(score);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR,"Unable to load GameOverView.fxml file, try again...!").showAndWait();
            throw new RuntimeException(e);
        }
    }
    private void addDataToDatabase(){
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement stm1 = connection.prepareStatement("SELECT * FROM Scores WHERE player_name = ?");
            stm1.setString(1,playerName);
            ResultSet rst1 = stm1.executeQuery();
            if (rst1.next()){
                int playerScore = rst1.getInt("player_score");
                String playerName1 = rst1.getString("player_name");
                if (score > playerScore){
                    PreparedStatement stm2 = connection.prepareStatement("UPDATE Scores SET player_score = ? WHERE player_name = ?");
                    stm2.setInt(1,score);
                    stm2.setString(2,playerName1);
                    stm2.executeUpdate();
                }
            }
            else if (!rst1.next()){
                PreparedStatement stm3 = connection.prepareStatement("INSERT INTO Scores (player_name,player_score) VALUES (?,?)");
                stm3.setString(1,playerName);
                stm3.setInt(2,score);
                stm3.executeUpdate();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Unable to load players. try again...!").showAndWait();
            throw new RuntimeException(e);
        }
    }
}

