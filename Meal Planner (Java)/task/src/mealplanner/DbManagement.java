package mealplanner;

import java.sql.*;

public class DbManagement {

    public Connection connect() throws SQLException {

        String DB_URL = "jdbc:postgresql://.../meals_db";
        String USER = "...";
        String PASS = "...";

        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        conn.setAutoCommit(true);

        return conn;

    }

    public boolean checkIsEmpty() throws SQLException {
        String sql = "SELECT COUNT(*) FROM meals;";
        try(Connection conn = connect();
        Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            return rs.next();
        }
    }

    public void display(String category) throws SQLException {
        if (!checkIsEmpty()) {
            System.out.println("No meals found.");
            return;
        }

        String sql = "SELECT * FROM meals WHERE category = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.isBeforeFirst()) {
                System.out.println("No meals found.");
            } else {
                System.out.println("Category: " + category);
                System.out.println();
                while (rs.next()) {
                    System.out.println("Name: " + rs.getString("meal"));
                    int meal_id = rs.getInt("meal_id");
                    System.out.println("Ingredients: ");
                    String ingredientSql = "SELECT ingredient FROM ingredients WHERE meal_id = ?";
                    try (PreparedStatement ingredientPstmt = conn.prepareStatement(ingredientSql)) {
                        ingredientPstmt.setInt(1, meal_id);
                        ResultSet rs2 = ingredientPstmt.executeQuery();
                        while (rs2.next()) {
                            System.out.println(rs2.getString("ingredient"));
                        }
                    }
                    System.out.println();
                }
            }
        }

    }

    public void insertIntoMeal(String category, String meal, int meal_id) throws SQLException {
        String sql = "INSERT INTO meals(category, meal, meal_id) VALUES(?, ?, ?)";
        try(Connection conn = connect();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category);
            stmt.setString(2, meal);
            stmt.setInt(3, meal_id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating meal failed, no rows affected.");
            }
        }
    }

    public void insertIntoIngredients(String ingredient, int ingredient_id, int meal_id) throws SQLException {
        String sql = "INSERT INTO ingredients(ingredient, ingredient_id, meal_id) VALUES(?, ?, ?)";
        try(Connection conn = connect();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ingredient);
            stmt.setInt(2,ingredient_id);
            stmt.setInt(3, meal_id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating meal failed, no rows affected.");
            }
        }
    }

    public void createPlan() throws SQLException {
        try(Connection conn = connect();
            Statement statement = conn.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS plan");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS plan(" +
                    "meals_option VARCHAR(104) NOT NULL," +
                    "meal_category VARCHAR(104) NOT NULL," +
                    "meal_id INTEGER REFERENCES meals(meal_id)" +
                    ");");
        }
    }

    public void createTables() throws SQLException {

        try(Connection conn = connect();
            Statement statement = conn.createStatement()) {

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS meals(" +
                    "category VARCHAR(1024) NOT NULL," +
                    "meal VARCHAR(1024) NOT NULL," +
                    "meal_id INTEGER PRIMARY KEY " +
                    ");");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ingredients(" +
                    "ingredient VARCHAR(1024)," +
                    "ingredient_id INTEGER PRIMARY KEY," +
                    "meal_id INTEGER" +
                    ");");
        }
    }

}
