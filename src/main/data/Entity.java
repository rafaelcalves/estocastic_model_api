package main.data;

public class Entity {

    private String name;
    private int id;
    private double creationTime;
    private int priority = -1; //sem prioridade = -1, mais alta = 0, mais baixa = 255
    //PetriNet net;

    public Entity (String name) { this.name = name; }

    /*public Entity (String name, PetriNet net) {
    this.name = name;
    this.net = net;
    }*/

    public int getId() { return id; }

    public int getPriority() {
        if (priority < 0 || priority > 255) priority = -1; //deve estar entre 0 e 255
        return priority;
    }

    public void setPriority(int priority) { this.priority = priority; }

    //TODO implementar getTimeSinceCreation
    public double getTimeSinceCreation() {
         return 1.0;
    }

    //public int getSets(EntitySet list) {  }

    //public int getPetriNet() { return net; }

    //public void setPetriNet(PetriNet net) { this.net = net; }

}
