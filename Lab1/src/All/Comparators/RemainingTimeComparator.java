package All.Comparators;

import java.util.Comparator;
import All.Process;
public class RemainingTimeComparator implements Comparator<Process> {

    @Override
    public int compare(Process o1, Process o2) {
        if(o2.getRemainingTime()==0) return -1;

        if(o1.getRemainingTime()==0) return 1;

        else return o1.getRemainingTime()-o2.getRemainingTime();
    }
}

