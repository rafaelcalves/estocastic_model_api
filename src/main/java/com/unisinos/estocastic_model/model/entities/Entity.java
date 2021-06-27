package com.unisinos.estocastic_model.model.entities;

public class Entity {

    private String name;
    private int id;
    private double setEntranceTime;

    public Entity (String name, int id, double setEntranceTime) {
        this.name = name;
        this.id = id;
        this.setEntranceTime = setEntranceTime;
    }

    public int getId() { return this.id; }

    public String getName() { return this.name; }

    public double getTimeSinceEntrance(double currentTime) {
         return currentTime - setEntranceTime;
    }

}
