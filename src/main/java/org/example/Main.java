//package org.example;
//
//import java.util.List;
//
//public class Main {
//
//    public static void main(String[] args) {
//
//        DataRetriever dr = new DataRetriever();
//
////test findDishId
//        System.out.println("=== TEST findDishById ===");
//
//        try {
//            Dish dish = dr.findDishById(2); // ex: Poulet grillé
//            System.out.println("Plat : " + dish.getName());
//            System.out.println("Type : " + dish.getDishType());
//
//            System.out.println("\n--- Ingredients ---");
//            List<Ingredient> ingredients = dr.findDishIngredient(dish.getId());
//            ingredients.forEach(i ->
//                    System.out.println(
//                            i.getName() + " | " +
//                                    i.getPrice() + " | " +
//                                    i.getCategory() + " | qty=" +
//                                    i.getRequiredQuantity()
//                    )
//            );
//
//            System.out.println("\nCoût total du plat : " + dish.getDishCost());
//
//        } catch (RuntimeException e) {
//            System.out.println("Erreur (coût) : " + e.getMessage());
//        }
//
////test gross margin
//        System.out.println("\n=== TEST getGrossMargin ===");
//
//        try {
//            Dish dish = dr.findDishById(2);
//
//            // prix de vente simulé
//            dish.setPrice(8000.0);
//
//            System.out.println("Plat : " + dish.getName());
//            System.out.println("Prix de vente : " + dish.getPrice());
//            System.out.println("Coût : " + dish.getDishCost());
//            System.out.println("Marge brute : " + dish.getGrossMargin());
//
//        } catch (RuntimeException e) {
//            System.out.println("Erreur (marge) : " + e.getMessage());
//        }
//
////test find  ingredient pagination
//        System.out.println("\n=== TEST findIngredients (page=0, size=5) ===");
//
//        List<Ingredient> allIngredients = dr.findIngredients(0, 5);
//        allIngredients.forEach(i ->
//                System.out.println(i.getName() + " | " + i.getPrice() + " | " + i.getCategory())
//        );
//
////        test de la creation d'ingredient
//        System.out.println("\n=== TEST createIngredients ===");
//
//        try {
//            Dish dishRef = new Dish(1, "Ref Dish", DishTypeEnum.MAIN);
//
//            List<Ingredient> newIngredients = List.of(
//                    new Ingredient("Sel fin", 200.0, CategoryEnum.OTHER, dishRef),
//                    new Ingredient("Poivre vert", 150.0, CategoryEnum.OTHER, dishRef)
//            );
//
//            dr.createIngredients(newIngredients);
//            System.out.println("Insertion réussie des ingrédients");
//
//        } catch (RuntimeException e) {
//            System.out.println("Échec insertion ingrédients : " + e.getMessage());
//        }
//        Order order = dr.findOrderByReference("CMD-001");
//        order.setPaymentStatus(PaymentStatusEnum.PAID);
//
//        Sale sale = dr.createSaleFrom(order);
//        System.out.println("Vente créée à : " + sale.getCreationDatetime());
//
//    }
//}



package org.example;

import java.time.Instant;

public class Main {

    public static void main(String[] args) {

        DataRetriever dr = new DataRetriever();

        /* =====================================================
           1) TEST saveOrder (création)
         ===================================================== */
        System.out.println("\n=== TEST 1 : saveOrder (CREATION) ===");

        Order order = new Order(
                null,
                "CMD-2026-TEST-01",
                Instant.now(),
                PaymentStatusEnum.UNPAID
        );

        try {
            order = dr.saveOrder(order);
            System.out.println("Commande créée : ID = " + order.getId());
        } catch (Exception e) {
            System.out.println("❌ Erreur création commande : " + e.getMessage());
        }

        /* =====================================================
           2) TEST findOrderByReference
         ===================================================== */
        System.out.println("\n=== TEST 2 : findOrderByReference ===");

        try {
            Order found = dr.findOrderByReference("CMD-2026-TEST-01");
            System.out.println("Commande trouvée");
            System.out.println("ID       : " + found.getId());
            System.out.println("Statut   : " + found.getPaymentStatus());
            System.out.println("Créée le : " + found.getCreationDatetime());
        } catch (Exception e) {
            System.out.println("❌ Erreur findOrderByReference : " + e.getMessage());
        }

        /* =====================================================
           3) TEST saveOrder (PASSAGE À PAID)
         ===================================================== */
        System.out.println("\n=== TEST 3 : saveOrder (PASSAGE À PAID) ===");

        try {
            order.setPaymentStatus(PaymentStatusEnum.PAID);
            dr.saveOrder(order);
            System.out.println("Commande mise à jour → PAID");
        } catch (Exception e) {
            System.out.println("❌ Erreur mise à jour statut : " + e.getMessage());
        }

        /* =====================================================
           4) TEST saveOrder SUR COMMANDE PAYÉE (doit échouer)
         ===================================================== */
        System.out.println("\n=== TEST 4 : MODIFICATION COMMANDE PAYÉE (ERREUR ATTENDUE) ===");

        try {
            order.setPaymentStatus(PaymentStatusEnum.UNPAID);
            dr.saveOrder(order);
            System.out.println("❌ ERREUR : modification acceptée (interdit)");
        } catch (Exception e) {
            System.out.println("✔ Exception attendue : " + e.getMessage());
        }

        /* =====================================================
           5) TEST createSaleFrom (commande PAYÉE)
         ===================================================== */
        System.out.println("\n=== TEST 5 : createSaleFrom (OK) ===");

        try {
            Sale sale = dr.createSaleFrom(order);
            System.out.println("Vente créée avec succès");
            System.out.println("ID Vente : " + sale.getId());
            System.out.println("Date     : " + sale.getCreationDatetime());
        } catch (Exception e) {
            System.out.println("❌ Erreur création vente : " + e.getMessage());
        }

        /* =====================================================
           6) TEST createSaleFrom DEUX FOIS (ERREUR ATTENDUE)
         ===================================================== */
        System.out.println("\n=== TEST 6 : createSaleFrom DEUX FOIS (ERREUR) ===");

        try {
            dr.createSaleFrom(order);
            System.out.println("❌ ERREUR : vente dupliquée");
        } catch (Exception e) {
            System.out.println("✔ Exception attendue : " + e.getMessage());
        }

        System.out.println("\n=== FIN DES TESTS ===");
    }
}

