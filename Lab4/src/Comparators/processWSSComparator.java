package Comparators;

import java.util.Comparator;
import Rest.Process;
public class processWSSComparator implements Comparator<Process> {
    @Override
    public int compare(Process o1, Process o2) {
        return o1.getWSS()-o2.getWSS();
    }
}
