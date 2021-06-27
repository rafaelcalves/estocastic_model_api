package com.unisinos.estocastic_model.data.processes;

public enum EventType {
    ARRIVAL(0),
    CONSUMPTION(0),
    ORDER(3),
    PREPARATION(2),
    WC(0);

    private int resources;

    EventType(int resources){
        this.resources = resources;
    }

    public int getResources() {
        return resources;
    }
}
