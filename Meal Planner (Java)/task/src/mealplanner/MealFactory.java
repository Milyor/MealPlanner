package mealplanner;

public class MealFactory {
    public Meal createMeal(String mealType, String name, String[] ingredients) {
        if (mealType == null || mealType.isEmpty()) {
            return null;
        }
        return switch (mealType.toLowerCase()) {
            case "breakfast" -> new Breakfast(mealType, name, ingredients);
            case "lunch" -> new Lunch(mealType, name, ingredients);
            case "dinner" -> new Dinner(mealType, name, ingredients);
            default -> throw new IllegalArgumentException("Unknown meal type" + mealType);
        };
    }
}
