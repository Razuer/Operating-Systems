import java.util.ArrayList;
import java.util.List;

public class Requests implements Cloneable {
    private final List<Request> requestList = new ArrayList<>();
    private final Head head;

    public Requests(Head head) {
        this.head = head;
    }

    public void generateList(int quantity, int maxArrival, boolean addPriorityRequests) {
        for (int i = 0; i < quantity; i++) {
            requestList.add(Request.generateRequest(head.getNumberOfCylinders(), maxArrival, addPriorityRequests));
        }
        sortArrivalTime();
        int whichRequest = 1;
        for (Request request : requestList) {
            request.setNumber(whichRequest++);
            request.setName("R" + request.getNumber());
        }
    }

    public void runFCFS(boolean simulate, boolean detailedStats) {
        System.out.printf("%n %43s %n", "----- Uruchomiono algorytm FCFS! -----");
        Requests toDoRequests = clone();
        Requests finishedRequests = new Requests(head);
        Requests activeRequests = new Requests(head);
        head.resetHead();
        toDoRequests.sortArrivalTime();

        while (!(activeRequests.requestList.isEmpty() && toDoRequests.requestList.isEmpty())) {
            activeRequests.addQueueTime();
            activeRequests.updateActiveRequests(toDoRequests);
            activeRequests.sortArrivalTime();

            if (activeRequests.isEmpty()) {
                head.standBy(1);
                if (simulate) head.printGraphicPositionSCAN(activeRequests);

            } else {
                head.directMoveToPosition(activeRequests.get(0), toDoRequests, activeRequests, simulate);
                if (simulate) head.printDirectGraphicPosition(activeRequests);

                activeRequests.finishOnlyOneRequestHere(finishedRequests);

//                activeRequests.finishAllRequestsHere(finishedRequests);
            }
        }
        finishedRequests.printStatistics(detailedStats);
    }

    public void runSSTF(boolean EDF, boolean FD_SCAN, boolean simulate, boolean detailedStats) {
        if (EDF) {
            System.out.printf("%n %46s %n", "----- Uruchomiono algorytm SSTF - EDF! -----");
        } else if (FD_SCAN) {
            System.out.printf("%n %43s %n", "----- Uruchomiono algorytm SSTF - FD-SCAN! -----");
        } else System.out.printf("%n %43s %n", "----- Uruchomiono algorytm SSTF! -----");
        Requests toDoRequests = clone();
        Requests finishedRequests = new Requests(head);
        Requests activeRequests = new Requests(head);
        head.resetHead();

        while (!(activeRequests.requestList.isEmpty() && toDoRequests.requestList.isEmpty())) {
            activeRequests.addQueueTime();
            activeRequests.updateActiveRequests(toDoRequests);
            activeRequests.updateDistanceToHead();
            if (EDF) {
                activeRequests.sortPriorityRequests();
                activeRequests.killDeadRequests(finishedRequests);
            }
            if (FD_SCAN) {
                activeRequests.sortPriorityRequests();
                activeRequests.killDeadRequests(finishedRequests);
                activeRequests.killInaccessibleRequests(finishedRequests);
            }
            if (activeRequests.requestList.isEmpty()) {
                head.standBy(1);
                if (simulate) head.printDirectGraphicPosition(activeRequests);

            } else {
                Request actRequest = activeRequests.get(0);
                head.moveOnceToPosition(actRequest);

                if (FD_SCAN) {
                    if (simulate) head.printGraphicPositionSCAN(activeRequests);
                    activeRequests.finishAllRequestsHere(finishedRequests);
                } else {
                    if (simulate) head.printDirectGraphicPosition(activeRequests);
                    if (head.getPosition() == actRequest.getPosition()) {
                        activeRequests.finishAllRequestsHere(finishedRequests);
                    }
                }
            }
        }
        finishedRequests.printStatistics(detailedStats);
    }

