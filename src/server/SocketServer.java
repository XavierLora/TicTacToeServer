package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * The `SocketServer` class represents a server application that listens for and handles incoming socket requests.
 */
public class SocketServer {

    /**
     * The default port number for the server.
     */
    private final int PORT;

    /**
     * Logger for the server class
     */
    private static final Logger logger = Logger.getLogger(SocketServer.class.getName());
    /**
     * Main method to start the server.
     *
     */

    //Server Socket
    private ServerSocket serverSocket;
    public static void main(String[] args) {
        SocketServer socketServer = new SocketServer();
        socketServer.setup();
        socketServer.startAcceptingRequest();
    }


    /**
     * Default constructor for the `SocketServer` class. It initializes the server with the default port number.
     */
    public SocketServer() {
        this.PORT = 5650;
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
        this.PORT = PORT;
    }

    /**
     * Sets up the server. Override this method to configure server settings and initialize resources.
     */
    public void setup() {
        try{
            serverSocket = new ServerSocket(PORT);

            //Get Server Info
            InetAddress localhost = InetAddress.getLocalHost();
            String hostname = localhost.getHostName();
            String hostAddress = localhost.getHostAddress();

            logger.log(Level.INFO, "Server started on host: " + hostname + ", address: " + hostAddress + ", port: " + PORT);
        }catch (IOException e){
            logger.log(Level.SEVERE, "Error setting up the server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Starts accepting incoming requests on the server. Override this method to define request-handling logic.
     */
    public void startAcceptingRequest() {
        try {
            int clientCount = 0;
            while (clientCount < 2) {
                // Accept a client connection
                Socket socket = serverSocket.accept();
                clientCount++;

                // Generate a unique username for each client (e.g., User1, User2)
                String username = "User" + clientCount;

                logger.log(Level.INFO, "Accepted client connection for " + username);

                // Create a ServerHandler thread for this client connection
                ServerHandler handler = new ServerHandler(socket, username);

                // Start the thread to handle the client connection
                handler.start();
            }
        }catch(IOException e){
            logger.log(Level.SEVERE, "Error accepting client connections: "  + e.getMessage());
        }
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
