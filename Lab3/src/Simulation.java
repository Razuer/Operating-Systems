import java.util.*;

public class Simulation {
    private final PhysicalMemory physicalMemory;
    private final VirtualMemory virtualMemory;
    private int pageErrors;

    public Simulation(int numberOfFrames, int referencesNumber, int maxReferenceNumber) {
        this.physicalMemory = new PhysicalMemory(numberOfFrames);
        this.virtualMemory = new VirtualMemory(referencesNumber, maxReferenceNumber);
    }

    public PhysicalMemory getPhysicalMemory() {
        return physicalMemory;
    }


    public VirtualMemory getVirtualMemory() {
        return virtualMemory;
    }

    public void FIFO() {
        System.out.printf("%n %46s %n", "----- The FIFO algorithm is running! -----");
        physicalMemory.reset();
        pageErrors = 0;
        int i = 0;
        for (Integer page : virtualMemory.referenceList) {
//            System.out.print("|"+ page + "| ");
            if (i == physicalMemory.getNumberOfFrames()) {
                i = 0;
            }
            if (physicalMemory.contains(page)) {
                physicalMemory.refreshFrameWithPage(page);
            } else {
                physicalMemory.replacePage(i, page);
                pageErrors++;
                i++;
            }
//            System.out.println(physicalMemory);
        }
        System.out.println("Number of page errors: " + pageErrors);
    }

    public void OPT() {
        System.out.printf("%n %45s %n", "----- The OPT algorithm is running! -----");
        physicalMemory.reset();
        pageErrors = 0;
        int emptyFrameIdx = 0;

        for (int i = 0; i < virtualMemory.referenceList.size(); i++) {
            physicalMemory.updateStorageTime();
            Integer page = virtualMemory.referenceList.get(i);
//            System.out.print("|"+ page + "| ");

            if (physicalMemory.contains(page)) {
                physicalMemory.refreshFrameWithPage(page);
            } else {
                if (emptyFrameIdx < physicalMemory.getNumberOfFrames()) {
                    physicalMemory.getFrame(emptyFrameIdx++).setPage(page);
                } else {
                    physicalMemory.getTheFurthestFrame().setPage(page);
                    physicalMemory.getTheFurthestFrame().reset();
                }
                pageErrors++;
            }
            physicalMemory.updateDistanceToReference(virtualMemory, i);
//            System.out.println(physicalMemory);
        }
        System.out.println("Number of page errors: " + pageErrors);
    }

    public void LRU() {
        System.out.printf("%n %45s %n", "----- The LRU algorithm is running! -----");
        physicalMemory.reset();
        pageErrors = 0;
        int emptyFrameIdx = 0;

        for (Integer page : virtualMemory.referenceList) {
            physicalMemory.updateStorageTime();
//            System.out.print("|" + page + "| ");

            if (physicalMemory.contains(page)) {
                physicalMemory.refreshFrameWithPage(page);
            } else{
                if (emptyFrameIdx < physicalMemory.getNumberOfFrames()) {
                    physicalMemory.getFrame(emptyFrameIdx++).setPage(page);
                } else {
                    physicalMemory.getOldestFrame().setPage(page);
                    physicalMemory.getOldestFrame().reset();
                }
                pageErrors++;
            }
//            System.out.println(physicalMemory);
        }
        System.out.println("Number of page errors: " + pageErrors);
    }

    public void aprLRU() {
        System.out.printf("%n %47s %n", "----- The aprLRU algorithm is running! -----");
        physicalMemory.reset();
        pageErrors = 0;
        int emptyFrameIdx = 0;
        LinkedList<Frame> frameQueue = new LinkedList<>();

        for (Integer page : virtualMemory.referenceList) {
//            System.out.print("|" + page + "| ");

            if (physicalMemory.contains(page)) {
                physicalMemory.resetLife(page);
            } else {
                if (emptyFrameIdx < physicalMemory.getNumberOfFrames()) {
                    Frame frame = physicalMemory.getFrame(emptyFrameIdx++);
                    frame.setPage(page);
                    frameQueue.add(frame);
                } else {
                    Frame frame = frameQueue.peek();
                    while (frame.hasTwoLifes()) {
                        frameQueue.add(frame);
                        frameQueue.getLast().setTwoLifes(false);
                        frameQueue.remove();
                        frame = frameQueue.peek();
                    }
                    frame.setPage(page);
                    frame.reset();
                    frameQueue.add(frameQueue.remove());
                }
                pageErrors++;
            }
//            System.out.println(physicalMemory);
        }
        System.out.println("Number of page errors: " + pageErrors);
    }


    public void RAND() {
        System.out.printf("%n %46s %n", "----- The RAND algorithm is running! -----");
        physicalMemory.reset();
        pageErrors = 0;
        int emptyFrameIdx = 0;
        Random random = new Random();
        int number;

        for (Integer page : virtualMemory.referenceList) {
            physicalMemory.updateStorageTime();
//            System.out.print("|" + page + "| ");
            if (physicalMemory.contains(page)) {
                physicalMemory.refreshFrameWithPage(page);
            } else {
                if (emptyFrameIdx < physicalMemory.getNumberOfFrames()) {
                    physicalMemory.getFrame(emptyFrameIdx++).setPage(page);
                } else {
                    number = random.nextInt(physicalMemory.getNumberOfFrames());
                    physicalMemory.getFrame(number).setPage(page);
                    physicalMemory.getFrame(number).reset();
                }
                pageErrors++;
            }
//            System.out.println(physicalMemory);
        }
        System.out.println("Number of page errors: " + pageErrors);
    }

}

