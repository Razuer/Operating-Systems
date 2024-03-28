import java.util.ArrayList;
import java.util.Random;

public class VirtualMemory {
    ArrayList<Integer> referenceList = new ArrayList<>();

    public VirtualMemory(int size, int maxReferenceNumber) {
        Random random = new Random();
        int fuks = 0;
        int fuksLiczby = 0;

        while (referenceList.size() < size) {
            double rand = random.nextDouble();
            if (rand > 0.85 && referenceList.size() > 5) {
                fuks++;
                for (int i = 0; i < random.nextInt(6) + 5; i++) {
                    if (referenceList.size() == size) break;
                    fuksLiczby++;
                    rand = random.nextDouble();
                    if (rand < 0.3) {
                        referenceList.add(referenceList.get(referenceList.size() - 1 - i));
                    } else if (rand < 0.5) {
                        referenceList.add(referenceList.get(referenceList.size() - 2 - i));
                    } else if (rand < 0.8) {
                        referenceList.add(referenceList.get(referenceList.size() - 3 - i));
                    } else {
                        referenceList.add(referenceList.get(referenceList.size() - 4 - i));
                    }
                }
            } else referenceList.add(random.nextInt(maxReferenceNumber) + 1);
        }
        System.out.println("Liczba fuksow: " + fuks + " - liczba fuksliczb: " + fuksLiczby);
        System.out.println("Liczb: " + referenceList.size());
    }

    public void printReferenceList() {
        System.out.println(referenceList);
    }

    @Override
    public String toString() {
        return referenceList.toString();
    }
}
