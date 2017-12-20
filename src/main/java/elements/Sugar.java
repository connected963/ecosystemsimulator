package main.java.elements;

import main.java.model.Sprite;

import java.util.List;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;

public class Sugar extends RecursiveAction implements Element {

    public static final String sugarImage = "Assets/sugar_cube.png";

    private Long calorieCounter;

    private final AtomicInteger elementsCounter;

    private final Sprite sugar;

    public Sugar(final Long calorieCounter, final AtomicInteger elementsCounter) {
        this.calorieCounter = calorieCounter;
        this.elementsCounter = elementsCounter;

        this.sugar = new Sprite();
    }

    @Override
    protected void compute() {
    }

    @Override
    public Long incrementCalorieCounter() {
        return --calorieCounter;
    }

    @Override
    public Long decrementCalorieCounter() {
        return --calorieCounter;
    }

    @Override
    public void die() {
        calorieCounter = 0L;
    }

    @Override
    public void eat(Element element) {
        element.die();
        incrementCalorieCounter();
    }

    @Override
    public void collisionDetect() {

    }

    @Override
    public void eat(List<Element> elements) {

    }


    @Override
    public void reproduce() {

    }

    @Override
    public void render() {

    }

    @Override
    public boolean intersects(Sprite s) {
        return false;
    }
}
