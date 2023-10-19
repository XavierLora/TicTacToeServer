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
        testDefaultConstructor();
        testParameterizedConstructor();
        testGettersAndSetters();
    }

    /**
     * Tests the default constructor of the `Request` class.
     */
    public static void testDefaultConstructor() {
        Request request = new Request();
        assert request.getType() == Request.RequestType.LOGIN;
        assert request.getData() == null;
    }

    /**
     * Tests the parameterized constructor of the `Request` class.
     */
    public static void testParameterizedConstructor() {
        Request request = new Request(Request.RequestType.REGISTER, "serializedData");
        assert request.getType() == Request.RequestType.REGISTER;
        assert request.getData().equals("serializedData");
    }

    /**
     * Tests the getters and setters of the `Request` class.
     */
    public static void testGettersAndSetters() {
        Request request = new Request();
        request.setType(Request.RequestType.SEND_INVITATION);
        request.setData("invitationData");
        assert request.getType() == Request.RequestType.SEND_INVITATION;
        assert request.getData().equals("invitationData");
    }
}
