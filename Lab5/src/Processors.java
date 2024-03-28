import java.util.ArrayList;
import java.util.Random;

public class Processors {
    ArrayList<Processor> processors = new ArrayList<>();

    public Processors(int N) {
        for (int i = 0; i < N; i++) {
            processors.add(new Processor(i));
        }
    }

    public Processor getRandom(Processor processor) {
        Random random = new Random();
        Processor processor1 = processors.get(random.nextInt(processors.size()));
        while (processor.equals(processor1)) {
            processor1 = processors.get(random.nextInt(processors.size()));
        }
        return processor1;
    }

    public Processor getRandom() {
        Random random = new Random();
        return processors.get(random.nextInt(processors.size()));
    }

    public Processor getRandomWithoutRepetitions(ArrayList<Processor> processors, Processor processor) {
        Random random = new Random();
        processors.remove(processor);
        Processor processor1 = processors.get(random.nextInt(processors.size()));
        processors.remove(processor1);

        return processor1;
    }

    public int updateLoopTime() {
        int counter = 0;
        for (Processor processor : processors) {
            for (int i = 0; i < processor.getProcesses().size(); i++) {
                Process process = processor.getProcesses().get(i);
                if (process.isActive()) {
                    process.setDuration(process.getDuration() - 1);
                    if (process.getDuration() == 0) {
                        counter++;
                        processor.setLoad(processor.getLoad() - process.getWeight());
                        process.deactivate();
                        processor.getProcesses().remove(process);
                        i--;
                    }
                }
            }
            if (!processor.waitingRoom.isEmpty()) {
                Process process = processor.waitingRoom.get(0);
                if (!(processor.getLoadAfterAdding(process) > 100)) {
                    processor.add(process);
                    processor.waitingRoom.remove(0);
                }
            }
        }
        return counter;
}

    public void reset() {
        for (Processor processor : processors) {
            processor.setLoad(0);
            processor.getProcesses().clear();
            processor.waitingRoom.clear();
        }
    }

    public double getAverageLoad() {
        int i = 0;
        for (Processor processor : processors) {
            i += processor.getLoad();
        }
        return (double) i / (double) processors.size();
    }

    public double getDeviation(double averageLoad) {
        int sum = 0;
        for (Processor processor : processors) {
            sum += Math.pow((double) processor.getLoad() - averageLoad, 2);
        }
        return Math.sqrt(sum / (double) processors.size());
    }

    public int maxLoad() {
        int i = 0;
        for (Processor processor : processors) {
            if (processor.getLoad() > i) i = processor.getLoad();
        }
        return i;
    }

    public Processor maxLoadProc() {
        int i = 0;
        Processor result = null;
        for (Processor processor : processors) {
            if (processor.getLoad() > i) {
                i = processor.getLoad();
                result = processor;
            }
        }
        return result;
    }

    public int askRandomProcessorAndStealSome(Processor processor, int howManyTries, int howMany, int p) {
        int count = 0;
        Random random = new Random();

        for (int j = 0; j < howManyTries; j++) {
            Processor giver = getRandom(processor);

            if (giver.getLoad() >= p-10) {
                if (giver.getProcesses().size() < howMany && giver.getProcesses().size() > 0) {
                    howMany = giver.getProcesses().size();
                }

                for (int i = 0; i < howMany; i++) {
                    Process toSteal = giver.getProcesses().remove(random.nextInt(giver.getProcesses().size()));
                    giver.setLoad(giver.getLoad() - toSteal.getWeight());

                    if (processor.getLoadAfterAdding(toSteal) < 100) {
                        processor.add(toSteal);
                        count++;
                    } else giver.add(toSteal);
                }
            }
        }
        return count;
    }
}
