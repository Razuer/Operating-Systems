package Rest;

public class Frame {

    private final String name;
    private int storageTime;
    private Page page;

    Process process;

    public Frame(int whichOne) {
        this.name = "Main.Frame" + whichOne;
        this.storageTime = 0;
        this.page = null;
        this.process = null;
    }


    public boolean isEmpty() {
        return (page == null);
    }

    public int getStorageTime() {
        return storageTime;
    }

    public String getName() {
        return name;
    }

    public void setStorageTime(int storageTime) {
        this.storageTime = storageTime;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
        storageTime = 0;
    }

    public void addStorageTime() {
        storageTime++;
    }

    public void resetStorageTime() {
        storageTime = 0;
    }
    public void reset() {
        process = null;
        page=null;
        storageTime = 0;
    }

    @Override
    public String toString() {
        if (process == null) {
            if (isEmpty()) {
                return name + " - " + "brak";
            }
            return name + " - " + page + " - " + "brak" + " - " + storageTime + "s";
        }
        if(isEmpty()){
            return name + " - " + process.term;
        }
        return name + " - " + page + " - " + process.term + " - " + storageTime + "s";
    }
}
