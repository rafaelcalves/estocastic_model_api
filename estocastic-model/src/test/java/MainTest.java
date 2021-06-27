import com.unisinos.estocastic_model.model.Scheduler;
import com.unisinos.estocastic_model.model.entities.EntitySetType;
import org.junit.Test;

import java.util.Scanner;

public class MainTest {
    @Test
    public void main() {

        Scheduler simulator = null;
        int option = -1;

        while(option != 3) {
            System.out.println("Simulador de restaurante. Selecione o modo: \n");
            System.out.println("(1/DEFAULT)-Simular POR X tempo; (2)-Simular ATÉ X tempo; (3)-SAIR. \n");

            Scanner input = new Scanner(System.in);
            option = input.nextInt();
            double duration = 0;
            if(option != 2 && option != 3) {
                System.out.println("Opção (1/DEFAULT). Selecione o tempo: \n");
                System.out.println("Por quanto tempo rodar (em minutos) ?\n");
                duration = input.nextDouble();
                simulator.simulateBy(duration);
                getOutput(simulator);
            } else if (option == 2){
                System.out.println("Opção (2). Selecione o tempo: \n");
                System.out.println("Até qual timestamp rodar (em minutos) ?\n");
                duration = input.nextDouble();
                simulator.simulateUntil(duration);
                getOutput(simulator);
            } else {
                break;
            }
        }
    }


    public void getOutput(Scheduler simulator) {
        System.out.println("Simulação finalizada.\n");

        int option = 0;

        System.out.println("Deseja informação para charts? \n");
        System.out.println("(1/DEFAULT)-Sim; (2)-Não. \n");

        Scanner input = new Scanner(System.in);
        option = input.nextInt();

        System.out.println("Total de clientes que passaram pelo restaurante: "
                + simulator.getTotalCustomers() + "\n");
        System.out.println("Tempo total de simulação: " + simulator.getTime() + "\n");
        System.out.println("Tamanho médio Fila 1: " +
                simulator.getEntitySetByType(EntitySetType.CASHIER_1).getAverageSize() + "\n");
        System.out.println("Tamanho médio Fila 2: " +
                simulator.getEntitySetByType(EntitySetType.CASHIER_2).getAverageSize() + "\n");
        System.out.println("Tempo médio de espera na Fila do Balcão: " +
                simulator.getEntitySetByType(EntitySetType.BAR_COUNTER).getAverageTimeInSet() + "\n");
        System.out.println("Tempo médio de espera na Fila das Mesas de 2: " +
                simulator.getEntitySetByType(EntitySetType.TABLE_2).getAverageTimeInSet() + "\n");
        System.out.println("Tempo médio de espera na Fila das Mesas de 4: " +
                simulator.getEntitySetByType(EntitySetType.TABLE_4).getAverageTimeInSet() + "\n");

        if(option != 2) {
            System.out.println("=========================================================="
                    + "\nAdicionais para chart: \n");
            System.out.println("Tamanho da Fila 1: " +
                    simulator.getEntitySetByType(EntitySetType.CASHIER_1).getSize() + "\n");
            System.out.println("Tamanho da Fila 2: " +
                    simulator.getEntitySetByType(EntitySetType.CASHIER_2).getSize() + "\n");
            System.out.println("Tamanho da Fila do Balcão: " +
                    simulator.getEntitySetByType(EntitySetType.BAR_COUNTER).getSize() + "\n");
            System.out.println("Tamanho das Filas das Mesas: " +
                    simulator.getEntitySetByType(EntitySetType.TABLE_2).getSize()
                    + " & " + simulator.getEntitySetByType(EntitySetType.TABLE_4).getSize()
                    + "\n");
            System.out.println("Tamanho da Fila de Pedidos: " +
                    simulator.getEntitySetByType(EntitySetType.ORDER).getSize() + "\n");
            System.out.println("Quantidade de clientes consumindo: " +
                    simulator.getConsumptionCustomersQuantity() + "\n");
        }
    }
}
