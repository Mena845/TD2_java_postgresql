package org.example;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        DataRetriever dr = new DataRetriever();

        try {
            Dish dish = dr.findDishById(1);
            System.out.println("Plat : " + dish.getName());
            System.out.println("Coût : " + dish.getDishCost());
        } catch (RuntimeException e) {
            System.out.println("Erreur : " + e.getMessage());
        }

        System.out.println("\n--- Ingredients ---");
        List<Ingredient> ingredients = dr.findIngredients(0,5);
        ingredients.forEach(i ->
                System.out.println(i.getName()+" | "+i.getPrice()+" | "+i.getCategory())
        );
    }
}
