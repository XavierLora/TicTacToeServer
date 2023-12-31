package test;
import socket.Request;

/**
 * The `RequestTest` class is responsible for testing the functionality of the `Request` class.
 */
public class RequestTest {

    /**
     * The main method that runs the test methods for the `Request` class.
     *
     * @param args Command-line arguments (not used in this example).
     */
    public static void main(String[] args) {

        /*
         * Tests constructors
         */
        Request request1 = new Request();

        System.out.println("Request 1: Testing Default Constructor");
        System.out.println(((request1.getType()==null) ? "PASSED":"FAILED") + ": type");
        System.out.println(((request1.getData()==null) ? "PASSED":"FAILED") + ": data");

        Request request2 = new Request(Request.RequestType.SEND_INVITATION, "bob");

        System.out.println("Request 2: Testing Parameterized Constructor");
        System.out.println(((request2.getType()==Request.RequestType.SEND_INVITATION) ? "PASSED":"FAILED") + ": type");
        System.out.println(((request2.getData().equals("bob")) ? "PASSED":"FAILED") + ": data");

        /*
         * Tests all getters and setters
         */
        Request request3 = new Request();
        request3.setType(Request.RequestType.SEND_INVITATION);
        request3.setData("bob");

        System.out.println("Request 3: Testing Getters and Setters");
        System.out.println(((request3.getType()==Request.RequestType.SEND_INVITATION) ? "PASSED":"FAILED") + ": type");
        System.out.println(((request3.getData().equals("bob")) ? "PASSED":"FAILED") + ": data");

    }

}
