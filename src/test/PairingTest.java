package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.DatabaseHelper;
import server.SocketServer;
import socket.Request;
import socket.Response;
import socket.PairingResponse;
import test.SocketClientHelper;
import model.User;

import java.sql.SQLException;

public class PairingTest {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) throws InterruptedException {
        Thread mainThread = new Thread(() -> {
            try {
                DatabaseHelper.getInstance().truncateTables();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            SocketServer.main(null);
        });
        mainThread.start();
        Thread.sleep(1000);

        // Gson gson = new GsonBuilder().setPrettyPrinting().create(); // already declared as a class variable

        User user1 = new User("user1", "password1", "User One", true);
        User user2 = new User("user2", "password2", "User Two", true);
        User user3 = new User("user3", "password3", "User Three", true);
        User user4 = new User("user4", "password4", "User Four", true);

        SocketClientHelper scUser1 = new SocketClientHelper();
        SocketClientHelper scUser2 = new SocketClientHelper();
        SocketClientHelper scUser3 = new SocketClientHelper();
        SocketClientHelper scUser4 = new SocketClientHelper();

        // Test cases

        // Test 1
        loginTest(scUser1, user1, Response.ResponseStatus.FAILURE, "User not registered");

        // Test 2
        registerTest(scUser1, user1, Response.ResponseStatus.SUCCESS, "Registration successful");

        // Test 3
        loginTest(scUser1, user1, Response.ResponseStatus.FAILURE, "Invalid username or password");

        // Test 4
        loginTest(scUser1, user1, Response.ResponseStatus.SUCCESS, "Login successful");

        // Register other users

        // Test 5
        updatePairingTest(scUser1, PairingResponse.ResponseStatus.SUCCESS, "Pairing information retrieved successfully", 0);

        loginTest(scUser2, user2, Response.ResponseStatus.FAILURE, "User not registered");

// Test 7
        registerTest(scUser2, user2, Response.ResponseStatus.SUCCESS, "Registration successful");

// Test 8
        loginTest(scUser2, user2, Response.ResponseStatus.FAILURE, "Invalid username or password");

// Test 9
        loginTest(scUser2, user2, Response.ResponseStatus.SUCCESS, "Login successful");

// Register other users

// Test 10
        updatePairingTest(scUser1, PairingResponse.ResponseStatus.SUCCESS, "Pairing information retrieved successfully", 1);

// Test 11
        updatePairingTest(scUser2, PairingResponse.ResponseStatus.FAILURE, "User not logged in", 0);

// Login user2
        loginTest(scUser2, user2, Response.ResponseStatus.SUCCESS, "Login successful");

// Test 12
        updatePairingTest(scUser1, PairingResponse.ResponseStatus.SUCCESS, "Pairing information retrieved successfully", 1);

// Test 13
        sendInvitationTest(scUser1, user2, Response.ResponseStatus.SUCCESS, "Invitation sent successfully");

// Test 14
        updatePairingTest(scUser2, PairingResponse.ResponseStatus.SUCCESS, "Pairing information retrieved successfully", 1);

// Test 15
        declineInvitationTest(scUser2, 1, Response.ResponseStatus.SUCCESS, "Invitation declined successfully");

// Test 16
        updatePairingTest(scUser1, PairingResponse.ResponseStatus.SUCCESS, "Pairing information retrieved successfully", 1);

// Test 17
        acknowledgeResponseTest(scUser1, 1, Response.ResponseStatus.SUCCESS, "Response acknowledged successfully");

// Test 18
        sendInvitationTest(scUser1, user3, Response.ResponseStatus.SUCCESS, "Invitation sent successfully");

// Test 19
        acceptInvitationTest(scUser3, 2, Response.ResponseStatus.SUCCESS, "Invitation accepted successfully");

// Test 20
        updatePairingTest(scUser1, PairingResponse.ResponseStatus.SUCCESS, "Pairing information retrieved successfully", 1);

// Test 21
        acknowledgeResponseTest(scUser1, 2, Response.ResponseStatus.SUCCESS, "Response acknowledged successfully");

// Test 22
        updatePairingTest(scUser2, PairingResponse.ResponseStatus.SUCCESS, "Pairing information retrieved successfully", 1);

// Test 23
        abortGameTest(scUser1, Response.ResponseStatus.SUCCESS, "Game aborted successfully");

// Test 24
        updatePairingTest(scUser2, PairingResponse.ResponseStatus.SUCCESS, "Pairing information retrieved successfully", 1);

        mainThread.interrupt();
    }

