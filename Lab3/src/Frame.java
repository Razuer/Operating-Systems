public class Frame {

    private final String name;
    private int storageTime;
    private int page;
    private int distanceToReference;
    private boolean twoLifes;

    public Frame(int whichOne) {
        this.name = "Frame" + whichOne;
        this.storageTime = 0;
        this.page = 0;
        this.distanceToReference = 0;
    }

    public boolean hasTwoLifes() {
        return twoLifes;
    }

    public void setTwoLifes(boolean twoLifes) {
        this.twoLifes = twoLifes;
    }
    public boolean isEmpty() {
        return (page == 0);
    }

    public int getStorageTime() {
        return storageTime;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getDistanceToReference() {
        return distanceToReference;
    }

    public void setDistanceToReference(int distanceToReference) {
        this.distanceToReference = distanceToReference;
    }
    public void addDistanceToReference(){
        distanceToReference++;
    }

    public void addStorageTime(){
        storageTime++;
    }

    public void reset(){
        storageTime = 0;
        twoLifes = true;
    }

    @Override
    public String toString() {
        if(isEmpty()){
            return name;
        }
        if(distanceToReference == -1){
            return name + " - " + page + " - " + storageTime + "s" + " 2lifes: " + twoLifes;
        }
        return name + " - " + page + " - " + storageTime + "s - distance: " + distanceToReference;
    }
}
