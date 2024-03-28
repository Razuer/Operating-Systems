package All;

//import All.Comparators.ArrivalTimeComparator;
//import All.Comparators.RemainingTimeComparator;
//import All.Comparators.RemainingTimeZerosComparator;
import All.Comparators.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Processes implements Cloneable {
    public final List<Process> processList = new ArrayList<>();

    private Process get(int index) {
        return processList.get(index);
    }

    public void generateList(int quantity, int maxDuration, int maxArrival) {
        for (int i = 0; i < quantity; i++) {
            processList.add(Process.generateProcess(i + 1, maxDuration, maxArrival));
        }
    }

    public void printProcesses() {
        for (Process process : processList) {
            System.out.println(process.toString());
        }
    }

    public void runFCFS(boolean printLiveActions, boolean printProcessesEndStatistics, boolean printStarvedProcessesStatistics) {
        System.out.printf("%n %48s %n", "------ Uruchomiono algorytm FCFS! ------");
        Processes clonedProcessList = clone();
        Clock clock = new Clock();
        Printer printer = new Printer(clonedProcessList, clock);
        clonedProcessList.sortArrivalTime();
        Process actProcess = clonedProcessList.get(0);
        boolean finished = false;

        while (!finished) {
            printer.printLiveActions(actProcess, printLiveActions);
            clonedProcessList.sortJustRemainingTimeZeros();
            actProcess = clonedProcessList.get(0);

            if (actProcess.getRemainingTime() == 0) {
                finished = true;

            } else if (actProcess.getArrivalTime() <= clock.getTimeStamp()) {
                printer.printActiveProcess(printLiveActions);
                clonedProcessList.addOneLoopInQueue(clock.getTimeStamp());
                clonedProcessList.addOneLoopInInactive(actProcess, clock.getTimeStamp());
                actProcess.doOnce();
            }
            clock.endLoop();
        }
        printer.printEndStatistics(printProcessesEndStatistics);
        printer.printStarvedProcesses(printStarvedProcessesStatistics);
    }

    public void runSJF(boolean printLiveActions, boolean printProcessesEndStatistics, boolean printStarvedProcessesStatistics) {
        System.out.printf("%n %47s %n", "------ Uruchomiono algorytm SJF! ------");
        Processes clonedProcessList = clone();
        Clock clock = new Clock();
        Printer printer = new Printer(clonedProcessList, clock);
        Process actProcess = new Process(0, 0, -1);
        Process nextProcess;
        boolean finished = false;
        int expropriationsCount = 0;

        while (!finished) {
            printer.printLiveActions(actProcess, printLiveActions);
            nextProcess = clonedProcessList.getShortestActiveProcess(clock.getTimeStamp());
            if (!actProcess.equals(nextProcess) && actProcess.getRemainingTime() != 0) {
                expropriationsCount++;
                if (printLiveActions) System.out.println("WYWŁASZCZENIE!");
            }
            if (nextProcess.getRemainingTime() == 0) {
                finished = true;
            } else {
                actProcess = nextProcess;

                if (actProcess.getArrivalTime() <= clock.getTimeStamp()) {
                    printer.printActiveProcess(printLiveActions);
                    clonedProcessList.addOneLoopInQueue(clock.getTimeStamp());
                    clonedProcessList.addOneLoopInInactive(actProcess, clock.getTimeStamp());
                    actProcess.doOnce();
                }
            }
            clock.endLoop();
        }
        printer.printEndStatistics(printProcessesEndStatistics);
        System.out.println("Liczba wywłaszczeń: " + expropriationsCount);
        System.out.println("Najdłuższy czas w kolejce: " + clonedProcessList.getLongestQueueProcess());
        printer.printStarvedProcesses(printStarvedProcessesStatistics);
    }

    public void runRR(int numberOfWork, boolean printLiveActions, boolean printProcessesEndStatistics, boolean printStarvedProcessesStatistics) {
        System.out.printf("%n %46s %n", "------ Uruchomiono algorytm RR! ------");
        Processes clonedProcessList = clone();
        Clock clock = new Clock();
        Printer printer = new Printer(clonedProcessList, clock);
        boolean finished = false;
        clonedProcessList.sortArrivalTime();
        int switches = 0;

        if (numberOfWork <= 0){
            finished = true;
            System.out.println("Błędna liczba prac do wykonania na jedną rundę!");
        }
            while (!finished) {
                if (clonedProcessList.get(0).getRemainingTime() == 0) {
                    finished = true;

                } else if (clonedProcessList.get(0).getArrivalTime() > clock.getTimeStamp()) {
                    clock.printTime(printLiveActions);
                    clock.endLoop();

                } else for (int i = 0; i < clonedProcessList.processList.size(); i++) {
                    if (clonedProcessList.get(i).getArrivalTime() <= clock.getTimeStamp() && clonedProcessList.get(i).getRemainingTime() > 0) {
                        switches++;

                        for (int j = 0; j < numberOfWork; j++) {
                            clock.printTime(printLiveActions);
                            if (j == 0 && i > 0 && clonedProcessList.get(i - 1).getRemainingTime() == 0)
                                printer.printJustFinishedProcess(clonedProcessList.get(i - 1), printLiveActions);
                            printer.printNewProcess(printLiveActions);
                            printer.printActiveProcess(clonedProcessList.get(i), printLiveActions);
                            clonedProcessList.addOneLoopInQueue(clock.getTimeStamp());
                            clonedProcessList.addOneLoopInInactive(clonedProcessList.get(i), clock.getTimeStamp());
                            clonedProcessList.get(i).doOnce();
                            clock.endLoop();
                            if (clonedProcessList.get(i).getRemainingTime() == 0) {
                                break;
                            }
                        }
                    }
                }
                clonedProcessList.sortJustRemainingTimeZeros();
            }
        clock.endLoop();
        printer.printEndStatistics(printProcessesEndStatistics);
        System.out.println("Liczba przełączeń: " + switches);
        printer.printStarvedProcesses(printStarvedProcessesStatistics);
    }

    private Process getShortestActiveProcess(int timeStamp) {
        sortRemainingTimeActiveProcesses(timeStamp);

        for (int i = 0; i < processList.size(); i++) {
            if (get(i).getRemainingTime() != 0) return get(i);
        }
        return get(0);
    }

    private void addOneLoopInQueue(int timeStamp) {
        for (Process process : processList) {
            if (process.getRemainingTime() != 0 && process.getArrivalTime() <= timeStamp) {
                process.setQueueTime(process.getQueueTime() + 1);
            }
        }
    }

    private int getLongestQueueProcess() {
        int longest = 0;
        for (Process process : processList) {
            if (process.getQueueTime() > longest) longest = process.getQueueTime();
        }
        return longest;
    }

    private void addOneLoopInInactive(Process process1, int timeStamp) {
        for (Process process : processList) {
            if (process.getRemainingTime() != 0 && process.getArrivalTime() <= timeStamp && !process.equals(process1)) {
                process.setInactiveQueueTime(process.getInactiveQueueTime() + 1);
            }
        }
    }

    public void randomGenerateAndAddProcess(int timeStamp) {
        Random random = new Random();
        int x = random.nextInt(101);

        if (x > 15) {
            Process process = new Process(processList.size() + 1, 30, timeStamp);
            System.out.println("Utworzono nowy proces: " + process.getName());
            processList.add(process);
        }
    }

    @Override
    public Processes clone() {
        Processes processes = new Processes();
        for (int i = 0; i < this.processList.size(); i++) {
            processes.processList.add(this.processList.get(i).clone());
        }
        return processes;
    }


    private boolean isDone(List<Process> processes) {
        processes.removeIf(process -> (process.getRemainingTime() <= 0));

        return processes.isEmpty();
    }

    private void sortRemainingTime() {
        processList.sort(new RemainingTimeComparator());
    }

    private void sortArrivalTime() {
        processList.sort(new ArrivalTimeComparator());
    }

    private void sortJustRemainingTimeZeros() {
        processList.sort(new RemainingTimeZerosComparator());
    }

    private void sortRemainingTimeWithZerosAtTheEnd() {
        sortRemainingTime();
        processList.sort(new RemainingTimeZerosComparator());
    }

    public void sortRemainingTimeActiveProcesses(int timeStamp) {
        sortRemainingTime();

        for (int i = 0, j = 0; i < processList.size() - j; i++) {
            if (get(i).getArrivalTime() > timeStamp) {
                processList.add(get(i));
                processList.remove(i);
                i--;
                j++;
            }
        }
    }
//        public void runFCFS(boolean printEachProcessStatistic) {
//        int timeStamp = 0;
//        boolean finished = false;
//        sortArrivalTime();
//
//        while (!finished) {
//            System.out.printf("%n %40s %n", "--- Aktualny czas: " + timeStamp + " ---");
//            printJustFinishedProcess(get(0));
//            printNewProcess(timeStamp);
//            sortJustRemainingTimeZeros();
//            if (get(0).getRemainingTime() == 0) {
//                finished = true;
//
//            } else if (get(0).getArrivalTime() <= timeStamp) {
//                printActiveProcess();
//                addOneLoopInQueue(timeStamp);
//                get(0).doOnce();
//            }
//            timeStamp++;
//        }
//        printEndStatistics(timeStamp, printEachProcessStatistic);
//    }
}
