package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    public Dish findDishById(int id) {
        String dishquery = "SELECT d.*, i.* FROM dish d LEFT JOIN ingredient i ON i.dish_id = d.id WHERE d.id = ?";
        String ingredientquery = "SELECT * FROM Ingredient WHERE id = ?";
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
        String ingredientquery = "SELECT * FROM Ingredient limit ? offset ?";
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
        String checkingquery = "SELECT * FROM Ingredient WHERE name = ?";
        String insertionquery = "INSERT INTO Ingredient  (name, price , category , id_dish) VALUES (?, ? ,? ,?)";

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

    public Dish saveDish(Dish dish){
        String insertDIsh = "INSERT INTO Dish (name, price, category) VALUES (?,?) RETURNING dish_id";
        String updateDish = "Update dish set name = ?, price = ?, dish_type = ? where id = ?";
        String deleteLinks = "Delete from dish_ingredient where dish_id = ?";
        String insertLink = "Insert into dish_ingredient(dish_is , ingredient_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection()){
            conn.setAutoCommit(false);
            try{ if (dish.getId() == 0){
                try (PreparedStatement ps = conn.prepareStatement(insertDIsh)){
                    ps.setString(1,dish.getName());
                    ps.setString(2,dish.getDishType().name());
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    dish.setId(rs.getInt(1));
                }
            }else{ try (PreparedStatement ps = conn.prepareStatement(updateDish)){
                ps.setString(1,dish.getName());
                ps.setString(2,dish.getDishType().name());
                ps.setInt(3,dish.getId());
                ps.executeUpdate();
            }try (PreparedStatement ps = conn.prepareStatement(deleteLinks)){
                ps.setInt(1,dish.getId());
                ps.executeUpdate();
            }
            }for (Ingredient ing : dish.getIngredients()){
                try(PreparedStatement ps = conn.prepareStatement(insertLink)){
                    ps.setInt(1 , dish.getId());
                    ps.setInt(2,ing.getId());
                    ps.executeUpdate();
                }
            } conn.commit();
                return dish;
            }catch (RuntimeException e){
                conn.rollback();
                throw e;
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public List<Dish> findDishByIngredientName(String ingredientName){
        String checkingquery = """
select distinct d.* from dish d join dish_ingredient di on d.id=di.dish_id join
                ingredient i on i.id =di.ingredient_id where lower(i.name) LIKE ?""";
        List<Dish> dishes = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps =conn.prepareStatement(checkingquery)){
            ps.setString(1,"%"+ingredientName.toLowerCase() + "%");
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                dishes.add(new Dish(rs.getInt("id") ,
                            rs.getString("name"),
                            DishTypeEnum.valueOf(rs.getString("dish_type"))
                ));
            }
            return dishes;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public List<Ingredient> findIngredientsByCriteria(
            String ingredientName,
            CategoryEnum category,
            String dishName,
            int page,
            int size){
        StringBuilder sql = new StringBuilder("""
                select distinct i.* from ingredient i left join dish_ingredient di on i.id = di.ingredient_id
                left join dish d on d.id-di.dish_id where 1=1""");
        List<Object> params = new ArrayList<>();
        if(ingredientName!=null){
            sql.append(" and lower (i.name) like ? ");
            params.add("%"+ingredientName.toLowerCase() + "%");
        }
        if(category!=null){
            sql.append(" and i.category = ? ");
            params.add(category);
        }
        if(dishName!=null){
            sql.append(" and lower (d.name) like ? ");
            params.add("%"+dishName.toLowerCase() + "%");
        }
        sql.append(" order by i.id limit ? offset ? ");
        params.add(size);
        params.add((page-1)*size);
        List<Ingredient> ingredients = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps=conn.prepareStatement(sql.toString())){
            for(int i =0;i<params.size();i++){
                ps.setObject(i+1,params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                ingredients.add(new Ingredient(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        CategoryEnum.valueOf(rs.getString("category")) , null
                ));
            }
            return ingredients;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
