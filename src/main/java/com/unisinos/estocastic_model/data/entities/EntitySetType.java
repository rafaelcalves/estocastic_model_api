package com.unisinos.estocastic_model.data.entities;

public enum EntitySetType {
    CASHIER_1(0),
    CASHIER_2(0),
    BAR_COUNTER(6),
    TABLE_2(4),
    TABLE_4(4),
    ORDER(0);

    private int resources;

    EntitySetType(int resources){
        this.resources = resources;
    }

    public int getResources() {
        return resources;
    }
}
