package com.example.demineur;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class Runner extends Application {
    public static Stage stageStatic;
    public static double height;
    public static double width;
    @Override
    public void start(Stage stage) throws IOException {
        height = Screen.getPrimary().getBounds().getHeight();
        width = Screen.getPrimary().getBounds().getWidth();
        FXMLLoader fxmlLoader = new FXMLLoader(Runner.class.getResource("StartGame.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        stageStatic = stage;
        stageStatic.setFullScreen(true);
        Runner.stageStatic.setFullScreen(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}