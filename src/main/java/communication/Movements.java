package main.java.communication;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;


public enum Movements {

    UP('U'),
    DOWN('D'),
    LEFT('L'),
    RIGHT('R');

    private Character value;

    Movements(Character value) {
        this.value = value;
    }

    public Character getValue() {
        return value;
    }

    public Movements invert() {
        if (this == UP) {
            return DOWN;
        }
        else if (this == DOWN) {
            return UP;
        }
        else if (this == RIGHT) {
            return LEFT;
        } else {
            return RIGHT;
        }
    }

    public static Optional<Movements> valueOf(Character mov) {
        return Arrays.stream(Movements.values())
                .filter(m -> m.value.equals(mov))
                .findFirst();
    }

    public static Movements getRandom(Movements lastMoviments) {
        final Random random = new Random();
        final Movements[] movements = Movements.values();
        Movements movement;

        do {
            movement = movements[random.nextInt(movements.length)];
        } while (lastMoviments != null && lastMoviments == movement.invert());

        return movement;
    }
}
