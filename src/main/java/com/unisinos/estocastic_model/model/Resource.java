package com.unisinos.estocastic_model.model;

import java.util.ArrayList;

public class Resource {

    private String name;
    private int id;
    private int quantity; //quantidade de recursos disponíveis
    private int totalQuantity; //total para controle de rates
    private double allocationTime; //para controle de tempo de alocação
    private double currentAllocate; //para controle do tempo de alocação - ultima vez alocado
    //reduz tempo atual da última vez que alocou para saber tempo de alocação
    private ArrayList<Double> allocationRatesTimes; //para a função de average da alocação (tempo)
    private ArrayList<Double> allocationRatesAverages; //para a função de average da alocação (quantidade)

    public Resource (String name, int id, int quantity) {
        this.name = name;
        this.id = id;
        this.quantity = quantity;
        this.totalQuantity = quantity;
    }

    //true se conseguiu alocar os recursos
    public boolean allocate (int quantity, double time) {
       if(quantity <= this.quantity) {
           this.quantity -= quantity;
           //se não tem nada alocado, seta tempo alocado para esse atual instante
           if (currentAllocate == 0) currentAllocate = time;
           return true; //sinal para o log?
       }
       else return false; //sinal para o log?
    }

    public void release (int quantity, double time) {
        this.quantity += quantity; //sinal para o log?
        if(quantity >= totalQuantity) {
            totalQuantity = quantity; //permite expandir a quantidade total, se release for maior que total
            //quando desaloca todos, recalcula
            //o recálculo é feito considerando o último instante em que foi alocado e o tempo atual
            allocationRatesTimes.add(allocationRate(time, currentAllocate));
            currentAllocate = 0; //e zera o tempo de alocação para parar de considerar até alocar de novo
        }
    }

    //coleta de estatísticas

    public double allocationRate(double currentTime, double currentAllocate) {
        /*percentual do tempo (em relação ao tempo total simulado) em que estes recursos foram alocados
         em 10 seg, durante 2 seg houveram recursos alocados (essa taxa seria
         então de 2/10 = 0.2 recursos/seg.)*/
        allocationTime = currentTime - currentAllocate;
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

    public int getId() { return this.id; }

}
