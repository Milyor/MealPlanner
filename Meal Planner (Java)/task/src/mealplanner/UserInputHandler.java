package mealplanner;

import java.util.LinkedHashMap;
import java.util.Scanner;

public class UserInputHandler {
    private final Scanner scanner;
    public UserInputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public String getMealChoice(LinkedHashMap<String, Integer> meals, String day, String category) {
        // Print all the meals
        meals.keySet().forEach(System.out::println);
        System.out.println("Choose the "+ category + " for " + day + " from the list above:");
        // Get meal from user
        String input = scanner.nextLine();
        while (!meals.containsKey(input)) {
            System.out.println("This meal doesn’t exist. Choose a meal from the list above.");
            input = scanner.nextLine();
        }
        return input;
    }
}
