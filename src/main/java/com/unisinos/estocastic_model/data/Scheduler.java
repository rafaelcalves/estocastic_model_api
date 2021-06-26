package com.unisinos.estocastic_model.data;

import com.unisinos.estocastic_model.data.entities.*;
import com.unisinos.estocastic_model.data.processes.EventType;
import com.unisinos.estocastic_model.data.processes.Process;
import com.unisinos.estocastic_model.data.processes.ProcessFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Scheduler {

    public static final int ORDER_PREPARATION_LIMIT = 3;
    public static final int CASHIER_ATTENDANCE_LIMIT = 2;
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
        entitySets.add(entitySetFactory.create(EntitySetType.CAIXA_1, getNextId()));
        entitySets.add(entitySetFactory.create(EntitySetType.CAIXA_2, getNextId()));
        entitySets.add(entitySetFactory.create(EntitySetType.BALCAO, getNextId()));
        entitySets.add(entitySetFactory.create(EntitySetType.MESA_2, getNextId()));
        entitySets.add(entitySetFactory.create(EntitySetType.MESA_4, getNextId()));
        entitySets.add(entitySetFactory.create(EntitySetType.PEDIDO, getNextId()));
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
                //vai precisar receber o entity/pedido para saber qual remover e de onde?
                break;
            case WC:
                break;
            default:
                break;
        }
    }

    private void runConsumption(Process process){

    }

    //só cria um consumo quando pronto?
    private void runPreparation(Process process)
    {
        EntitySet filaPedido = getEntitySetByType(EntitySetType.PEDIDO);
        Entity pedido = filaPedido.getFirst();

        Process consumo = processFactory.create(EventType.CONSUMPTION, getNextId());
        processes.add(consumo);
    }

    private void runOrder(Process process)
    {
        EntitySet filaCaixa = getEntitySetByType(process.getEntitySetType());
        CustomerEntity customer = (CustomerEntity)filaCaixa.getFirst();
        filaCaixa.releaseFirst();

        EntitySet pedidos = getEntitySetByType(EntitySetType.PEDIDO);
        Entity pedido = entityFactory.createOrder(getNextId(),getTime());
        customer.setOrderId(pedido.getId());
        pedidos.insert(pedido);

        switch (customer.getQuantity()){
            case 1:
                EntitySet filaBalcao = getEntitySetByType(EntitySetType.BALCAO);
                filaBalcao.insert(customer);
                break;
            case 2:
                EntitySet filaMesa2 = getEntitySetByType(EntitySetType.MESA_2);
                filaMesa2.insert(customer);
                break;
            default:
                EntitySet filaMesa4 = getEntitySetByType(EntitySetType.MESA_4);
                filaMesa4.insert(customer);
                break;
        }
        triggerOrder(ORDER_PREPARATION_LIMIT, EventType.PREPARATION);
    }

    private void runArrival(Process process)
    {
        int customers =  1 + ((int)Math.random()) * 3;
        CustomerEntity entity = entityFactory.createCustomer(getNextId(), 0, customers);
        EntitySet filaCaixa1 = getEntitySetByType(EntitySetType.CAIXA_1);
        EntitySet filaCaixa2 = getEntitySetByType(EntitySetType.CAIXA_2);
        if(filaCaixa1.getSize() < filaCaixa2.getSize()) {
            filaCaixa1.insert(entity);
            process.setEntitySetType(filaCaixa1.getType());
        }else {
            filaCaixa2.insert(entity);
            process.setEntitySetType(filaCaixa2.getType());
        }

        triggerOrder(CASHIER_ATTENDANCE_LIMIT, EventType.ORDER);
    }

    private void triggerOrder(int resources, EventType eventType)
    {
        List<Process> pedidos = processes.stream()
              .filter(pedido -> pedido.getEventType().equals(eventType))
              .collect(Collectors.toList());

        if(pedidos.size() < resources)
        {
            Process processPedido = processFactory.create(eventType, getNextId());
            processes.add(processPedido);
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
                destroyProcess(process);
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
