import java.util.ArrayList;

public class Processor {
    private String name;
    private int load;
    private ArrayList<Process> processes;
    public ArrayList<Process> waitingRoom = new ArrayList<>();

    public Processor(int whichOne){
        name = "Processor " + (whichOne+1);
        load = 0;
        processes = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    public ArrayList<Process> getProcesses() {
        return processes;
    }

    public void setProcesses(ArrayList<Process> processes) {
        this.processes = processes;
    }

    public void add(Process process){
        processes.add(process);
        load += process.getWeight();
    }

    public int getLoadAfterAdding(Process process){
        return (process.getWeight() + getLoad());
    }

    @Override
    public String toString() {
        return name +" " + load;
    }
}
