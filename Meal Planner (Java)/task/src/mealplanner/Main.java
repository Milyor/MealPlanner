package mealplanner;

import java.sql.*;

public class Main {
  public static void main(String[] args) throws SQLException {
    MealPlanner planner = new MealPlanner();
    planner.runService();
  }
}

