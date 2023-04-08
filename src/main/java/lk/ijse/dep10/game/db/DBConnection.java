package lk.ijse.dep10.game.db;

import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static DBConnection dbConnection;
    private final Connection connection;
    private DBConnection(){
        File configurationFile = new File("application.properties");
        Properties configuration = new Properties();
        try {
            FileReader fileReader = new FileReader(configurationFile);
            configuration.load(fileReader);
            fileReader.close();

            String host = configuration.getProperty("snake_game.host", "localhost");
            String port = configuration.getProperty("snake_game.port", "3306");
            String database = configuration.getProperty("snake_game.database", "snake_game_scores");
            String username = configuration.getProperty("snake_game.username", "root");
            String password = configuration.getProperty("snake_game.password", "");

            String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?createDatabaseIfNotExist=true&allowMultiQueries=true";
            connection = DriverManager.getConnection(url,username,password);

        } catch (FileNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,"Configuration file doesn't exist. try again...!").showAndWait();
            throw new RuntimeException(e);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR,"Failed to load configuration file. try again...!").showAndWait();
            throw new RuntimeException(e);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Unable to establish a database connection. try again...!").showAndWait();
            throw new RuntimeException(e);
        }
    }
    public static DBConnection getInstance(){
        return (dbConnection == null)? dbConnection = new DBConnection(): dbConnection;
    }
    public Connection getConnection(){
        return connection;
    }
}
