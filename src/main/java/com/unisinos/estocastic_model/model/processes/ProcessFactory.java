package com.unisinos.estocastic_model.model.processes;

import com.unisinos.estocastic_model.model.entities.EntitySetType;

import java.util.Random;

public class ProcessFactory {
    public Process create(EventType eventType, int id){
        Process newProcess = new Process(eventType, getProcessDurationByEventType(eventType));
        newProcess.setProcessId(id);
        return newProcess;
    }
    public ConsumptionProcess createConsumption(int id, EntitySetType tableType){
        ConsumptionProcess consumption = (ConsumptionProcess) create(EventType.CONSUMPTION, id);
        consumption.setTableType(tableType);
        return consumption;
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

        Random fRandom = new Random();
        return minValue + (maxValue - minValue) * fRandom.nextDouble();
    }

    private double exponential(double meanValue) {
        Random fRandom = new Random();
        return - (Math.log(fRandom.nextDouble()) / meanValue);
    }

    private double normal(double meanValue, double stdDeviationValue) {
        Random fRandom = new Random();
        return meanValue + fRandom.nextGaussian()*stdDeviationValue;
    }
}
