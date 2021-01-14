package edu.ranken.prsmith;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class MortgageCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input Principal
        // prompt until valid number is entered
        double principal;
        for (;;) {
            try {
                System.out.print("Principal ($1K - $1M): ");
                String line = scanner.nextLine();
                principal = Double.parseDouble(line);
                if (principal < 1000 || principal > 1_000_000) {
                    throw new IllegalArgumentException();
                } else {
                    break;
                }
            } catch (NoSuchElementException ex) {
                return;
            } catch (Exception ex) {
                System.out.println("Enter a number between 1,000 and 1,000,000.");
            }
        }

        // Input Annual Interest Rate
        // prompt until valid number is entered
        double annualInterestRate;
        for (;;) {
            try {
                System.out.print("Annual Interest Rate: ");
                String line = scanner.nextLine();
                annualInterestRate = Double.parseDouble(line);
                if (annualInterestRate < 1 || annualInterestRate > 100) {
                    throw new IllegalArgumentException();
                } else {
                    break;
                }
            } catch (Exception ex) {
                System.out.println("Enter a number between 1 and 100.");
            }
        }

        // Input Period
        // prompt until valid number is entered
        double periodInYears;
        for (;;) {
            try {
                System.out.print("Period in Years: ");
                String line = scanner.nextLine();
                periodInYears = Double.parseDouble(line);
                if (periodInYears < 1 || periodInYears > 100) {
                    throw new IllegalArgumentException();
                } else {
                    break;
                }
            } catch (Exception ex) {
                System.out.println("Enter a number between 1 and 100.");
            }
        }

        // Calculate monthly mortgage payment using the formula
        // https://www.wikihow.com/Calculate-Mortgage-Payments#Calculating-Mortgage-Payments-with-an-Equation
        double monthlyInterestRate = annualInterestRate / 100 / 12;
        double periodInMonths = periodInYears * 12;
        double pow = Math.pow(1 + monthlyInterestRate, periodInMonths);
        double monthlyMortgagePayment = principal * (pow * monthlyInterestRate) / (pow - 1);

        // Print the monthly mortgage payment
        System.out.println("-----");
        System.out.println("Monthly Mortgage Payment: " + NumberFormat.getCurrencyInstance().format(monthlyMortgagePayment));
        System.out.println("-----");
    }
}
