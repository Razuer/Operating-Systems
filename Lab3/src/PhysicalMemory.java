import java.util.ArrayList;

public class PhysicalMemory {
    private final ArrayList<Frame> frameList = new ArrayList<>();
    private final int numberOfFrames;

    public PhysicalMemory(int numberOfFrames) {
        for (int i = 1; i <= numberOfFrames; i++) {
            Frame frame = new Frame(i);
            frameList.add(frame);
        }
        this.numberOfFrames = numberOfFrames;
    }

    public void replacePage(int frameIdx, int page) {
        getFrame(frameIdx).setPage(page);
        getFrame(frameIdx).reset();
    }

    public void refreshFrameWithPage(int page) {
        Frame frame = getFrame(findPageIdx(page));
        frame.reset();
        frame.setTwoLifes(true);
    }

    public void resetLife(int page) {
        Frame frame = getFrame(findPageIdx(page));
        frame.setTwoLifes(true);
    }

    public void updateStorageTime() {
        for (Frame frame : frameList) {
            if (!frame.isEmpty()) frame.addStorageTime();
        }
    }

    public void reset() {
        for (Frame frame : frameList) {
            frame.reset();
            frame.setPage(0);
            frame.setDistanceToReference(-1);
        }
    }

    public int getNumberOfFrames() {
        return numberOfFrames;
    }

    public Frame getFrame(int idx) {
        return frameList.get(idx);
    }

    public boolean contains(Integer page) {
        for (Frame frame : frameList) {
            if (frame.getPage() == page) return true;
        }
        return false;
    }

    public int findPageIdx(Integer page) {
        for (int i = 0; i < frameList.size(); i++) {
            if (getFrame(i).getPage() == page) return i;
        }
        return -1;
    }

    public Frame getOldestFrame() {
        Frame result = frameList.get(0);
        for (Frame frame : frameList) {
            if (frame.getStorageTime() > result.getStorageTime()) {
                result = frame;
            }
        }
        return result;
    }

    public void updateDistanceToReference(VirtualMemory virtualMemory, int actualReference) {
        ArrayList<Integer> list = virtualMemory.referenceList;

        for (Frame frame : frameList) {
            frame.setDistanceToReference(0);
            if (!frame.isEmpty()) {
                frame.addDistanceToReference();
                for (int i = actualReference + 1; i < list.size(); i++) {
                    if (list.get(i) == frame.getPage()) {
                        break;
                    }
                    frame.addDistanceToReference();
                }
            }
        }
    }

    public Frame getTheFurthestFrame() {
        Frame result = frameList.get(0);
        for (Frame frame : frameList) {
            if (frame.getDistanceToReference() > result.getDistanceToReference()) {
                result = frame;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return frameList.toString();
    }
}
