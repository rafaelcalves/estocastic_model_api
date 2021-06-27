package com.unisinos.estocastic_model.model.entities;

import com.unisinos.estocastic_model.model.Constants;

public enum EntitySetType {
    CASHIER_1(0),
    CASHIER_2(0),
    BAR_COUNTER(Constants.BAR_COUNTER_RESOURCES),
    TABLE_2(Constants.TABLE_2_RESOURCES),
    TABLE_4(Constants.TABLE_4_RESOURCES),
    ORDER(0);

    private int resources;

    EntitySetType(int resources){
        this.resources = resources;
    }

    public int getResources() {
        return resources;
    }
}
