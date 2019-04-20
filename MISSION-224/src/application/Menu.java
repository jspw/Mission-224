package application;

import View.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Menu extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        ViewManager manager = new ViewManager();
        manager.createMenuButtons();
        primaryStage = manager.getMainStage("Main Menu");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
