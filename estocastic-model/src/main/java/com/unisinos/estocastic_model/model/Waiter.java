package com.unisinos.estocastic_model.model;

import com.unisinos.petrinet.models.Document;
import com.unisinos.petrinet.pflowimport.PFLOWImporter;
import com.unisinos.petrinet.services.CycleService;

import javax.xml.bind.JAXBException;

public class Waiter {
    public static final String CASHIER_NEEDS_REPLACE = "2";
    public static final String CASHIER_RETURNS = "6";
    public static final String ORDER_READY = "3";
    public static final String ORDER_ARRIVES = "8";
    public static final String CUSTOMER_GOING_TO_SEAT = "4";
    public static final String TABLE_CLEANED = "10";

    private final PFLOWImporter importer = new PFLOWImporter();
    private final CycleService cycleService = new CycleService();

    private Document document;

    public Waiter() throws JAXBException {
        document = importer.importPflow("waiter.pflow");
    }

    public void runNetCycle() {
        cycleService.runCycles(document,1);
    }

    public boolean isWaiterAvailable() {
        return document.isFinished();
    }

    public void addCashierReplacement(){
        allocateToken(CASHIER_NEEDS_REPLACE,1);
    }

    public void addCashierReturn(){
        allocateToken(CASHIER_RETURNS,1);
    }

    public void addOrderReady(){
        allocateToken(ORDER_READY,1);
    }

    public void addOrderArrives(){
        allocateToken(ORDER_ARRIVES,1);
    }

    public void addCCustomerGoingToSeat(){
        allocateToken(CUSTOMER_GOING_TO_SEAT,1);
    }

    public void addTableCleaned(){
        allocateToken(TABLE_CLEANED,1);
    }

    private void allocateToken(String id, int tokens) {
        document.setPlaceTokenById(id, tokens);
    }

}
