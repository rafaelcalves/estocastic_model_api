package com.unisinos.estocastic_model.model.entities;

import java.util.ArrayList;
import java.util.List;

public class EntitySet {

    private EntitySetType type;
    private int id;
    private List<Entity> entities;
    private List<Integer> sizes;
    private List<Double> timesToLeaveSet;
    private int highestSize;
    private double highestTimeToLeaveSet;

    public EntitySet(EntitySetType type) {
        this.type = type;
        entities = new ArrayList<>();
        sizes = new ArrayList<>();
        timesToLeaveSet = new ArrayList<>();
    }

    //pega média de tamanhos do set (histórica)
    public double getAverageSize() {
        int average = 0;
        for (int i = 0; i<this.sizes.size(); i++) {
            average += this.sizes.get(i);
        }
        if(this.sizes.size() != 0) {
            average = average/this.sizes.size();
            return average;
        }
        else return 0;
    }

    public double getAverageTimeInSet() {
        int average = 0;
        for (int i = 0; i<this.timesToLeaveSet.size(); i++) {
            average += this.timesToLeaveSet.get(i);
        }
        if(this.timesToLeaveSet.size() != 0) {
            average = average/this.timesToLeaveSet.size();
            return average;
        }
        else return 0;
    }

    //tempo máximo que algo ficou no set
    public double maxTimeInSet() {
        return highestTimeToLeaveSet;
    }

    public void insert(Entity entity) {
        this.entities.add(entity);
        if(getSize()>highestSize) highestSize = getSize();
    }

    public void remove(Entity entity, double currentTime) {
        if (!this.entities.isEmpty()) {
            logTime(entity, currentTime);
            entities.remove(entity);
        }
    }

    public Entity getFirst()
    {
        return entities.get(0);
    }

    public void releaseFirst(double currentTime){
        if(!entities.isEmpty()) {
            logTime(entities.get(0), currentTime);
            entities.remove(0);
        }
    }

    public int getSize() {
        return entities.size();
    }

    public void logSize(){
        sizes.add(this.entities.size());
    }

    public void logTime(Entity entity, double currentTime) {
        double timeSinceEntrance = entity.getTimeSinceEntrance(currentTime);
        timesToLeaveSet.add(timeSinceEntrance);
        if(timeSinceEntrance > highestTimeToLeaveSet) highestTimeToLeaveSet = timeSinceEntrance;
    }

    public void setEntitySetId(int id) { this.id = id; }

    public int getEntitySetId() { return this.id; }

    public EntitySetType getType() {
        return type;
    }

    public void setType(EntitySetType type) {
        this.type = type;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }
}
