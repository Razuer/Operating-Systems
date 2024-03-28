import java.util.Random;

public class Request implements Cloneable {
    private String name;
    private int number;
    private int position;
    private int arrivalTime;
    private int queueTime;
    private boolean finished = false;
    private int deadline;
    private int distanceToHead;

    public Request(int position, int arrivalTime, int deadline) {
        this.position = position;
        this.arrivalTime = arrivalTime;
        this.deadline = deadline;
    }

    public int getDistanceToHead() {
        return distanceToHead;
    }

    public void setDistanceToHead(int distanceToHead) {
        this.distanceToHead = distanceToHead;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getQueueTime() {
        return queueTime;
    }

    public void setQueueTime(int queueTime) {
        this.queueTime = queueTime;
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public static Request generateRequest(int maxPosition, int maxArrival, boolean generatePriorityRequests) {
        Random random = new Random();
        int deadline = 0;
        int arrival = random.nextInt(maxArrival+1);
        int position = random.nextInt(maxPosition+1);

        if (generatePriorityRequests) {
            if ((random.nextInt(99) + 1) < 25) {
                deadline = random.nextInt(50) + arrival;
            }
        }

        return new Request(position, arrival, deadline);
    }

    @Override
    public String toString() {
        if (deadline > 0) {
            return "Żądanie priorytetowe: " + name +
                    " - arrival: " + arrivalTime +
                    " - position: " + position +
                    " - queue: " + queueTime +
                    " - deadline: " + deadline +
                    " - finished: " + finished;

        } else return "Żądanie: " + name +
                " - arrival: " + arrivalTime +
                " - position: " + position +
                " - queue: " + queueTime +
                " - finished: " + finished;
    }

    @Override
    public Request clone() {
        try {
            return (Request) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
