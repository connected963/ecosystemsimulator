package main.java;

import javafx.stage.Stage;
import main.java.view.GameScreen;

/**
 * Created by connected on 7/1/17.
 */
public class Application extends javafx.application.Application {

    public static void main(String[] args) {
        GameScreen.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //GameScreen.launch(args);
    }
}
