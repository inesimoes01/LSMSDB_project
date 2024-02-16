package com.example.lsmsdb;

import com.example.lsmsdb.Database.DatabaseMongoDB;
import com.example.lsmsdb.Database.DatabaseNeo4j;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class HelloApplication extends Application {
    private static Stage stc;

    @Override
    public void start(Stage primaryStage) throws IOException {
        stc = primaryStage;
        primaryStage.setResizable(false);
        DatabaseMongoDB.connectMongoDB();
        DatabaseNeo4j.connectNeo4j();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        stc.setTitle("CineLink");
        primaryStage.setScene(scene);
        primaryStage.show();

        // before closing
        stc.setOnCloseRequest(event -> {
            DatabaseMongoDB.closeMongoDB();
        });

    }

    public static void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource(fxml)));
        stc.getScene().setRoot(pane);
    }

    public static void main(String[] args) {
        launch();
    }
}