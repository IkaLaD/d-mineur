package com.example.demineur.Controlleur;

import com.example.demineur.Vue.VueDemineur;
import com.example.demineur.module.Demineur;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class GameControlleur implements Initializable {

    public TilePane tilePane;
    public VBox VBox;
    public Label bombFlagged;
    public HBox hbox;
    private Demineur demineur;
    public static int height;
    public static int width;
    public static double bombRate;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        demineur = new Demineur(height, width, bombRate);
        new VueDemineur(tilePane, demineur, VBox, bombFlagged);
    }
}
