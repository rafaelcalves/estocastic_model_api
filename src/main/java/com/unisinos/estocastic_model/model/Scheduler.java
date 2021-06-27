package com.unisinos.estocastic_model.model;

import com.unisinos.estocastic_model.model.entities.*;
import com.unisinos.estocastic_model.model.processes.ConsumptionProcess;
import com.unisinos.estocastic_model.model.processes.EventType;
import com.unisinos.estocastic_model.model.processes.Process;
import com.unisinos.estocastic_model.model.processes.ProcessFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class Scheduler {

    private double time;
    private List<EntitySet> entitySets;
    private List<Process> processes;
    private List<Resource> resources;
    private int idIterator;
    private List<PetriNet> waiters;
    private ProcessFactory processFactory;
    private EntitySetFactory entitySetFactory;
    private EntityFactory entityFactory;

    Scheduler(){
        init();
    }

    void init(){
        processFactory = new ProcessFactory();
        entitySetFactory = new EntitySetFactory();
        entityFactory = new EntityFactory();
        entitySets = createEntitySets();
        processes = new ArrayList<>();
    }

    public List<EntitySet> createEntitySets(){
        List<EntitySet> newSets = new ArrayList<>();
        entitySets.add(entitySetFactory.create(EntitySetType.CASHIER_1, getNextId()));
        entitySets.add(entitySetFactory.create(EntitySetType.CASHIER_2, getNextId()));
        entitySets.add(entitySetFactory.create(EntitySetType.BAR_COUNTER, getNextId()));
        entitySets.add(entitySetFactory.create(EntitySetType.TABLE_2, getNextId()));
        entitySets.add(entitySetFactory.create(EntitySetType.TABLE_4, getNextId()));
        entitySets.add(entitySetFactory.create(EntitySetType.ORDER, getNextId()));
        return newSets;
    }

    //pega o tempo do exato instante atual
    public double getTime() { return this.time; }

    //dependendo do tipo de evento, calcula tempo para iniciar

    public void runProcess(Process process) {
        switch(process.getEventType()) {
            case ARRIVAL:
                runArrival(process);
                break;
            case ORDER:
                runOrder(process);
                break;
            case PREPARATION:
                runPreparation(process);
                break;
            case CONSUMPTION:
                runConsumption(process);
                break;
            default:
                break;
        }
    }

    private void runConsumption(Process process){
        ConsumptionProcess consumption = (ConsumptionProcess) process;
        EntitySet tableSet = getEntitySetByType(consumption.getTableType());
        tableSet.releaseFirst();
        destroyProcess(consumption);
    }

    private void runPreparation(Process process)
    {
        EntitySet ordersLine = getEntitySetByType(EntitySetType.ORDER);
        Entity order = ordersLine.getFirst();

        CustomerEntity customer = startConsumption(order);
        if(customer!=null) {
            destroyProcess(process);
            ordersLine.releaseFirst();
        }
    }

    private CustomerEntity startConsumption(Entity order) {
        EntitySetType entitySetType = EntitySetType.BAR_COUNTER;
        CustomerEntity customer = popCustomerFromEntitySetByOrderAndType(order, entitySetType);
        if(customer!=null) return customer;
        customer = popCustomerFromEntitySetByOrderAndType(order, EntitySetType.TABLE_2);
        if(customer!=null) return customer;
        return popCustomerFromEntitySetByOrderAndType(order, EntitySetType.TABLE_4);
    }

    private boolean canTriggerConsumption(EntitySetType tableType) {
        List<Process> filteredProcesses = processes.stream()
                .filter(process -> process instanceof ConsumptionProcess)
                .map(ConsumptionProcess.class::cast)
                .filter(consumptionProcess -> consumptionProcess.getTableType().equals(tableType))
                .collect(Collectors.toList());
        return filteredProcesses.size() < tableType.getResources();
    }

    private void triggerConsumption(EntitySetType tableType) {
        Process consumption = processFactory.createConsumption(getNextId(), tableType);
        processes.add(consumption);
    }

    private CustomerEntity popCustomerFromEntitySetByOrderAndType(Entity order, EntitySetType entitySetType) {
        Optional<CustomerEntity> optionalCustomer;
        optionalCustomer = getCustomerByOrder(order, entitySetType);
        if(canTriggerConsumption(entitySetType)) {
            triggerConsumption(entitySetType);
            return optionalCustomer.map(customerEntity -> popFromEntitySet(customerEntity, entitySetType)).orElse(null);
        }
        return null;
    }

    private CustomerEntity popFromEntitySet(CustomerEntity customer, EntitySetType entitySetType) {
        EntitySet entitySet = getEntitySetByType(entitySetType);
        entitySet.remove(customer);
        return customer;
    }

    private Optional<CustomerEntity> getCustomerByOrder(Entity order, EntitySetType entitySetType) {
        return getEntitySetByType(entitySetType).getEntities()
                .stream()
                .filter(entity -> entity instanceof CustomerEntity)
                .map(CustomerEntity.class::cast)
                .filter(customerEntity -> customerEntity.getOrderId() == order.getId())
                .findFirst();
    }

    private void runOrder(Process process)
    {
        EntitySet cashierLine = getEntitySetByType(process.getEntitySetType());
        CustomerEntity customer = (CustomerEntity)cashierLine.getFirst();
        cashierLine.releaseFirst();

        EntitySet orders = getEntitySetByType(EntitySetType.ORDER);
        Entity order = entityFactory.createOrder(getNextId(),getTime());
        customer.setOrderId(order.getId());
        orders.insert(order);

        switch (customer.getQuantity()){
            case 1:
                EntitySet barCounterLine = getEntitySetByType(EntitySetType.BAR_COUNTER);
                barCounterLine.insert(customer);
                break;
            case 2:
                EntitySet tableFor2Line = getEntitySetByType(EntitySetType.TABLE_2);
                tableFor2Line.insert(customer);
                break;
            default:
                EntitySet tableFor4Line = getEntitySetByType(EntitySetType.TABLE_4);
                tableFor4Line.insert(customer);
                break;
        }
        triggerOrder(EventType.PREPARATION);
        destroyProcess(process);
    }

    private void runArrival(Process process)
    {
        int customers =  1 + ((int)Math.random()) * 3;
        CustomerEntity entity = entityFactory.createCustomer(getNextId(), 0, customers);
        EntitySet cashier1Line = getEntitySetByType(EntitySetType.CASHIER_1);
        EntitySet cashier2Line = getEntitySetByType(EntitySetType.CASHIER_2);
        if(cashier1Line.getSize() < cashier2Line.getSize()) {
            cashier1Line.insert(entity);
            process.setEntitySetType(cashier1Line.getType());
        }else {
            cashier2Line.insert(entity);
            process.setEntitySetType(cashier2Line.getType());
        }

        triggerOrder(EventType.ORDER);
        destroyProcess(process);
    }

    private void triggerOrder(EventType eventType)
    {
        List<Process> orders = processes.stream()
              .filter(order -> order.getEventType().equals(eventType))
              .collect(Collectors.toList());

        if(orders.size() < eventType.getResources())
        {
            Process processOrder = processFactory.create(eventType, getNextId());
            processes.add(processOrder);
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
        if(processes.isEmpty()){
            processFactory.create(EventType.ARRIVAL, getNextId());
        }

        processes.forEach(process -> {
            if(process.getTimeTo()>0) process.setTimeTo(process.getTimeTo() - 1);
            if(process.getTimeTo() == 0 && process.getDuration()>0) process.setDuration(process.getDuration() - 1);
            if(process.getDuration() == 0){
                runProcess(process);
            }
        });
    }

    public void simulateBy(double duration) {  }

    public void simulateUntil(double absoluteTime) {  }

    //criação, destruição e acesso para componentes

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

    public EntitySet getEntitySetByType(EntitySetType type) {
        for(int i = 0; i < entitySets.size(); i++) {
            if(entitySets.get(i).getType().equals(type)) return entitySets.get(i);
        }
        return null;
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
