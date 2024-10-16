package mealplanner;

public abstract class Meal {
    protected String category;
    protected String name;
    protected String[] ingredients;



    public String getName() {
        return name;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public String getCategory() {
        return category;
    }
}
