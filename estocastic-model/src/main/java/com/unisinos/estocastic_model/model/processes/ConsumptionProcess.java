package com.unisinos.estocastic_model.model.processes;

import com.unisinos.estocastic_model.model.entities.EntitySetType;

public class ConsumptionProcess extends Process {
    private EntitySetType tableType;
    private int customersQuantity;

    public ConsumptionProcess(int id, EventType eventType, double duration, EntitySetType tableType, int customersQuantity) {
        super(id, eventType, duration);
        this.tableType = tableType;
        this.customersQuantity = customersQuantity;
    }

    public EntitySetType getTableType() {
        return tableType;
    }

    public void setTableType(EntitySetType tableType) {
        this.tableType = tableType;
    }

    public int getCustomersQuantity() {
        return customersQuantity;
    }

    public void setCustomersQuantity(int customersQuantity) {
        this.customersQuantity = customersQuantity;
    }
}
