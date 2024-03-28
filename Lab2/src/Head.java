public class Head {
    private Clock clock = new Clock();
    private int position = 0;
    private int numberOfCylinders;

    private int numberOfMoves = 0;


    public Head(int numberOfCylinders) {
        this.numberOfCylinders = numberOfCylinders;
    }

    public Clock getClock() {
        return clock;
    }

    public int getPosition() {
        return position;
    }

    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    public void setNumberOfMoves(int numberOfMoves){
        this.numberOfMoves = numberOfMoves;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getNumberOfCylinders() {
        return numberOfCylinders;
    }

    public void setNumberOfCylinders(int numberOfCylinders) {
        this.numberOfCylinders = numberOfCylinders;
    }

    public void directMoveToPosition(int destination, Requests toDoRequests, Requests activeRequests, boolean simulate) {
        while (position != destination) {
            if (destination > position) {
                activeRequests.updateActiveRequests(toDoRequests);
                position++;
                numberOfMoves++;
                clock.endLoop();
                activeRequests.addQueueTime();
                if (position != destination) {
                    if(simulate) printGraphicPositionOnly(activeRequests);
                }
            }
            if (destination < position) {
                activeRequests.updateActiveRequests(toDoRequests);
                position--;
                numberOfMoves++;
                clock.endLoop();
                activeRequests.addQueueTime();
                if (position != destination) {
                    if(simulate) printGraphicPositionOnly(activeRequests);
                }
            }
        }
    }

    public void directMoveToPosition(Request request, Requests toDoRequests, Requests activeRequests, boolean simulate) {
        int destination = request.getPosition();
        directMoveToPosition(destination, toDoRequests, activeRequests, simulate);
    }

    public void moveOnceToPosition(int destination) {
        if (destination > position) {
            position++;
            numberOfMoves++;
            clock.endLoop();
        }
        if (destination < position) {
            position--;
            numberOfMoves++;
            clock.endLoop();
        }
    }

    public void moveOnceToPosition(Request request) {
        int destination = request.getPosition();
        moveOnceToPosition(destination);
    }

    public void standBy(int howLong) {
        for (int i = 0; i < howLong; i++) {
            clock.endLoop();
        }
    }

    public void resetHead() {
        setPosition(0);
        setNumberOfMoves(0);
        clock.resetTime();
    }

    public void printPosition() {
        System.out.println("Głowica znajduje się na pozycji: " + position);
    }

    public void printGraphicPositionSCAN(Requests requests) {
        for (int i = 0; i <= getNumberOfCylinders(); i++) {
            if (i == getPosition()) {
                if (requests.isRequestHere(i)) {
                    System.out.print("X");
                } else System.out.print("v");

            } else {
                if (requests.isRequestHere(i)) {
                    if(requests.isPriorityRequestHere(i)){
                        System.out.print("P");
                    } else System.out.print("o");
                } else System.out.print("-");
            }
        }
        System.out.println(" time: " + clock.getTimeStamp() + " pos: " + getPosition());
    }

    public void printDirectGraphicPosition(Requests requests) {
        for (int i = 0; i <= getNumberOfCylinders(); i++) {
            if (i == getPosition()) {
                if (!requests.isEmpty() && requests.get(0).getPosition() == i) {
                    System.out.print("X");
                } else System.out.print("v");

            } else {
                if (requests.isRequestHere(i)) {
                    if(requests.isPriorityRequestHere(i)){
                        System.out.print("P");
                    } else System.out.print("o");
                } else System.out.print("-");
            }
        }
        System.out.println(" time: " + clock.getTimeStamp() + " pos: " + getPosition());
    }

    public void printGraphicPositionOnly(Requests requests) {
        for (int i = 0; i <= getNumberOfCylinders(); i++) {
            if (i == getPosition()) {
                System.out.print("v");
            } else {
                if (requests.isRequestHere(i)) {
                    if(requests.isPriorityRequestHere(i)){
                        System.out.print("P");
                    } else System.out.print("o");
                } else System.out.print("-");
            }
        }
        System.out.println(" time: " + clock.getTimeStamp() + " pos: " + getPosition());
    }
}
