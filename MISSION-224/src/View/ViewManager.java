package View;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.GameButtons;

import java.util.ArrayList;
import java.util.List;

public class ViewManager {

    private static final int SCREEN_WIDTH = 1200;
    private static final int SCREEN_HEIGHT = 720;
    private static final int MENU_BUTTON_START_X = 100;
    private static final int MENU_BUTTON_START_Y = 200;

    List<GameButtons> menuButtons;

    private AnchorPane mainPane;
    private Scene mainScene;
    private Stage mainStage;

    public ViewManager() {

        menuButtons = new ArrayList<>();
        mainPane = new AnchorPane();
        mainScene = new Scene(mainPane, SCREEN_WIDTH, SCREEN_HEIGHT);
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        createBackground();
        createLogo();
    }

    public Stage getMainStage(String title) {
        mainStage.setTitle(title);
        return mainStage;
    }

    private void addButton(GameButtons button) {

        button.setLayoutX(MENU_BUTTON_START_X);
        button.setLayoutY(MENU_BUTTON_START_Y + menuButtons.size()*100);
        menuButtons.add(button);
        mainPane.getChildren().add(button);
    }

    public void createMenuButtons() {

        createStartButton();
        createScoreButton();
        createHelpButton();
        createCreditsButton();
        createExitButton();
    }

    private void createStartButton() {

        GameButtons startButton = new GameButtons("Start");
        addButton(startButton);
    }

    private void createScoreButton() {

        GameButtons ScoreButton = new GameButtons("Score");
        addButton(ScoreButton);
    }

    private void createHelpButton() {

        GameButtons HelpButton = new GameButtons("Help");
        addButton(HelpButton);
    }

    private void createCreditsButton() {

        GameButtons CreditsButton = new GameButtons("Credits");
        addButton(CreditsButton);
    }

    private void createExitButton() {

        GameButtons ExitButton = new GameButtons("Exit");
        addButton(ExitButton);
    }

    public void createBackground() {

        Image backgroundImage = new Image("view/resources/menu_background.jpg", SCREEN_WIDTH, SCREEN_HEIGHT, false, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
        mainPane.setBackground(new Background(background));
    }

    private void createLogo() {

        ImageView logo = new ImageView("view/resources/GameLogo.png");
        logo.setLayoutX(180);
        logo.setLayoutY(-160);

        logo.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                logo.setEffect(new DropShadow());
            }
        });

        logo.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                logo.setEffect(null);
            }
        });

        mainPane.getChildren().add(logo);
    }
}
