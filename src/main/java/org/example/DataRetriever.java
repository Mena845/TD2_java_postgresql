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




//    findIngredients

    public List<Ingredient> findIngredients(int page, int size) {
        String sql = "SELECT * FROM ingredient LIMIT ? OFFSET ?";
        List<Ingredient> ingredients = new ArrayList<>();

        try (Connection conn = DbConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, size);
            ps.setInt(2, page * size);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ingredients.add(new Ingredient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        CategoryEnum.valueOf(rs.getString("category")),
                        null
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ingredients;
    }

//    create ingredient
public List<Ingredient> createIngredients(List<Ingredient> newIngredients) {
    String checkSql = "SELECT COUNT(*) FROM ingredient WHERE name = ?";
    String insertSql = """
                INSERT INTO ingredient(name, price, category, id_dish)
                VALUES (?, ?, ?, ?)
                """;

    try (Connection conn = DBConnection.getDBConnection()) {
        conn.setAutoCommit(false);

        try {
            for (Ingredient ing : newIngredients) {
                try (PreparedStatement check = conn.prepareStatement(checkSql)) {
                    check.setString(1, ing.getName());
                    ResultSet rs = check.executeQuery();
                    rs.next();

                    if (rs.getInt(1) > 0) {
                        throw new RuntimeException("Ingredient already exists: " + ing.getName());
                    }
                }

                try (PreparedStatement insert = conn.prepareStatement(insertSql)) {
                    insert.setString(1, ing.getName());
                    insert.setDouble(2, ing.getPrice());
                    insert.setString(3, ing.getCategory().name());
                    insert.setObject(4,
                            ing.getDish() != null ? ing.getDish().getId() : null,
                            Types.INTEGER
                    );
                    insert.executeUpdate();
                }
            }

            conn.commit();
            return newIngredients;

        } catch (RuntimeException e) {
            conn.rollback();
            throw e;
        }

    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}
}
