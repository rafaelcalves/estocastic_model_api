package com.unisinos.estocastic_model.model.processes;

import com.unisinos.estocastic_model.model.entities.EntitySetType;

import java.util.Random;

public class ProcessFactory {
    public Process create(EventType eventType, int id){
        return new Process(id,eventType, getProcessDurationByEventType(eventType));
    }
    public ConsumptionProcess createConsumption(int id, EntitySetType tableType, int customersQuantity){
        return new ConsumptionProcess(id, EventType.CONSUMPTION, getProcessDurationByEventType(EventType.CONSUMPTION),tableType, customersQuantity);
    }

    private double getProcessDurationByEventType(EventType eventType) {
        switch(eventType) {
            case ARRIVAL:
                return exponential(3.0);
            case ORDER:
                return normal(8, 2);
            case PREPARATION:
                return normal(14, 5);
            case CONSUMPTION:
                return normal(20, 8);
            case WC:
                return uniform(120, 180);
            default:
                return 0;
        }
    }

    private double uniform(double minValue, double maxValue) {
        double result = 0.0;
        Random fRandom = new Random();
        result = minValue + (maxValue - minValue) * fRandom.nextDouble();
        return fixNegatives(result);
    }

    private double exponential(double meanValue) {
        Random fRandom = new Random();
        double result = 0.0;
        result = - (Math.log(fRandom.nextDouble()) / meanValue);
        return fixNegatives(result);
    }

    private double normal(double meanValue, double stdDeviationValue) {
        Random fRandom = new Random();
        double result = 0.0;
        result =  meanValue + fRandom.nextGaussian()*stdDeviationValue;
        return fixNegatives(result);
    }

    private double fixNegatives(double value) {
        double result = value;
        if(result < 0) result *= -1;
        return result;
    }

}