    public void runSCAN(boolean simulate, boolean detailedStats) {
        System.out.printf("%n %43s %n", "----- Uruchomiono algorytm SCAN! -----");
        Requests toDoRequests = clone();
        Requests finishedRequests = new Requests(head);
        Requests activeRequests = new Requests(head);
        head.resetHead();
        boolean moveRight = true;

        while (!(activeRequests.isEmpty() && toDoRequests.isEmpty())) {
            activeRequests.addQueueTime();
            activeRequests.updateActiveRequests(toDoRequests);

            if (moveRight) {
                head.moveOnceToPosition(head.getNumberOfCylinders());
                if (simulate) head.printGraphicPositionSCAN(activeRequests);
                activeRequests.finishAllRequestsHere(finishedRequests);
                if (head.getNumberOfCylinders() == head.getPosition()) moveRight = false;

            } else {
                head.moveOnceToPosition(0);
                if (simulate) head.printGraphicPositionSCAN(activeRequests);
                activeRequests.finishAllRequestsHere(finishedRequests);
                if (0 == head.getPosition()) moveRight = true;
            }
        }
        finishedRequests.printStatistics(detailedStats);
    }

    public void runC_SCAN(boolean simulate, boolean detailedStats) {
        System.out.printf("%n %44s %n", "----- Uruchomiono algorytm C-SCAN! -----");
        Requests toDoRequests = clone();
        Requests finishedRequests = new Requests(head);
        Requests activeRequests = new Requests(head);
        head.resetHead();
        boolean moveRight = true;
        int movesToLeft = 0;

        if (simulate) head.printGraphicPositionSCAN(activeRequests);

        while (!(activeRequests.isEmpty() && toDoRequests.isEmpty())) {
            activeRequests.addQueueTime();
            activeRequests.updateActiveRequests(toDoRequests);

            if (moveRight) {
                if (head.getPosition() == 0) {
                    if (simulate) head.printGraphicPositionSCAN(activeRequests);
                    activeRequests.finishAllRequestsHere(finishedRequests);
                }
                head.moveOnceToPosition(head.getNumberOfCylinders());
                if (simulate) head.printGraphicPositionSCAN(activeRequests);
                activeRequests.finishAllRequestsHere(finishedRequests);

                if (head.getNumberOfCylinders() == head.getPosition()) moveRight = false;

            } else {
//                head.directMoveToPosition(0, toDoRequests, activeRequests, simulate);

                head.setPosition(0);
                movesToLeft++;

                moveRight = true;
            }
        }
        finishedRequests.printStatistics(detailedStats);
        System.out.println("> Liczba natychmiastowych przesunięć: " + movesToLeft);
    }


    public void printRequests() {
        if (requestList.isEmpty()) {
            System.out.println("Brak żądań.");
        } else {
            System.out.println("Lista wszystkich żądań:");
            for (Request request : requestList) {
                System.out.println(request);
            }
        }
    }

    public void printActiveRequests() {
        if (requestList.isEmpty()) {
            System.out.println("Brak aktywnych żądań.");
        } else {
            System.out.println("Lista aktywnych żądań:");
            for (Request request : requestList) {
                System.out.println(request);
            }
        }
    }

    public void printStatistics(boolean detailedStats) {
        Requests unfinishedRequests = new Requests(head);
        if (detailedStats) printRequests();

        int queueSum = 0;
        int longestQueueTime = 0;
        int numberOfPriorities = 0;
        for (int i = 0; i < size(); i++) {
            queueSum += get(i).getQueueTime();
            if (get(i).getQueueTime() > longestQueueTime) longestQueueTime = get(i).getQueueTime();
            if (get(i).getDeadline()>0) numberOfPriorities++;
        }
        for (Request request : requestList) {
            if (!request.isFinished()) {
                unfinishedRequests.add(request);
            }
        }
        if (!unfinishedRequests.isEmpty() && detailedStats) {
            System.out.println("Nieukończone żądania:");
            for (Request request : unfinishedRequests.requestList) {
                System.out.println(request);
            }
        }

        System.out.println("\n> Wszystkie żądania zostały zrealizowane w: " + head.getClock().getTimeStamp());
        if (!unfinishedRequests.isEmpty()) {
            System.out.println("> Liczba priorytetowych żądań: " + numberOfPriorities);
            System.out.println("> Liczba nieukończonych żądań: " + unfinishedRequests.size());
        }
        System.out.println("> Średni czas oczekiwania: " + queueSum / size());
        System.out.println("> Najdłuższy czas oczekiwania: " + longestQueueTime);
        System.out.println("> Liczba ruchów głowicy: " + head.getNumberOfMoves());
    }

