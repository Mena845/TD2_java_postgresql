package org.example;

import java.sql.*;
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


    public List<Ingredient> findIngredients (int page , int size){
        String ingredientquery = "SELECT * FROM ingredient limit ? offset ?";
        List<Ingredient> ingredients = new ArrayList<>();

        try(Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(ingredientquery)){
            ps.setInt(1,size);
            ps.setInt(2,page*size);

            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                ingredients.add(new Ingredient(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        CategoryEnum.valueOf(rs.getString("category")),
                        null
                ));
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return ingredients;
    }


    public List<Ingredient> createIngredients (List<Ingredient> newIngredients){
        String checkingquery = "SELECT * FROM ingredients WHERE name = ?";
        String insertionquery = "INSERT INTO ingredients (name, price , category , id_dish) VALUES (?, ? ,? ,?)";

        try (Connection conn = DBConnection.getConnection()){
            conn.setAutoCommit(false);
            try{
                for(Ingredient ing : newIngredients){
                    try(PreparedStatement check = conn.prepareStatement(checkingquery)){
                        check.setString(1,ing.getName());
                        ResultSet rs = check.executeQuery();
                        rs.next();
                        if(rs.getInt(1)>0){
                            throw new RuntimeException("Ingredient already exists"+ing.getName());
                        }
                    }
                    try (PreparedStatement insert = conn.prepareStatement(insertionquery)){
                        insert.setString(1,ing.getName());
                        insert.setDouble(2,ing.getPrice());
                        insert.setString(3,ing.getCategory().name());
                        insert.setObject(4, ing.getDish() !=null ? ing.getDish().getId() : null , Types.INTEGER);
                                insert.executeUpdate();
                    }
                }
                conn.commit();
                return newIngredients;
            }catch (RuntimeException e){
                conn.rollback();
                throw e;
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }
}
