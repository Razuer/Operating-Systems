package Rest;

import java.util.*;

public class Simulation {
    private final PhysicalMemory physicalMemory;
    private final VirtualMemory virtualMemory;

    private final Processes processes;
    int pagesSum = 0;

    public Simulation(int numberOfFrames, int referencesNumber, int maxPagesPerProcess, int numberOfProcesses) {
        this.physicalMemory = new PhysicalMemory(numberOfFrames);
        this.processes = new Processes(numberOfProcesses);
        this.virtualMemory = new VirtualMemory(referencesNumber, maxPagesPerProcess, processes);

        for (Process process : processes.processes) {
            pagesSum += process.pages.size();
        }
    }

    public PhysicalMemory getPhysicalMemory() {
        return physicalMemory;
    }


    public VirtualMemory getVirtualMemory() {
        return virtualMemory;
    }

    public void run_EqualLRU() {
        System.out.printf("%n %43s %n", "----- The equal LRU algorithm is running! -----");
        processes.reset();
        getPhysicalMemory().reset();
        processes.sortByName();

        int num = physicalMemory.getNumberOfFrames() / processes.count;
        int rest = physicalMemory.getNumberOfFrames() % processes.count;

        int i = 0;
        for (Process process : processes.processes) {
            for (int j = 0; j < num; j++) {
                physicalMemory.getFrame(i++).setProcess(process);
                process.framesAcquired++;
            }
            if (rest > 0) {
                physicalMemory.getFrame(i++).setProcess(process);
                process.framesAcquired++;
                rest--;
            }
        }
//        for (Main.Process process : processes.processes) {
//            System.out.println(process + ": " + process.framesAcquired);
//        }
        LRU();
    }

    public void run_ProportionalLRU() {
        System.out.printf("%n %42s %n", "----- The proportional LRU algorithm is running! -----");

        getPhysicalMemory().reset();
        processes.reset();

        proportionalAllocation();

        LRU();
    }

    public void proportionalAllocation() {
        int i = 0;

        processes.sortBySize();
        for (Process process : processes.processes) {
            int size = process.pages.size() * physicalMemory.getNumberOfFrames() / pagesSum;
            if (size == 0) {
                size = 1;
            }

            for (int j = 0; j < size; j++) {
                physicalMemory.getFrame(i++).setProcess(process);
                process.framesAcquired++;
            }
        }
        for (int j = i; j < physicalMemory.getNumberOfFrames(); j++) {
            processes.sortByFramesAcquired();
            Process process = processes.processes.get(0);
            physicalMemory.getFrame(j).setProcess(process);
            process.framesAcquired++;
        }

        processes.sortByName();
//        for (Main.Process process : processes.processes) {
//            System.out.println(process + ": " + process.framesAcquired);
//        }
    }

    public void run_SteeringPFF(int refreshTime) {
        System.out.printf("%n %42s %n", "----- The SteeringPFF LRU algorithm is running! -----");
        physicalMemory.reset();
        processes.reset();
        proportionalAllocation();
        int pageErrors = 0;

        double PFMax = (double) 12 / (double) refreshTime;
        double PFMin = (double) 1 / (double) refreshTime;
        System.out.println("max: " + PFMax + " min: " + PFMin);

        int stolen = 0;
        int liberations = 0;
        int suspensions = 0;

        LinkedList<Page> referenceListCopy = virtualMemory.clone();
        LinkedList<Page> suspendedProcessReferenceList = new LinkedList<>();

        while (!referenceListCopy.isEmpty()) {
            pageErrors += LRU_time(referenceListCopy, refreshTime, suspendedProcessReferenceList);

            for (Process process : processes.processes) {
                if (process.isActive()) {
                    double PPF = (double) process.getErrors() / (double) refreshTime;
                    process.setPFF(PPF);
                    process.setErrors(0);
//                System.out.println(process + " - " + PPF);
                }
            }
            Process maxValuePFF = processes.processes.get(0);
            Process minValuePFF = processes.processes.get(0);
            for (Process process : processes.processes) {
                if (process.isActive() && process.framesAcquired > 1 && process.getPFF() > 0) {
                    maxValuePFF = process;
                    minValuePFF = process;
                    break;
                }
            }

            for (Process process : processes.processes) {
                if (maxValuePFF.getPFF() < process.getPFF() && process.isActive()) {
                    maxValuePFF = process;
                }
                if (minValuePFF.getPFF() >= process.getPFF() && process.framesAcquired > 1 && process.isActive() && process.getPFF() > 0) {
                    minValuePFF = process;
                }
            }
//            System.out.println(maxValuePPF + " " + maxValuePPF.getPFF());
//            System.out.println(minValuePPF + " " + minValuePPF.getPFF());
            if (minValuePFF.getPFF() <= PFMin && minValuePFF.framesAcquired > 1 && minValuePFF.getPFF() > (double) 0) {
                for (Frame frame : getPhysicalMemory().frameList) {
                    if (frame.getProcess() == minValuePFF) {
                        minValuePFF.framesAcquired--;
                        liberations++;
                        frame.reset();
                        break;
                    }
                }
            }
            swapMax:
            if (maxValuePFF.getPFF() > PFMax) {
                for (Frame frame : getPhysicalMemory().frameList) {
                    if (frame.getProcess() == null) {
                        frame.setProcess(maxValuePFF);
                        maxValuePFF.framesAcquired++;
                        stolen++;
                        break swapMax;
                    }
                }
                maxValuePFF.setActive(false);
                suspensions++;
                for (Frame frame : physicalMemory.frameList) {
                    if (frame.process == maxValuePFF) {
                        frame.process.framesAcquired--;
                        frame.reset();
                    }
                }
            }
        }
        System.out.println("Global number of page errors: " + pageErrors);
        System.out.println("Liberated frames: " + liberations + ", stolen frames: " + stolen + ", suspended processes: " + suspensions);
    }

