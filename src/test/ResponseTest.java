package test;
import socket.Response;

/**
 * The `ResponseTest` class is responsible for testing the functionality of the `Response` class.
 */
public class ResponseTest {

    /**
     * The main method that runs the test methods for the `Response` class.
     *
     * @param args Command-line arguments (not used in this example).
     */
    public static void main(String[] args) {
        testDefaultConstructor();
        testParameterizedConstructor();
        testGettersAndSetters();
    }

    /**
     * Tests the default constructor of the `Response` class.
     */
    public static void testDefaultConstructor() {
        Response response = new Response();
        assert response.getStatus() == Response.ResponseStatus.SUCCESS;
        assert response.getMessage() == null;
    }

    /**
     * Tests the parameterized constructor of the `Response` class.
     */
    public static void testParameterizedConstructor() {
        Response response = new Response(Response.ResponseStatus.FAILURE, "Error message");
        assert response.getStatus() == Response.ResponseStatus.FAILURE;
        assert response.getMessage().equals("Error message");
    }

    /**
     * Tests the getters and setters of the `Response` class.
     */
    public static void testGettersAndSetters() {
        Response response = new Response();
        response.setStatus(Response.ResponseStatus.FAILURE);
        response.setMessage("New error message");
        assert response.getStatus() == Response.ResponseStatus.FAILURE;
        assert response.getMessage().equals("New error message");
    }
}
