package mealplanner;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;


public class DayPlanner{
    private Scanner scanner = new Scanner(System.in);
    private final Planner planner;
    public static final List<String> DAYS_OF_WEEK = Arrays.asList(
            "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    );

    public DayPlanner(PlannerDao plannerDao, UserInputHandler inputHandler) throws SQLException {
        this.planner = new Planner(plannerDao, inputHandler);
    }

    public void savePlanToText() throws SQLException {
        if (planner.doesPlanExists()) {
            LinkedHashMap<String, Integer> ingredients = planner.SavePlanToText();
            System.out.println("Input a filename:");
            String filename = scanner.nextLine();
            try(FileWriter writer = new FileWriter(filename)) {
                for (Map.Entry<String, Integer> entry : ingredients.entrySet()) {
                    String ing = entry.getKey();
                    int count = entry.getValue();
                    if (count > 1) {
                        writer.write(ing + " x" + count + "\n");
                    } else {
                        writer.write(ing + "\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Saved!");

        } else {
            System.out.println("Unable to save. Plan your meals first.");
        }
    }

    public void planMealS() throws SQLException {
        for (String day : DAYS_OF_WEEK) {
            planMealsForDays(day);
        }
        printAll();
    }

    public void planMealsForDays(String day) throws SQLException {
        System.out.println(day);
        planner.planMeal(day, "breakfast");
        planner.planMeal(day, "lunch");
        planner.planMeal(day, "dinner");
        System.out.println("Yeah! We planned the meals for " + day + ".");

    }

    public void printAll() throws SQLException {
        for (String day : DAYS_OF_WEEK) {
            printPlan(day);
        }
    }

    public void printPlan(String day) throws SQLException {
        if (!planner.doesPlanExists()) {
            System.out.println("Database does not contain any meal plans");
        } else {
            LinkedHashMap<String, String> dayPlan = planner.printPlan(day);
            System.out.println(day);
            dayPlan.forEach((key, value) -> System.out.println(key + ": " + value));
            System.out.println();
        }
    }

}
