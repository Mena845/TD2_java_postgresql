package org.example;

public class Ingredient {

    private Integer id;
    private String name;
    private double price;
    private CategoryEnum category;
    private Double requiredQuantity;
    private Dish dish;

    public Ingredient(Integer id, String name, double price,
                      CategoryEnum category, Double requiredQuantity, Dish dish) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.requiredQuantity = requiredQuantity;
        this.dish = dish;
    }

    public Ingredient(String name, double price, CategoryEnum category, Dish dish) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.dish = dish;
        this.requiredQuantity = null;
    }

    public <T> Ingredient(int id, String name, double price, CategoryEnum category, T requiredQuantity) {
    }

    public Integer getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public CategoryEnum getCategory() { return category; }
    public Double getRequiredQuantity() { return requiredQuantity; }

    public Dish getDish() { return dish; }
}
