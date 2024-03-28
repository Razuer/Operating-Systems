package All.Comparators;

import java.util.Comparator;
import All.Process;
public class RemainingTimeZerosComparator implements Comparator<Process> {
    @Override
    public int compare(Process o1, Process o2) {
        if(o2.getRemainingTime()==0) return -1;

        if(o1.getRemainingTime()==0) return 1;

        return 0;
    }
}
