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
import model.Event;
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
    private static Event gameEvent;
    private static final Logger logger = Logger.getLogger(ServerHandler.class.getName());

    /**
     * Constructor for the `ServerHandler` class that accepts a Socket and a username.
     *
     * @param socket The Socket associated with the client connection.
     * @param username The username of the client.
     */
    public ServerHandler(Socket socket, String username) {
        this.socket = socket;
        this.currentUsername = username;

        gson = new GsonBuilder().serializeNulls().create();

        gameEvent = new Event();
        gameEvent.setEventId(0);
        gameEvent.setSender(null);
        gameEvent.setOpponent(null);
        gameEvent.setStatus(null);
        gameEvent.setTurn(null);
        gameEvent.setMove(-1);

        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error initializing input/output streams: " + e.getMessage());
        }
    }

    /**
     * This method is the entry point for the server thread. It should be overridden to define the server's behavior.
     *
     * @param request The request to be handled by the server.
     * @return A response to the request.
     */
    public Response handleRequest(Request request){
        switch(request.getType()){
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
        if (gameEvent.getMove() == -1) {
            gameEvent.setMove(move);
            // Implement logic to check and update the player's turn if needed
            return new Response(Response.ResponseStatus.SUCCESS, "Move sent successfully.");
        } else {
            return new Response(Response.ResponseStatus.FAILURE, "You can't make consecutive moves.");
        }
    }

    /**
     * Handles the "REQUEST_MOVE" request type.
     *
     * @return A response indicating the result of the request for a move.
     */
    public Response handleRequestMove() {
        int move = gameEvent.getMove();
        if (move != -1) {
            // Implement logic to ensure a valid move by the opponent
            gameEvent.setMove(-1); // Clear the move after it's been sent
            return new Response(Response.ResponseStatus.SUCCESS, "Received opponent's move: " + move);
        } else {
            return new Response(Response.ResponseStatus.FAILURE, "No valid move by the opponent.");
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String serializedRequest = dataInputStream.readUTF();
                Request request = gson.fromJson(serializedRequest, Request.class);
                Response response = handleRequest(request);

                String serializedResponse = gson.toJson(response);
                dataOutputStream.writeUTF(serializedResponse);
                dataOutputStream.flush();
            } catch (EOFException e) {
                // Client disconnected, close the connection
                close();
                break;
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error while handling request: " + e.getMessage());
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
}
