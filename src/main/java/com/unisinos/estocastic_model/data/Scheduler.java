package com.unisinos.estocastic_model.data;

import java.util.ArrayList;
import java.util.Random;

public class Scheduler {

    private double time;
    private ArrayList<EntitySet> entitySets;
    private ArrayList<Event> events;
    private ArrayList<Process> processes;
    private ArrayList<Resource> resources;

    private Random fRandom = new Random(); //para cálculos usando random (distribuições)

    //pega o tempo do exato instante atual
    public double getTime() { return this.time; }

    //dependendo do tipo de evento, calcula tempo para iniciar
    public double mapEventDuration(Event event) {
        double result = 0;
        switch(event.getName()) {
            case "chegada":
                result = exponential(3.0);
            break;
            case "pedido":
                result = normal(8, 2);
            break;
            case "preparo":
                result = normal(14, 5);
            break;
            case "refeição":
                result = normal(20, 8);
            break;
            case "banheiro":
                result = uniform(120, 180);
            break;
            default:
            break;
        }
        return result;
    }

    //converte um evento em um processo marcado para esse exato instante
    public void scheduleNow(Event event) {
        Process newProcess = new Process(event.getName(), mapEventDuration(event));
        newProcess.activate(true);
        processes.add(newProcess);
    }

    //converte um evento em um processo marcado para em algum tempo
    public void scheduleIn(Event event, double timeToEvent) {
        Process newProcess = new Process(event.getName(), mapEventDuration(event));
        newProcess.setTimeTo(timeToEvent);
        processes.add(newProcess);
    }

    //converte um evento em um processo marcado para um exato timestamp
    public void scheduleAt(Event event, double absoluteTime) {
        Process newProcess = new Process(event.getName(), mapEventDuration(event));
        newProcess.setTimeTo(absoluteTime - time);
        processes.add(newProcess);
    }

    //inicia um processo por ID nesse exato instante
    public void startProcessNow(int processId) {
        for(int i=0; i < processes.size(); i++) {
            if(processes.get(i).getProcessId() == processId) processes.get(i).activate(true);
        }
    }

    //inicia um processo por ID em algum tempo
    public void startProcessIn(int processId, double timeToEvent) {
        for(int i=0; i < processes.size(); i++) {
            if(processes.get(i).getProcessId() == processId) processes.get(i).setTimeTo(timeToEvent);
        }
    }

    //inicia um processo por ID em um exato timestamp
    public void startProcessAt(int processId, double absoluteTime) {
        for(int i=0; i < processes.size(); i++) {
            if(processes.get(i).getProcessId() == processId) processes.get(i).setTimeTo(absoluteTime - time);
        }
    }

    //espera por um tempo
    public void waitFor(double time) {
        this.time += time;
    }

    public void simulate () {
        //executa até esgotar o modelo, isto é, até a engine não ter mais nada para processar (FEL vazia,
        //    i.e., lista de eventos futuros vazia)
    }

    public void simulateOneStep () {
        //executa somente uma primitiva da API e interrompe execução; por ex.: dispara um
        //    evento e para; insere numa fila e para, etc.

    }

    public void simulateBy(double duration) {  }

    public void simulateUntil(double absoluteTime) {  }

    //criação, destruição e acesso para componentes

    public Entity createEntity(String name, int id, int priority, PetriNet net) {
        Entity newEntity = new Entity(name, id, this.getTime(), net);
        newEntity.setPriority(priority);
        return newEntity;
    }

    //a princípio IDs vão ser todos únicos
    public void destroyEntity(int id) {
        for(int i = 0; i < entitySets.size(); i++) { //vai em todos os entitysets
            entitySets.get(i).removeById(id); //procura e remove
        }
    }

    //a princípio IDs vão ser todos únicos
    public Entity getEntity(int id) {
        Entity temp = null; //inicia com "ponteiro" vazio
        for(int i = 0; i < entitySets.size(); i++) { //vai em todas as entitysets
            temp = entitySets.get(i).findEntity(id); //procura e passa pro ponteiro
        }
        return temp;
    }

    //o createResource(name, quantity):id -- revisar. Precisa retornar ID??
    public Resource createResource(String name, int id, int quantity) {
        Resource newResource = new Resource(name, id, quantity);
        return newResource;
    }

    public Resource getResource(int id) {
        Resource result = null;
        for(int i = 0; i < resources.size(); i++) {
            if(resources.get(i).getId() == id) result = resources.get(i);
        }
        return result;
    }

    //o createProcess(name, duration): processId -- revisar. Precisa retornar ID??
    public Process createProcess(String name, int id, double duration) {
        Process newProcess = new Process(name, duration);
        newProcess.setProcessId(id);
        return newProcess;
    }

    //o createEvent(name): eventId -- revisar. Precisa retornar ID??
    public Event createEvent(String name, int id) {
        Event newEvent = new Event(name);
        newEvent.setEventId(id);
        return newEvent;
    }

    public Event getEvent(int id) {
        Event result = null;
        for(int i = 0; i < events.size(); i++) {
            if(events.get(i).getEventId() == id) result = events.get(i);
        }
        return result;
    }

    //o createEntitySet(name, mode, maxPossibleSize): id -- revisar. Precisa retornar ID??
    public EntitySet createEntitySet(String name, int id, String mode, int maxPossibleSize) {
        EntitySet newEntitySet = new EntitySet(name, mode, maxPossibleSize);
        newEntitySet.setEntitySetId(id);
        return newEntitySet;
    }

    public EntitySet getEntitySet(int id) {
        EntitySet result = null;
        for(int i = 0; i < entitySets.size(); i++) {
            if(entitySets.get(i).getEntitySetId() == id) result = entitySets.get(i);
        }
        return result;
    }

    //random variates

    public double uniform(double minValue, double maxValue) {
        return minValue + (maxValue - minValue) * fRandom.nextDouble();
    }

    public double exponential(double meanValue) {
        return - (Math.log(fRandom.nextDouble()) / meanValue);
    }

    public double normal(double meanValue, double stdDeviationValue) {
        return meanValue + fRandom.nextGaussian()*stdDeviationValue;
    }

    //coleta de estatísticas

    public int getEntityTotalQuantity() {
        int totalEntities = 0;
        for(int i = 0; i < entitySets.size(); i++) {
            totalEntities += entitySets.get(i).getSize();
        }
        return totalEntities;
    }

    public int getEntityTotalQuantityNamed(String name) {
        int totalEntities = 0;
        for(int i = 0; i < entitySets.size(); i++) {
            totalEntities += entitySets.get(i).countEntity(name);
        }
        return totalEntities;
    }

    public double averageTimeInModel() {
        double average = 0;
        int amount = 0;
        for(int i = 0; i < entitySets.size(); i++) {
            average += entitySets.get(i).averageTimeInSet(this.getTime());
            amount++;
        }
        return average/amount;
    }

    public int maxEntitiesPresent() {
        int max = 0;
        for(int i = 0; i < entitySets.size(); i++) {
            max += entitySets.get(i).getMaxPossibleSize();
        }
        return max;
    }

}
