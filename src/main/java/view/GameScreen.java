package main.java.view;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import main.java.communication.Movements;
import main.java.model.Sprite;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GameScreen extends Application {

    private Group root;
    private Scene scene;
    private Canvas canvas;
    private GraphicsContext graphicsContext;
    private List<Sprite> bomb;
    private AtomicInteger elementsCounter;
    private List<Movements> movements;
    private Font font;
    private Animation animation;

    public GameScreen() {
        this.bomb = new ArrayList<>();
        this.movements = new ArrayList<>();

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

        this.canvas = new Canvas(1024, 700);

        holder.getChildren().add(canvas);
        this.root.getChildren().add(holder);

        holder.setStyle("-fx-background-color: #b5b5b5");

        this.graphicsContext = canvas.getGraphicsContext2D();

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
    }

    private void setupTitle(final Stage primaryStage, final String title) {
        primaryStage.setTitle(title);
    }

    private void setupFont() {
        this.font = Font.font("Helvetica", FontWeight.BOLD, 40);
    }

    private void setupAnimation() {
        this.animation = new Animation.AnimationBuilder()
                                       .withCanvas(this.canvas)
                                       .withGraphicsContext(this.graphicsContext)
                                       .withBomb(this.bomb)
                                       .withElementsCounter(this.elementsCounter)
                                       .withMovements(this.movements)
                                       .withFont(this.font)
                                       .build();

        this.animation.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}