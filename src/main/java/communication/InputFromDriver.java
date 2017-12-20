package main.java.communication;

import java.util.Optional;

/**
 * Created by connected on 7/12/17.
 */
public class InputFromDriver {

    static {
        System.loadLibrary(InputFromDriver.libraryName);
    }

    public static final String libraryName = "readRobot";

    public static native char read();

    public static Optional<Movements> getMovement() {

        try {
            return Movements.valueOf(InputFromDriver.read());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

}
