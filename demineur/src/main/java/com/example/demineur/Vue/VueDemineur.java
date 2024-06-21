package com.example.demineur.Vue;

import com.example.demineur.Runner;
import com.example.demineur.module.Demineur;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;

public class VueDemineur {

    private IntegerProperty[][] openedCase;
    private IntegerProperty[][] caseFlaged;
    private Button[][] buttons;
    private TilePane tilePane;
    private Demineur demineur;
    private VBox vBox;
    private Label bombFlagged;
    private IntegerProperty cptFlag;
    private int buttonSize;
    private String buttonFontSize;
    private int gapCase;
    public VueDemineur(TilePane tilePane, Demineur demineur, VBox vBox, Label bombFlagged){
        this.tilePane = tilePane;
        this.gapCase = 2;

        this.vBox = vBox;
        this.bombFlagged = bombFlagged;

        this.demineur = demineur;
        this.openedCase = demineur.getCaseOpened();
        this.caseFlaged = new IntegerProperty[openedCase.length][openedCase[0].length];
        this.cptFlag = new SimpleIntegerProperty(0);
        cptFlagListener();

        sizeGameBoard();
        this.buttonFontSize = "-fx-font: "+(int)(buttonSize/2)+"px arial;";

        setCaseFlaged();
        setListenerLoose(demineur.isLoose());

        this.buttons = new Button[openedCase.length][openedCase[0].length];

        for(int i = 0 ; i < openedCase.length; i++){
            for(int j = 0 ; j < openedCase[i].length ; j++){
                Button b = createButtonViewGame(openedCase[i][j].get(), i, j);
                this.buttons[i][j] = b;
                flagedListener(i, j, b);
                this.tilePane.getChildren().add(b);
                setListenerCase(openedCase[i][j], b);
            }
        }
    }

    public void sizeGameBoard(){
        double height;
        double width;

        if(openedCase.length > openedCase[0].length){
            buttonSize = (int)(Math.floor(800.0 / openedCase.length));

        }
        else
            buttonSize = (int)(Math.floor(800.0 / openedCase[0].length));


        height = (buttonSize+gapCase)*openedCase.length;
        width = (buttonSize+gapCase)*openedCase[0].length;

        tilePane.setMaxSize(width, height);
        tilePane.setMinSize(width, height);
        tilePane.setVgap(gapCase);
        tilePane.setHgap(gapCase);
    }

    public Button createButtonViewGame(int value, int h, int w){
        Button button = new Button();
        button.setMaxSize(buttonSize, buttonSize);
        button.setMinSize(buttonSize, buttonSize);
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: #dddddd;"+ buttonFontSize);
        button.setOnMouseEntered((event)-> {
            if(!demineur.isCaseDisplayed(h, w) && !isCaseFlaged(h, w) && !demineur.isLoose().get())
                button.setStyle("-fx-background-color: #cccccc; "+ buttonFontSize);
        });

        button.setOnMouseExited((event)->{
            if(!demineur.isCaseDisplayed(h, w) && !isCaseFlaged(h, w) && !demineur.isLoose().get())
                button.setStyle("-fx-background-color: #dddddd;"+ buttonFontSize);
        });

        if(value>0)
            button.setText(value+"");

        button.setOnMouseClicked((event)->{
            if(!demineur.isLoose().get()) {
                if (event.getButton().equals(MouseButton.PRIMARY))
                    caseClicked(h, w);
                else if (event.getButton().equals(MouseButton.SECONDARY))
                    flagedOrUnflaged(h, w);
            }
        });
        return button;
    }

    public void setCaseFlaged(){
        for(int h = 0 ; h < caseFlaged.length ; h++){
            for(int w = 0 ; w < caseFlaged[h].length ; w++){
                caseFlaged[h][w] = new SimpleIntegerProperty(0);
            }
        }
    }

    public void caseClicked(int h, int w){
        int cptFlag = 0;
        for(int i = -1 ; i <= 1 ; i++){
            for(int j = -1 ; j <= 1 ; j++) {
                if (demineur.isOnBoard(h + i, w + j) && isCaseFlaged(h + i, w + j))
                    cptFlag++;
            }
        }

        if(cptFlag == openedCase[h][w].get()){
            for(int i = -1 ; i <= 1 ; i++){
                for(int j = -1 ; j <= 1 ; j++)
                    if(demineur.isOnBoard(h+i, w+j) && !demineur.isCaseDisplayed(h+i, w+j) && !isCaseFlaged(h+i, w+j))
                        this.demineur.clicked(h+i, w+j);
            }
            return;
        }
        this.demineur.clicked(h, w);
    }
    public void flagedListener(int h, int w, Button button){
        caseFlaged[h][w].addListener((e)->{
            if(caseFlaged[h][w].get()==1)
                button.setStyle("-fx-background-color : rgb(0, 0, 0);");
            else
                button.setStyle("-fx-background-color : #dddddd");
        });
    }

    public void cptFlagListener(){
        this.bombFlagged.setText(cptFlag.get()+" flag on "+demineur.getCptBomb()+".");
        this.cptFlag.addListener((e)->bombFlagged.setText(cptFlag.get()+" flag on "+demineur.getCptBomb()+"."));
    }
    public void flagedOrUnflaged(int h, int w){
        if(openedCase[h][w].get()==-1) {
            caseFlaged[h][w].set(caseFlaged[h][w].get() == 1 ? 0 : 1);
            cptFlag.set(caseFlaged[h][w].get() == 1 ? cptFlag.get()+1 : cptFlag.get()-1);
        }
    }

    public boolean isCaseFlaged(int h, int w){
        return caseFlaged[h][w].get()==1;
    }

    public void transformButtonToBomb(Button button){
        button.setStyle("-fx-background-color: rgb(255, 0, 0);");
    }


    public void setListenerCase(IntegerProperty value, Button button){
        value.addListener((obs, old, nouv)->{
            if(!demineur.isLoose().get())
                vueCase(value, button);
        });
    }

    public void setListenerLoose(BooleanProperty booleanProperty){
        booleanProperty.addListener((obs, old, nouv)-> {
            if (booleanProperty.get()) {
                int[][] tab = demineur.getBombBoard();
                for (int i = 0; i < openedCase.length; i++) {
                    for (int j = 0; j < openedCase[i].length; j++) {
                        if(tab[i][j]==1)
                            transformButtonToBomb(this.buttons[i][j]);
                    }
                }
                Button button = new Button();
                button.setText("Relancer une partie");
                button.setOnMouseClicked((e)->{
                    FXMLLoader fxmlLoader = new FXMLLoader(Runner.class.getResource("StartGame.fxml"));
                    Scene scene = null;
                    try {
                        scene = new Scene(fxmlLoader.load(), Runner.width, Runner.height);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    Runner.stageStatic.setScene(scene);
                    Runner.stageStatic.setFullScreen(true);
                    Runner.stageStatic.setFullScreen(false);
                });
                vBox.getChildren().add(button);
            }
        });
    }

    public void vueCase(IntegerProperty value, Button button){
        int red;
        int green;
        int total = 100 * value.get();
        if(total<=360) {
            green = total > 180 ? 360 - total : 180;
            red = total > 180 ? 360 : total;
        }
        else{
            green = 0;
            red = 255;
        }
        String style = total > 0 ?  "rgb("+red+", "+green+", 0);" : "#aaaaaa;";
        button.setStyle("-fx-background-color: "+style+";"+buttonFontSize);
        if(value.get()!=0)
            button.setText(value.get() + "");

    }
}
