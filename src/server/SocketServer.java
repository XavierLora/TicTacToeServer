package server;

/**
 * The `SocketServer` class represents a server application that listens for and handles incoming socket requests.
 */
public class SocketServer {

    /**
     * The default port number for the server.
     */
    public static int PORT = 5000;

    /**
     * Main method to start the server.
     *
     * @param args Command-line arguments (not used in this example).
     */
    public static void main(String[] args) {
        SocketServer socketServer = new SocketServer();
        socketServer.setup();
        socketServer.startAcceptingRequest();
    }

    /**
     * Default constructor for the `SocketServer` class. It initializes the server with the default port number.
     */
    public SocketServer() {
        this(PORT);
    }

    /**
     * Parameterized constructor for the `SocketServer` class. It allows setting a custom port number for the server.
     *
     * @param PORT The port number on which the server will listen.
     */
    public SocketServer(int PORT) {
        if(PORT <0){
            throw new IllegalArgumentException("Port number cannot be negative");
        }
        SocketServer.PORT = PORT;
    }

    /**
     * Sets up the server. Override this method to configure server settings and initialize resources.
     */
    public void setup() {
        // Implement server setup logic here
    }

    /**
     * Starts accepting incoming requests on the server. Override this method to define request-handling logic.
     */
    public void startAcceptingRequest() {
        // Implement request-handling logic here
    }

    /**
     * Gets the port number on which the server is listening.
     *
     * @return The port number of the server.
     */
    public int getPort() {
        return PORT;
    }
}
