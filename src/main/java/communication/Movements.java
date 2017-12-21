package main.java.communication;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;


public enum Movements {

    UP('U'),
    DOWN('D'),
    LEFT('L'),
    RIGHT('R'),
    UP_LEFT('E'),
    UP_RIGHT('C'),
    DOWN_LEFT('V'),
    DOWN_RIGHT('O'),;

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
        }
        else if (this == LEFT) {
            return RIGHT;
        }
        else if (this == UP_LEFT) {
            return DOWN_RIGHT;
        }
        else if (this == UP_RIGHT) {
            return DOWN_LEFT;
        }
        else if (this == DOWN_RIGHT) {
            return UP_LEFT;
        } else {
            return UP_RIGHT;
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
