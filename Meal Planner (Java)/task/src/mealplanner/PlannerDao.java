package mealplanner;

import java.sql.SQLException;
import java.util.LinkedHashMap;

public interface PlannerDao {

    boolean doesPlanTableExists() throws SQLException;
    LinkedHashMap<String, Integer> getMealsByCategory(String category) throws SQLException;
    void addMealToPlan(String day, String category, int mealId) throws SQLException;
    LinkedHashMap<String, String> getMealPlan(String day) throws SQLException;
    LinkedHashMap<String, Integer> savePlan() throws SQLException;
}
