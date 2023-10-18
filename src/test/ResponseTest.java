package test;
import socket.Response;

public class ResponseTest {
    public static void main(String[] args) {
        testDefaultConstructor();
        testParameterizedConstructor();
        testGettersAndSetters();
    }

    public static void testDefaultConstructor() {
        Response response = new Response();
        assert response.getStatus() == Response.ResponseStatus.SUCCESS;
        assert response.getMessage() == null;
    }

    public static void testParameterizedConstructor() {
        Response response = new Response(Response.ResponseStatus.FAILURE, "Error message");
        assert response.getStatus() == Response.ResponseStatus.FAILURE;
        assert response.getMessage().equals("Error message");
    }

    public static void testGettersAndSetters() {
        Response response = new Response();
        response.setStatus(Response.ResponseStatus.FAILURE);
        response.setMessage("New error message");
        assert response.getStatus() == Response.ResponseStatus.FAILURE;
        assert response.getMessage().equals("New error message");
    }
}
