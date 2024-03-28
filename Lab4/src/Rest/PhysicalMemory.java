package Rest;

import java.util.ArrayList;

public class PhysicalMemory {
    final ArrayList<Frame> frameList = new ArrayList<>();
    private final int numberOfFrames;

    public PhysicalMemory(int numberOfFrames) {
        for (int i = 1; i <= numberOfFrames; i++) {
            Frame frame = new Frame(i);
            frameList.add(frame);
        }
        this.numberOfFrames = numberOfFrames;
    }

    public void updateStorageTime() {
        for (Frame frame : frameList) {
            if (!frame.isEmpty()) frame.addStorageTime();
        }
    }

    public void reset() {
        for (Frame frame : frameList) {
            frame.reset();
        }
    }

    public int getNumberOfFrames() {
        return numberOfFrames;
    }

    public Frame getFrame(int idx) {
        return frameList.get(idx);
    }

    public boolean contains(Page page) {
        for (Frame frame : frameList) {
            if (frame.getPage() == page) {
                frame.setStorageTime(0);
                return true;
            }
        }
        return false;
    }

    public int findPageIdx(Page page) {
        for (int i = 0; i < frameList.size(); i++) {
            if (getFrame(i).getPage().equals(page)) return i;
        }
        return -1;
    }

    public Frame getFreeFrame() {
        for (Frame frame : frameList) {
            if (frame.getProcess() == null) return frame;
        }
        return null;
    }


    public Frame getNextFrame(Process process) {
        Frame result = getProcessFrame(process);
        if (result != null && process.isActive()) {
            for (Frame frame : frameList) {
                if (frame.process == process) {
                    if (frame.getPage() == null) {
                        return frame;
                    }
                    result = frame;
                }
            }

            for (Frame frame : frameList) {
                if (frame.process == (process)) {
                    if (frame.getStorageTime() >= result.getStorageTime()) {
                        result = frame;
                    }
                }
            }
        }
        return result;
    }

    public Frame getProcessFrame(Process process) {
        for (Frame frame : frameList) {
            if (frame.process == process) return frame;
        }
        return null;
    }

    public int howManyFreeFrames(){
        int i = 0;
        for(Frame frame : frameList){
            if(frame.getProcess() == null) i++;
        }
        return i;
    }



    @Override
    public String toString() {
        return frameList.toString();
    }
}
