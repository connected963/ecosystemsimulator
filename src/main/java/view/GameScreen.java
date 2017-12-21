package main.java.view;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GameScreen extends Application {

    private Group root;
    private Scene scene;
    private Canvas canvas;
    private GraphicsContext graphicsContext;
    private AtomicInteger elementsCounter;
    private Font font;
    private Animation animation;

    private static final String BACKGROUND = "Assets/grass.png";

    public GameScreen() {
        this.elementsCounter = new AtomicInteger(0);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setupScreen(primaryStage);
        setupTitle(primaryStage, "Ecosystem Simulator");
        setupFont();

        primaryStage.show();

        setupAnimation();
    }

    private void setupScreen(Stage primaryStage) {
        this.root = new Group();
        this.scene = new Scene(root);
        StackPane holder = new StackPane();

        this.canvas = new Canvas(Toolkit.getDefaultToolkit().getScreenSize().width * 0.98, Toolkit.getDefaultToolkit().getScreenSize().height * 0.9);

        holder.getChildren().add(canvas);
        this.root.getChildren().add(holder);

        setBackround(holder);

        this.graphicsContext = canvas.getGraphicsContext2D();

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
    }

    private void setBackround(final StackPane stackPane) {
        final Image image = new Image(GameScreen.BACKGROUND);

        stackPane.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
    }

    private void setupTitle(final Stage primaryStage, final String title) {
        primaryStage.setTitle(title);
    }

    private void setupFont() {
        this.font = Font.font("Helvetica", FontWeight.BOLD, 20);
    }

    private void setupAnimation() {
        this.animation = new Animation.AnimationBuilder()
                                       .withCanvas(this.canvas)
                                       .withGraphicsContext(this.graphicsContext)
                                       .withElementsCounter(this.elementsCounter)
                                       .withFont(this.font)
                                       .build();

        this.animation.start();
    }
}