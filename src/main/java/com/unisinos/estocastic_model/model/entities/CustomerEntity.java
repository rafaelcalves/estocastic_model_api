package com.unisinos.estocastic_model.model.entities;


public class CustomerEntity extends Entity {
    private int quantity;
    private int orderId;

    public CustomerEntity(int id, double creationTime, int quantity) {
        super("cliente", id, creationTime);
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
