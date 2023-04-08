package lk.ijse.dep10.game.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.awt.*;
import java.util.ArrayList;

import static javafx.scene.paint.Color.*;

public class NewGameViewController {

    @FXML
    private Canvas cvnGame;
    private String playerName;
    private static final int WIDTH = 800;
    private static final int HEIGHT = WIDTH;
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
    private int currentDirection;

    public void getPlayerName(String name){
        playerName = name;
    }
    public void initialize(){
        Platform.runLater(() -> {
            gc = cvnGame.getGraphicsContext2D();
            drawBackground(gc);
        });
    }
    private void drawBackground(GraphicsContext gc){
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if ((i +j) % 2 == 0){
                    gc.setFill(Color.color(0.7,0.78,0.98,0.05));
                }
                else {
                    gc.setFill(Color.color(0.7,0.78,0.98,0.1));
                }
                gc.fillRect(i * SQUARE_SIZE,j * SQUARE_SIZE,SQUARE_SIZE,SQUARE_SIZE);
            }
        }
    }

}

