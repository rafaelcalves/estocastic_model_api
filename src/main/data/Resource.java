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
    public boolean allocate (int quantity) {
       if(quantity <= this.quantity) {
           this.quantity -= quantity;
           return true; //sinal para o log?
       }
       else return false; //sinal para o log?
    }

    /*pensei em adicionar tratamento pra não passar de um limite (int maxQuantity), mas creio que vai poder aumentar
    e logo, controle estrito é desnecessário/inútil e release pode ser usado para adicionar mais resources além
    de recoletar os que estão em uso*/
    public void release (int quantity) {
        this.quantity += quantity; //sinal para o log?
    }

    //coleta de estatísticas

    public double allocationRate(double currentTime) {
        /*percentual do tempo (em relação ao tempo total simulado) em que estes recursos foram alocados
         em 10 seg, durante 2 seg houveram recursos alocados (essa taxa seria
         então de 2/10 = 0.2 recursos/seg.)*/
        return 1.0;
    }

    public double averageAllocation(double CurrentTime) {
        /* quantidade média destes recursos que foram alocados (em relação ao tempo total simulado)
           Ex.: ao longo de 10 seg., houveram 5 unidades deste recurso alocado (esta quant. média
           ficaria 5/10 = 0.5 recursos) */
        return 1.0;
    }
}
