package com.unisinos.estocastic_model.data;

import java.util.ArrayList;

public class EntitySet {

    private String name;
    private int id;
    private String mode = "None"; //suportados: "FIFO","LIFO","Priority based" e "None"
    private int size = 0;
    private ArrayList<int> averageSizes; //para pegar média de tamanhos
    private int maxPossibleSize = 0; //0 = ilimitado
    private ArrayList<Entity> entities; //rever tipos - arraylist é o ideal ou não?
    boolean logging = false; //para controle global de se está realizando logging
    double loggingTimeGap = 5.0; //de quanto em quanto tempo vai logar

    public EntitySet(String name, String mode, int maxPossibleSize) {
        this.name = name;
        this.mode = mode;
        this.maxPossibleSize = maxPossibleSize;

        //correção de modo na inicialização
        if (!mode.matches("(?i)FIFO|LIFO|Priority based|None")) {
            this.mode = "None";
        }

        //correção de tamanho na inicialização
        if (maxPossibleSize < 0) {
            this.maxPossibleSize = 0;
        }
    }

    public String getMode() { return mode; }

    public void setMode(String mode) { this.mode = mode; }

    public void insert(Entity entity) {
        if (!this.isFull()) {
            this.entities.add(entity);
            this.size++;
        }
    }

    public Entity remove() {
        Entity output = null;
        if (!this.entities.isEmpty()) {
            output = this.entities.get(this.entities.size()-1);
            this.entities.remove(this.entities.size()-1);
            this.size--;
        }
        return output;
    }

    public void removeById(int id) {
        for(int i=0; i < entities.size(); i++) {
            if(entities.get(i).getId() == id) entities.remove(i);
        }
    }

    public boolean isEmpty() {
        return this.entities.isEmpty();
    }

    public boolean isFull() {
        if(this.entities.size() == this.maxPossibleSize) return true;
        else return false;
    }

    public Entity findEntity(int id) {
        Entity target = null;
        for(int i=0; i < entities.size(); i++) {
            if(entities.get(i).getId() == id) target = entities.get(i);
        }
        return target;
    }

    public int getSize() { return size; }

    public int getMaxPossibleSize() { return maxPossibleSize; }

    public void setMaxPossibleSize(int maxPossibleSize) { this.maxPossibleSize = maxPossibleSize; }

    //pega média de tamanhos do set (histórica)
    public double averageSize() {
        int average = 0;
        for (int i=0; i<this.averageSizes.size(); i++) {
            average += this.averageSizes.get(i);
        }
        average = average/this.averageSizes.size();
        return average;
    }

    //passa o tempo atual como current
    public double averageTimeInSet(double current) {
        double average = 0;
        for(int i=0; i < entities.size(); i++) {
            average += entities.get(i).getTimeSinceCreation(current);
        }
        average = average/entities.size();
        return average;
    }

    //tempo máximo que algo ficou no set
    public double maxTimeInSet(double current) {
        double maximum = 0.0;
        for(int i=0; i < entities.size(); i++) {
            if(entities.get(i).getTimeSinceCreation(current) > maximum) maximum = entities.get(i).getTimeSinceCreation(current);
        }
        return maximum;
    }

    public void startLog(double timeGap) {
        this.loggingTimeGap = timeGap;
        this.logging = true;
    }

    public void stopLog() {
        this.logging = false;
    }

    //pega tamanho agora para fins de logging (temporizado)
    public void getLog() {
        averageSizes.add(this.getSize());
    }

}
