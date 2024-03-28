package Rest;

import java.util.ArrayList;
import java.util.Objects;

public class Process {
    int name;
    String term;
    int errors = 0;
    ArrayList<Page> pages;
    int framesAcquired = 0;
    double PFF = 0;
    boolean active = true;
    int WSS = 0;
    int references = 0;

    public int getReferences() {
        return references;
    }

    public void addReference() {
        references++;
    }

    public int getWSS() {
        return WSS;
    }

    public void setWSS(int WSS) {
        this.WSS = WSS;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getPFF() {
        return PFF;
    }

    public void setPFF(double PFF) {
        this.PFF = PFF;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public ArrayList<Page> getPages() {
        return pages;
    }

    public void setPages(ArrayList<Page> pages) {
        this.pages = pages;
    }

    public void setErrors(int errors){
        this.errors = errors;
    }

    public void addError(){
        errors++;
    }

    public int getErrors() {
        return errors;
    }
    boolean isNowUsed = false;

    public boolean isNowUsed() {
        return isNowUsed;
    }

    public void setNowUsed(boolean nowUsed) {
        isNowUsed = nowUsed;
    }

    public Process(int name) {
        this.name = name;
        this.term = "Main.Process " + name;
        this.pages = new ArrayList<>();
    }

    public void addPages(int range, Process process){
        for(int i = 1; i<=range; i++){
            pages.add(new Page(i, process));
        }
    }

    public int getFramesAcquired() {
        return framesAcquired;
    }

    public void setFramesAcquired(int framesAcquired) {
        this.framesAcquired = framesAcquired;
    }

    @Override
    public String toString() {
        return term;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Process)) return false;
        Process process = (Process) o;
        return getName() == process.getName() && getErrors() == process.getErrors() && getFramesAcquired() == process.getFramesAcquired() && Double.compare(process.getPFF(), getPFF()) == 0 && isActive() == process.isActive() && getWSS() == process.getWSS() && Objects.equals(getTerm(), process.getTerm()) && Objects.equals(getPages(), process.getPages());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getTerm(), getErrors(), getPages(), getFramesAcquired(), getPFF(), isActive(), getWSS());
    }
}
