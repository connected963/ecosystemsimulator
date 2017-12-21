package main.java.elements;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import main.java.model.Sprite;

import java.util.List;

public class Sugar implements Element {

    private static final String sugarImage = "Assets/sugar_cube.png";

    private final GraphicsContext graphicsContext;

    private final List<Element> sugars;

    private final Sprite sugar;

    private Boolean isAlive;

    public Sugar(final GraphicsContext graphicsContext, final Canvas canvas, final List<Element> sugars) {
        this.graphicsContext = graphicsContext;
        this.sugars = sugars;
        this.isAlive = true;

        this.sugar = generateElement(canvas, Sugar.sugarImage);
    }

    @Override
    public Long incrementCalorieCounter() {
        return null;
    }

    @Override
    public Long decrementCalorieCounter() {
        return null;
    }

    @Override
    public void die() {
        isAlive = false;
        synchronized (sugars) {
            this.sugars.remove(this);
        }
    }

    @Override
    public void render() {
        this.sugar.render(this.graphicsContext);
    }

    @Override
    public Boolean isAlive() {
        return isAlive;
    }

    @Override
    public Sprite getSprite() {
        return this.sugar;
    }

    @Override
    public void eat(Element element) {
    }

    @Override
    public void eat(List<Element> elements) {
    }

    @Override
    public synchronized void reproduce(Element element) {
    }

    @Override
    public boolean intersects(Sprite s) {
        return this.sugar.intersects(s);
    }

    @Override
    public synchronized void collisionDetect() {
    }

    @Override
    public void avaibleToReproduce() {
    }

    @Override
    public void updateLastReproduction() {
    }
}
