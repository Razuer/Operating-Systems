public class Main {
    public static void main(String[] args) {

        Requests requests = new Requests(new Head(180));
        requests.generateList(200, 1500, true);


//        requests.runFCFS(false, false);

        requests.runSSTF(false, false, true, false);

//        requests.runSSTF(true, false, false, false);
//
//        requests.runSSTF(false, true, false, false);
//
//        requests.runSCAN(false, false);
//
//        requests.runC_SCAN(false, false);

    }
}
