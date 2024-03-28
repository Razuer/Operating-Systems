import Rest.*;

public class Main {
    public static void main(String[] args) {

        Simulation simulation = new Simulation(120, 100000, 20, 50);

        simulation.run_EqualLRU();
        simulation.run_ProportionalLRU();
        simulation.run_SteeringPFF(2000);
        simulation.run_zoneModel(300);
    }
}
