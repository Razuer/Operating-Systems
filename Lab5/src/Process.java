public class Process {
    private int weight;
    private int duration;
    private boolean active = true;
    private Processor processor;

    public Process(int weight, int duration, Processor processor) {
        this.weight = weight;
        this.duration = duration;
        this.processor = processor;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Processor getProcessor() {
        return processor;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    public void deactivate() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return "Process{" +
                "weight=" + weight +
                ", processor=" + processor +
                '}';
    }
    public Process clone(){
        return new Process(this.weight,this.duration,this.processor);
    }
}