package main.data;

import java.util.ArrayList;

public class EntitySet {

    private String name;
    private int id;
    private String mode = "None"; //suportados: "FIFO","LIFO","Priority based" e "None"
    private int size = 0;
    private int maxPossibleSize = 0; //0 = ilimitado
    private ArrayList<Entity> entities;

    public EntitySet(String name, String mode, int maxPossibleSize) {
        this.name = name;
        this.mode = mode;
        this.maxPossibleSize = maxPossibleSize;

        //correção de modo na inicialização
        if (!mode.matches("(?i)FIFO|LIFO|Priority based|None")) {
            this.mode = "None";
        }

        //correção de tamanho na inicialização
        if (maxPossibleSize < 0) {
            this.maxPossibleSize = 0;
        }
    }

    public String getMode() { return mode; }

    public void setMode(String mode) { this.mode = mode; }

    public void insert(Entity entity) {
        if (!this.isFull()) {
            this.entities.add(entity);
            this.size++;
        }
    }

    public Entity remove() {
        Entity output = null;
        if (!this.entities.isEmpty()) {
            output = this.entities.get(this.entities.size()-1);
            this.entities.remove(this.entities.size()-1);
            this.size--;
        }
        return output;
    }

    //public void removeById(int id) {  }

    public boolean isEmpty() {
        return this.entities.isEmpty();
    }

    public boolean isFull() {
        if(this.entities.size() == this.maxPossibleSize) return true;
        else return false;
    }

    //public Entity findEntity(int id) {  }

    //public double averageSize() {  }

    public int getSize() { return size; }

    public int getMaxPossibleSize() { return maxPossibleSize; }

    public void setMaxPossibleSize(int maxPossibleSize) { this.maxPossibleSize = maxPossibleSize; }

    //public double averageSize() {  }

    //public double averageTimeInSet() {  }

    //public double maxTimeInSet() {  }

    //public void startLog(double timeGap) {  }

    //public void stopLog() {  }

    //public Log getLog() {  }

    /*o removeById(id): Entity
    o findEntity(id): Entity  retorna referência para uma Entity, se esta estiver presente nesta EntitySet
    coleta de estatísticas
    o averageSize(): double  retorna quantidade média de entidades no conjunto
    o averageTimeInSet(): double  retorna tempo médio que as entidades permaneceram neste conjunto
    o maxTimeInSet(): double  retorna tempo mais longo que uma entidade permaneceu neste conjunto
    o startLog(timeGap)  dispara a coleta (log) do tamanho do conjunto; esta coleta é realizada a cada
    timeGap unidades de tempo
    o stopLog()
    o getLog())  retorna uma lista contendo o log deste Resource até o momento; cada elemento desta lista
    é um par <tempoAbsoluto, tamanhoConjunto>*/
}
