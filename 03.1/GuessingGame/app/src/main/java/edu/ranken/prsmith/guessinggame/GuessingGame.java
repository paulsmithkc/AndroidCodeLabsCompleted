package edu.ranken.prsmith.guessinggame;

import java.util.Random;

public class GuessingGame {
    private Random rand;
    private int prevGuess = 0;
    private int guessCount = 0;
    private int secretNumber = 0;

    public GuessingGame() {
        rand = new Random();
        reset();
    }

    public GuessResult guess(int guess) {
        if (guess < 1 || guess > 10) {
            throw new IllegalArgumentException("guess out of range");
        }

        if (guess != prevGuess) {
            ++guessCount;
            prevGuess = guess;
        }

        if (guess == secretNumber) {
            return GuessResult.WIN;
        } else if (guess > secretNumber) {
            return GuessResult.HIGH;
        } else {
            return GuessResult.LOW;
        }
    }

    public void reset() {
        prevGuess = 0;
        guessCount = 0;
        secretNumber = 1 + rand.nextInt(10);
    }

    public int getPrevGuess() {
        return prevGuess;
    }

    public int getGuessCount() {
        return guessCount;
    }

    public int getSecretNumber() {
        return secretNumber;
    }
}
