import java.util.ArrayList;

public class Simulation {
    private final Processes processes;
    private final Processors processors;
    private final int size;
    private final int p;
    private final int z;
    private final int r;

    public Simulation(int N, int size, int p, int z, int r) {
        processors = new Processors(N);
        processes = new Processes(size, N, processors);
        this.size = size;
        this.p = p;
        this.z = z;
        this.r = r;
    }

    public Processes getProcesses() {
        return processes;
    }

    public Processors getProcessors() {
        return processors;
    }

    public void alg1() {
        System.out.println("----- Uruchomiono Algorytm 1! -----");

        int migrations = 0;
        int deactivations = 0;
        int inquiries = 0;
        int rejected = 0;
        int maxLoad = 0;
        processors.reset();
        Processes processesCopy = processes.clone();

        double averageLoad = 0;
        double deviation = 0;
        double count = 0;
        int time = 0;

        int clock = 0;

        while (!processesCopy.processList.isEmpty()) {
            Process process = processesCopy.processList.get(0);
            Processor currentProc = process.getProcessor();

            if(currentProc.getLoadAfterAdding(process)>p) {
                int i = 0;
                ArrayList<Processor> searchingList = (ArrayList<Processor>) processors.processors.clone();
                while (i < z) {
                    inquiries++;
                    Processor processor = processors.getRandomWithoutRepetitions(searchingList, currentProc);
                    if (processor.getLoad() < p) {
                        migrations++;
                        currentProc = processor;
                        break;
                    }
                    if(searchingList.isEmpty()) break;
                    i++;
                }
            }
            if (currentProc.getLoadAfterAdding(process) > 100) {
                processesCopy.skip(330);
//                currentProc.waitingRoom.add(process);
                rejected++;
                time++;

            } else {
//                if(currentProc.getLoadAfterAdding(process)>100) rejected++;
                currentProc.add(process);
                processesCopy.processList.remove(0);

                if (time++ == 100) {
                    double currAvLoad = processors.getAverageLoad();
                    averageLoad += currAvLoad;

                    deviation += processors.getDeviation(currAvLoad);

                    time = 0;
                    count++;
                }
                int max = processors.maxLoad();
                if (max > maxLoad) {
                    maxLoad = max;
                }
            }
            deactivations += processors.updateLoopTime();
            clock++;
        }
        averageLoad = averageLoad / count;
        deviation = deviation / count;



        System.out.println("Wyniki:");
        System.out.println("> Migracji: " + migrations);
        System.out.println("> Ukonczone procesy: " + deactivations);
        System.out.println("> Ilosc zapytan: " + inquiries);
        System.out.println("> Srednie obciazenie: " + averageLoad + "%");
        System.out.println("> Srednie odchylenie: " + deviation + "%");
        System.out.println("> Maksymalne obciazenie: " + maxLoad + "%");
        System.out.println("> Odlozone na pozniej procesy: " + rejected);
        System.out.println("> Czas: " + clock+"\n");
    }



    public void alg2() {
        System.out.println("----- Uruchomiono Algorytm 2! -----");

        int migrations = 0;
        int deactivations = 0;
        int inquiries = 0;
        int rejected = 0;
        int maxLoad = 0;
        processors.reset();
        Processes processesCopy = processes.clone();

        double averageLoad = 0;
        double deviation = 0;
        double count = 0;
        int time = 0;
        int clock = 0;

        while (!processesCopy.processList.isEmpty()) {
            Process process = processesCopy.processList.get(0);
            Processor currentProc = process.getProcessor();

            boolean broke = false;
            if(currentProc.getLoadAfterAdding(process)>p) {
                ArrayList<Processor> searchingList = (ArrayList<Processor>) processors.processors.clone();
                while (!broke) {
                    inquiries++;
                    Processor processor = processors.getRandomWithoutRepetitions(searchingList, currentProc);
                    if (processor.getLoad() < p) {
                        migrations++;
                        currentProc = processor;
                        break;
                    }
                    if(searchingList.isEmpty()) broke = true;
                }
            }
            if (broke) {
                processesCopy.skip(330);
//                currentProc.waitingRoom.add(process);
                rejected++;
                time++;

            } else {
                currentProc.add(process);
                processesCopy.processList.remove(0);

                if (time++ == 100) {
                    double currAvLoad = processors.getAverageLoad();
                    averageLoad += currAvLoad;

                    deviation += processors.getDeviation(currAvLoad);
                    time = 0;
                    count++;
                }
                int max = processors.maxLoad();
                if (max > maxLoad) {
                    maxLoad = max;
                }
            }
            deactivations += processors.updateLoopTime();
            clock++;
        }
        averageLoad = averageLoad / count;
        deviation = deviation / count;

        System.out.println("Wyniki:");
        System.out.println("> Migracji: " + migrations);
        System.out.println("> Ukonczone procesy: " + deactivations);
        System.out.println("> Ilosc zapytan: " + inquiries);
        System.out.println("> Srednie obciazenie: " + averageLoad + "%");
        System.out.println("> Srednie odchylenie: " + deviation + "%");
        System.out.println("> Maksymalne obciazenie: " + maxLoad + "%");
        System.out.println("> Odlozone na pozniej procesy: " + rejected);
        System.out.println("> Czas: " + clock+"\n");
    }

