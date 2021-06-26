package com.unisinos.estocastic_model.data.entities;

public class EntitySetFactory {
    public EntitySet create(EntitySetType type, int id) {
        EntitySet newEntitySet = new EntitySet(type);
        newEntitySet.setEntitySetId(id);
        return newEntitySet;
    }
}
