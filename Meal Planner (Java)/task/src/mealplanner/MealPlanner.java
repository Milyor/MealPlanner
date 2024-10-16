package mealplanner;

import java.sql.SQLException;
import java.util.*;
import java.util.Random;

public class MealPlanner {
    Scanner sc = new Scanner(System.in);
    private List<Meal> meals = new ArrayList<>();
    DbManagement db = new DbManagement();
    Random rand = new Random();
    int meal_id;
    UserInputHandler inputHandler = new UserInputHandler(new Scanner(System.in));
    PlannerDao plannerDao = new SqlPlannerDao(db);


    public void runService() throws SQLException {
        DayPlanner dayPlanner = new DayPlanner(plannerDao, inputHandler);
        db.createTables();
        while (true) {
            System.out.println("What would you like to do (add, show, plan, list plan, save, exit)?");
            switch (sc.nextLine()) {
                case "exit" -> {
                    System.out.println("Bye!");
                    return;
                }
                case "add" -> meals.add(addMeal());

                case "show" -> show();

                case "plan" -> {
                    db.createPlan();
                    dayPlanner.planMealS();
                }
                case "list plan" -> dayPlanner.printAll();

                case "save" -> dayPlanner.savePlanToText();
            }
        }
    }
    public void show() throws SQLException {
        System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");
        while (true) {
            String regex = "breakfast|lunch|dinner";
            String category = sc.nextLine().toLowerCase();
            if(category.matches(regex)) {
                db.display(category);
                return;
            }else {
                System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            }
        }
    }

    public Meal addMeal() throws SQLException {
        Scanner sc = new Scanner(System.in);
        MealFactory mealFactory = new MealFactory();
        String category;
        String name;
        String[] ingredients;
        String regexNum = "^[a-zA-Z][a-zA-Z\\s]*$";
        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        while (true) {
            String regex = "breakfast|lunch|dinner";
            category = sc.nextLine().toLowerCase();
            if (category.matches(regex)) {
                break;
            } else {
                System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            }
        }
        System.out.println("Input the meal's name:");
        while (true) {
            name = sc.nextLine();
            if (name.matches(regexNum)) {
                break;
            } else {
                System.out.println("Wrong format. Use letters only!");
            }
        }
        meal_id = rand.nextInt();
        db.insertIntoMeal(category, name, meal_id);
        System.out.println("Input the ingredients:");

        outerloop:
        while (true){
            String[] tempIng = sc.nextLine().split(",");
            for (String ing : tempIng) {
                if (!ing.strip().matches(regexNum)) {
                    System.out.println("Wrong format. Use letters only!");
                    continue outerloop;
                }
            }
            ingredients = tempIng;
            break;
        }
        System.out.println("The meal has been added!");
        for (String ing : ingredients) {
            db.insertIntoIngredients(ing.trim(), rand.nextInt(), meal_id);
        }
        return mealFactory.createMeal(category,name, ingredients);
    }

}
