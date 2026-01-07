package org.example;


public class Ingredient {

    private Integer id;
    private String name;
    private double price;
    private CategoryEnum category;
    private Double requiredQuantity;
    private Dish dish;


    public Ingredient(Integer id, String name, Double price, CategoryEnum category, Double requiredQuantity, Dish dish) {
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


    public Ingredient(Integer id, String name, double price, CategoryEnum category, Object requiredQuantity) {
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public Double getRequiredQuantity() {
        return requiredQuantity;
    }

    public void setRequiredQuantity(Double requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}


