package All;

import java.util.Objects;
import java.util.Random;

public class Process implements Cloneable {
    private String name;
    private int number;
    private int durationTime;
    private int arrivalTime;
    private int remainingTime;
    private int queueTime;
    private int inactiveQueueTime;

    public Process(int number, int durationTime, int arrivalTime) {
        this.name = "P" + number;
        this.number = number;
        this.durationTime = durationTime;
        this.arrivalTime = arrivalTime;
        this.remainingTime = durationTime;
        this.queueTime = 0;
        this.inactiveQueueTime = 0;
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

    public int getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(int durationTime) {
        this.durationTime = durationTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getQueueTime() {
        return queueTime;
    }

    public void setQueueTime(int queueTime) {
        this.queueTime = queueTime;
    }

    public int getInactiveQueueTime() {
        return inactiveQueueTime;
    }

    public void setInactiveQueueTime(int inactiveQueueTime) {
        this.inactiveQueueTime = inactiveQueueTime;
    }

//    @Override
//    public String toString() {
//        return "Nazwa: " + name +
//                "\nCzas trwania: " + durationTime +
//                "\nCzas wejścia: " + arrivalTime +
//                "\nPozostały czas: " + remainingTime +
//                "\nCzas w kolejce: " + queueTime + "\n";
//    }

    @Override
    public String toString() {
            return "Proces: " + name +
                    " - duration: " + durationTime +
                    " - arrival: " + arrivalTime +
                    " - remaining: " + remainingTime +
                    " - queue: " + queueTime +
                    " - inactive: " + inactiveQueueTime;
    }

    public static Process generateProcess(int whichProcess, int maxDuration, int maxArrival) {
        Random random = new Random();
        int duration = random.nextInt(maxDuration - 1) + 1;
        int arrival = random.nextInt(maxArrival);
        return new Process(whichProcess, duration, arrival);
    }

    public void doOnce() {
        remainingTime--;
    }

    @Override
    public Process clone() {
        try {
            return (Process) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Process)) return false;
        Process process = (Process) o;
        return getNumber() == process.getNumber() && getDurationTime() == process.getDurationTime() && getArrivalTime() == process.getArrivalTime() && getRemainingTime() == process.getRemainingTime() && getQueueTime() == process.getQueueTime() && getInactiveQueueTime() == process.getInactiveQueueTime() && Objects.equals(getName(), process.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getNumber(), getDurationTime(), getArrivalTime(), getRemainingTime(), getQueueTime(), getInactiveQueueTime());
    }
}