    public void alg3() {
        System.out.println("----- Uruchomiono Algorytm 3! -----");

        int migrations = 0;
        int deactivations = 0;
        int inquiries = 0;
        int rejected = 0;
        int maxLoad = 0;
        int stealingAttempts = 0;
        int stolen = 0;
        processors.reset();
        Processes processesCopy = processes.clone();

        double averageLoad = 0;
        double deviation = 0;
        double count = 0;
        int time = 0;

        int clock = 0;

        while (!processesCopy.processList.isEmpty()) {
            Process process = processesCopy.processList.get(0);
            Processor currentProc = process.getProcessor();

            boolean broke = false;
            if(currentProc.getLoadAfterAdding(process)>p) {
                ArrayList<Processor> searchingList = (ArrayList<Processor>) processors.processors.clone();
                while (!broke) {
                    inquiries++;
                    Processor processor = processors.getRandomWithoutRepetitions(searchingList, currentProc);
                    if (processor.getLoad() < p) {
                        migrations++;
                        currentProc = processor;
                        break;
                    }
                    if(searchingList.isEmpty()) broke = true;
                }
            }
            if (broke) {
                processesCopy.skip(330);
//                currentProc.waitingRoom.add(process);
                rejected++;
                time++;

            } else {
                currentProc.add(process);
                processesCopy.processList.remove(0);

                if(currentProc.getLoad()<r) {
                    stealingAttempts++;
                    stolen+=processors.askRandomProcessorAndStealSome(currentProc, 20, 5, p);
                }
                if (time++ == 100) {
                    double currAvLoad = processors.getAverageLoad();
                    averageLoad += currAvLoad;

                    deviation += processors.getDeviation(currAvLoad);

                    time = 0;
                    count++;
                }
                int max = processors.maxLoad();
                if (max > maxLoad) {
                    maxLoad = max;
                }
            }
            deactivations += processors.updateLoopTime();
            clock++;
        }
        averageLoad = averageLoad / count;
        deviation = deviation / count;

        System.out.println("Wyniki:");
        System.out.println("> Migracji: " + migrations);
        System.out.println("> Ukonczone procesy: " + deactivations);
        System.out.println("> Ilosc zapytan: " + inquiries);
        System.out.println("> Srednie obciazenie: " + averageLoad + "%");
        System.out.println("> Srednie odchylenie: " + deviation + "%");
        System.out.println("> Maksymalne obciazenie: " + maxLoad + "%");
        System.out.println("> Proby kradziezy: " + maxLoad);
        System.out.println("> Ukradzione procesy: " + stolen);
        System.out.println("> Odlozone na pozniej procesy: " + rejected);
        System.out.println("> Czas: " + clock+"\n");
    }

    public void printAll() {
        for (Processor processor : processors.processors) {
            System.out.println(processor);
        }
        for (Process process : processes.processList) {
            System.out.println(process);
        }
    }

