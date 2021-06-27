package com.unisinos.estocastic_model.model;

import com.unisinos.petrinet.models.Document;
import com.unisinos.petrinet.services.CycleService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PetriNet {
    //Fazer baseada nos elementos básicos possíveis (models) conforme trabalho do GA (Net em particular)???

    private Document document;

    public PetriNet() {  }

    private void runNet() {
        ExecutorService service = Executors.newFixedThreadPool(1);
        service.submit(() -> {
            CycleService cycleService = new CycleService();
            cycleService.getCyclesQuantityToFinish(document);
        });
    }

    private void runNetFor(double duration) {
        runNet();
    }

    private boolean getCallback() {
        return document.isFinished();
    }

    //pega id da location na rede e coloca token
    private void allocateToken(String id, int tokens) {
        document.setPlaceTokenById(id, tokens);
    }

    /*Talvez gets e sets para os places em particular com os quais o modelo irá interagir
    (da rede importada do garçom) ou até mesmo a completa importação do projeto do GA para controlar a rede em
    "paralelo"*/
}
