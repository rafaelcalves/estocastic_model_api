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
    private double lastLog;
    private List<EntitySet> entitySets;
    private List<Process> processes;
    private int idIterator;
    private List<Waiter> waiters;
    private ProcessFactory processFactory;
    private EntitySetFactory entitySetFactory;
    private EntityFactory entityFactory;

    private int totalCustomers;

    public Scheduler(){
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
        newSets.add(entitySetFactory.create(EntitySetType.CASHIER_1, getNextId()));
        newSets.add(entitySetFactory.create(EntitySetType.CASHIER_2, getNextId()));
        newSets.add(entitySetFactory.create(EntitySetType.BAR_COUNTER, getNextId()));
        newSets.add(entitySetFactory.create(EntitySetType.TABLE_2, getNextId()));
        newSets.add(entitySetFactory.create(EntitySetType.TABLE_4, getNextId()));
        newSets.add(entitySetFactory.create(EntitySetType.ORDER, getNextId()));
        return newSets;
    }

    public double getTime() { return this.time; }

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
        tableSet.releaseFirst(getTime());
        destroyProcess(consumption);
    }

    private void runPreparation(Process process)
    {
        EntitySet ordersLine = getEntitySetByType(EntitySetType.ORDER);
        Entity order = ordersLine.getFirst();

        CustomerEntity customer = startConsumption(order);
        if(customer!=null) {
            destroyProcess(process);
            ordersLine.releaseFirst(getTime());
        }
    }

    private CustomerEntity startConsumption(Entity order) {
        CustomerEntity customer = popCustomerFromEntitySetByOrderAndType(order, EntitySetType.BAR_COUNTER);
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

    private void triggerConsumption(EntitySetType tableType, CustomerEntity customer) {
        Process consumption = processFactory.createConsumption(getNextId(), tableType, customer.getQuantity());
        processes.add(consumption);
    }

    private CustomerEntity popCustomerFromEntitySetByOrderAndType(Entity order, EntitySetType entitySetType) {
        Optional<CustomerEntity> optionalCustomer;
        optionalCustomer = getCustomerByOrder(order, entitySetType);
        if(optionalCustomer.isPresent() && canTriggerConsumption(entitySetType)) {
            triggerConsumption(entitySetType, optionalCustomer.get());
            return optionalCustomer.map(customerEntity -> popFromEntitySet(customerEntity, entitySetType)).orElse(null);
        }
        return null;
    }

    private CustomerEntity popFromEntitySet(CustomerEntity customer, EntitySetType entitySetType) {
        EntitySet entitySet = getEntitySetByType(entitySetType);
        entitySet.remove(customer, getTime());
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
        cashierLine.releaseFirst(getTime());

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
        customer.getTimeSinceEntrance(getTime());
        triggerOrder(EventType.PREPARATION);
        destroyProcess(process);
    }

    private void runArrival(Process process)
    {
        int customers =  1 + (int)(Math.random() * 3);
        totalCustomers+=customers;
        CustomerEntity entity = entityFactory.createCustomer(getNextId(), getTime(), customers);
        EntitySet cashier1Line = getEntitySetByType(EntitySetType.CASHIER_1);
        EntitySet cashier2Line = getEntitySetByType(EntitySetType.CASHIER_2);
        EntitySetType smallerEntitySetType;
        if(cashier1Line.getSize() < cashier2Line.getSize()) {
            cashier1Line.insert(entity);
            process.setEntitySetType(cashier1Line.getType());
            smallerEntitySetType = EntitySetType.CASHIER_1;
        }else {
            cashier2Line.insert(entity);
            process.setEntitySetType(cashier2Line.getType());
            smallerEntitySetType = EntitySetType.CASHIER_1;
        }

        triggerOrderFromArrival(smallerEntitySetType);
        destroyProcess(process);
    }

    private Process triggerOrder(EventType eventType)
    {
        List<Process> orders = processes.stream()
              .filter(order -> order.getEventType().equals(eventType))
              .collect(Collectors.toList());

        Process createdProcess = null;
        if(orders.size() < eventType.getResources())
        {
            createdProcess = processFactory.create(eventType, getNextId());
            processes.add(createdProcess);
        }
        return createdProcess;
    }

    private void triggerOrderFromArrival(EntitySetType entitySetType) {
        Process process = triggerOrder(EventType.ORDER);
        if(process!=null) process.setEntitySetType(entitySetType);
    }

    public void destroyProcess(Process process) {
        for(int i = 0; i < processes.size(); i++) { //vai em todos os entitysets
            if(processes.get(i).getId() == process.getId()) processes.remove(i); //procura e remove
        }
    }

    public EntitySet getEntitySetByType(EntitySetType type) {
        for(int i = 0; i < entitySets.size(); i++) {
            if(entitySets.get(i).getType().equals(type)) return entitySets.get(i);
        }
        return null;
    }

    public int getNextId()
    {
        return idIterator++;
    }

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
        if(processes.stream().noneMatch(process -> process.getEventType().equals(EventType.ARRIVAL))) {
            processes.add(processFactory.create(EventType.ARRIVAL, getNextId()));
        }

        List<Process> processesSnapshot = new ArrayList<>(processes);

        processesSnapshot.forEach(process -> {
            if(process.getTimeTo()>0) process.setTimeTo(process.getTimeTo() - 1);
            if(process.getTimeTo() <= 0 && process.getDuration()>0) process.setDuration(process.getDuration() - 1);
            if(process.getDuration() <= 0){
                runProcess(process);
            }
        });
        if(time == lastLog + Constants.LOG_PERIOD)log();
    }

    public void log(){
        logSizes();
        //Not needed since removing should do it.
        //logTimes();
        lastLog = getTime();
    }

    public void logSizes() {
        entitySets.forEach(EntitySet::logSize);
    }

    public void logTimes() {
        List<Entity> temp;
        for(int setsIterator = 0; setsIterator < entitySets.size(); setsIterator++) {
            temp = entitySets.get(setsIterator).getEntities();
            for(int entitiesIterator = 0; entitiesIterator < temp.size(); entitiesIterator++) {
                entitySets.get(setsIterator).logTime(temp.get(entitiesIterator), getTime());
            }
        }
    }

    public void simulateBy(double duration) {
        double timeFrame = this.getTime() + duration;
        while(this.getTime() < timeFrame) {
            simulateOneStep();
            this.time++;
        }
    }

    public void simulateUntil(double absoluteTime) {
        while(this.getTime() < absoluteTime) {
            simulateOneStep();
            this.time++;
        }
    }

    //coleta de estatísticas

    public int getTotalCustomers() {
        return totalCustomers;
    }

    public int getConsumptionCustomersQuantity(){
        return processes.stream()
            .filter(process -> process instanceof ConsumptionProcess)
            .map(process -> ((ConsumptionProcess)process).getCustomersQuantity())
            .reduce(0, Integer::sum);
    }

}
