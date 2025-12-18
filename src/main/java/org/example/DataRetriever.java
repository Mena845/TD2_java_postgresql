package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    public Dish findDishById(int id) {
        String dishquery = "SELECT * FROM dishes WHERE id = ?";
        String ingredientquery = "SELECT * FROM ingredients WHERE id = ?";
        try(Connection conn = DBConnection.getConnection()){
            Dish dish;
            try (PreparedStatement ps = conn.prepareStatement(dishquery)){
                ps.setInt(1,id);
                ResultSet rs = ps.executeQuery();
                if(!rs.next()) return null;
                dish = new Dish( rs.getInt("ID"),
                rs.getString("name"),DishTypeEnum.valueOf(rs.getString("dish_type"))
                );
            }

            List<Ingredient> ingredients= new ArrayList<>();
            try(PreparedStatement ps = conn.prepareStatement(ingredientquery)){
                ps.setInt(1,id);
                ResultSet rs =ps.executeQuery();
                while (rs.next()){
                    ingredients.add(new Ingredient(rs.getInt("id") ,
                            rs.getString("name") ,
                            rs.getDouble("price") ,
                            CategoryEnum.valueOf(rs.getString("category")),
                                    dish )
                    );
                }
            }

            dish.setIngredients(ingredients);
            return dish;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
