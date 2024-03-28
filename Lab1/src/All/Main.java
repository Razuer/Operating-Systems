package All;

public class Main {
    public static void main(String[] args) {

        Processes processList = new Processes();
        processList.printProcesses();

        processList.generateList(10000, 30, 20000);

        processList.runFCFS(true, true, true);

        processList.runSJF(false, false, false);

        processList.runRR(10, false, false, false);
    }
}