    public void run_zoneModel(int refreshTime) {
        System.out.printf("%n %42s %n", "----- The Zone Model LRU algorithm is running! -----");
        physicalMemory.reset();
        processes.reset();
        int pageErrors = 0;
        int suspensions = 0;
        int restarted = 0;

        ArrayList<Process> suspendedProcesses = new ArrayList<>();

        LinkedList<Page> referenceListCopy = virtualMemory.clone();
        LinkedList<Page> suspendedProcessReferenceList = new LinkedList<>();

        while (!referenceListCopy.isEmpty()) {
            int D = calculateWSS(refreshTime, referenceListCopy);
//            System.out.println(D);

            while (D > getPhysicalMemory().getNumberOfFrames()) {
                Process min = processes.processes.get(0);
                for (Process process : processes.processes) {
                    if (process.isActive() && process.getWSS() > min.getWSS()) {
                        min = process;
                    }
                }
                min.setActive(false);
                suspensions++;
                suspendedProcesses.add(min);

                for (Frame frame : physicalMemory.frameList) {
                    if (frame.process == min) {
                        frame.reset();
                        min.framesAcquired--;
                    }
                }
                D -= min.getWSS();
                min.setWSS(0);
            }

            for (Process process : processes.processes) {
                if (process.isActive()) {
                    while (process.getFramesAcquired() > process.getWSS()) {
                        getPhysicalMemory().getProcessFrame(process).reset();
                        process.framesAcquired--;
                    }
                }
            }
            for (Process process : processes.processes) {
                if (process.isActive()) {
                    while (process.getFramesAcquired() < process.getWSS()) {
                        getPhysicalMemory().getFreeFrame().setProcess(process);
                        process.framesAcquired++;
                    }
                }
            }
//            if (suspendedProcesses.size() > 0 && getPhysicalMemory().howManyFreeFrames() > 3) {
//                Main.Process process = suspendedProcesses.remove(0);
//                System.out.println("ile ramek: " + getPhysicalMemory().howManyFreeFrames());
//                processes.processes.get(processes.processes.indexOf(process)).setActive(true);
//                restarted++;
//                System.out.println("tutaj");
//                getPhysicalMemory().getFreeFrame().setProcess(process);
//                System.out.println("błąd");
//                getVirtualMemory().addSuspendedReferences(suspendedProcessReferenceList, process);
//            }

            pageErrors += LRU_time(referenceListCopy, (refreshTime), suspendedProcessReferenceList);
        }
        System.out.println("Global number of page errors: " + pageErrors);
        System.out.println("Suspended processes: " + suspensions + ", reactivated processes: " + restarted);
    }

    public int calculateWSS(int refreshTime, LinkedList<Page> referenceListCopy) {
        int D = 0;
        for (Process process : processes.processes) {
            if (process.isActive()) {
                HashSet<Page> set = new HashSet<>();
                if (referenceListCopy.size() < refreshTime) refreshTime = referenceListCopy.size();
                for (int i = 0; i < refreshTime; i++) {
                    if (referenceListCopy.get(i).getProcess() == (process)) {
                        set.add(referenceListCopy.get(i));
                    }
                }
                process.setWSS(set.size());
                D += process.getWSS();
            }
        }
        return D;
    }

    private int smallProportionalAllocation(int idx, Process inactive) {
        processes.sortBySize();
        int howMuch = inactive.getWSS();
        int i = idx;
        while (inactive.getWSS() > 0) {

            for (Process process : processes.processes) {
                int size = process.pages.size() * howMuch / pagesSum;

                for (int j = 0; j < size; j++) {
                    getPhysicalMemory().getFrame(i++).setProcess(process);
                    process.framesAcquired++;
                    inactive.setWSS(inactive.getWSS() - 1);
                }
            }
        }
        processes.sortByName();
        return i;
    }

    public void LRU() {
        int pageErrors = 0;

        for (Page page : getVirtualMemory().referenceList) {
            physicalMemory.updateStorageTime();
//            System.out.print("|" + page + "| ");

            if (!physicalMemory.contains(page)) {
                physicalMemory.getNextFrame(page.process).setPage(page);
                page.process.addError();
                pageErrors++;
            }
//            System.out.println(physicalMemory);
        }
        System.out.println("Global number of page errors: " + pageErrors);
//        for (Main.Process process : processes.processes){
//            System.out.println(process + " page errors: " + process.getErrors());
//        }
    }

    public int LRU_time(LinkedList<Page> referenceList, int time, LinkedList<Page> stoppedPages) {
        int pageErrors = 0;

        for (int i = 0; i < time; i++) {
            Page page = referenceList.getFirst();
            if (page.process.isActive()) {
                referenceList.pop();
                physicalMemory.updateStorageTime();
//            System.out.print("|" + page + "| ");

                if (!physicalMemory.contains(page)) {
                    physicalMemory.getNextFrame(page.process).setPage(page);
                    page.process.addError();
                    pageErrors++;
                }
            } else {
                stoppedPages.add(referenceList.pop());
            }
            if (referenceList.isEmpty()) break;
        }
        return pageErrors;
    }

    public void test() {

        for (Process process : processes.processes) {
            ArrayList<Integer> arrayList = new ArrayList<>();

            for (Page page : virtualMemory.referenceList) {
                if (page.process.equals(process) && !arrayList.contains(page.name)) {
                    arrayList.add(page.name);
                }
            }
            System.out.println(process + " - " + arrayList.size() + "/" + process.pages.size());
        }
    }
}

