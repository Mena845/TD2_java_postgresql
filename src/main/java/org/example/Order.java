package org.example;

import java.time.Instant;
import java.util.List;

public class Order {

    private Integer id;
    private String reference;
    private Instant creationDatetime;
    private PaymentStatusEnum paymentStatus;
    private List<Dish> dishes; // ou DishOrder selon ton modèle

    public Order(Integer id, String reference, Instant creationDatetime, PaymentStatusEnum paymentStatus) {
        this.id = id;
        this.reference = reference;
        this.creationDatetime = creationDatetime;
        this.paymentStatus = paymentStatus;
    }

    /* ===== REGLE METIER CLE ===== */
    private void checkIfModifiable() {
        if (this.paymentStatus == PaymentStatusEnum.PAID) {
            throw new IllegalStateException(
                    "La commande est déjà payée et ne peut plus être modifiée"
            );
        }
    }

    /* ===== EXEMPLES DE MODIFICATION ===== */
    public void setDishes(List<Dish> dishes) {
        checkIfModifiable();
        this.dishes = dishes;
    }

    public void setPaymentStatus(PaymentStatusEnum paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    /* ===== GETTERS ===== */
    public Integer getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public Instant getCreationDatetime() {
        return creationDatetime;
    }

    public PaymentStatusEnum getPaymentStatus() {
        return paymentStatus;
    }

    public List<Dish> getDishes() {
        return dishes;
    }
}
