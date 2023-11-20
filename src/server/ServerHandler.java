package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import model.Event;
import model.User;
import socket.GamingResponse;
import socket.PairingResponse;
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
    private int currentEventId;
    private final Logger logger;

    /**
     * Constructor for the `ServerHandler` class that accepts a Socket and a username.
     *
     * @param socket   The Socket associated with the client connection.
     */
    public ServerHandler(Socket socket) {
        logger = Logger.getLogger(ServerHandler.class.getName());
        this.socket = socket;

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
            } catch (SQLException e) {
                throw new RuntimeException(e);
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

            // Check if the user has previously logged in
            if (currentUsername != null) {
                // Get the User object corresponding to the currentUsername
                User user = DatabaseHelper.getInstance().getUser(currentUsername);

                // Set the user online attribute to false
                user.setOnline(false);

                // Update the user in the database
                DatabaseHelper.getInstance().updateUser(user);

                // Abort any event that is not either COMPLETED or ABORTED
                DatabaseHelper.getInstance().abortAllUserEvents(currentUsername);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error closing resources: " + e.getMessage());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating user or aborting events: " + e.getMessage());
        }
    }


    /**
     * This method is the entry point for the server thread. It should be overridden to define the server's behavior.
     *
     * @param request The request to be handled by the server.
     * @return A response to the request.
     */
    public Response handleRequest(Request request) throws SQLException {
        switch (request.getType()) {
            case LOGIN:
                // handle LOGIN
                break;

            case REGISTER:
                User user = gson.fromJson(request.getData(), User.class);
                return handleRegister(user);

            case UPDATE_PAIRING:
                return handleUpdatePairing();

            case SEND_INVITATION:
                String opponent = gson.fromJson(request.getData(), String.class);
                return handleSendInvitation(opponent);

            case ACCEPT_INVITATION:
                int eventIdAccept = Integer.parseInt(request.getData());
                return handleAcceptInvitation(eventIdAccept);

            case DECLINE_INVITATION:
                int eventIdDecline = Integer.parseInt(request.getData());
                return handleDeclineInvitation(eventIdDecline);

            case ACKNOWLEDGE_RESPONSE:
                int eventIdAcknowledge = Integer.parseInt(request.getData());
                return handleAcknowledgeResponse(eventIdAcknowledge);

            case REQUEST_MOVE:
                return handleRequestMove();

            case SEND_MOVE:
                int move = gson.fromJson(request.getData(), Integer.class);
                return handleSendMove(move);

            case ABORT_GAME:
                return handleAbortGame();

            case COMPLETE_GAME:
                return handleCompleteGame();

            default:
                return new Response(Response.ResponseStatus.FAILURE, "Invalid request type.");
        }
        return new Response(Response.ResponseStatus.FAILURE, "Invalid request type.");
    }

    /**
     * Handles the "SEND_MOVE" request type.
     *
     * @param move The move to be sent.
     * @return A response indicating the result of the move operation.
     */
    public Response handleSendMove(int move) {
        try {
            // Create a local Event variable using the database helper function getEvent()
            Event gameEvent = DatabaseHelper.getInstance().getEvent(currentEventId);

            // Check for a valid move
            if (move < 0 || move > 8) {
                return new Response(Response.ResponseStatus.FAILURE, "Invalid Move");
            }

            // Check if it's the player's turn
            if (gameEvent.getTurn() == null || !gameEvent.getTurn().equals(currentUsername)) {
                // Set the move in the local Event
                gameEvent.setMove(move);
                gameEvent.setTurn(currentUsername);

                // Update the event in the database using the database helper function updateEvent()
                DatabaseHelper.getInstance().updateEvent(gameEvent);

                // Return a standard Response
                return new Response(Response.ResponseStatus.SUCCESS, "Move Added");
            } else {
                return new Response(Response.ResponseStatus.FAILURE, "Not your turn to move");
            }
        } catch (SQLException e) {
            // Handle SQLException
            logger.log(Level.SEVERE, "Error handling send move: " + e.getMessage(), e);
            return new Response(Response.ResponseStatus.FAILURE, "Database error");
        }
    }

    /**
     * Handles the "REQUEST_MOVE" request type.
     *
     * @return A response indicating the result of the request for a move.
     */
    public Response handleRequestMove() {
        try {
            // Create a local Event variable using the database helper function getEvent()
            Event gameEvent = DatabaseHelper.getInstance().getEvent(currentEventId);

            GamingResponse response = new GamingResponse();
            response.setStatus(Response.ResponseStatus.SUCCESS);

            // Check if the game is still active
            if (gameEvent.getStatus() == Event.EventStatus.ABORTED) {
                response.setActive(false);
                response.setMessage("Opponent Abort");
            } else if (gameEvent.getStatus() == Event.EventStatus.COMPLETED) {
                response.setActive(false);
                response.setMessage("Opponent Deny Play Again");
            } else {
                response.setActive(true);
            }

            // Check if there is a valid move made by the opponent
            if (gameEvent.getMove() != -1 && !gameEvent.getTurn().equals(currentUsername)) {
                response.setMove(gameEvent.getMove());

                // Delete the move in the local Event
                gameEvent.setMove(-1);
                gameEvent.setTurn(null);

                // Update the event in the database using the database helper function updateEvent()
                DatabaseHelper.getInstance().updateEvent(gameEvent);
            } else {
                response.setMove(-1);
            }

            return response;
        } catch (SQLException e) {
            // Handle SQLException
            logger.log(Level.SEVERE, "Error handling request move: " + e.getMessage(), e);
            return new Response(Response.ResponseStatus.FAILURE, "Database error");
        }
    }

    public Response handleRegister(User user) {
        try {
            // Check if the username already exists
            if (DatabaseHelper.getInstance().isUsernameExists(user.getUsername())) {
                return new Response(Response.ResponseStatus.FAILURE, "Username already exists.");
            }

            // If the username doesn't exist, create the user in the database
            DatabaseHelper.getInstance().createUser(user);

            return new Response(Response.ResponseStatus.SUCCESS, "Registration successful.");
        } catch (SQLException e) {
            // Log any database-related errors
            logger.log(Level.SEVERE, "Error handling registration: " + e.getMessage());
            return new Response(Response.ResponseStatus.FAILURE, "Internal server error.");
        }
    }
    /**
     * Handles the "LOGIN" request type.
     *
     * @param user The user object containing login credentials.
     * @return A response indicating the result of the login operation.
     */
    public Response handleLogin(User user) {
        try {
            // Get the user from the database
            User storedUser = DatabaseHelper.getInstance().getUser(user.getUsername());

            // Check if the user exists and the password is correct
            if (storedUser != null && storedUser.getPassword().equals(user.getPassword())) {
                // Set currentUsername
                currentUsername = storedUser.getUsername();

                // Set the user as online
                storedUser.setOnline(true);

                // Update the user in the database
                DatabaseHelper.getInstance().updateUser(storedUser);

                return new Response(Response.ResponseStatus.SUCCESS, "Login successful");
            } else {
                return new Response(Response.ResponseStatus.FAILURE, "Invalid username or password");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error handling login request", e);
            return new Response(Response.ResponseStatus.FAILURE, "Internal server error");
        }
    }
    /**
     * Create a new function called handleUpdatePairing().
     * This function takes no parameters and returns a PairingResponse class.
     * The function should first check if a user is already logged in by checking the value of currentUsername.
     * If the user is not logged in, return a FAILURE response with an appropriate message and null values for all other attributes.
     * Otherwise, the function should return a PairingResponse object.
     * Use the database helper function getAvailableUsers(), getUserInvitation(), and getUserInvitationResponse() to construct the PairingResponse object.
     */
    public PairingResponse handleUpdatePairing() {
        // Check if the user is logged in
        if (currentUsername == null || currentUsername.isEmpty()) {
            return new PairingResponse(Response.ResponseStatus.FAILURE, "User not logged in", null, null, null);
        }

        DatabaseHelper dbHelper = DatabaseHelper.getInstance();
        try {
            // Get the list of available users, user invitation, and user invitation response
            List<User> availableUsers = dbHelper.getAvailableUsers(currentUsername);
            Event userInvitation = dbHelper.getUserInvitation(currentUsername);
            Event userInvitationResponse = dbHelper.getUserInvitationResponse(currentUsername);
            // Create a PairingResponse object with the obtained information
            return new PairingResponse(Response.ResponseStatus.SUCCESS, "Pairing information retrieved successfully",
                    availableUsers, userInvitation, userInvitationResponse);
        } catch (Exception e) {
            // Log and return a failure response in case of an exception
            logger.log(Level.SEVERE, "Error handling pairing update: " + e.getMessage(), e);
            return new PairingResponse(Response.ResponseStatus.FAILURE, "Error handling pairing update", null, null, null);
        }
    }
    public Response handleSendInvitation(String opponent) throws SQLException {
        // Check if the user is logged in
        if (currentUsername == null || currentUsername.isEmpty()) {
            return new Response(Response.ResponseStatus.FAILURE, "User not logged in");
        }
        DatabaseHelper dbHelper = DatabaseHelper.getInstance();

        // Check if the opponent is available to receive an invitation
        if (!dbHelper.isUserAvailable(opponent)) {
            return new Response(Response.ResponseStatus.FAILURE, "Selected opponent is not available");
        }

        try {
            // Create a new Event for the invitation
            Event invitationEvent = new Event();
            invitationEvent.setSender(currentUsername);
            invitationEvent.setOpponent(opponent);
            invitationEvent.setStatus(Event.EventStatus.PENDING);
            invitationEvent.setMove(-1);

            // Save the invitation event to the database
            DatabaseHelper.getInstance().createEvent(invitationEvent);

            return new Response(Response.ResponseStatus.SUCCESS, "Invitation sent successfully");
        } catch (SQLException e) {
            // Log and return a failure response in case of an exception
            logger.log(Level.SEVERE, "Error handling send invitation: " + e.getMessage(), e);
            return new Response(Response.ResponseStatus.FAILURE, "Error sending invitation");
        }
    }
        public Response handleAcceptInvitation(int eventId) {
            // Check if the user is logged in
            if (currentUsername == null || currentUsername.isEmpty()) {
                return new Response(Response.ResponseStatus.FAILURE, "User not logged in");
            }

            try {
                // Retrieve the Event object with the corresponding eventId
                Event invitationEvent = DatabaseHelper.getInstance().getEvent(eventId);

                // Check if the event exists, the status is PENDING, and the opponent is the current user
                if (invitationEvent == null || invitationEvent.getStatus() != Event.EventStatus.PENDING
                        || !invitationEvent.getOpponent().equals(currentUsername)) {
                    return new Response(Response.ResponseStatus.FAILURE, "Invalid invitation acceptance");
                }

                // Change the status of the event to ACCEPTED
                invitationEvent.setStatus(Event.EventStatus.ACCEPTED);

                // Abort any other pending invitations for the user
                DatabaseHelper.getInstance().abortAllUserEvents(currentUsername);

                // Update the event in the database
                DatabaseHelper.getInstance().updateEvent(invitationEvent);

                // Set currentEventId to the accepted eventId
                currentEventId = eventId;

                return new Response(Response.ResponseStatus.SUCCESS, "Invitation accepted successfully");
            } catch (SQLException e) {
                // Log and return a failure response in case of an exception
                logger.log(Level.SEVERE, "Error handling accept invitation: " + e.getMessage(), e);
                return new Response(Response.ResponseStatus.FAILURE, "Error accepting invitation");
            }
        }
    public Response handleDeclineInvitation(int eventId) {
        // Check if the user is logged in
        if (currentUsername == null || currentUsername.isEmpty()) {
            return new Response(Response.ResponseStatus.FAILURE, "User not logged in");
        }

        try {
            // Retrieve the Event object with the corresponding eventId
            Event invitationEvent = DatabaseHelper.getInstance().getEvent(eventId);

            // Check if the event exists, the status is PENDING, and the opponent is the current user
            if (invitationEvent == null || invitationEvent.getStatus() != Event.EventStatus.PENDING
                    || !invitationEvent.getOpponent().equals(currentUsername)) {
                return new Response(Response.ResponseStatus.FAILURE, "Invalid invitation decline");
            }

            // Change the status of the event to DECLINED
            invitationEvent.setStatus(Event.EventStatus.DECLINED);

            // Update the event in the database
            DatabaseHelper.getInstance().updateEvent(invitationEvent);

            return new Response(Response.ResponseStatus.SUCCESS, "Invitation declined successfully");
        } catch (SQLException e) {
            // Log and return a failure response in case of an exception
            logger.log(Level.SEVERE, "Error handling decline invitation: " + e.getMessage(), e);
            return new Response(Response.ResponseStatus.FAILURE, "Error declining invitation");
        }
    }
    public Response handleAcknowledgeResponse(int eventId) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance();

        try {
            // Get the event from the database
            Event event = dbHelper.getEvent(eventId);

            // Check if the event exists and the sender is the current user
            if (event == null || !event.getSender().equals(currentUsername)) {
                return new Response(Response.ResponseStatus.FAILURE, "Invalid event or unauthorized access");
            }

            // Check the response status and update the event accordingly
            if (event.getStatus() == Event.EventStatus.DECLINED) {
                // If the response was DECLINED, set the status to ABORTED
                event.setStatus(Event.EventStatus.ABORTED);
            } else if (event.getStatus() == Event.EventStatus.ACCEPTED) {
                // If the response was ACCEPTED, set currentEventId to eventId
                currentEventId = eventId;
                // Abort any other pending invitations
                dbHelper.abortAllUserEvents(currentUsername);
            }

            // Update the event in the database
            dbHelper.updateEvent(event);

            return new Response(Response.ResponseStatus.SUCCESS, "Acknowledgment processed successfully");
        } catch (SQLException e) {
            // Handle SQLException
            logger.log(Level.SEVERE, "Error handling acknowledgment response: " + e.getMessage(), e);
            return new Response(Response.ResponseStatus.FAILURE, "Database error");
        }
    }
    public Response handleCompleteGame() {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance();

        try {
            // Get the event from the database
            Event event = dbHelper.getEvent(currentEventId);

            // Check if the event exists and the status is PLAYING
            if (event == null || event.getStatus() != Event.EventStatus.PLAYING) {
                return new Response(Response.ResponseStatus.FAILURE, "Invalid event or event not in PLAYING state");
            }

            // Set the status to COMPLETED
            event.setStatus(Event.EventStatus.COMPLETED);

            // Update the event in the database
            dbHelper.updateEvent(event);

            // Reset currentEventId to -1
            currentEventId = -1;

            return new Response(Response.ResponseStatus.SUCCESS, "Game completed successfully");
        } catch (SQLException e) {
            // Handle SQLException
            logger.log(Level.SEVERE, "Error handling complete game: " + e.getMessage(), e);
            return new Response(Response.ResponseStatus.FAILURE, "Database error");
        }
    }

    public Response handleAbortGame() {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance();

        try {
            // Get the event from the database
            Event event = dbHelper.getEvent(currentEventId);

            // Check if the event exists and the status is PLAYING
            if (event == null || event.getStatus() != Event.EventStatus.PLAYING) {
                return new Response(Response.ResponseStatus.FAILURE, "Invalid event or event not in PLAYING state");
            }

            // Set the status to ABORTED
            event.setStatus(Event.EventStatus.ABORTED);

            // Update the event in the database
            dbHelper.updateEvent(event);

            // Reset currentEventId to -1
            currentEventId = -1;

            return new Response(Response.ResponseStatus.SUCCESS, "Game aborted successfully");
        } catch (SQLException e) {
            // Handle SQLException
            logger.log(Level.SEVERE, "Error handling abort game: " + e.getMessage(), e);
            return new Response(Response.ResponseStatus.FAILURE, "Database error");
        }
    }




}

