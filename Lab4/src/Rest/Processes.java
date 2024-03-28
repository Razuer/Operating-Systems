package Rest;

import Comparators.processFramesComparator;
import Comparators.processNameComparator;
import Comparators.processSizeComparator;
import Comparators.processWSSComparator;

import java.util.ArrayList;
import java.util.Random;

public class Processes {
    int count;
    ArrayList<Process> processes;


    public Processes(int count) {
        this.count = count;

        processes = new ArrayList<>();

        for(int i=0; i<count; i++){
            processes.add(new Process(i+1));
        }
    }
    public void resetErrors(){
        for(Process process : processes){
            process.setErrors(0);
            process.setActive(true);
        }
    }
    public void resetAcquiredPages(){
        for(Process process : processes){
            process.framesAcquired = 0;
        }
    }
    public void reset(){
        for(Process process : processes){
            process.setErrors(0);
            process.setActive(true);
            process.framesAcquired = 0;
        }
    }
    public void setAllUnused(){
        for(Process process : processes){
            process.setNowUsed(false);
        }
    }

    public void sortBySize(){
        processes.sort(new processSizeComparator());
    }
    public void sortByName(){
        processes.sort(new processNameComparator());
    }
    public void sortByFramesAcquired(){
        processes.sort(new processFramesComparator());
    }
    public void sortByWSS(){
        processes.sort(new processWSSComparator());
    }

    public Process getRandomProcessBasedOnSize(){
        sortBySize();
        Random random = new Random();

        double rand = random.nextDouble();
        int part = processes.size()/4;

        if(rand > 0.5){
            return processes.get(random.nextInt(0,part));
        } else if(rand > 0.3){
            return processes.get(random.nextInt(part, part*2));
        } else return processes.get(random.nextInt(part*2, processes.size()));
    }
}
