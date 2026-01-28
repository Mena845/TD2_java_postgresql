package org.example;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        DataRetriever dr = new DataRetriever();

//test findDishId
        System.out.println("=== TEST findDishById ===");

        try {
            Dish dish = dr.findDishById(2); // ex: Poulet grillé
            System.out.println("Plat : " + dish.getName());
            System.out.println("Type : " + dish.getDishType());

            System.out.println("\n--- Ingredients ---");
            List<Ingredient> ingredients = dr.findDishIngredient(dish.getId());
            ingredients.forEach(i ->
                    System.out.println(
                            i.getName() + " | " +
                                    i.getPrice() + " | " +
                                    i.getCategory() + " | qty=" +
                                    i.getRequiredQuantity()
                    )
            );

            System.out.println("\nCoût total du plat : " + dish.getDishCost());

        } catch (RuntimeException e) {
            System.out.println("Erreur (coût) : " + e.getMessage());
        }

//test gross margin
        System.out.println("\n=== TEST getGrossMargin ===");

        try {
            Dish dish = dr.findDishById(2);

            // prix de vente simulé
            dish.setPrice(8000.0);

            System.out.println("Plat : " + dish.getName());
            System.out.println("Prix de vente : " + dish.getPrice());
            System.out.println("Coût : " + dish.getDishCost());
            System.out.println("Marge brute : " + dish.getGrossMargin());

        } catch (RuntimeException e) {
            System.out.println("Erreur (marge) : " + e.getMessage());
        }

//test find  ingredient pagination
        System.out.println("\n=== TEST findIngredients (page=0, size=5) ===");

        List<Ingredient> allIngredients = dr.findIngredients(0, 5);
        allIngredients.forEach(i ->
                System.out.println(i.getName() + " | " + i.getPrice() + " | " + i.getCategory())
        );

//        test de la creation d'ingredient
        System.out.println("\n=== TEST createIngredients ===");

        try {
            Dish dishRef = new Dish(1, "Ref Dish", DishTypeEnum.MAIN);

            List<Ingredient> newIngredients = List.of(
                    new Ingredient("Sel fin", 200.0, CategoryEnum.OTHER, dishRef),
                    new Ingredient("Poivre vert", 150.0, CategoryEnum.OTHER, dishRef)
            );

            dr.createIngredients(newIngredients);
            System.out.println("Insertion réussie des ingrédients");

        } catch (RuntimeException e) {
            System.out.println("Échec insertion ingrédients : " + e.getMessage());
        }
    }
}
