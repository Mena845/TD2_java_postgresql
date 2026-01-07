package org.example;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("Connection Successful!");
        } catch (SQLException e) {
            System.out.println("Connection Failed");
        }

        DataRetriever dataRetriever = new DataRetriever();

        System.out.println("Test findDishById()");
        Dish dish = dataRetriever.findDishById(1);
        if (dish == null) {
            System.out.println("Dish pas trouve");
        }else {
            System.out.println("Plat : "+dish.getName());
            System.out.println("Type : "+dish.getDishType());

            dish.getIngredients().forEach(i -> System.out.println("- "+i.getName() +" | "+i.getPrice() + " Ar | " +i.getCategory()));
            System.out.println("Prix total : "+dish.getDishCost() +" Ar");
        }

        System.out.println("Test findIngredients (page=0 , size=5)");
        List<Ingredient> ingredients = dataRetriever.findIngredients(0, 5);

        ingredients.forEach(i ->
                System.out.println(i.getName() +" | "+i.getPrice() + " Ar | "+i.getCategory()));


        System.out.println("Test createIngredients");
        try{
            Dish dishRef = new Dish(1 , "Test Dish" , DishTypeEnum.MAIN);
            List<Ingredient> newIngredients = List.of(new
                    Ingredient("Sel fin",200.0 , CategoryEnum.OTHER,dishRef),
                    new    Ingredient("Poivre vert",150.0 , CategoryEnum.OTHER,dishRef));
            dataRetriever.createIngredients(newIngredients);
            System.out.println("Insertion reussi");
        }catch (RuntimeException e){
            System.out.println("Echec de l'insertion : "+e.getMessage());
        }
    }



}
