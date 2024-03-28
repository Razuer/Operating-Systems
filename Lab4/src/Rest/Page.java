package Rest;

public class Page {
    Integer name;
    Process process;

    public Page(Integer name, Process process) {
        this.name = name;
        this.process = process;
    }
    public Page(Integer name) {
        this.name = name;
        this.process = new Process(-1);
    }

    public Integer getName() {
        return name;
    }

    public void setName(Integer name) {
        this.name = name;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    @Override
    public String toString() {
        return "(" + name + "," + process.name + ")";
    }
}
