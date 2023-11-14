package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import model.Event;
import socket.GamingResponse;
import socket.Request;
import socket.Response;

/**
 * The `ServerHandler` class is responsible for handling server-related operations and extends the Thread class.
 * It can be used to manage server threads and custom server logic.
 */
public class ServerHandler extends Thread {

    private Socket socket;         // Class attribute to store the Socket
    private String currentUsername; // Class attribute to store the username
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private Gson gson;
    /**
     * Will be used to store game move
     * A RDBMS will be used to store game move in later milestones
     */
    public static Event gameEvent = new Event(1, null, null, null, null, -1);
    private final Logger logger;

    /**
     * Constructor for the `ServerHandler` class that accepts a Socket and a username.
     *
     * @param socket   The Socket associated with the client connection.
     * @param username The username of the client.
     */
    public ServerHandler(Socket socket, String username) {
        logger = Logger.getLogger(ServerHandler.class.getName());
        this.socket = socket;
        this.currentUsername = username;

        this.gson = new GsonBuilder().serializeNulls().create();

        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error initializing input/output streams: " + e.getMessage());
        }
    }
    /**
     * Runs immediately after the thread is started
     * The function continuously waits for a client request and sends a response
     * Until a client disconnects
     */
    @Override
    public void run() {
        // Keep accepting request until client disconnects are send invalid request
        while (true) {
            try {
                String serializedRequest = dataInputStream.readUTF(); // read/receive clients request (blocking operation)
                Request request = gson.fromJson(serializedRequest, Request.class); // deserialized the request
                logger.log(Level.INFO, "Client Request: " + currentUsername + " - " + request.getType());

                Response response = handleRequest(request); // get response to client's request
                String serializedResponse = gson.toJson(response); // serialize the response
                dataOutputStream.writeUTF(serializedResponse); // write/send the response
                dataOutputStream.flush(); // Flush the stream, force response to go
            } catch (EOFException e) {
                logger.log(Level.INFO, "Server Info: Client Disconnected: " + currentUsername + " - " + socket.getRemoteSocketAddress());
                close();
                break;
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Server Info: Client Connection Failed", e);
            } catch (JsonSyntaxException e) {
                logger.log(Level.SEVERE, "Server Info: Serialization Error", e);
            }
        }
    }
    /**
     * Closes the server handler. This method can be used to perform any cleanup or resource release operations.
     */
    public void close() {
        try {
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error closing resources: " + e.getMessage());
        }
    }

    /**
     * This method is the entry point for the server thread. It should be overridden to define the server's behavior.
     *
     * @param request The request to be handled by the server.
     * @return A response to the request.
     */
    public Response handleRequest(Request request) {
        switch (request.getType()) {
            case SEND_MOVE:
                int move = gson.fromJson(request.getData(), Integer.class);
                return handleSendMove(move);

            case REQUEST_MOVE:
                return handleRequestMove();

            default:
                return new Response(Response.ResponseStatus.FAILURE, "Invalid request type.");
        }
    }

    /**
     * Handles the "SEND_MOVE" request type.
     *
     * @param move The move to be sent.
     * @return A response indicating the result of the move operation.
     */
    public Response handleSendMove(int move) {
        if(move < 0 || move > 8){ // Check for valid move
            return new Response(Response.ResponseStatus.FAILURE, "Invalid Move");
        }
        if(gameEvent.getTurn() == null || !gameEvent.getTurn().equals(currentUsername)) {
            // Save the move in the server and return a standard Response
            gameEvent.setMove(move);
            gameEvent.setTurn(currentUsername);
            return new Response(Response.ResponseStatus.SUCCESS, "Move Added");
        }else{
            return new Response(Response.ResponseStatus.FAILURE, "Not your turn to move");
        }
    }

    /**
     * Handles the "REQUEST_MOVE" request type.
     *
     * @return A response indicating the result of the request for a move.
     */
    public Response handleRequestMove() {
        GamingResponse response = new GamingResponse();
        response.setStatus(Response.ResponseStatus.SUCCESS);
        // check if there is a valid move made by my opponent
        if (gameEvent.getMove() != -1 && !gameEvent.getTurn().equals(currentUsername)){
            response.setMove(gameEvent.getMove());
            // Delete the move
            gameEvent.setMove(-1);
            gameEvent.setTurn(null);
        }else{
            response.setMove(-1);
        }
        return response;
        }
    }
