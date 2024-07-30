package com.cipherbytetechnologygame;

import java.util.Random;
import java.util.Scanner;

public class GuessTheNumberFunGame {
    
    public static void main(String[] args) {
        // Scanner is used to take input from the user
        try (Scanner sc = new Scanner(System.in)) {
            
            // It is used to take a random number
            Random rnd = new Random();
            
            int maxRound = 4;
            int maxAttempts = 5;
            int totalScore = 0; // Variable to keep track of the total score
            
            System.out.println("--- Welcome to 'GUESS THE NUMBER' game ---\n");
            System.out.println("Rules: ");
            System.out.println("You have " + maxRound + " rounds to play & " + maxAttempts + " attempts per round.");
            
            // This line starts a loop that runs for each round of the game
            for (int round = 1; round <= maxRound; round++) {
                int score = 0;
                int guessNumber = rnd.nextInt(100) + 1; // Generates a random integer between 1 and 100
                int attempts = 1;
                boolean correctlyGuessed = false;
                
                System.out.println("\nRound: " + round + " : ");
                System.out.println("I have picked a number between 1 and 100. Try to guess the number!\n");
                
                // The while loop is used for attempts
                while (attempts <= maxAttempts && !correctlyGuessed) {
                    System.out.println("\nEnter your guess number: ");
                    int userGuess = sc.nextInt();
                    
                    if (guessNumber == userGuess) {
                        System.out.println("Congratulations! You correctly guessed the number in " + attempts + " attempts.");
                        score += calculateScore(attempts);
                        correctlyGuessed = true;
                    } else if (userGuess < guessNumber) {
                        System.out.println("It is lower than the number!");
                    } else {
                        System.out.println("It is higher than the number!");
                    }
                    attempts++;
                }
                if (!correctlyGuessed) {
                    System.out.println("\nSorry! You have used all attempts. The number was: " + guessNumber);
                }
                
                System.out.println("Your score in round " + round + ": " + score + "\n");
                totalScore += score; // Update the total score
            }
            System.out.println("Total Score after " + maxRound + " rounds: " + totalScore+" / 400");
            System.out.println("\nGame Over!");
        }
    }
    
    public static int calculateScore(int att) {
        switch (att) {
            case 1:
                return 100;
            case 2:
                return 80;
            case 3:
                return 60;
            case 4:
                return 40;
            case 5:
                return 20;
            default:
                return 0;
        }
    }
}
