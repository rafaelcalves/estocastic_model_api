import com.unisinos.estocastic_model.model.Waiter;
import org.junit.Before;
import org.junit.Test;

public class WaiterTest {
    private Waiter waiter;

    @Before
    public void setUp() throws Exception {
        waiter = new Waiter();
    }

    @Test
    public void name() {
        waiter.runNetCycle();
        waiter = waiter;
    }
}
