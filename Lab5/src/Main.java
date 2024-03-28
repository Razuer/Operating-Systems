import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Simulation simulation = new Simulation(50, 100000,75,35,10);
        //simulation.printAll();

        simulation.alg1();
        simulation.alg2();
        simulation.alg3();

//        simulation.proporcja();
//        simulation.obciazenie();
    }
}
