package Rest;

import java.util.LinkedList;
import java.util.Random;

public class VirtualMemory {
    LinkedList<Page> referenceList = new LinkedList<>();
    Processes processes;

    public VirtualMemory(int size, int maxReferenceNumber, Processes processes) {
        Random random = new Random();
        int fuks = 0;
        int fuksLiczby = 0;
        int nowyFuks = 0;
        this.processes = processes;

        for (Process process : processes.processes) {
            double rand = random.nextDouble(0.2, 1);
            int range = (int) (rand * maxReferenceNumber);
            process.addPages(range, process);
        }

        for (Process process : processes.processes) {
            System.out.println(process + ": " + process.pages.size());
        }

        mainLoop:
        while (referenceList.size() < size) {
            double rand = random.nextDouble();
            if (rand > 0.80 && referenceList.size() > 25) {
                fuks++;
                Process process = processes.getRandomProcessBasedOnSize();
                for (int i = 0; i < random.nextInt(6) + 5; i++) {
                    if (referenceList.size() == size) break mainLoop;
                    fuksLiczby++;
                    referenceList.add(getLocalRef(process));
                }
            } else if (rand > 0.75 && referenceList.size() > 25) {
                referenceList.add(getLocalRef(processes.processes.get(random.nextInt(processes.count))));
                nowyFuks++;
            } else if (rand < 0.25 && referenceList.size() > 25) {
                Process process = referenceList.get(referenceList.size() - 1).process;
                referenceList.add(process.pages.get(random.nextInt(process.pages.size())));
            } else {
                Process process = processes.getRandomProcessBasedOnSize();
                referenceList.add(process.pages.get(random.nextInt(process.pages.size())));
            }
        }
        System.out.println("Liczba fuksow: " + fuks + " - liczba fuksliczb: " + fuksLiczby);
        System.out.println("Nowe fuksLiczby: " + nowyFuks);
        System.out.println("Liczb: " + referenceList.size());
    }

    public Page getLocalRef(Process process) {
        Random random = new Random();
        Page previousPage = referenceList.getLast();
        for (int i = referenceList.size() - 1; i > 0; i--) {
            Page page = referenceList.get(i);
            double rand = random.nextDouble();
            if (page.process == (process) && rand > 0.55 && previousPage !=(page)) {
                return page;
            }
            previousPage = page;
        }
        return process.pages.get(random.nextInt(process.pages.size()));
    }

    public LinkedList<Page> clone() {
        LinkedList<Page> linkedList = new LinkedList<>();
        for (Page page : referenceList) {
            linkedList.add(page);
        }
        return linkedList;
    }
    public void test(){
        for(Page page : referenceList){
            page.getProcess().addReference();
        }
        System.out.println("Liczba odwołań do procesów:");
        for(Process process : processes.processes){
            System.out.println(process.term + ": " + process.getReferences());
        }
    }

    public void addSuspendedReferences(LinkedList<Page> suspendedReferences, Process process){
        for(int i = 0; i<suspendedReferences.size(); i++){
            Page page = suspendedReferences.get(i);
            if(page.process == process) {
                referenceList.addFirst(suspendedReferences.remove(i));
                break;
            }
        }

        for(int i = 0; i<suspendedReferences.size(); i++){
            Page page = suspendedReferences.get(i);
            Random random = new Random();
            int rand = random.nextInt(referenceList.size()/2);

            if(page.process == process) referenceList.add(rand, suspendedReferences.remove(i--));
        }
    }

    @Override
    public String toString() {
        return referenceList.toString();
    }
}
