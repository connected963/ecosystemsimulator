package main.java.elements;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import main.java.communication.Movements;
import main.java.model.Sprite;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Tamandua extends RecursiveAction implements Element {

    public static final String tamanduaImage = "Assets/tamandua.png";

    private Long calorieCounter;

    private final AtomicInteger elementsCounter;

    private final GraphicsContext graphicsContext;

    private Canvas canvas;

    private final Sprite tamandua;

    private Movements lastMoviment;

    private Integer drives;

    private final List<Element> tamanduas;

    private final List<Element> ants;

    private Boolean avaibleToReproduce;

    public Tamandua(final Long calorieCounter, final AtomicInteger elementsCounter, final GraphicsContext graphicsContext, final Canvas canvas, final List<Element> tamanduas, final List<Element> ants) {
        this.calorieCounter = calorieCounter;
        this.elementsCounter = elementsCounter;
        this.graphicsContext = graphicsContext;
        this.canvas = canvas;
        this.tamanduas = tamanduas;
        this.ants = ants;
        this.drives = 0;
        this.avaibleToReproduce = true;

        this.tamandua = generateElement(canvas, Tamandua.tamanduaImage);

        this.startLifeCycle();
    }

    @Override
    protected void compute() {
        while (calorieCounter > 0) {
            try {
                lastMoviment = moves(tamandua, canvas, lastMoviment, drives++);
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Long incrementCalorieCounter() {
        return ++calorieCounter;
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
    public synchronized void die() {
        calorieCounter = 0L;
        this.tamanduas.remove(this);
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
    public synchronized void reproduce() {
        final Tamandua tamandua = new Tamandua(15L, elementsCounter, graphicsContext, canvas, tamanduas, ants);

        tamanduas.add(tamandua);

        tamandua.fork();

        notAvaibleToReproduce();
    }

    @Override
    public void render() {
        this.tamandua.render(this.graphicsContext);
    }

    @Override
    public boolean intersects(Sprite s) {
        return this.tamandua.intersects(s);
    }

    @Override
    public synchronized void collisionDetect() {
        eat(intersectsWithAnt());
        Optional<Tamandua> spouse = intersectsWithTamandua();
        if (spouse.isPresent() && avaibleToReproduce) {
            spouse.get().notAvaibleToReproduce();
            reproduce();
        }
    }

    private List<Element> intersectsWithAnt() {
        final Predicate<Element> intersectsWithTamandua = ant -> ant.intersects(tamandua);

        return ants.stream()
                .filter(intersectsWithTamandua)
                .collect(Collectors.toList());
    }

    private Optional<Tamandua> intersectsWithTamandua() {
        final Predicate<Tamandua> intersectsWithTamandua = t -> t.intersects(tamandua) && t.isAvaibleToReproduce();
        final Predicate<Tamandua> itsTheSame = this::equals;
        return tamanduas.stream()
                .map(element -> (Tamandua) element)
                .filter(itsTheSame.negate())
                .filter(intersectsWithTamandua)
                .findAny();
    }

    public Boolean isAvaibleToReproduce() {
        return this.avaibleToReproduce;
    }

    public void notAvaibleToReproduce() {
        this.avaibleToReproduce = false;
    }
}
