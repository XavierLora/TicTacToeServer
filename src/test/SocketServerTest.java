package test;
import server.SocketServer;

/**
 * The `SocketServerTest` class is responsible for testing the functionality of the `SocketServer` class.
 */
public class SocketServerTest {

    public static void main(String[] args) throws Exception {

        /*
         * Tests constructors
         */
        SocketServer server1 = new SocketServer();

        System.out.println("SocketServer 1: Testing Default Constructor");
        System.out.println(((server1.getPORT()==5000) ? "PASSED":"FAILED") + ": Default port set");

        SocketServer server2 = new SocketServer(7000);

        System.out.println("SocketServer 2: Testing Parameterize Constructor");
        System.out.println(((server2.getPORT()==7000) ? "PASSED":"FAILED") + ": port set");
    }
}
