package main.java.elements;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import main.java.common.Parameters;
import main.java.communication.Movements;
import main.java.model.Sprite;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Tamandua implements Element, Callable<Void> {

    private static final String tamanduaImage = "Assets/tamandua.png";

    private Long calorieCounter;

    private final AtomicInteger elementsCounter;

    private final GraphicsContext graphicsContext;

    private Canvas canvas;

    private final Sprite tamandua;

    private Movements lastMoviment;

    private Integer drives;

    private final List<Element> tamanduas;

    private final List<Element> ants;

    private final ExecutorService executorService;

    private Boolean avaibleToReproduce;

    private LocalTime lastReproduction;

    private Boolean isAlive;

    private final Integer incrementoPorIngestao = Parameters.getInstance().getIncrementoPorIngestao();
    private final Integer intervaloReproducaoTamanduas = Parameters.getInstance().getIntervaloReproducaoTamandua();
    private final Long numeroCalorias = Long.valueOf(Parameters.getInstance().getNumeroCalorias());

    public Tamandua(final Long calorieCounter, final AtomicInteger elementsCounter, final GraphicsContext graphicsContext, final Canvas canvas, final List<Element> tamanduas, final List<Element> ants, final ExecutorService executorService) {
        this.calorieCounter = calorieCounter;
        this.elementsCounter = elementsCounter;
        this.graphicsContext = graphicsContext;
        this.canvas = canvas;
        this.tamanduas = tamanduas;
        this.ants = ants;
        this.drives = 0;
        this.executorService = executorService;
        this.avaibleToReproduce = true;
        this.lastReproduction = LocalTime.MIN;
        this.isAlive = true;

        this.tamandua = generateElement(canvas, Tamandua.tamanduaImage);
        this.elementsCounter.incrementAndGet();
        this.startLifeCycle();
    }

    @Override
    public Void call() {
        while (calorieCounter > 0) {
            try {
                collisionDetect();
                lastMoviment = moves(tamandua, canvas, lastMoviment, drives++);
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public Long incrementCalorieCounter() {
        return calorieCounter += incrementoPorIngestao;
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
        this.isAlive = false;
        this.elementsCounter.decrementAndGet();
        synchronized (tamanduas) {
            this.tamanduas.remove(this);
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
        final Tamandua tamandua = new Tamandua(numeroCalorias, elementsCounter, graphicsContext, canvas, tamanduas, ants, executorService);

        tamanduas.add(tamandua);

        executorService.submit(tamandua);

        notAvaibleToReproduce();

        element.updateLastReproduction();
        updateLastReproduction();
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
    public void collisionDetect() {
        eat(intersectsWithAnt());
        Optional<Tamandua> optSpouse = intersectsWithTamandua();
        if (optSpouse.isPresent() && isAvaibleToReproduce()) {
            final Tamandua spouse = optSpouse.get();
            spouse.notAvaibleToReproduce();
            reproduce(spouse);
        }
    }

    @Override
    public Boolean isAlive() {
        return this.isAlive;
    }

    private List<Element> intersectsWithAnt() {
        final Predicate<Element> intersectsWithTamandua = ant -> ant.intersects(tamandua);
        return ants.stream()
                .filter(Element::isAlive)
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

    private Boolean isAvaibleToReproduce() {
        return this.avaibleToReproduce && Duration.between(lastReproduction, LocalTime.now()).getSeconds() > intervaloReproducaoTamanduas && !ants.isEmpty();
    }

    private void notAvaibleToReproduce() {
        this.avaibleToReproduce = false;
    }

    @Override
    public void avaibleToReproduce() {
        this.avaibleToReproduce = true;
    }

    @Override
    public void updateLastReproduction() {
        this.lastReproduction = LocalTime.now();
    }

    @Override
    public Sprite getSprite() {
        return tamandua;
    }
}