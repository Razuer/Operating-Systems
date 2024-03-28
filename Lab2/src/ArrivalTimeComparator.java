import java.util.Comparator;

public class ArrivalTimeComparator implements Comparator<Request> {
    @Override
    public int compare(Request o1, Request o2) {

        return o1.getArrivalTime()-o2.getArrivalTime();
    }
}