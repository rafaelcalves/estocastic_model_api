package com.unisinos.estocastic_model.model.processes;

import com.unisinos.estocastic_model.model.entities.EntitySetType;

public class Process {

    private EventType eventType;
    private int processId;
    private double timeTo; //tempo até ativar
    private double duration; //duração do processo conforme evento de definição
    private boolean active;
    private EntitySetType entitySetType;

    public Process (EventType eventType, double duration) {
        this.eventType = eventType;
        this.duration = duration;
    }

    public double getDuration() { return duration; }

    public void setDuration(double duration) { this.duration = duration; }

    public double getTimeTo() { return timeTo; }

    public void setTimeTo(double timeTo) { this.timeTo = timeTo; }

    public int getProcessId () { return this.processId; }

    public void setProcessId(int id) { this.processId = id; }

    public boolean isActive() { return active; }

    public void activate(boolean active) { this.active = active; }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public EntitySetType getEntitySetType() {
        return entitySetType;
    }

    public void setEntitySetType(EntitySetType entitySetType) {
        this.entitySetType = entitySetType;
    }
}
