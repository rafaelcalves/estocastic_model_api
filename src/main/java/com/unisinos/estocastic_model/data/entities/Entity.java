package com.unisinos.estocastic_model.data.entities;

import com.unisinos.estocastic_model.data.PetriNet;

import java.util.ArrayList;

public class Entity {

    private String name;
    private int id;
    private double creationTime;
    private int priority = -1; //sem prioridade = -1, mais alta = 0, mais baixa = 255
    private PetriNet net;

    public Entity (String name, int id, double creationTime) {
        this.name = name;
        this.id = id;
        this.creationTime = creationTime;
    }

    public Entity (String name, int id, double creationTime, PetriNet net) {
        this.name = name;
        this.id = id;
        this.creationTime = creationTime;
        this.net = net;
    }

    public int getId() { return this.id; }

    public String getName() { return this.name; }

    public int getPriority() {
        if (priority < 0 || priority > 255) priority = -1; //deve estar entre 0 e 255
        return priority;
    }

    public void setPriority(int priority) { this.priority = priority; }

    //Vai ter que receber algo de fora mesmo. Scheduler tem o getTime
    public double getTimeSinceCreation(double currentTime) {
         return currentTime - creationTime;
    }

    //Vai precisar da montagem de uma lista de listas
    public ArrayList<EntitySet> getSets(ArrayList<EntitySet> list) {
        ArrayList<EntitySet> result = null; //resultado nulo inicialmente
        Entity temp = null; //ponteiro pra entity comparativa
        for(int i = 0; i < list.size(); i++) { //vai em todos os entitysets
            temp = list.get(i).findEntity(id); //testa se está em um
            if(temp != null) { //se estiver
                result.add(list.get(i)); //joga o entityset no result
                temp = null; //e zera o "ponteiro" de novo pra próxima entityset
            }
        }
        return result;
    }

    public PetriNet getPetriNet() { return net; }

    public void setPetriNet(PetriNet net) { this.net = net; }
}
