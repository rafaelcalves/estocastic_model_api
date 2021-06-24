package com.unisinos.estocastic_model.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class Scheduler {

    public static final String FILA_CAIXA_1 = "filaCaixa1";
    public static final String FILA_CAIXA_2 = "filaCaixa2";
    public static final String FILA_MESA_2 = "filaMesa2";
    public static final String FILA_MESA_4 = "filaMesa4";
    public static final String FILA_BALCAO = "filaBalcao";
    public static final String FILA_PEDIDO = "filaPedido";
    private double time;
    private ArrayList<EntitySet> entitySets;
    private ArrayList<Event> events;
    private ArrayList<Process> processes;
    private ArrayList<Resource> resources;
    private int idIterator;
    private List<PetriNet> waiters;

    private Random fRandom = new Random(); //para cálculos usando random (distribuições)

    void init(){
        entitySets.add(createEntitySet(FILA_CAIXA_1, getNextId(), "FIFO", 0));
        entitySets.add(createEntitySet(FILA_CAIXA_2, getNextId(), "FIFO", 0));
        entitySets.add(createEntitySet(FILA_BALCAO,getNextId(), "FIFO", 0));
        entitySets.add(createEntitySet(FILA_MESA_2,getNextId(), "FIFO", 0));
        entitySets.add(createEntitySet(FILA_MESA_4,getNextId(), "FIFO", 0));
        entitySets.add(createEntitySet(FILA_PEDIDO,getNextId(), "FIFO",0));
    }

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

    public void runProcess(Process process) {
        double result = 0;
        switch(process.getName()) {
            case "chegada":
                runChegada(process);
                break;
            case "pedido":
                runPedido(process);
                break;
            case "preparo":
                runPreparo(process);
                break;
            case "refeição":
                //result = normal(20, 8);
                //runConsumo();
                //vai precisar receber o entity/pedido para saber qual remover e de onde?
                break;
            case "banheiro":
                //result = uniform(120, 180);
                break;
            default:
                break;
        }
    }

    //só cria um consumo quando pronto?
    private void runPreparo(Process process)
    {
        Event consumir = createEvent("refeição", getNextId());
        Process consumo = createProcess(consumir.getName(), getNextId(), mapEventDuration(consumir));
        processes.add(consumo);
    }

    private void runPedido(Process process)
    {
        EntitySet filaCaixa = getEntitySetByName(process.getEntitySetName());
        Entity customer = filaCaixa.getFirst();
        filaCaixa.releaseFirst();

        EntitySet pedidos = getEntitySetByName(FILA_PEDIDO);
        Entity pedido = createEntity("pedido", getNextId(),0,0);
        customer.setIdPedido(pedido.getId());
        pedidos.insert(pedido);

        switch (customer.getQuantity()){
            case 1:
                EntitySet filaBalcao = getEntitySetByName(FILA_BALCAO);
                filaBalcao.insert(customer);
                break;
            case 2:
                EntitySet filaMesa2 = getEntitySetByName(FILA_MESA_2);
                filaMesa2.insert(customer);
                break;
            default:
                EntitySet filaMesa4 = getEntitySetByName(FILA_MESA_4);
                filaMesa4.insert(customer);
                break;
        }
        int resources = 3;
        String processName = "preparo";
        triggerPedido(resources, processName);
    }

    private void runChegada(Process process)
    {
        int customers =  1 + ((int)Math.random()) * 3;
        Entity entity = createEntity( "cliente", getNextId(), 0, customers);
        EntitySet filaCaixa1 = getEntitySetByName(FILA_CAIXA_1);
        EntitySet filaCaixa2 = getEntitySetByName(FILA_CAIXA_2);
        if(filaCaixa1.getSize() < filaCaixa2.getSize()) {
            filaCaixa1.insert(entity);
            process.setEntitySetName(filaCaixa1.getName());
        }else {
            filaCaixa2.insert(entity);
            process.setEntitySetName(filaCaixa2.getName());
        }

        int resources = 2;
        String processName = "pedido";
        triggerPedido(resources, processName);
    }

    private void triggerPedido(int resources, String processName)
    {
        List<Process> pedidos = processes.stream()
              .filter(pedido -> pedido.getName().equals(processName))
              .collect(Collectors.toList());

        if(pedidos.size() < resources)
        {
            Event eventPedido = createEvent(processName, getNextId());
            Process processPedido = createProcess(processName, getNextId(), mapEventDuration(eventPedido));
            processes.add(processPedido);
        }
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
        if(events.isEmpty()){
            Event chegada = createEvent("chegada", idIterator++);
            events.add(chegada);
        } else {
            Event event = events.get(0);
            createProcess(event.getName(), getNextId(), mapEventDuration(event));
        }

        processes.forEach(process -> {
            if(process.getTimeTo()>0) process.setTimeTo(process.getTimeTo() - 1);
            if(process.getTimeTo() == 0 && process.getDuration()>0) process.setDuration(process.getDuration() - 1);
            if(process.getDuration() == 0){
                runProcess(process);
                destroyProcess(process);
            }
        });
    }

    public void simulateBy(double duration) {  }

    public void simulateUntil(double absoluteTime) {  }

    //criação, destruição e acesso para componentes

    public Entity createEntity(String name, int id, int priority, int quantity) {
        Entity newEntity = new Entity(name, id, this.getTime(), quantity);
        newEntity.setPriority(priority);
        return newEntity;
    }

    //a princípio IDs vão ser todos únicos
    public void destroyEntity(int id) {
        for(int i = 0; i < entitySets.size(); i++) { //vai em todos os entitysets
            entitySets.get(i).removeById(id); //procura e remove
        }
    }
    public void destroyProcess(Process process) {
        for(int i = 0; i < processes.size(); i++) { //vai em todos os entitysets
            if(processes.get(i).getProcessId() == process.getProcessId()) processes.remove(i); //procura e remove
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

    public EntitySet getEntitySetByName(String name) {
        for(int i = 0; i < entitySets.size(); i++) {
            if(entitySets.get(i).getName().equals(name)) return entitySets.get(i);
        }
        return null;
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

    public int getNextId()
    {
        return idIterator++;
    }
}
