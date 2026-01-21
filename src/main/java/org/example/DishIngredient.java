package org.example;

public class DishIngredient {

    private Integer dishId;
    private Integer ingredientId;

    private Dish dish;
    private Ingredient ingredient;

    public DishIngredient(Integer dishId, Integer ingredientId) {
        this.dishId = dishId;
        this.ingredientId = ingredientId;
    }

    public DishIngredient(Dish dish, Ingredient ingredient) {
        this.dish = dish;
        this.ingredient = ingredient;
        this.dishId = dish != null ? dish.getId() : null;
        this.ingredientId = ingredient != null ? ingredient.getId() : null;
    }

    public Integer getDishId() {
        return dishId;
    }

    public void setDishId(Integer dishId) {
        this.dishId = dishId;
    }

    public Integer getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Integer ingredientId) {
        this.ingredientId = ingredientId;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
        this.dishId = dish != null ? dish.getId() : null;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        this.ingredientId = ingredient != null ? ingredient.getId() : null;
    }
}

