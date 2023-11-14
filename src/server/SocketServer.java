package server;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Enumeration;

/**
 *  The main class for TicTacToe Server that sets up the socket server
 *
 * @author Ahmad Suleiman
 */
public class SocketServer {
    /**
     * Used for printing server logs of different levels
     */
    private final Logger LOGGER;

    /*
    The socket server's port number
     */
    private final int PORT;

    /**
     * ServerSocket instance
     */
    private ServerSocket serverSocket;

    /**
     * The main function of the application
     * It instantiates the class, sets up the server and start accepting client's request
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try {
            SocketServer socketServer = new SocketServer();
            socketServer.setup();
            socketServer.startAcceptingRequest();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Default constructor with default port = 5000
     *
     * @throws Exception when invalid port is provided
     */
    public SocketServer() throws Exception{
        this(5850);
    }

    /**
     * Constructor that set the {@link #PORT} attribute
     *
     * @param port The socket server's port number
     * @throws Exception when invalid port is provided
     */
    public SocketServer(int port) throws Exception{
        if(port < 0){
            throw new Exception("Port number cannot be negative");
        }
        PORT = port;
        LOGGER = Logger.getLogger(SocketServer.class.getName());
    }

    /**
     * Sets up the socket server
     */
    private void setup() {
        try {
            serverSocket = new ServerSocket(PORT);
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            String localIP = "Unknown";

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> interfaceAddresses = networkInterface.getInetAddresses();

                while (interfaceAddresses.hasMoreElements()) {
                    InetAddress address = interfaceAddresses.nextElement();

                    if (!address.isLoopbackAddress() && !address.isLinkLocalAddress() && !address.isMulticastAddress()) {
                        localIP = address.getHostAddress();
                        break;
                    }
                }

                if (!localIP.equals("Unknown")) {
                    break;
                }
            }

            LOGGER.log(Level.INFO, "Server started on local IP: " + localIP + ", port: " + PORT);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error setting up the server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Start accepting client's request
     */
    private void startAcceptingRequest() {
        try {
            // Accept socket connection from the first player and create a new handler to handle all connections
            Socket socketPlayer1 = serverSocket.accept();
            LOGGER.log(Level.INFO,"New Socket Client Connect with IP: " + socketPlayer1.getRemoteSocketAddress());
            ServerHandler serverHandlerPlayer1 = new ServerHandler(socketPlayer1, "Bob");
            serverHandlerPlayer1.start();

            // Accept socket connection from the second player and create a new handler to handle all connections
            Socket socketPlayer2 = serverSocket.accept();
            LOGGER.log(Level.INFO,"New Socket Client Connect with IP: " + socketPlayer2.getRemoteSocketAddress());
            ServerHandler serverHandlerPlayer2 = new ServerHandler(socketPlayer2, "Smith");
            serverHandlerPlayer2.start();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE,"Server Error: Client Connection Failed", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,"Server Error: Unknown Exception Occurred", e);
        }
    }

    /**
     * Getter for PORT attribute
     *
     * @return PORT
     */
    public int getPort() {
        return PORT;
    }
}
