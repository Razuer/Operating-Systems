import java.util.Comparator;

public class PriorityComparator implements Comparator<Request> {
    @Override
    public int compare(Request o1, Request o2) {
        if (o2.getDeadline()>0) return 1;

        if (o1.getDeadline()>0) return -1;


        return 0;
    }
}
