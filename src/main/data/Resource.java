package main.data;

public class Resource {

    private String name;
    private int id;
    private int quantity; //quantidade de recursos disponíveis

    public Resource (String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    //true se conseguiu alocar os recursos
    //TODO implementar verificação de alocação de recursos
    public boolean allocate (int quantity){
       return true;
    }


    public void release (int quantity) { }

    //coleta de estatísticas

    public double allocationRate() {
        /*percentual do tempo (em relação ao tempo total simulado) em que estes recursos foram alocados
         em 10 seg, durante 2 seg houveram recursos alocados (essa taxa seria
         então de 2/10 = 0.2 recursos/seg.)*/
        return 1.0;
    }

    public double averageAllocation() {
        /* quantidade média destes recursos que foram alocados (em relação ao tempo total simulado)
           Ex.: ao longo de 10 seg., houveram 5 unidades deste recurso alocado (esta quant. média
           ficaria 5/10 = 0.5 recursos) */
        return 1.0;
    }
}
