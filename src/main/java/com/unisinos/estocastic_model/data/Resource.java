package com.unisinos.estocastic_model.data;

import java.util.ArrayList;

public class Resource {

    private String name;
    private int id;
    private int quantity; //quantidade de recursos disponíveis
    private int totalQuantity; //total para controle de rates
    private double allocationTime; //para controle de tempo de alocação - de repente fazer um array/list ?
    private ArrayList<double> allocationRatesTimes; //para a função de average da alocação (tempo)
    private ArrayList<double> allocationRatesAverages; //para a função de average da alocação (quantia)

    public Resource (String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
        this.totalQuantity = quantity;
    }

    //true se conseguiu alocar os recursos
    public boolean allocate (int quantity) {
       if(quantity <= this.quantity) {
           this.quantity -= quantity;
           return true; //sinal para o log?
       }
       else return false; //sinal para o log?
    }

    /*quebra o galho como adição de resources também*/
    public void release (int quantity) {
        this.quantity += quantity; //sinal para o log?
        if(quantity > totalQuantity) totalQuantity = quantity;
    }

    //coleta de estatísticas

    //REVISAR!

    /*para funcionar da forma que está teria que rodar o tempo inteiro, porém o output pode ser abstraido e só
    referenciado quando necessário*/
    public double allocationRate(double currentTime) {
        /*percentual do tempo (em relação ao tempo total simulado) em que estes recursos foram alocados
         em 10 seg, durante 2 seg houveram recursos alocados (essa taxa seria
         então de 2/10 = 0.2 recursos/seg.)*/
        if(quantity != totalQuantity) {
            allocationTime++;
        }
        else allocationTime = 0;
        return allocationTime/currentTime;
    }

    public double averageAllocation(double currentTime) {
        /* quantidade média destes recursos que foram alocados (em relação ao tempo total simulado)
           Ex.: ao longo de 10 seg., houveram 5 unidades deste recurso alocado (esta quant. média
           ficaria 5/10 = 0.5 recursos) */
        double allocation = totalQuantity - quantity;
        allocationRatesAverages.add(allocation);
        return allocation/currentTime;
    }
}
