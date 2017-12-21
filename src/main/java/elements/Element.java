package main.java.elements;

import javafx.scene.canvas.Canvas;
import main.java.common.Parameters;
import main.java.communication.Movements;
import main.java.model.Sprite;

import java.util.List;
import java.util.Random;

public interface Element {

    Long incrementCalorieCounter();

    Long decrementCalorieCounter();

    void die();

    void eat(Element element);

    void eat(List<Element> elements);

    void reproduce(Element element);

    void render();

    boolean intersects(Sprite s);

    void collisionDetect();

    void updateLastReproduction();

    default Movements moves(final Sprite sprite, final Canvas canvas, final Movements last, Integer drives) {
        final Movements mov = drives % Parameters.getInstance().getMovimentosPorDirecao() == 0 ? Movements.getRandom(last) : last;
        clearVelocity(sprite);

        if (mov == Movements.UP) {
            if (sprite.getBoundary().getMinY() - 5 > 0) {
                sprite.addVelocity(0, -15);
            } else {
                clearVelocity(sprite);
            }
        }

        if (mov == Movements.DOWN) {
            if (sprite.getBoundary().getMaxY() + 5 < canvas.getHeight()) {
                sprite.addVelocity(0, 15);
            } else {
                clearVelocity(sprite);
            }
        }

        if (mov == Movements.LEFT) {
            if (sprite.getBoundary().getMinX() - 5 > 0) {
                sprite.addVelocity(-15, 0);
            } else {
                clearVelocity(sprite);
            }
        }

        if (mov == Movements.RIGHT) {
            if (sprite.getBoundary().getMaxX() + 5 < canvas.getWidth()) {
                sprite.addVelocity(15, 0);
            } else {
                clearVelocity(sprite);
            }
        }

        if (mov == Movements.UP_RIGHT) {
            if (sprite.getBoundary().getMinY() - 5 > 0 && sprite.getBoundary().getMaxX() + 5 < canvas.getWidth()) {
                sprite.addVelocity(15, -15);
            } else {
                clearVelocity(sprite);
            }
        }

        if (mov == Movements.UP_LEFT) {
            if (sprite.getBoundary().getMinY() - 5 > 0 && sprite.getBoundary().getMinX() - 5 > 0) {
                sprite.addVelocity(-15, -15);
            } else {
                clearVelocity(sprite);
            }
        }

        if (mov == Movements.DOWN_RIGHT) {
            if (sprite.getBoundary().getMaxY() + 5 < canvas.getHeight() && sprite.getBoundary().getMaxX() + 5 < canvas.getWidth()) {
                sprite.addVelocity(15, 15);
            } else {
                clearVelocity(sprite);
            }
        }

        if (mov == Movements.DOWN_LEFT) {
            if (sprite.getBoundary().getMaxY() + 5 < canvas.getHeight() && sprite.getBoundary().getMinX() - 5 > 0) {
                sprite.addVelocity(-15, 15);
            } else {
                clearVelocity(sprite);
            }
        }

        sprite.update(0.3);

        return mov;
    }

    private void clearVelocity(final Sprite sprite) {
        sprite.setVelocity(0, 0);
    }

    default Sprite generateElement(final Canvas canvas, final String image) {
        final Random random = new Random();

        final int x = random.nextInt((int)canvas.getWidth() - 100);
        final int y = random.nextInt((int)canvas.getHeight() - 100);

        return createNewSprite(x, y, image);

    }

    private Sprite createNewSprite(int x, int y, final String image) {
        final Sprite sprite = new Sprite();

        sprite.setImage(image);
        sprite.setPosition(x, y);

        return sprite;
    }

    default void startLifeCycle() {
        new Thread(() -> {
            while (isAlive() && decrementCalorieCounter() > 0) {
                try {
                    collisionDetect();
                    Thread.sleep(Parameters.getInstance().getTempoDecrementoCalorias());
                    avaibleToReproduce();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void avaibleToReproduce();

    Boolean isAlive();

    Sprite getSprite();
}
