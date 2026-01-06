;

import org.example.CategoryEnum;
import org.example.DataRetriever;
import org.example.Dish;
import org.example.Ingredient;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataRetrieverTest {

    DataRetriever dr = new DataRetriever();

    /* a */
    @Test
    void findDishById_ok() {
        Dish dish = dr.findDishById(1);

        assertEquals("Salade Fraîche", dish.getName());
        assertEquals(2, dish.getIngredients().size());
    }

    /* b */
    @Test
    void findDishById_notFound() {
        assertThrows(RuntimeException.class,
                () -> dr.findDishById(999));
    }

    /* c */
    @Test
    void findIngredients_page2_size2() {
        List<Ingredient> list = dr.findIngredients(2, 2);

        assertEquals("Poulet", list.get(0).getName());
        assertEquals("Chocolat", list.get(1).getName());
    }

    /* d */
    @Test
    void findIngredients_page3_size5() {
        List<Ingredient> list = dr.findIngredients(3, 5);
        assertTrue(list.isEmpty());
    }

    /* e */
    @Test
    void findDishByIngredientName() {
        List<Dish> dishes = dr.findDishByIngredientName("eur");

        assertEquals(1, dishes.size());
        assertEquals("Gateau au chocolat", dishes.get(0).getName());
    }

    /* f */
    @Test
    void findIngredientsByCriteria_vegetables() {
        List<Ingredient> list = dr.findIngredientsByCriteria(
                null, CategoryEnum.VEGETABLE, null, 1, 10);

        assertEquals(2, list.size());
    }

    /* g */
    @Test
    void findIngredientsByCriteria_empty() {
        List<Ingredient> list = dr.findIngredientsByCriteria(
                "cho", null, "Sal", 1, 10);

        assertTrue(list.isEmpty());
    }

    /* h */
    @Test
    void findIngredientsByCriteria_chocolat() {
        List<Ingredient> list = dr.findIngredientsByCriteria(
                "cho", null, "gâteau", 1, 10);

        assertEquals(1, list.size());
        assertEquals("Chocolat", list.get(0).getName());
    }

    /* i */
    @Test
    void createIngredients_ok() {
        List<Ingredient> list = dr.createIngredients(List.of(
                new Ingredient(null,"Fromage",1200.0,CategoryEnum.DAIRY,null),
                new Ingredient(null,"Oignon",500.0,CategoryEnum.VEGETABLE,null)
        ));

        assertEquals(2, list.size());
    }

    /* j */
    @Test
    void createIngredients_duplicate() {
        assertThrows(RuntimeException.class, () ->
                dr.createIngredients(List.of(
                        new Ingredient(null,"Carotte",2000.0,CategoryEnum.VEGETABLE,null),
                        new Ingredient(null,"Laitue",2000.0,CategoryEnum.VEGETABLE,null)
                )));
    }

    /* k l m → validés via saveDish */
}
