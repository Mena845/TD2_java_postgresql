package org.example;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    /* ================= FIND DISH BY ID ================= */
    public Dish findDishById(Integer id) {
        String sql = """
            SELECT d.id d_id, d.name d_name, d.dish_type,
                   i.id i_id, i.name i_name, i.price, i.category, i.required_quantity
            FROM dish d
            LEFT JOIN dish_ingredient di ON d.id = di.dish_id
            LEFT JOIN ingredient i ON i.id = di.ingredient_id
            WHERE d.id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            Dish dish = null;
            List<Ingredient> ingredients = new ArrayList<>();

            while (rs.next()) {
                if (dish == null) {
                    dish = new Dish(
                            rs.getInt("d_id"),
                            rs.getString("d_name"),
                            DishTypeEnum.valueOf(rs.getString("dish_type"))
                    );
                }

                if (rs.getObject("i_id") != null) {
                    ingredients.add(new Ingredient(
                            rs.getInt("i_id"),
                            rs.getString("i_name"),
                            rs.getBigDecimal("price").doubleValue(),
                            CategoryEnum.valueOf(rs.getString("category")),
                            rs.getObject("required_quantity", Double.class),
                            dish
                    ));
                }
            }

            if (dish == null) {
                throw new RuntimeException("Dish not found");
            }

            dish.setIngredients(ingredients);
            return dish;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* ================= FIND INGREDIENTS ================= */
    public List<Ingredient> findIngredients(int page, int size) {
        String sql = "SELECT * FROM ingredient ORDER BY id LIMIT ? OFFSET ?";
        List<Ingredient> result = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, size);
            ps.setInt(2, page * size);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new Ingredient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price").doubleValue(),
                        CategoryEnum.valueOf(rs.getString("category")),
                        rs.getObject("required_quantity", Double.class),
                        null
                ));
            }
            return result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* ================= CREATE INGREDIENTS ================= */
    public List<Ingredient> createIngredients(List<Ingredient> ingredients) {
        String check = "SELECT 1 FROM ingredient WHERE name = ?";
        String insert = "INSERT INTO ingredient(name, price, category, required_quantity) VALUES (?,?,?,?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            for (Ingredient i : ingredients) {
                try (PreparedStatement ps = conn.prepareStatement(check)) {
                    ps.setString(1, i.getName());
                    if (ps.executeQuery().next()) {
                        throw new RuntimeException("Ingredient already exists");
                    }
                }

                try (PreparedStatement ps = conn.prepareStatement(insert)) {
                    ps.setString(1, i.getName());
                    ps.setBigDecimal(2, BigDecimal.valueOf(i.getPrice()));
                    ps.setString(3, i.getCategory().name());
                    ps.setObject(4, i.getRequiredQuantity());
                    ps.executeUpdate();
                }
            }

            conn.commit();
            return ingredients;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