    @Override
    public Requests clone() {
        Requests requests = new Requests(head);
        for (Request request : this.requestList) {
            requests.requestList.add(request.clone());
        }
        return requests;
    }

    public void addQueueTime() {
        for (Request request : requestList) {
            request.setQueueTime(request.getQueueTime() + 1);
        }
    }

    private void finishAllRequestsHere(Requests finishedRequests) {
        while (isRequestHere(head.getPosition())) {
            Request actRequest = getRequestFromPosition(head.getPosition());
            actRequest.setFinished(true);
            finishedRequests.add(actRequest);
            remove(actRequest);
        }
    }

    private void finishOnlyOneRequestHere(Requests finishedRequests) {
        if (isRequestHere(head.getPosition())) {
            Request actRequest = getRequestFromPosition(head.getPosition());
            actRequest.setFinished(true);
            finishedRequests.add(actRequest);
            remove(actRequest);
        }
    }


    public Request get(int index) {
        return requestList.get(index);
    }

    public boolean isRequestHere(int position) {
        for (Request request : requestList) {
            if (request.getPosition() == position) return true;
        }
        return false;
    }

    private boolean isSearchedRequestHere(Request req, int position) {
        for (Request request : requestList) {
            if (request.getPosition() == position && request.equals(req)) return true;
        }
        return false;
    }

    public boolean isPriorityRequestHere(int position) {
        for (Request request : requestList) {
            if (request.getPosition() == position && request.getDeadline() > 0) return true;
        }
        return false;
    }

    private Request getRequestFromPosition(int position) {
        for (Request request : requestList) {
            if (request.getPosition() == position) return request;
        }
        return null;
    }

    private void killDeadRequests(Requests finishedRequests) {
        for (int i = 0; i < requestList.size(); i++) {
            Request request = get(i);
            if (request.getDeadline() > 0 && request.getDeadline() < head.getClock().getTimeStamp()) {
                finishedRequests.add(request);
                remove(request);
                i--;
            }
        }
    }

    private void killInaccessibleRequests(Requests finishedRequests) {
        for (int i = 0; i < requestList.size(); i++) {
            Request request = get(i);
            if (request.getDeadline() > 0 && ((Math.abs(request.getPosition() - head.getPosition())) > (request.getDeadline() - head.getClock().getTimeStamp()))) {
                finishedRequests.add(request);
                remove(request);
            }
        }
    }

    public int size() {
        return requestList.size();
    }

    private void remove(int index) {
        requestList.remove(index);
    }

    private void remove(Request o) {
        requestList.remove(o);
    }

    private void add(Request request) {
        requestList.add(request);
    }

    public boolean isEmpty() {
        return requestList.isEmpty();
    }

    private void sortArrivalTime() {
        requestList.sort(new ArrivalTimeComparator());
    }

    private void sortDistanceToHead() {
        requestList.sort(new DistanceToHeadComparator());
    }

    private void updateDistanceToHead() {
        for (Request request : requestList) {
            request.setDistanceToHead(Math.abs(head.getPosition() - request.getPosition()));
        }
        sortDistanceToHead();
    }

    private void sortPriorityRequests() {
        requestList.sort(new PriorityComparator());
    }

    public void updateActiveRequests(Requests source) {
        source.sortArrivalTime();
        for (int i = 0; i < source.size(); i++) {
            if (source.get(i).getArrivalTime() == head.getClock().getTimeStamp()) {
                add(source.get(i));
                source.remove(i);
                i--;
            }
        }
    }
}
