package test;
import server.SocketServer;

public class SocketServerTest {
    public static void main(String[] args) {
        testDefaultConstructor();
        testParameterizedConstructor();
        testNegativePort();
    }

    public static void testDefaultConstructor() {
        SocketServer server = new SocketServer();
        assert server.getPort() == 5000;
    }

    public static void testParameterizedConstructor() {
        SocketServer server = new SocketServer(8080);
        assert server.getPort() == 8080;
    }

    public static void testNegativePort() {
        try {
            SocketServer server = new SocketServer(-123);
            System.err.println("Error: No exception thrown for a negative port");
        } catch (IllegalArgumentException e) {
            System.out.println("Successfully caught an exception for a negative port: " + e.getMessage());
        }
    }
}
