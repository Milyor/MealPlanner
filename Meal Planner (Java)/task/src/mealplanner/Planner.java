package mealplanner;

import java.sql.*;
import java.util.LinkedHashMap;


public class Planner{

    private final PlannerDao plannerDao;
    private final UserInputHandler inputHandler;


    public Planner(PlannerDao plannerDao, UserInputHandler inputHandler){
        this.plannerDao = plannerDao;
        this.inputHandler = inputHandler;
    }

    public void planMeal(String day, String category) throws SQLException {
        LinkedHashMap<String, Integer> meals = plannerDao.getMealsByCategory(category);
        String chosenMeal = inputHandler.getMealChoice(meals, day, category);
        int mealId = meals.get(chosenMeal);
        plannerDao.addMealToPlan(day, category, mealId);
    }

    public LinkedHashMap<String, String> printPlan(String day) throws SQLException {
        return plannerDao.getMealPlan(day);
    }

    public boolean doesPlanExists() throws SQLException {
         return plannerDao.doesPlanTableExists();
    }

    public LinkedHashMap<String, Integer> SavePlanToText() throws SQLException {
        return plannerDao.savePlan();
    }

}
