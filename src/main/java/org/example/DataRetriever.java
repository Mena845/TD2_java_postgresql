package org.example;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    public Dish findDishById(Integer id) {
        String sql = """
            SELECT d.id AS d_id, d.name AS d_name, d.dish_type,
                   i.id AS i_id, i.name AS i_name, i.price, i.category, i.required_quantity
            FROM dish d
            LEFT JOIN dish_ingredient di ON d.id = di.dish_id
            LEFT JOIN ingredient i ON i.id = di.ingredient_id
            WHERE d.id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            Dish dish = null;
            List<Ingredient> ingredients = new ArrayList<>();

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    if (dish == null) {
                        dish = new Dish(
                                rs.getInt("d_id"),
                                rs.getString("d_name"),
                                DishTypeEnum.valueOf(rs.getString("dish_type"))
                        );
                    }

                    if (rs.getObject("i_id") != null) {
                        BigDecimal priceBd = rs.getBigDecimal("price");
                        double price = priceBd == null ? 0.0 : priceBd.doubleValue();

                        ingredients.add(new Ingredient(
                                rs.getInt("i_id"),
                                rs.getString("i_name"),
                                price,
                                CategoryEnum.valueOf(rs.getString("category")),
                                rs.getObject("required_quantity", Double.class),
                                dish
                        ));
                    }
                }
            }

            if (dish == null) {
                throw new RuntimeException("Dish not found with id = " + id);
            }

            dish.setIngredients(ingredients);
            return dish;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Ingredient> findIngredients(int page, int size) {
        String sql = """
            SELECT id, name, price, category, required_quantity
            FROM ingredient
            ORDER BY id
            LIMIT ? OFFSET ?
        """;

        List<Ingredient> result = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, size);
            ps.setInt(2, page * size);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BigDecimal priceBd = rs.getBigDecimal("price");
                    double price = priceBd == null ? 0.0 : priceBd.doubleValue();

                    result.add(new Ingredient(
                            rs.getInt("id"),
                            rs.getString("name"),
                            price,
                            CategoryEnum.valueOf(rs.getString("category")),
                            rs.getObject("required_quantity", Double.class),
                            null
                    ));
                }
            }

            return result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Ingredient> createIngredients(List<Ingredient> ingredients) {
        String checkSql = "SELECT 1 FROM ingredient WHERE name = ?";
        String insertSql = """
            INSERT INTO ingredient(name, price, category, required_quantity)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try {
                for (Ingredient i : ingredients) {

                    try (PreparedStatement check = conn.prepareStatement(checkSql)) {
                        check.setString(1, i.getName());
                        try (ResultSet rs = check.executeQuery()) {
                            if (rs.next()) {
                                throw new RuntimeException("Ingredient already exists: " + i.getName());
                            }
                        }
                    }

                    try (PreparedStatement insert = conn.prepareStatement(insertSql)) {
                        insert.setString(1, i.getName());
                        insert.setBigDecimal(2, BigDecimal.valueOf(i.getPrice()));
                        insert.setString(3, i.getCategory().name());
                        insert.setObject(4, i.getRequiredQuantity());
                        insert.executeUpdate();
                    }
                }

                conn.commit();
                return ingredients;

            } catch (RuntimeException e) {
                conn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Ingredient> findDishIngredient(Integer dishId) {
        String sql = """
            SELECT i.id, i.name, i.price, i.category, i.required_quantity
            FROM ingredient i
            JOIN dish_ingredient di ON i.id = di.ingredient_id
            WHERE di.dish_id = ?
            ORDER BY i.id
        """;

        List<Ingredient> ingredients = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, dishId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BigDecimal priceBd = rs.getBigDecimal("price");
                    double price = priceBd == null ? 0.0 : priceBd.doubleValue();

                    ingredients.add(new Ingredient(
                            rs.getInt("id"),
                            rs.getString("name"),
                            price,
                            CategoryEnum.valueOf(rs.getString("category")),
                            rs.getObject("required_quantity", Double.class),
                            null
                    ));
                }
            }

            return ingredients;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Ingredient saveIngredient(Ingredient ingredientToSave) {

        String insertStockMovement = """
        INSERT INTO stock_mouvement
        (id, id_ingredient, quantity, unit, type, creation_datetime)
        VALUES (?, ?, ?, ?, ?, ?)
        ON CONFLICT (id) DO NOTHING
    """;

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            for (StockMovement sm : ingredientToSave.getStockMovementList()) {

                try (PreparedStatement ps = conn.prepareStatement(insertStockMovement)) {

                    ps.setInt(1, sm.getId());
                    ps.setInt(2, ingredientToSave.getId());
                    ps.setDouble(3, sm.getValue().getQuantity());
                    ps.setString(4, sm.getValue().getUnit().name());
                    ps.setString(5, sm.getType().name());
                    ps.setTimestamp(6, Timestamp.from(sm.getCreationDatetime()));

                    ps.executeUpdate();
                }
            }

            conn.commit();
            return ingredientToSave;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
