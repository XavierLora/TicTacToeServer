package socket;

/**
 * The `GamingResponse` class represents a response specific to gaming-related operations, extending the `Response` class.
 */
public class GamingResponse extends Response {
    private int move;
    private boolean active;

    /**
     * Default constructor for the `GamingResponse` class.
     */
    public GamingResponse() {
        super();
    }

    /**
     * Parameterized constructor for the `GamingResponse` class, allowing you to set the response status, message, move, and activity status.
     *
     * @param status  The response status (e.g., SUCCESS or ERROR).
     * @param message The response message.
     * @param move    The move associated with the gaming response.
     * @param active  The activity status (e.g., true if active, false if not).
     */
    public GamingResponse(ResponseStatus status, String message, int move, boolean active) {
        super(status, message);
        this.move = move;
        this.active = active;
    }

    /**
     * Gets the move associated with the gaming response.
     *
     * @return The move.
     */
    public int getMove() {
        return move;
    }

    /**
     * Checks the activity status of the gaming response.
     *
     * @return `true` if the response is active, `false` if not.
     */
    public boolean getActive() {
        return active;
    }

    /**
     * Sets the move associated with the gaming response.
     *
     * @param move The new move to set.
     */
    public void setMove(int move) {
        this.move = move;
    }

    /**
     * Sets the activity status of the gaming response.
     *
     * @param active `true` if the response is active, `false` if not.
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}
