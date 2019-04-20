package model;

import javafx.animation.TranslateTransition;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class GameSubScene extends SubScene {

    private final static String FONT_PATH = "src/model/resources/consolab.ttf";
    private final static String BACKGROUND_IMAGE = "/model/resources/blue_panel.png";

    private boolean isHidden;

    public GameSubScene(int WIDTH, int HEIGHT) {

        super(new AnchorPane(), WIDTH, HEIGHT);
        prefWidth(WIDTH);
        prefHeight(HEIGHT);

        BackgroundImage image = new BackgroundImage(new Image(BACKGROUND_IMAGE, WIDTH, HEIGHT, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);

        AnchorPane root = (AnchorPane) this.getRoot();
        root.setBackground(new Background(image));

        isHidden = true;

        setLayoutX(1450);
        setLayoutY(350);
    }

    public void moveSubScene() {

        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.3));
        transition.setNode(this);
        if(isHidden) {
            transition.setToX(-950);
            isHidden = false;
        }
        else {
            transition.setToX(0);
            isHidden = true;
        }

        transition.play();
    }

    public AnchorPane getPane() {
        return (AnchorPane) this.getRoot();
    }
}
