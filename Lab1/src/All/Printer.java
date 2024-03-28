package All;

public class Printer {
    private Processes processList = new Processes();
    private Clock clock = new Clock();

    public Printer(Processes processList, Clock clock) {
        this.processList = processList;
        this.clock = clock;
    }

    public void printLiveActions(Process process, boolean printIt) {
        if (printIt) {
            clock.printTime();
            printJustFinishedProcess(process);
            printNewProcess();
        }
    }

    public void printStarvedProcesses(boolean printEachStarvedProcessStatistic) {
        Processes starvedProcesses = new Processes();
        for (Process process : processList.processList) {
            if (process.getQueueTime() >= (clock.getTimeStamp() / 2)) {
                starvedProcesses.processList.add(process);
            }
        }
        System.out.println("Liczba zagłodzonych procesów: " + starvedProcesses.processList.size());
        if (printEachStarvedProcessStatistic) {
            System.out.println("Statystyki zagłodzonych procesów:");
            starvedProcesses.printProcesses();
        }
    }

    public void printActiveProcess(boolean printIt) {
        if (printIt) {
            System.out.println("Aktualnie pracujący:\n" + processList.processList.get(0));
        }
    }

    public void printActiveProcess(Process process, boolean printIt) {
        if (printIt) {
            System.out.println("Aktualnie pracujący:\n" + process);
        }
    }

    public void printEndStatistics(boolean printEachProcessStatistic) {
        if (printEachProcessStatistic) {
            System.out.println("\nStatystyki procesów:");
            processList.printProcesses();
        }
        System.out.println("\nUkończono wszystkie procesy w: " + clock.getTimeStamp());

        double sum = 0;
        for (Process process : processList.processList) {
//            sum += process.getQueueTime();
            sum += process.getInactiveQueueTime();
        }
        double averageWaitTime = sum / processList.processList.size();

        System.out.println("Średni czas oczekiwania: " + averageWaitTime);
    }

    public void printNewProcess() {
        for (Process process : processList.processList) {
            if (process.getArrivalTime() == clock.getTimeStamp()) {
                System.out.println("NOWY PROCES - " + process.getName() + " - duration: " + process.getDurationTime());
            }
        }
    }

    public void printNewProcess(boolean printIt) {
        if (printIt) {
            for (Process process : processList.processList) {
                if (process.getArrivalTime() == clock.getTimeStamp()) {
                    System.out.println("NOWY PROCES - " + process.getName() + " - duration: " + process.getDurationTime());
                }
            }
        }
    }

    public void printJustFinishedProcess(Process process) {
        if ((process.getRemainingTime()) == 0 && process.getArrivalTime() >= 0) {
            System.out.println("UKOŃCZONO " + process.getName() + "!\n");
        }
    }

    public void printJustFinishedProcess(Process process, boolean printIt) {
        if (printIt) {
            if ((process.getRemainingTime()) == 0 && process.getArrivalTime() >= 0) {
                System.out.println("UKOŃCZONO " + process.getName() + "!");
            }
        }
    }

}
