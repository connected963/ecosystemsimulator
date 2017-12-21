package main.java.elements;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import main.java.common.Parameters;
import main.java.communication.Movements;
import main.java.model.Sprite;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Ant extends RecursiveAction implements Element {

    private static final String antImage = "Assets/ant.png";

    private Long calorieCounter;

    private final AtomicInteger elementsCounter;

    private final GraphicsContext graphicsContext;

    private Canvas canvas;

    private final Sprite ant;

    private Movements lastMoviment;

    private Integer drives;

    private final List<Element> ants;

    private final List<Element> sugar;

    private Boolean avaibleToReproduce;

    private LocalTime lastReproduction;

    private Boolean isAlive;

    private final Integer incrementoPorIngestao = Parameters.getInstance().getIncrementoPorIngestao();
    private final Integer caloriasReproducaoFormigas = Parameters.getInstance().getCaloriasReproducaoFormigas();
    private final Integer caloriasRemovidasReproducao = Parameters.getInstance().getCaloriasRemovidasReproducaoFormigas();
    private final Long numeroCalorias = Long.valueOf(Parameters.getInstance().getNumeroCalorias());

    public Ant(final Long calorieCounter, final AtomicInteger elementsCounter, final GraphicsContext graphicsContext, final Canvas canvas, final List<Element> ants, final List<Element> sugar) {
        this.calorieCounter = calorieCounter;
        this.elementsCounter = elementsCounter;
        this.graphicsContext = graphicsContext;
        this.canvas = canvas;
        this.ants = ants;
        this.sugar = sugar;
        this.drives = 0;
        this.avaibleToReproduce = true;
        this.lastReproduction = LocalTime.MIN;
        this.isAlive = true;

        this.ant = generateElement(canvas, Ant.antImage);
        this.elementsCounter.incrementAndGet();
        this.startLifeCycle();
    }

    @Override
    protected void compute() {
        while (calorieCounter > 0) {
            try {
                lastMoviment = moves(ant, canvas, lastMoviment, drives++);
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Long incrementCalorieCounter() {
        calorieCounter += incrementoPorIngestao;

        if (calorieCounter >= caloriasReproducaoFormigas) {
            reproduce(null);
        }

        return calorieCounter;
    }

    @Override
    public Long decrementCalorieCounter() {
        final Long decremented = --calorieCounter;

        if (decremented == 0) {
            die();
        }

        return decremented;
    }

    @Override
    public void die() {
        calorieCounter = 0L;
        isAlive = false;
        this.elementsCounter.decrementAndGet();
        synchronized (ants) {
            this.ants.remove(this);
        }
    }

    @Override
    public void eat(Element element) {
        element.die();
        incrementCalorieCounter();
    }

    @Override
    public void eat(List<Element> elements) {
        elements.forEach(this::eat);
    }

    @Override
    public synchronized void reproduce(Element element) {
        final Ant ant = new Ant(numeroCalorias, elementsCounter, graphicsContext, canvas, ants, sugar);

        ants.add(ant);

        ant.fork().quietlyJoin();

        updateLastReproduction();

        this.calorieCounter -= caloriasRemovidasReproducao;
    }

    @Override
    public void render() {
        this.ant.render(this.graphicsContext);
    }

    @Override
    public boolean intersects(Sprite s) {
        return this.ant.intersects(s);
    }

    @Override
    public synchronized void collisionDetect() {
        eat(intersectsWithSugar());
    }

    private List<Element> intersectsWithSugar() {
        final Sprite copy = this.ant.copy();
        final Predicate<Element> intersectsWithSugar = sugar -> sugar.intersects(copy);

        return sugar.stream()
                .filter(Element::isAlive)
                .filter(intersectsWithSugar)
                .collect(Collectors.toList());
    }

    @Override
    public void avaibleToReproduce() {
        this.avaibleToReproduce = true;
    }

    @Override
    public Boolean isAlive() {
        return isAlive;
    }

    @Override
    public Sprite getSprite() {
        return this.ant;
    }

    @Override
    public void updateLastReproduction() {
        this.lastReproduction = LocalTime.now();
    }
}
