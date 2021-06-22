package com.unisinos.estocastic_model.data;

public class Process {

    private String name;
    private int processId;
    private double timeTo; //tempo até ativar
    private double duration; //duração do processo conforme evento de definição
    private boolean active;
    private Event event;
    private String entitySetName;

    public Process (String name, double duration) {
        this.name = name;
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

    public String getName()
    {
        return name;
    }

    public Event getEvent()
    {
        return event;
    }

    public String getEntitySetName()
    {
        return entitySetName;
    }

    public void setEntitySetName(String entitySetName)
    {
        this.entitySetName = entitySetName;
    }
}
