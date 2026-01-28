package org.example;

import java.util.List;

public class Dish {

    private Integer id;
    private String name;
    private DishTypeEnum dishType;
    private List<Ingredient> ingredients;
    private Double price;

    public Dish(Integer id, String name, DishTypeEnum dishType) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
    }

    public Double getDishCost() {
        if (ingredients == null || ingredients.isEmpty()) {
            return 0.0;
        }

        double total = 0.0;

        for (Ingredient ingredient : ingredients) {
            if (ingredient.getRequiredQuantity() == null) {
                throw new RuntimeException(
                        "Quantité requise inconnue pour l'ingrédient : " + ingredient.getName()
                );
            }
            total += ingredient.getPrice() * ingredient.getRequiredQuantity();
        }

        return total;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public DishTypeEnum getDishType() { return dishType; }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    public Double getGrossMargin() {
        if (price == null) {
            throw new RuntimeException("Prix de vente du plat inconnu");
        }

        Double cost = getDishCost();
        return price - cost;
    }


}
