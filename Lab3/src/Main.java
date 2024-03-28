import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        Simulation simulation =  new Simulation(8, 1000,  30);
        simulation.getVirtualMemory().printReferenceList();

        simulation.FIFO();

        simulation.OPT();

        simulation.LRU();

        simulation.aprLRU();

        simulation.RAND();
    }
}
