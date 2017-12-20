package main.java.mathutils;

/**
 * Created by connected on 7/11/17.
 */
public class PrimeNumber {

    private PrimeNumber() {
        super();
    }

    public static boolean isPrime(final Integer num) {
        int i = num > 0 ? num / 2 : 0;

        while (i != 0 && num % i != 0) {
            i--;
        }

        return i != 1;
    }
}
