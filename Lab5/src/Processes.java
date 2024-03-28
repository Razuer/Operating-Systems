import java.util.ArrayList;
import java.util.Random;

public class Processes {
    ArrayList<Process> processList = new ArrayList<>();

    public Processes(int size, int N, Processors processors) {
        Random random = new Random();

        for(int i=0;i<size;i++){
            int weight = random.nextInt(1,9);
            int duration = random.nextInt(1600)+(300);
//            int process = random.nextInt(N);
            processList.add(new Process(weight, duration, processors.getRandom()));
        }
    }

    public Processes() {
    }

    public void howManyActive(){
        int counter = 0;
        for(Process process : processList){
//            if(process.isActive()) counter++;
            counter+=process.getDuration();
        }

        System.out.println(counter);
    }
    public void skip(int howBig){
        int size = processList.size();
        Process process = processList.remove(0);
        if (size > howBig) {
            processList.add((howBig-1), process);
        } else {
            processList.add(process);
        }
    }

    public Processes clone(){
        Processes processes = new Processes();
        for (int i = 0; i < this.processList.size(); i++) {
            processes.processList.add(this.processList.get(i).clone());
        }
        return processes;
    }

    @Override
    public String toString() {
        return processList.toString();
    }
}
