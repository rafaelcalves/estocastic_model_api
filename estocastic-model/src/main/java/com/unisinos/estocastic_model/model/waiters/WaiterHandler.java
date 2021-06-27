package com.unisinos.estocastic_model.model.waiters;

import java.util.List;

public class WaiterHandler {
    public void runWaitersCycle(List<Waiter> waiters){
        waiters.forEach(Waiter::runNetCycle);
    }

    public Waiter getWaiterWithLessTasks(List<Waiter> waiters){
        Waiter lessTasksWaiter = null;
        for (Waiter waiter : waiters) {
            if (lessTasksWaiter == null || lessTasksWaiter.getTotalPendingTasks() > waiter.getTotalPendingTasks()) {
                lessTasksWaiter = waiter;
            }
        }
        return lessTasksWaiter;
    }
}
