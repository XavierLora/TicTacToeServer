package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.DatabaseHelper;
import server.SocketServer;
import socket.Request;
import socket.Response;
import socket.PairingResponse;
import model.User;

import java.sql.SQLException;

/**
 * The PairingTest class contains a series of test cases for the pairing functionality.
 */
public class PairingTest {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    /**
     * The main method that runs the pairing test cases.
     *
     * @param args Command line arguments (not used)
     * @throws InterruptedException Thrown if the main thread is interrupted during sleep
     */
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

        User user1 = new User("user1", "password1", "User One", false);
        User user2 = new User("user2", "password2", "User Two", false);
        User user3 = new User("user3", "password3", "User Three", false);
        User user4 = new User("user4", "password4", "User Four", false);
        User invalidPasswordUser1 = new User("user1", "wrongpassword", "User One", true);

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
        loginTest(scUser1, invalidPasswordUser1, Response.ResponseStatus.FAILURE, "Invalid username or password");

        // Test 4
        loginTest(scUser1, user1, Response.ResponseStatus.SUCCESS, "Login successful");

        // Register other users
        registerTest(scUser2, user2, Response.ResponseStatus.SUCCESS, "Registration successful");
        registerTest(scUser3, user3, Response.ResponseStatus.SUCCESS, "Registration successful");
        registerTest(scUser4, user4, Response.ResponseStatus.SUCCESS, "Registration successful");

        Thread.sleep(1000);  // Adjust the sleep duration as needed

        // Test 5
        updatePairingTest(scUser1, PairingResponse.ResponseStatus.SUCCESS, "Pairing information retrieved successfully", 0);

        // Test 6
        updatePairingTest(scUser2, PairingResponse.ResponseStatus.SUCCESS, "Pairing information retrieved successfully", 0);
        loginTest(scUser2, user2, Response.ResponseStatus.FAILURE, "User not registered");

        // Test 7
        updatePairingTest(scUser1, PairingResponse.ResponseStatus.SUCCESS, "Pairing information retrieved successfully", 1);
        loginTest(scUser3, user3, Response.ResponseStatus.FAILURE, "User not registered");
        loginTest(scUser4, user4, Response.ResponseStatus.FAILURE, "User not registered");

        // Test 8
        updatePairingTest(scUser2, PairingResponse.ResponseStatus.SUCCESS, "Pairing information retrieved successfully", 3);

        // Test 9
        scUser4.close();
        Thread.sleep(1000);
        updatePairingTest(scUser2, PairingResponse.ResponseStatus.SUCCESS, "Pairing information retrieved successfully", 2);

        // Test 10
        SocketClientHelper scUser4New = new SocketClientHelper();
        loginTest(scUser4New, user4, Response.ResponseStatus.SUCCESS, "Login Successful");
        updatePairingTest(scUser2, PairingResponse.ResponseStatus.SUCCESS, "Pairing information retrieved successfully", 3);

        // Test 11
        sendInvitationTest(scUser1, user2, Response.ResponseStatus.SUCCESS, "Invite Sent Successfully");

        // Test 12
        PairingResponse pairingResponse11 = updatePairingTest(scUser2, PairingResponse.ResponseStatus.SUCCESS, "Pairing Successful", 1);
        int eventIdToDecline = pairingResponse11.getInvitation().getEventId(); // Capture the eventId from the response of Test 11

        // Test 13
        declineInvitationTest(scUser2, eventIdToDecline, Response.ResponseStatus.SUCCESS,"Pairing information retrieved successfully");

        // Test 14
        updatePairingTest(scUser1, PairingResponse.ResponseStatus.SUCCESS, "Invitation sent successfully",3);

        // Test 15
        acknowledgeResponseTest(scUser1, eventIdToDecline, Response.ResponseStatus.SUCCESS, "Response Acknowledged");

        // Test 16
        sendInvitationTest(scUser1, user3, Response.ResponseStatus.SUCCESS, "Invitation sent successfully");

        // Test 17
        PairingResponse pairingResponse17 = updatePairingTest(scUser3, PairingResponse.ResponseStatus.SUCCESS, "Pairing information retrieved successfully", 1);
        int eventIdToAccept = pairingResponse17.getInvitation().getEventId();

        // Test 18
        acceptInvitationTest(scUser3, eventIdToAccept, Response.ResponseStatus.SUCCESS, "Response acknowledged successfully");

        // Test 19
        updatePairingTest(scUser1, PairingResponse.ResponseStatus.SUCCESS, "Invitation sent successfully", 3);

        // Test 20
        acknowledgeResponseTest(scUser3, 2, Response.ResponseStatus.SUCCESS, "Invitation accepted successfully");

        // Test 21
        updatePairingTest(scUser2, PairingResponse.ResponseStatus.SUCCESS, "Pairing information retrieved successfully", 1);

        // Test 22
        abortGameTest(scUser1, Response.ResponseStatus.SUCCESS, "Game aborted successfully");

        // Test 23
        updatePairingTest(scUser2, PairingResponse.ResponseStatus.SUCCESS, "Pairing information retrieved successfully", 1);

