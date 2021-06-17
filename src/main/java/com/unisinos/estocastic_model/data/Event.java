package com.unisinos.estocastic_model.data;

public class Event {

    private String name;
    private int eventId;

    public Event (String name) {
        this.name = name;
    }

    public String getName() { return this.name; }

    public int getEventId() { return this.eventId; }

    public void setEventId(int id) { this.eventId = id; }

}
