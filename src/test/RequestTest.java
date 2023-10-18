package test;
import socket.Request;

public class RequestTest {
    public static void main(String[] args) {
        testDefaultConstructor();
        testParameterizedConstructor();
        testGettersAndSetters();
    }

    public static void testDefaultConstructor() {
        Request request = new Request();
        assert request.getType() == Request.RequestType.LOGIN;
        assert request.getData() == null;
    }

    public static void testParameterizedConstructor() {
        Request request = new Request(Request.RequestType.REGISTER, "serializedData");
        assert request.getType() == Request.RequestType.REGISTER;
        assert request.getData().equals("serializedData");
    }

    public static void testGettersAndSetters() {
        Request request = new Request();
        request.setType(Request.RequestType.SEND_INVITATION);
        request.setData("invitationData");
        assert request.getType() == Request.RequestType.SEND_INVITATION;
        assert request.getData().equals("invitationData");
    }
}