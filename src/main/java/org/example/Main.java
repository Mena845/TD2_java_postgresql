package org.example;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        DataRetriever dr = new DataRetriever();

        try {
            Dish dish = dr.findDishById(2);
            System.out.println("Plat : " + dish.getName());
            System.out.println("Coût : " + dish.getDishCost());
            System.out.println("\n--- Ingredients ---");
            List<Ingredient> ingredients = dr.findDishIngredient(dish.getId());
            ingredients.forEach(i ->
                    System.out.println(i.getName()+" | "+i.getPrice()+" | "+i.getCategory())
            );
        } catch (RuntimeException e) {
            System.out.println("Erreur : " + e.getMessage());
        }


    }
}
