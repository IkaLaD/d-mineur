package com.example.demineur.Controlleur;

import com.example.demineur.Runner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartGame implements Initializable {

    @FXML
    public TextField heightTextField;
    @FXML
    public TextField widthTextField;
    @FXML
    public TextField bombRate;
    @FXML
    public Button startButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void startButton(MouseEvent mouseEvent) throws IOException {
        int height = 0;
        int width = 0;
        int bombRate = 0;
        boolean unvalid = false;

        try{
            height = Integer.parseInt(heightTextField.getText());
            width = Integer.parseInt(widthTextField.getText());
            bombRate = Integer.parseInt(this.bombRate.getText());
            if(bombRate<10 || bombRate>=100)
                unvalid = true;
        }
        catch (NumberFormatException n){
           n.printStackTrace();
           unvalid = true;
        }

        heightTextField.clear();
        widthTextField.clear();
        this.bombRate.clear();

        if(!unvalid){
            GameControlleur.height = height;
            GameControlleur.width = width;
            GameControlleur.bombRate = bombRate/100.0;
            FXMLLoader fxmlLoader = new FXMLLoader(Runner.class.getResource("Game.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), Runner.width, Runner.height);
            Runner.stageStatic.setScene(scene);
            Runner.stageStatic.setFullScreen(true);
            Runner.stageStatic.setFullScreen(false);
        }
    }
}
