package model;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.text.Font;

import java.io.*;

public class InfoLabel extends Label {

    public final static String FONT_PATH = "src/model/resources/consolab.ttf";
    //public final static String FOREGROUND = "/model/resources/green_foreground.png";

    BufferedReader br = null;

    public InfoLabel(String path) {

        setPrefWidth(600);
        setPrefHeight(400);
        setPadding(new Insets(40, 40, 40, 40));

        try {
            br = new BufferedReader(new FileReader(path));
            setText(br.readLine());
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        setWrapText(true);
        setLabelFont();

        /*BackgroundImage background = new BackgroundImage(new Image(FOREGROUND, 580, 380, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
        setBackground(new Background(background));*/
    }

    public void setLabelFont() {

        try {
            setFont(Font.loadFont(new FileInputStream(new File(FONT_PATH)), 23));
        } catch (FileNotFoundException e) {
            setFont(Font.font("Verdana", 23));
        }
    }
}
