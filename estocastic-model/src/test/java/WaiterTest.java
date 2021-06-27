import com.unisinos.estocastic_model.model.waiters.Waiter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WaiterTest {
    private Waiter waiter;

    @Before
    public void setUp() throws Exception {
        waiter = new Waiter();
    }

    @Test
    public void whenInitializedWaiterIsAvailable() {
        waiter.runNetCycle();
        Assert.assertTrue(waiter.isAvailable());
    }

    @Test
    public void whenCashierAsksReplacementWaitingForCashier() {
        waiter.addCashierReplacement();
        waiter.runNetCycle();
        Assert.assertTrue(waiter.isWaitingCashierReturns());
    }

    @Test
    public void whenCashierReturnsWaiterIsAvailable() {
        waiter.addCashierReplacement();
        waiter.runNetCycle();
        waiter.addCashierReturn();
        waiter.runNetCycle();
        Assert.assertTrue(waiter.isAvailable());
    }
    
    @Test
    public void whenOrderIsReadyWaiterIsDelivering(){
        waiter.addOrderReady();
        waiter.runNetCycle();
        Assert.assertTrue(waiter.isDeliveringOrder());
    }

    @Test
    public void whenOrderArrivesWaiterIsAvailable() {
        waiter.addOrderReady();
        waiter.runNetCycle();
        waiter.addOrderArrives();
        waiter.runNetCycle();
        Assert.assertTrue(waiter.isAvailable());
    }

    @Test
    public void whenCustomerIsGoingToSeatWaiterIsCleaningTable() {
        waiter.addCustomerGoingToSeat();
        waiter.runNetCycle();
        Assert.assertTrue(waiter.isCleaningTable());
    }

    @Test
    public void whenTableIsCleanedWaiterIsAvailable(){
        waiter.addCustomerGoingToSeat();
        waiter.runNetCycle();
        waiter.addTableCleaned();
        waiter.runNetCycle();
        Assert.assertTrue(waiter.isAvailable());

    }
}
