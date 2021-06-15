package com.unisinos.estocastic_model.data;

public class Process {

    private String name;
    private int processId;
    private double timeTo; //tempo até ativar
    private double duration;
    private boolean active;

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

}
