package com.unisinos.estocastic_model.data;

import java.util.ArrayList;
import java.util.Random;

public class Scheduler {

    private double time;
    private ArrayList<EntitySet> entitySets;
    private ArrayList<Event> events;
    private ArrayList<Process> processes;

    private Random fRandom = new Random(); //para cálculos

    public double getTime() { return time; }

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

    public void scheduleNow(Event event) {
        Process newProcess = new Process(event.getName(), mapEventDuration(event));
        newProcess.activate(true);
        processes.add(newProcess);
    }

    public void scheduleIn(Event event, double timeToEvent) {
        Process newProcess = new Process(event.getName(), mapEventDuration(event));
        newProcess.setTimeTo(timeToEvent);
        processes.add(newProcess);
    }

    public void scheduleAt(Event event, double absoluteTime) {
        Process newProcess = new Process(event.getName(), mapEventDuration(event));
        newProcess.setTimeTo(absoluteTime - time);
        processes.add(newProcess);
    }

    public void startProcessNow(int processId) {

    }

    public void startProcessIn(int processId, double timeToEvent) {

    }

    public void startProcessAt(int processId, double absoluteTime) {

    }

    public void waitFor(double time) {
        //se a abordagem para especificação da passagem de tempo nos processos for explícita
        //    controlando tempo de execução
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

    public void createEntity(String name, int id, int priority, PetriNet net) {

    }

    public void destroyEntity(int id) {  }

    //public Entity getEntity(int id) {  }

    //o createResource(name, quantity):id
    //public int createResource(String name, int quantity) {  }

    //public Resource getResource(int id) {  }

    //o createProcess(name, duration): processId
    //public int createProcess(String name, double duration) {  }

    //o createEvent(name): eventId
    //public int createEvent(String name) {  }

    //public Event getEvent(int id) {  }

    //o createEntitySet(name, mode, maxPossibleSize): id
    //public int createEntitySet(String name, int mode, int maxPossibleSize) {  }

    //public EntitySet getEntitySet(int id) {  }

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

    //public int getEntityTotalQuantity() {  }

    //public int getEntityTotalQuantityNamed(String name) {  }

    //public double averageTimeInModel() {  }

    //public int maxEntitiesPresent() {  }

    /*coleta de estatísticas
    o getEntityTotalQuantity(): integer  retorna quantidade de entidades criadas até o momento
    o getEntityTotalQuantity(name): integer  retorna quantidade de entidades criadas cujo nome
    corresponde ao parâmetro, até o momento
    o averageTimeInModel(): double  retorna o tempo médio que as entidades permanecem no modelo,
    desde sua criação até sua destruição
    o maxEntitiesPresent():integer  retorna o número máximo de entidades presentes no modelo até o
            momento*/

}
