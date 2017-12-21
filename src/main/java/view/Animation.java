package main.java.view;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import main.java.common.Parameters;
import main.java.elements.Ant;
import main.java.elements.Element;
import main.java.elements.Sugar;
import main.java.elements.Tamandua;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;


public class Animation extends AnimationTimer {

    private Stage stage;
    private Canvas canvas;
    private GraphicsContext graphicsContext;
    private AtomicInteger elementsCounter;
    private LocalTime simulationStarted;
    private Font font;
    private LocalTime lastRenderedSugar;
    private final ForkJoinPool forkJoinPool;
    private final List<Element> tamanduas;
    private final List<Element> ants;
    private final List<Element> sugars;

    private final Long numeroCalorias = Long.valueOf(Parameters.getInstance().getNumeroCalorias());
    private final Integer tempoGeracaoAcucar = Parameters.getInstance().getTempoGeracaoAcucares();

    private static final String elementsCounterText = "Elements: %d";
    private static final String timeSimulationText = "Time: %02d:%02d:%02d";

    private Animation(AnimationBuilder animationBuilder) {
        this.canvas = animationBuilder.canvas;
        this.graphicsContext = animationBuilder.graphicsContext;
        this.elementsCounter = animationBuilder.elementsCounter;
        this.simulationStarted = LocalTime.now();
        this.font = animationBuilder.font;
        this.stage = animationBuilder.stage;

        this.lastRenderedSugar = LocalTime.MIN;
        this.forkJoinPool = new ForkJoinPool(200);
        this.tamanduas = new CopyOnWriteArrayList<>();
        this.ants = new CopyOnWriteArrayList<>();
        this.sugars = new CopyOnWriteArrayList<>();

        createInitialAnteaters();
        createAnts();
    }

    private void createInitialAnteaters() {
        Integer anteatersNumber = Parameters.getInstance().getNumeroTamanduas();

        for (int i = 0; i < anteatersNumber; i++) {
            Tamandua tamandua = new Tamandua(numeroCalorias, elementsCounter, graphicsContext, canvas, tamanduas, ants);
            tamanduas.add(tamandua);
            forkJoinPool.execute(tamandua);
        }
    }

    private void createAnts() {
        Integer antsNumber = Parameters.getInstance().getNumeroFormigas();

        for (int i = 0; i < antsNumber; i++) {
            Ant ant = new Ant(numeroCalorias, elementsCounter, graphicsContext, canvas, ants, sugars);
            ants.add(ant);
            forkJoinPool.execute(ant);
        }
    }

    @Override
    public void handle(long now) {
        render();

        if (!hasElements()) {
            stop();
        }

    }

    private void render() {
        clearGraphicsContext();

        for (int i = 0; i < tamanduas.size(); i++) {
            if (tamanduas.get(i).isAlive()) {
                tamanduas.get(i).render();
            }
        }

        for (int i = 0; i < ants.size(); i++) {
            if (ants.get(i).isAlive()) {
                ants.get(i).render();
            }
        }

        generateSugar();
        renderSugars();
        renderCounters();

    }

    @Override
    public void stop() {
        endOfSimulation();
        super.stop();
    }

    private void clearGraphicsContext() {
        this.graphicsContext.clearRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
    }

    private void renderSugars() {
        this.sugars.stream()
                .filter(Element::isAlive)
                .forEach(Element::render);
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
        final double x = this.canvas.getWidth() * 0.02;
        final double y = this.canvas.getHeight() * 0.05;

        setupFontContext();

        final String counterToShow = String.format(Animation.elementsCounterText, this.elementsCounter.get());

        this.graphicsContext.fillText(counterToShow, x, y);
        this.graphicsContext.strokeText(counterToShow, x, y);
    }

    private void renderSimulationTime() {
        final double x = this.canvas.getWidth() * 0.02;
        final double y = this.canvas.getHeight() * 0.085;

        setupFontContext();

        final Duration duration = Duration.between(simulationStarted, LocalTime.now());
        final String scoreToShow = String.format(Animation.timeSimulationText, duration.toHours(), duration.toMinutes() % 60, duration.toSeconds() % 60);

        this.graphicsContext.fillText(scoreToShow, x, y);
        this.graphicsContext.strokeText(scoreToShow, x, y);
    }

    private void generateSugar() {
        validateAndAddSugar(createNewSugar());
    }

    private void validateAndAddSugar(final Sugar newSugar) {
        final LocalTime now = LocalTime.now();

        if (isAValidLocationToSugar(newSugar) && Duration.between(this.lastRenderedSugar, now).getNano() > tempoGeracaoAcucar) {
            this.sugars.add(newSugar);
            this.lastRenderedSugar = now;
        }
    }

    private boolean isAValidLocationToSugar(final Sugar newSugar) {

        final Function<Sugar, Boolean> hasIntersectsWithAnotherSugar = s -> sugars.stream()
                .map(Element::getSprite)
                .anyMatch(s::intersects);

        return !hasIntersectsWithAnotherSugar.apply(newSugar);
    }

    private Sugar createNewSugar() {
        return new Sugar(graphicsContext, canvas, sugars);
    }

    private Boolean hasElements() {
        return !this.tamanduas.isEmpty() || !this.ants.isEmpty();
    }

    private void endOfSimulation() {
        stage.setTitle("Finalized!!!");
    }

    public static class AnimationBuilder {

        private Canvas canvas;
        private GraphicsContext graphicsContext;
        private AtomicInteger elementsCounter;
        private Font font;
        private Stage stage;


        public AnimationBuilder withCanvas(Canvas canvas) {
            this.canvas = canvas;
            return this;
        }

        public AnimationBuilder withGraphicsContext(GraphicsContext graphicsContext) {
            this.graphicsContext = graphicsContext;
            return this;
        }

        public AnimationBuilder withElementsCounter(AtomicInteger elementsCounter) {
            this.elementsCounter = elementsCounter;
            return this;
        }

        public AnimationBuilder withFont(Font font) {
            this.font = font;
            return this;
        }

        public AnimationBuilder withStage(Stage stage) {
            this.stage = stage;
            return this;
        }

        public Animation build() {
            return new Animation(this);
        }
    }
}
