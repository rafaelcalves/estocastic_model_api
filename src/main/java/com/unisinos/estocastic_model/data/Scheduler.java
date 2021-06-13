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

    public void simulateBy(double duration) {  }

    public void simulateUntil(double absoluteTime) {  }

    //criação, destruição e acesso para componentes

    public void createEntity(Entity entity) {  }

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

    //coleta de estatísticas

    //public double uniform(double minValue, double maxValue) {  }

    //public double exponential(double meanValue) {  }

    //public double normal(double meanValue, double stdDeviationValue) {  }

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
