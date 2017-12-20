package main.java.view;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import main.java.communication.Movements;
import main.java.elements.Element;
import main.java.elements.Sugar;
import main.java.elements.Tamandua;
import main.java.mathutils.PrimeNumber;
import main.java.model.Sprite;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class Animation extends AnimationTimer {

    private Canvas canvas;
    private GraphicsContext graphicsContext;
    private Sprite robot;
    private List<Sprite> bomb;
    private AtomicInteger elementsCounter;
    private LocalTime simulationTime;
    private List<Movements> movements;
    private Font font;
    private LocalTime lastRenderedSugar;
    private Semaphore screen;
    private final ForkJoinPool forkJoinPool;
    private final List<Element> tamanduas;

    private static final String elementsCounterText = "Elements: %d";
    private static final String timeSimulationText = "'Time:' hh:mm:ss";

    private Animation(AnimationBuilder animationBuilder) {
        this.canvas = animationBuilder.canvas;
        this.graphicsContext = animationBuilder.graphicsContext;
        this.robot = animationBuilder.robot;
        this.bomb = animationBuilder.bomb;
        this.elementsCounter = animationBuilder.elementsCounter;
        this.simulationTime = LocalTime.of(0, 0);
        this.movements = animationBuilder.movements;
        this.font = animationBuilder.font;

        this.lastRenderedSugar = LocalTime.MIN;
        this.screen = new Semaphore(1);
        this.forkJoinPool = new ForkJoinPool(30);
        this.tamanduas = new ArrayList<>();

        Tamandua tamandua = new Tamandua(10L, elementsCounter, graphicsContext, canvas, tamanduas, new ArrayList<>());
        Tamandua tamandua1 = new Tamandua(10L, elementsCounter, graphicsContext, canvas, tamanduas, new ArrayList<>());
//        Tamandua tamandua2 = new Tamandua(10L, elementsCounter, graphicsContext, canvas, tamanduas, new ArrayList<>());
//        Tamandua tamandua3 = new Tamandua(10L, elementsCounter, graphicsContext, canvas, tamanduas, new ArrayList<>());
//        Tamandua tamandua4 = new Tamandua(10L, elementsCounter, graphicsContext, canvas, tamanduas, new ArrayList<>());
//        Tamandua tamandua5 = new Tamandua(10L, elementsCounter, graphicsContext, canvas, tamanduas, new ArrayList<>());

        tamanduas.addAll(List.of(tamandua, tamandua1/*, tamandua2, tamandua3, tamandua4, tamandua5*/));

        forkJoinPool.execute(tamandua);
        forkJoinPool.execute(tamandua1);
//        forkJoinPool.execute(tamandua2);
//        forkJoinPool.execute(tamandua3);
//        forkJoinPool.execute(tamandua4);
//        forkJoinPool.execute(tamandua5);
    }

    @Override
    public void handle(long now) {
//        findMovement();
//        movRobot();
//        collisionDetect();
        render();
    }

    private void collisionDetect() {
        final Predicate<Sprite> intersectsWithRobot = robot::intersects;

        final List<Sprite> collisionBombs = this.bomb.stream()
                                                .filter(intersectsWithRobot)
                                                .collect(Collectors.toList());

        this.bomb.removeAll(collisionBombs);
    }


    private void render() {
        clearGraphicsContext();

        synchronized (tamanduas) {
            this.tamanduas.forEach(Element::render);
        }

//        generateSugar();
//        renderSugars();
        renderCounters();

    }

    private void clearGraphicsContext() {
        this.graphicsContext.clearRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
    }

    private void renderSugars() {
        final Consumer<Sprite> renderBomb = bomb -> bomb.render(this.graphicsContext);

        this.bomb.forEach(renderBomb);
    }

    private void renderCounters() {
        renderElementsCounter();
        renderSimulationTime();
    }

    private void setupFontContext() {
        this.graphicsContext.setFont(this.font);
        this.graphicsContext.setFill(Color.BLACK);
        this.graphicsContext.setStroke(Color.WHITE);
        this.graphicsContext.setLineWidth(1);
    }


    private void renderElementsCounter() {
        final double x = this.canvas.getWidth() * 0.87;
        final double y = this.canvas.getHeight() * 0.05;

        setupFontContext();

        final String counterToShow = String.format(Animation.elementsCounterText, this.elementsCounter.get());

        this.graphicsContext.fillText(counterToShow, x, y);
        this.graphicsContext.strokeText(counterToShow, x, y);
    }

    private void renderSimulationTime() {
        final double x = this.canvas.getWidth() * 0.87;
        final double y = this.canvas.getHeight() * 0.085;

        setupFontContext();

        final String scoreToShow = this.simulationTime.format(DateTimeFormatter.ofPattern(Animation.timeSimulationText));
        this.graphicsContext.fillText(scoreToShow, x, y);
        this.graphicsContext.strokeText(scoreToShow, x, y);
    }

    private void generateSugar() {
        final Random random = new Random();

        final int x = random.nextInt((int)this.canvas.getWidth() - 30);
        final int y = random.nextInt((int)this.canvas.getHeight() - 30);

        validateAndAddSugar(createNewBomb(x, y), x, y);

    }

    private void validateAndAddSugar(final Sprite newBomb, final int x, final int y) {
        final LocalTime now = LocalTime.now();

        if (isAValidLocationToSugar(newBomb, x, y) && Duration.between(this.lastRenderedSugar, now).getSeconds() > 3) {
            this.bomb.add(newBomb);

            this.lastRenderedSugar = now;
        }
    }

    private boolean isAValidLocationToSugar(final Sprite newBomb, final int x, final int y) {

        final Function<Sprite, Boolean> hasIntersectsWithAnotherBomb = b -> bomb.stream().anyMatch(b::intersects);

        return !hasIntersectsWithAnotherBomb.apply(newBomb)
            && !PrimeNumber.isPrime(x)
            && !PrimeNumber.isPrime(y);
    }

    private Sprite createNewBomb(int x, int y) {
        final Sprite newBomb = new Sprite();

        newBomb.setImage(Sugar.sugarImage);
        newBomb.setPosition(x, y);

        return newBomb;
    }

    public static class AnimationBuilder {

        private Canvas canvas;
        private GraphicsContext graphicsContext;
        private Sprite robot;
        private List<Sprite> bomb;
        private AtomicInteger elementsCounter;
        private List<Movements> movements;
        private Font font;


        public AnimationBuilder withCanvas(Canvas canvas) {
            this.canvas = canvas;
            return this;
        }

        public AnimationBuilder withGraphicsContext(GraphicsContext graphicsContext) {
            this.graphicsContext = graphicsContext;
            return this;
        }

        public AnimationBuilder withRobot(Sprite robot) {
            this.robot = robot;
            return this;
        }

        public AnimationBuilder withBomb(List<Sprite> bomb) {
            this.bomb = bomb;
            return this;
        }

        public AnimationBuilder withElementsCounter(AtomicInteger elementsCounter) {
            this.elementsCounter = elementsCounter;
            return this;
        }

        public AnimationBuilder withMovements(List<Movements> movements) {
            this.movements = movements;
            return this;
        }

        public AnimationBuilder withFont(Font font) {
            this.font = font;
            return this;
        }

        public Animation build() {
            return new Animation(this);
        }
    }
}
