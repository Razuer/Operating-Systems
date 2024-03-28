package Comparators;

import java.util.Comparator;
import Rest.Process;
public class processSizeComparator implements Comparator<Process> {
    @Override
    public int compare(Process o1, Process o2) {
        return o2.getPages().size() - o1.getPages().size();
    }
}
