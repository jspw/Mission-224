package model;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GameButtons extends Button {

    private final String FONT_PATH = "src/model/resources/consolab.ttf";
    private final String BUTTON_PRESSED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/model/resources/blue_button_pressed.png');";
    private final String BUTTON_RElEASED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/model/resources/blue_button_released.png');";
    private final int ButtonWidth = 190;
    private final int ButtonHeight = 49;

    public GameButtons(String text) {

        //setLayoutX(x-(ButtonWidth/2));
        //setLayoutY(y-(ButtonHeight/2)+position);
        setText(text);
        setButtonFont();
        setPrefWidth(ButtonWidth);
        setPrefHeight(ButtonHeight - 4);
        setStyle(BUTTON_RElEASED_STYLE);
        initializeButtonListeners();
    }

    public void setButtonFont() {

        try {
            setFont(Font.loadFont(new FileInputStream(FONT_PATH), 23));
        } catch (FileNotFoundException e) {
            setFont(Font.font("Verdana", 23));
        }
    }

    public void setButtonPressedStyle() {

        setStyle(BUTTON_PRESSED_STYLE);
        setPrefHeight(ButtonHeight - 4);
        setLayoutY(getLayoutY() + 4);
    }

    public void setButtonReleasedStyle() {

        setStyle(BUTTON_RElEASED_STYLE);
        setPrefHeight(ButtonHeight);
        setLayoutY(getLayoutY() - 4);
    }

    private void initializeButtonListeners() {

        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton().equals(MouseButton.PRIMARY))
                    setButtonPressedStyle();
            }
        });

        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton().equals(MouseButton.PRIMARY))
                    setButtonReleasedStyle();
            }
        });

        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setEffect(new DropShadow());
            }
        });

        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setEffect(null);
            }
        });
    }
}
