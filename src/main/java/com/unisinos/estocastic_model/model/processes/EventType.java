package com.unisinos.estocastic_model.model.processes;

import com.unisinos.estocastic_model.model.Constants;

public enum EventType {
    ARRIVAL(0),
    CONSUMPTION(0),
    ORDER(Constants.CASHIER_ATTENDANCE_RESOURCES),
    PREPARATION(Constants.ORDER_PREPARATION_RESOURCES),
    WC(0);

    private int resources;

    EventType(int resources){
        this.resources = resources;
    }

    public int getResources() {
        return resources;
    }
}
