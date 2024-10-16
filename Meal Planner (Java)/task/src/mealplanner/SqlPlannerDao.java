package mealplanner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SqlPlannerDao implements PlannerDao {

    private final DbManagement dbManagement;
    private static final String SELECT_ALL = "SELECT meal_id, meal FROM meals WHERE category = ? ORDER BY meal;";
    private static final String INSERT_DATA = "INSERT INTO plan(meals_option, meal_category, meal_id)" +
            "VALUES (?, ?, ?);";
    private static final String SELECT_PLAN = "SELECT p.meal_category, m.meal " +
            "FROM plan p " +
            "JOIN meals m ON p.meal_id = m.meal_id " +
            "WHERE p.meals_option = ?";
    private static final String CHECK_TABLE_EXISTS =
            "SELECT EXISTS (" +
            "SELECT FROM information_schema.tables " +
            "WHERE table_schema = 'public' " +
            "AND table_name = 'plan'" +
            ");";
    private static final String SELECT_PLAN_INGREDIENTS = "SELECT ingredient, COUNT(ingredient) AS count " +
            "FROM ingredients ing " +
            "JOIN plan p ON ing.meal_id = p.meal_id " +
            "GROUP BY ingredient";

    public SqlPlannerDao(DbManagement dbManagement) {
        this.dbManagement = dbManagement;
    }

    @Override
    public LinkedHashMap<String, Integer> savePlan() throws SQLException {
        LinkedHashMap<String, Integer> ingredients = new LinkedHashMap<>();
        try (Connection conn = dbManagement.connect();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_PLAN_INGREDIENTS)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int count = rs.getInt("count");
                    String meal = rs.getString("ingredient");
                    ingredients.put(meal, count);
                }
            }
        }
        return ingredients;
    }

    @Override
    public boolean doesPlanTableExists() throws SQLException {
        try (Connection conn = dbManagement.connect();
        PreparedStatement pstmt = conn.prepareStatement(CHECK_TABLE_EXISTS);
        ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getBoolean(1);
            }
        }
        return false;
    }

    @Override
    public LinkedHashMap<String, Integer> getMealsByCategory(String category) throws SQLException {
        LinkedHashMap<String, Integer> meals = new LinkedHashMap<>();
        try(Connection conn = dbManagement.connect();
            PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL)) {
            pstmt.setString(1, category);
            try(ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int mealId = rs.getInt("meal_id");
                    String meal = rs.getString("meal");
                    meals.put(meal, mealId);
                }
            }
        }
        return meals;
    }

    @Override
    public LinkedHashMap<String, String> getMealPlan(String day) throws SQLException {
        LinkedHashMap<String, String> meals = new LinkedHashMap<>();
        try(Connection conn = dbManagement.connect();
        PreparedStatement pstmt = conn.prepareStatement(SELECT_PLAN)) {
            pstmt.setString(1, day);
            try(ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String meal = rs.getString("meal");
                    String category = rs.getString("meal_category");
                    meals.put(category, meal);
                }
            }
        }
        return meals;
    }



    @Override
    public void addMealToPlan(String day, String category, int mealId) throws SQLException {
        try(Connection conn = dbManagement.connect();
        PreparedStatement pstmt = conn.prepareStatement(INSERT_DATA)) {
            pstmt.setString(1, day);
            pstmt.setString(2,category);
            pstmt.setInt(3,mealId);
            pstmt.executeUpdate();
        }
    }
}
