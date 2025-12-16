package org.example;
import org.example.DbConnection;
import org.example.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    public Dish findDishById(Integer id) {
        String dishSql = "SELECT * FROM dish WHERE id = ?";
        String ingredientSql = "SELECT * FROM ingredient WHERE id_dish = ?";

        try (Connection conn = DbConnection.getDBConnection()) {

            Dish dish;
            try (PreparedStatement ps = conn.prepareStatement(dishSql)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();

                if (!rs.next()) return null;

                dish = new Dish(
                        rs.getInt("id"),
                        rs.getString("name"),
                        DishTypeEnum.valueOf(rs.getString("dish_type"))
                );
            }

            List<Ingredient> ingredients = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement(ingredientSql)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    ingredients.add(new Ingredient(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            CategoryEnum.valueOf(rs.getString("category")),
                            dish
                    ));
                }
            }

            dish.setIngredients(ingredients);
            return dish;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

