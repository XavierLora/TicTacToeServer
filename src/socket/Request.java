package socket;

/**
 * The `Request` class represents a request made to a server or system, and it includes a request type and associated data.
 */
public class Request {

    /**
     * Enumeration representing different types of requests.
     */
    public enum RequestType {
        LOGIN,
        REGISTER,
        UPATE_PAIRING,
        SEND_INVITATION,
        ACCEPT_INVITATION,
        DECLINE_INVITATION,
        ACKNOWLEDGE_RESPONSE,
        REQUEST_MOVE,
        SEND_MOVE,
        ABORT_GAME,
        COMPLETE_GAME
    }

    private RequestType type;
    private String data;

    /**
     * Default constructor for the `Request` class.
     */
    public Request() {
    }

    /**
     * Parameterized constructor for the `Request` class, allowing you to specify the request type and associated data.
     *
     * @param type The type of request (e.g., LOGIN, REGISTER).
     * @param data The data associated with the request.
     */
    public Request(RequestType type, String data) {
        this.type = type;
        this.data = data;
    }

    /**
     * Gets the type of the request.
     *
     * @return The request type.
     */
    public RequestType getType() {
        return type;
    }

    /**
     * Sets the type of the request.
     *
     * @param type The new request type to set.
     */
    public void setType(RequestType type) {
        this.type = type;
    }

    /**
     * Gets the data associated with the request.
     *
     * @return The request data.
     */
    public String getData() {
        return data;
    }

    /**
     * Sets the data associated with the request.
     *
     * @param data The new request data to set.
     */
    public void setData(String data) {
        this.data = data;
    }
}