    private static void loginTest(SocketClientHelper sc, User user, Response.ResponseStatus expectedStatus, String expectedMessage) {
        Request loginRequest = new Request(Request.RequestType.LOGIN, gson.toJson(user));
        Response loginResponse = sc.sendRequest(loginRequest, Response.class);
        System.out.println(gson.toJson(loginResponse));
        // Add assertions based on expectedStatus and expectedMessage
    }

    private static void registerTest(SocketClientHelper sc, User user, Response.ResponseStatus expectedStatus, String expectedMessage) {
        Request registerRequest = new Request(Request.RequestType.REGISTER, gson.toJson(user));
        Response registerResponse = sc.sendRequest(registerRequest, Response.class);
        System.out.println(gson.toJson(registerResponse));
        // Add assertions based on expectedStatus and expectedMessage
    }

    private static void updatePairingTest(SocketClientHelper sc, PairingResponse.ResponseStatus expectedStatus, String expectedMessage, int expectedAvailableUsers) {
        Request updatePairingRequest = new Request(Request.RequestType.UPDATE_PAIRING, "");
        PairingResponse pairingResponse = sc.sendRequest(updatePairingRequest, PairingResponse.class);
        System.out.println(gson.toJson(pairingResponse));
        // Add assertions based on expectedStatus, expectedMessage, and expectedAvailableUsers
    }

    public static void acknowledgeResponseTest(SocketClientHelper client, int eventId, Response.ResponseStatus expectedStatus, String message) {
            Request request = new Request(Request.RequestType.ACKNOWLEDGE_RESPONSE, String.valueOf(eventId));
            Response response = client.sendRequest(request, Response.class);
            assertResponse(response, expectedStatus, message);
    }

    // Method for sending an invitation
    public static void sendInvitationTest(SocketClientHelper sender, User receiver, Response.ResponseStatus expectedStatus, String message) {
            Request request = new Request(Request.RequestType.SEND_INVITATION, gson.toJson(receiver.getUsername()));
            Response response = sender.sendRequest(request, Response.class);
            assertResponse(response, expectedStatus, message);
    }

    // Method for accepting an invitation
    public static void acceptInvitationTest(SocketClientHelper receiver, int eventId, Response.ResponseStatus expectedStatus, String message) {
            Request request = new Request(Request.RequestType.ACCEPT_INVITATION, String.valueOf(eventId));
            Response response = receiver.sendRequest(request, Response.class);
            assertResponse(response, expectedStatus, message);
    }

    // Method for aborting a game
    public static void abortGameTest(SocketClientHelper player, Response.ResponseStatus expectedStatus, String message) {
        Request request = new Request(Request.RequestType.ABORT_GAME, String.valueOf(message)/* provide appropriate data here */);
            Response response = player.sendRequest(request, Response.class);
            assertResponse(response, expectedStatus, message);
    }

    // Helper method to assert the response
    private static void assertResponse(Response response, Response.ResponseStatus expectedStatus, String message) {
        assert response != null;
        assert response.getStatus() == expectedStatus : message + " - Expected: " + expectedStatus + ", Actual: " + response.getStatus();
        System.out.println(message + " - " + response.getMessage());
    }
    public static void declineInvitationTest(SocketClientHelper receiver, int eventId, Response.ResponseStatus expectedStatus, String message) {
        Request request = new Request(Request.RequestType.DECLINE_INVITATION, String.valueOf(eventId));
        Response response = receiver.sendRequest(request, Response.class);
        assertResponse(response, expectedStatus, message);
    }
}

