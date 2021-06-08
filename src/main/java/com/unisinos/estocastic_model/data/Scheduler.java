package com.unisinos.estocastic_model.data;

public class Scheduler {

    private double time;

    public double getTime() { return time; }

    public void scheduleNow(Event event) { }

    public void scheduleIn(Event event, double timeToEvent) { }

    public void scheduleAt(Event event, double absoluteTime) { }

    public void startProcessNow(int processId) { }

    public void startProcessIn(int processId, double timeToEvent) { }

    public void startProcessAt(int processId, double absoluteTime) { }

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

    //TODO
    /*
    o simulateBy(duration)
    o simulateUntil(absoluteTime)
    criação, destruição e acesso para componentes
    o createEntity(Entity)  instancia nova Entity e destroyEntity(id)
    o getEntity(id): Entity Entity  retorna referência para instância de Entity
    o createResource(name, quantity):id
    o getResource(id): Resource  retorna referência para instância de Resource
    o createProcess(name, duration): processId
    o getProcess(processId):Process  retorna referência para instancia de Process
    o createEvent(name): eventId
    o getEvent(eventId):Event  retorna referência para instancia de Event
    o createEntitySet(name, mode, maxPossibleSize): id
    o getEntitySet(id): EntitySet  retorna referência para instancia de EntitySet
    random variates
    o uniform(minValue, maxValue: double
    o exponential(meanValue): double
    o normal(meanValue, stdDeviationValue): double
    coleta de estatísticas
    o getEntityTotalQuantity(): integer  retorna quantidade de entidades criadas até o momento
    o getEntityTotalQuantity(name): integer  retorna quantidade de entidades criadas cujo nome
    corresponde ao parâmetro, até o momento
    o averageTimeInModel(): double  retorna o tempo médio que as entidades permanecem no modelo,
    desde sua criação até sua destruição
    o maxEntitiesPresent():integer  retorna o número máximo de entidades presentes no modelo até o
    momento*/
}
