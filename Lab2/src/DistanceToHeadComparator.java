import java.util.Comparator;

public class DistanceToHeadComparator implements Comparator<Request> {
    @Override
    public int compare(Request o1, Request o2) {

        return o1.getDistanceToHead()-o2.getDistanceToHead();
    }
}