    public void proporcja() {
        for (Processor processor : processors.processors) {
            int i = 0;
            for (Process process : processes.processList) {
                if (process.getProcessor() == processor) i++;
            }
            System.out.println(processor + " ma " + i + " zadan");
        }
    }

    public void obciazenie() {
        for (Processor processor : processors.processors) {
            int i = 0;
            for (Process process : processes.processList) {
                if (process.getProcessor() == processor) i += process.getWeight();
            }
            System.out.println(processor + " ma " + i + " generalnego obciazenia");
        }
    }

    public void alg1_alfa() {
        System.out.println("----- Uruchomiono Algorytm 1! -----");

        int migrations = 0;
        int deactivations = 0;
        int inquiries = 0;
        int rejected = 0;
        int maxLoad = 0;
        processors.reset();
        Processes processesCopy = processes.clone();

        double averageLoad = 0;
        double count = 0;
        int time = 0;

        while (!processesCopy.processList.isEmpty()) {
            Process process = processesCopy.processList.get(0);
            Processor currentProc = process.getProcessor();

            if(currentProc.getLoadAfterAdding(process)>p) {
                int i = 0;
                while (i < z) {
                    inquiries++;
                    Processor processor = processors.getRandom(currentProc);
                    if (processor.getLoad() < p) {
                        migrations++;
                        currentProc = processor;
                        break;
                    }
                    i++;
                }
            }
            if (currentProc.getLoadAfterAdding(process) > 100) {
                processesCopy.skip(50);
                rejected++;
                time++;

            } else {
                currentProc.add(process);
                processesCopy.processList.remove(0);

                if (time++ == 100) {
                    averageLoad += processors.getAverageLoad();
                    time = 0;
                    count++;
                }
                int max = processors.maxLoad();
                if (max > maxLoad) {
                    maxLoad = max;
                }
            }
            deactivations += processors.updateLoopTime();
        }
        averageLoad = averageLoad / count;

        System.out.println("Wyniki:");
        System.out.println("> Migracji: " + migrations);
        System.out.println("> Ukonczone procesy: " + deactivations);
        System.out.println("> Ilosc zapytan: " + inquiries);
        System.out.println("> Srednie obciazenie: " + averageLoad);
        System.out.println("> Maksymalne obciazenie: " + maxLoad);
        System.out.println("> Odlozone na pozniej procesy: " + rejected + "\n");
    }
    public void alg2_alfa() {
        System.out.println("----- Uruchomiono Algorytm 2! -----");

        int migrations = 0;
        int deactivations = 0;
        int inquiries = 0;
        int rejected = 0;
        int maxLoad = 0;
        processors.reset();
        Processes processesCopy = processes.clone();

        double averageLoad = 0;
        double count = 0;
        int time = 0;

        while (!processesCopy.processList.isEmpty()) {
            Process process = processesCopy.processList.get(0);
            Processor currentProc = process.getProcessor();

            boolean broke = false;
            if(currentProc.getLoadAfterAdding(process)>p) {
                int i = 0;
                while (!broke) {
                    inquiries++;
                    Processor processor = processors.getRandom(currentProc);
                    if (processor.getLoad() < p) {
                        migrations++;
                        currentProc = processor;
                        break;
                    }
                    if(i++>10*processes.processList.size()) broke = true;
                }
            }
            if (broke) {
                processesCopy.skip(50);
                rejected++;
                time++;

            } else {
                currentProc.add(process);
                processesCopy.processList.remove(0);

                if (time++ == 100) {
                    averageLoad += processors.getAverageLoad();
                    time = 0;
                    count++;
                }
                int max = processors.maxLoad();
                if (max > maxLoad) {
                    maxLoad = max;
                }
            }
            deactivations += processors.updateLoopTime();
        }
        averageLoad = averageLoad / count;

        System.out.println("Wyniki:");
        System.out.println("> Migracji: " + migrations);
        System.out.println("> Ukonczone procesy: " + deactivations);
        System.out.println("> Ilosc zapytan: " + inquiries);
        System.out.println("> Srednie obciazenie: " + averageLoad);
        System.out.println("> Maksymalne obciazenie: " + maxLoad);
        System.out.println("> Odlozone na pozniej procesy: " + rejected + "\n");
    }
}
