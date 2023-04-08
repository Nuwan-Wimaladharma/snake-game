package lk.ijse.dep10.game.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.dep10.game.util.Direction;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import static javafx.scene.paint.Color.*;

public class NewGameViewController {

    public AnchorPane root;
    public Label txtScore;
    public Label txtPlayer;
    @FXML
    private String playerName;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 730;
    private static final int FOOD_RADIUS = 5;
    private static final int SNAKE_HEAD = 8;
    private static final int STEP = 5;
    private static final int ROWS = 40;
    private static final int COLUMNS = ROWS;
    private static final int SQUARE_SIZE = WIDTH / ROWS;
    private static final String[] FOOD_IMAGES = new String[]{"/img/egg.png","/img/easter-egg.png"};
    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;
    private GraphicsContext gc;
    private ArrayList<Point> snakeBody = new ArrayList<>();
    private Point snakeHead;
    private Image foodImage;
    private int foodX;
    private int foodY;
    private boolean gameOver;
    private Circle food;
    private Circle snake;
    private Direction currectDirection;
    private Timeline timeline;
    private int currentPosX;
    private int currentPosY;

    public void getPlayerName(String name){
        playerName = name;
    }
    public void initialize(){
        Platform.runLater(() -> {
            txtPlayer.setText("Player : " + playerName);
            newFood();
            newSnake();
            root.getScene().addEventFilter(KeyEvent.KEY_PRESSED,event -> {
                KeyCode code = event.getCode();
                if (code == KeyCode.UP && currectDirection != Direction.DOWN){
                    if (timeline != null){
                        timeline.stop();
                    }
                    currectDirection = Direction.UP;
                    KeyFrame key = new KeyFrame(Duration.millis(50), event2 -> {
                        snake.setCenterY(snake.getCenterY() - STEP);
                        currentPosX = (int) snake.getCenterX();
                        currentPosY = (int) snake.getCenterY();
                        if (gameOverBeyondBoundary()){
                            timeline.stop();
                            Stage closeStage = (Stage) root.getScene().getWindow();
                            closeStage.close();
                            openGameOverView();
                        }
                    });
                    timeline = new Timeline(key);
                    timeline.setCycleCount(Animation.INDEFINITE);
                    timeline.playFromStart();
                }
                else if (code == KeyCode.DOWN && currectDirection != Direction.UP){
                    if (timeline != null){
                        timeline.stop();
                    }
                    currectDirection = Direction.DOWN;
                    KeyFrame key = new KeyFrame(Duration.millis(50), event2 -> {
                        snake.setCenterY(snake.getCenterY() + STEP);
                        currentPosX = (int) snake.getCenterX();
                        currentPosY = (int) snake.getCenterY();
                        if (gameOverBeyondBoundary()){
                            timeline.stop();
                            Stage closeStage = (Stage) root.getScene().getWindow();
                            closeStage.close();
                            openGameOverView();
                        }
                    });
                    timeline = new Timeline(key);
                    timeline.setCycleCount(Animation.INDEFINITE);
                    timeline.playFromStart();
                }
                else if (code == KeyCode.LEFT && currectDirection != Direction.RIGHT){
                    if (timeline != null){
                        timeline.stop();
                    }
                    currectDirection = Direction.LEFT;
                    KeyFrame key = new KeyFrame(Duration.millis(50), event2 -> {
                        snake.setCenterX(snake.getCenterX() - STEP);
                        currentPosX = (int) snake.getCenterX();
                        currentPosY = (int) snake.getCenterY();
                        if (gameOverBeyondBoundary()){
                            timeline.stop();
                            Stage closeStage = (Stage) root.getScene().getWindow();
                            closeStage.close();
                            openGameOverView();
                        }
                    });
                    timeline = new Timeline(key);
                    timeline.setCycleCount(Animation.INDEFINITE);
                    timeline.playFromStart();
                }
                else if (code == KeyCode.RIGHT && currectDirection != Direction.LEFT){
                    if (timeline != null){
                        timeline.stop();
                    }
                    currectDirection = Direction.RIGHT;
                    KeyFrame key = new KeyFrame(Duration.millis(50), event2 -> {
                        snake.setCenterX(snake.getCenterX() + STEP);
                        currentPosX = (int) snake.getCenterX();
                        currentPosY = (int) snake.getCenterY();
                        if (gameOverBeyondBoundary()){
                            timeline.stop();
                            Stage closeStage = (Stage) root.getScene().getWindow();
                            closeStage.close();
                            openGameOverView();
                        }
                    });
                    timeline = new Timeline(key);
                    timeline.setCycleCount(Animation.INDEFINITE);
                    timeline.playFromStart();
                }
            });
        });

    }
    public void rootOnKeyPressed(KeyEvent keyEvent) {
    }
    private void newFood(){
        Random random = new Random();
        food = new Circle(random.nextInt(WIDTH),random.nextInt(HEIGHT),FOOD_RADIUS);
        food.setFill(RED);
        root.getChildren().add(food);
    }

    private void newSnake(){
        snake = new Circle(WIDTH / 2, HEIGHT / 2,SNAKE_HEAD);
        snake.setFill(BLACK);
        root.getChildren().add(snake);
    }
    private boolean gameOverBeyondBoundary(){
        boolean gameOver = false;
        if (currentPosX < 0 || currentPosY < 0 || currentPosX > WIDTH || currentPosY > HEIGHT){
            gameOver = true;
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
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR,"Unable to load GameOverView.fxml file, try again...!").showAndWait();
            throw new RuntimeException(e);
        }
    }
}

