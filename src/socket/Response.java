package socket;

/**
 * The `Response` class represents a response that is sent in response to a request. It includes a response status and a message.
 */
public class Response {

    /**
     * Enumeration representing different response statuses.
     */
    public enum ResponseStatus {
        SUCCESS,
        FAILURE
    }

    private ResponseStatus status;
    private String message;

    /**
     * Default constructor for the `Response` class.
     */
    public Response() {
    }

    /**
     * Parameterized constructor for the `Response` class, allowing you to specify the response status and message.
     *
     * @param status  The response status (e.g., SUCCESS or FAILURE).
     * @param message The response message.
     */
    public Response(ResponseStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * Gets the response status.
     *
     * @return The response status (e.g., SUCCESS or FAILURE).
     */
    public ResponseStatus getStatus() {
        return status;
    }

    /**
     * Sets the response status.
     *
     * @param status The new response status to set.
     */
    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    /**
     * Gets the response message.
     *
     * @return The response message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the response message.
     *
     * @param message The new response message to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
