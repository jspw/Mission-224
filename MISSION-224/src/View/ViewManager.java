package View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.GameButtons;
import model.GameSubScene;
import model.InfoLabel;

import java.util.ArrayList;
import java.util.List;

public class ViewManager {

    private static final int SCREEN_WIDTH = 1200;
    private static final int SCREEN_HEIGHT = 720;
    private static final int MENU_BUTTON_START_X = 100;
    private static final int MENU_BUTTON_START_Y = 200;
    private static final String CREDITS_PATH = "src/model/documents/Credits.txt";
    private static final String SCORE_PATH = "src/model/documents/Score.txt";
    private static final String HELP_PATH = "src/model/documents/Help.txt";

    List<GameButtons> menuButtons;

    private AnchorPane mainPane;
    private Scene mainScene;
    private Stage mainStage;
    private GameSubScene creditsSubScene;
    private GameSubScene helpSubScene;
    private GameSubScene scoreSubScene;
    private GameSubScene sceneToHide;

    public ViewManager() {

        menuButtons = new ArrayList<>();
        mainPane = new AnchorPane();
        mainScene = new Scene(mainPane, SCREEN_WIDTH, SCREEN_HEIGHT);
        mainStage = new Stage();
        mainStage.setScene(mainScene);

        createBackground();
        createLogo();
        createSubScene();
    }

    public void showSubScene(GameSubScene subscene) {

        if(sceneToHide != null)
            sceneToHide.moveSubScene();
        subscene.moveSubScene();
        sceneToHide = subscene;
    }

    public void createSubScene() {

        creditsSubScene = new GameSubScene(600, 300);
        mainPane.getChildren().add(creditsSubScene);

        helpSubScene = new GameSubScene(600, 300);
        mainPane.getChildren().add(helpSubScene);

        scoreSubScene = new GameSubScene(600, 300);
        mainPane.getChildren().add(scoreSubScene);
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

        GameButtons startButton = new GameButtons("Play");
        addButton(startButton);
    }

    private void createScoreButton() {

        GameButtons ScoreButton = new GameButtons("Score");
        addButton(ScoreButton);

        InfoLabel scoreLabel = new InfoLabel(SCORE_PATH);
        scoreLabel.setLayoutX(110);
        scoreLabel.setLayoutY(25);
        scoreSubScene.getPane().getChildren().add(scoreLabel);

        ScoreButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showSubScene(scoreSubScene);
            }
        });
    }

    private void createHelpButton() {

        GameButtons HelpButton = new GameButtons("Help");
        addButton(HelpButton);

        InfoLabel helpLabel = new InfoLabel(HELP_PATH);
        helpLabel.setLayoutX(110);
        helpLabel.setLayoutY(25);
        helpSubScene.getPane().getChildren().add(helpLabel);

        HelpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showSubScene(helpSubScene);
            }
        });
    }

    private void createCreditsButton() {

        GameButtons CreditsButton = new GameButtons("Credits");
        addButton(CreditsButton);

        InfoLabel creditLabel = new InfoLabel(CREDITS_PATH);
        creditLabel.setLayoutX(110);
        creditLabel.setLayoutY(25);
        creditsSubScene.getPane().getChildren().add(creditLabel);

        CreditsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showSubScene(creditsSubScene);
            }
        });
    }

    private void createExitButton() {

        GameButtons ExitButton = new GameButtons("Exit");
        addButton(ExitButton);

        ExitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainStage.close();
            }
        });
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
        logo.setEffect(new DropShadow());

        /*logo.setOnMouseEntered(new EventHandler<MouseEvent>() {
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
        });*/

        mainPane.getChildren().add(logo);
    }
}