        mainThread.interrupt();
    }
    /**
     * Executes a login test for a given user using the provided SocketClientHelper.
     *
     * @param sc             The SocketClientHelper for communication.
     * @param user           The user to be used for login.
     * @param expectedStatus The expected response status.
     * @param expectedMessage The expected response message.
     */
            private static void loginTest(SocketClientHelper sc, User user, Response.ResponseStatus expectedStatus, String expectedMessage) {
                Request loginRequest = new Request(Request.RequestType.LOGIN, gson.toJson(user));
                Response loginResponse = sc.sendRequest(loginRequest, Response.class);
                System.out.println(gson.toJson(loginResponse));
                // Add assertions based on expectedStatus and expectedMessage
            }
    /**
     * Executes a registration test for a given user using the provided SocketClientHelper.
     *
     * @param sc             The SocketClientHelper for communication.
     * @param user           The user to be used for registration.
     * @param expectedStatus The expected response status.
     * @param expectedMessage The expected response message.
     */
            private static void registerTest(SocketClientHelper sc, User user, Response.ResponseStatus expectedStatus, String expectedMessage) {
                Request registerRequest = new Request(Request.RequestType.REGISTER, gson.toJson(user));
                Response registerResponse = sc.sendRequest(registerRequest, Response.class);
                System.out.println(gson.toJson(registerResponse));
                // Add assertions based on expectedStatus and expectedMessage
            }
    /**
     * Executes a pairing update test using the provided SocketClientHelper.
     *
     * @param sc                   The SocketClientHelper for communication.
     * @param expectedStatus       The expected response status.
     * @param expectedMessage      The expected response message.
     * @param expectedAvailableUsers The expected number of available users in the pairing response.
     * @return The PairingResponse received from the server.
     */
            private static PairingResponse updatePairingTest(SocketClientHelper sc, PairingResponse.ResponseStatus expectedStatus, String expectedMessage, int expectedAvailableUsers) {
                // Add a delay to allow the server to process previous requests
                try {
                    Thread.sleep(1000); // Adjust the sleep duration as needed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Request updatePairingRequest = new Request(Request.RequestType.UPDATE_PAIRING, "");
                PairingResponse pairingResponse = sc.sendRequest(updatePairingRequest, PairingResponse.class);
                System.out.println(gson.toJson(pairingResponse));
                // Add assertions based on expectedStatus, expectedMessage, and expectedAvailableUsers
                return pairingResponse;
            }

    /**
     * Executes an acknowledgment test for a given event ID using the provided SocketClientHelper.
     *
     * @param client         The SocketClientHelper for communication.
     * @param eventId        The event ID to be acknowledged.
     * @param expectedStatus The expected response status.
     * @param message        The expected response message.
     */
            public static void acknowledgeResponseTest(SocketClientHelper client, int eventId, Response.ResponseStatus expectedStatus, String message) {
                    Request request = new Request(Request.RequestType.ACKNOWLEDGE_RESPONSE, String.valueOf(eventId));
                    Response response = client.sendRequest(request, Response.class);
                    assertResponse(response, expectedStatus, message);
            }
    /**
     * Executes an invitation test for sending an invitation to another user.
     *
     * @param sender         The SocketClientHelper sending the invitation.
     * @param receiver       The user receiving the invitation.
     * @param expectedStatus The expected response status.
     * @param message        The expected response message.
     */
            // Method for sending an invitation
            public static void sendInvitationTest(SocketClientHelper sender, User receiver, Response.ResponseStatus expectedStatus, String message) {
                    Request request = new Request(Request.RequestType.SEND_INVITATION, gson.toJson(receiver.getUsername()));
                    Response response = sender.sendRequest(request, Response.class);
                    assertResponse(response, expectedStatus, message);
            }
    /**
     * Executes an invitation acceptance test for a given event ID.
     *
     * @param receiver       The SocketClientHelper receiving the invitation.
     * @param eventId        The event ID to be accepted.
     * @param expectedStatus The expected response status.
     * @param message        The expected response message.
     */
            // Method for accepting an invitation
            public static void acceptInvitationTest(SocketClientHelper receiver, int eventId, Response.ResponseStatus expectedStatus, String message) {
                    Request request = new Request(Request.RequestType.ACCEPT_INVITATION, String.valueOf(eventId));
                    Response response = receiver.sendRequest(request, Response.class);
                    assertResponse(response, expectedStatus, message);
            }


    /**
     * Executes a game abortion test using the provided SocketClientHelper.
     *
     * @param player         The SocketClientHelper for communication.
     * @param expectedStatus The expected response status.
     * @param message        The expected response message.
     */
            public static void abortGameTest(SocketClientHelper player, Response.ResponseStatus expectedStatus, String message) {
                Request request = new Request(Request.RequestType.ABORT_GAME, ""); // No need to pass a message
                Response response = player.sendRequest(request, Response.class);
                assertResponse(response, expectedStatus, message);
            }

            // Helper method to assert the response
            private static void assertResponse(Response response, Response.ResponseStatus expectedStatus, String message) {
                assert response != null;
                assert response.getStatus() == expectedStatus : message + " - Expected: " + expectedStatus + ", Actual: " + response.getStatus();
                System.out.println(message + " - " + response.getMessage());
            }
    /**
     * Executes a decline invitation test for a given event ID.
     *
     * @param receiver       The SocketClientHelper receiving the invitation.
     * @param eventId        The event ID to be declined.
     * @param expectedStatus The expected response status.
     * @param message        The expected response message.
     */
            public static void declineInvitationTest(SocketClientHelper receiver, int eventId, Response.ResponseStatus expectedStatus, String message) {
                Request request = new Request(Request.RequestType.DECLINE_INVITATION, String.valueOf(eventId));
                Response response = receiver.sendRequest(request, Response.class);
                assertResponse(response, expectedStatus, message);
            }
        }

