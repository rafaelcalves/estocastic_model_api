package com.unisinos.estocastic_model.data.processes;

import com.unisinos.estocastic_model.data.entities.EntitySetType;

public class ConsumptionProcess extends Process {
    private EntitySetType tableType;

    public ConsumptionProcess(EventType eventType, double duration, EntitySetType tableType) {
        super(eventType, duration);
        this.tableType = tableType;
    }

    public EntitySetType getTableType() {
        return tableType;
    }

    public void setTableType(EntitySetType tableType) {
        this.tableType = tableType;
    }
}
