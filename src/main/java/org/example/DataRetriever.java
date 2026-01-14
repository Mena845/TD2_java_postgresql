// java
package org.example;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    private int normalizeSize(int size) {
        return size > 0 ? size : 10;
    }

    private int normalizePage(int page) {
        return Math.max(0, page); // accepte page = 0 comme première page
    }

    private int computeOffset(int page, int size) {
        return normalizePage(page) * normalizeSize(size);
    }

    /* ===================== FIND DISH BY ID ===================== */
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
            try (ResultSet rs = ps.executeQuery()) {

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

                if (dish == null) {
                    throw new RuntimeException("Dish not found with id " + id);
                }

                dish.setIngredients(ingredients);
                return dish;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* ===================== FIND INGREDIENTS (PAGINATION) ===================== */
    public List<Ingredient> findIngredients(int page, int size) {
        int safeSize = normalizeSize(size);
        int offset = computeOffset(page, size);

        String sql = "SELECT id, name, price, category, required_quantity FROM ingredient ORDER BY id LIMIT ? OFFSET ?";
        List<Ingredient> result = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, safeSize);
            ps.setInt(2, offset);

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

    /* ===================== CREATE INGREDIENTS (ATOMIC) ===================== */
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

    /* ===================== SAVE DISH ===================== */
    public Dish saveDish(Dish dish) {
        String insertDish = """
            INSERT INTO dish(name, dish_type)
            VALUES (?, ?) RETURNING id
        """;

        String updateDish = """
            UPDATE dish SET name = ?, dish_type = ? WHERE id = ?
        """;

        String deleteLinks = "DELETE FROM dish_ingredient WHERE dish_id = ?";
        String insertLink = "INSERT INTO dish_ingredient(dish_id, ingredient_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try {
                if (dish.getId() == null) {
                    try (PreparedStatement ps = conn.prepareStatement(insertDish)) {
                        ps.setString(1, dish.getName());
                        ps.setString(2, dish.getDishType().name());
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                dish.setId(rs.getInt(1));
                            } else {
                                throw new RuntimeException("Failed to insert dish and retrieve id");
                            }
                        }
                    }
                } else {
                    try (PreparedStatement ps = conn.prepareStatement(updateDish)) {
                        ps.setString(1, dish.getName());
                        ps.setString(2, dish.getDishType().name());
                        ps.setInt(3, dish.getId());
                        ps.executeUpdate();
                    }

                    try (PreparedStatement ps = conn.prepareStatement(deleteLinks)) {
                        ps.setInt(1, dish.getId());
                        ps.executeUpdate();
                    }
                }

                if (dish.getIngredients() != null) {
                    for (Ingredient i : dish.getIngredients()) {
                        try (PreparedStatement ps = conn.prepareStatement(insertLink)) {
                            ps.setInt(1, dish.getId());
                            ps.setInt(2, i.getId());
                            ps.executeUpdate();
                        }
                    }
                }

                conn.commit();
                return dish;

            } catch (RuntimeException e) {
                conn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* ===================== FIND INGREDIENTS BY CRITERIA ===================== */
    public List<Ingredient> findIngredientsByCriteria(
            String ingredientName,
            CategoryEnum category,
            String dishName,
            int page,
            int size) {

        int safeSize = normalizeSize(size);
        int offset = computeOffset(page, size);

        StringBuilder sql = new StringBuilder("""
            SELECT DISTINCT i.id, i.name, i.price, i.category, i.required_quantity
            FROM ingredient i
            LEFT JOIN dish_ingredient di ON i.id = di.ingredient_id
            LEFT JOIN dish d ON d.id = di.dish_id
            WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();

        if (ingredientName != null) {
            sql.append(" AND lower(i.name) LIKE ? ");
            params.add("%" + ingredientName.toLowerCase() + "%");
        }

        if (category != null) {
            sql.append(" AND i.category = ? ");
            params.add(category.name());
        }

        if (dishName != null) {
            sql.append(" AND lower(d.name) LIKE ? ");
            params.add("%" + dishName.toLowerCase() + "%");
        }

        sql.append(" ORDER BY i.id LIMIT ? OFFSET ? ");
        params.add(safeSize);
        params.add(offset);

        List<Ingredient> result = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

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
}
