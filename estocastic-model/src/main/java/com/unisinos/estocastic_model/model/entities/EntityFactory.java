package com.unisinos.estocastic_model.model.entities;

public class EntityFactory {
    public Entity create(String name, int id, double creationTime) {
        return new Entity(name, id, creationTime);
    }
    public CustomerEntity createCustomer(int id, double creationTime, int quantity) {
        return new CustomerEntity(id, creationTime, quantity);
    }
    public OrderEntity createOrder(int id, double creationTime){
        return new OrderEntity(id, creationTime);
    }
}
