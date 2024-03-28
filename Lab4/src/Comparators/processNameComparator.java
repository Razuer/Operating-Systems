package Comparators;

import java.util.Comparator;
import Rest.*;
import Rest.Process;

public class processNameComparator implements Comparator<Process> {
    @Override
    public int compare(Process o1, Process o2) {
        return o1.getName()-o2.getName();
    }
}
