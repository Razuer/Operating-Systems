package All;

public class Clock {
    private int timeStamp = 0;

    public Clock() {
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void endLoop(){
        timeStamp++;
    }

    public void printTime(){
        System.out.printf("%n %40s %n" , "--- Aktualny czas: " + getTimeStamp() + " ---");    }

    public void printTime(boolean printIt){
        if(printIt) System.out.printf("%n %40s %n" , "--- Aktualny czas: " + getTimeStamp() + " ---");    }
}
