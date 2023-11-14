package test;
import server.SocketServer;

/**
 * The `SocketServerTest` class is responsible for testing the functionality of the `SocketServer` class.
 */
public class SocketServerTest {

    /**
     * The main method that runs the test methods for the `SocketServer` class.
     *
     * @param args Command-line arguments (not used in this example).
     */
    public static void main(String[] args) throws Exception {
        testDefaultConstructor();
        testParameterizedConstructor();
        testNegativePort();
    }

    /**
     * Tests the default constructor of the `SocketServer` class.
     */
    public static void testDefaultConstructor() throws Exception {
        SocketServer server = new SocketServer();
        assert server.getPort() == 5000;
    }

    /**
     * Tests the parameterized constructor of the `SocketServer` class.
     */
    public static void testParameterizedConstructor() throws Exception {
        SocketServer server = new SocketServer(8080);
        assert server.getPort() == 8080;
    }

    /**
     * Tests the behavior when a negative port is provided to the constructor.
     * Expects an `IllegalArgumentException` to be thrown.
     */
    public static void testNegativePort() {
        try {
            SocketServer server = new SocketServer(-123);
            System.err.println("Error: No exception thrown for a negative port");
        } catch (Exception e) {
            System.out.println("Successfully caught an exception for a negative port: " + e.getMessage());
        }
    }
}
