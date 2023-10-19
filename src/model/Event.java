package model;

/**
 * The `Event` class represents an event in your application, such as a game event or notification.
 */
public class Event {

    /**
     * Enumeration representing the status of an event.
     */
    public enum EventStatus {
        PENDING,
        DECLINED,
        ACCEPTED,
        PLAYING,
        COMPLETED,
        ABORTED
    }

    private int eventId;
    private String sender;
    private String opponent;
    private EventStatus status;
    private String turn;
    private int move;

    /**
     * Default constructor for the `Event` class.
     */
    public Event() {
    }

    /**
     * Parameterized constructor for the `Event` class.
     *
     * @param eventId   The unique identifier for the event.
     * @param sender    The sender of the event.
     * @param opponent  The opponent involved in the event.
     * @param status    The status of the event (e.g., PENDING, ACCEPTED).
     * @param turn      The turn associated with the event.
     * @param move      The move related to the event (e.g., in a game).
     */
    public Event(int eventId, String sender, String opponent, EventStatus status, String turn, int move) {
        this.eventId = eventId;
        this.sender = sender;
        this.opponent = opponent;
        this.status = status;
        this.turn = turn;
        this.move = move;
    }

    /**
     * Gets the event ID.
     *
     * @return The event ID.
     */
    public int getEventId() {
        return eventId;
    }

    /**
     * Gets the sender of the event.
     *
     * @return The sender of the event.
     */
    public String getSender() {
        return sender;
    }

    /**
     * Gets the opponent involved in the event.
     *
     * @return The opponent's name.
     */
    public String getOpponent() {
        return opponent;
    }

    /**
     * Gets the status of the event.
     *
     * @return The event status (e.g., PENDING, ACCEPTED).
     */
    public EventStatus getStatus() {
        return status;
    }

    /**
     * Gets the turn associated with the event.
     *
     * @return The turn information.
     */
    public String getTurn() {
        return turn;
    }

    /**
     * Gets the move related to the event (e.g., in a game).
     *
     * @return The move.
     */
    public int getMove() {
        return move;
    }

    /**
     * Sets the event ID.
     *
     * @param eventId The new event ID to set.
     */
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    /**
     * Sets the sender of the event.
     *
     * @param sender The new sender to set.
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Sets the opponent involved in the event.
     *
     * @param opponent The new opponent to set.
     */
    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    /**
     * Sets the status of the event.
     *
     * @param status The new event status to set (e.g., PENDING, ACCEPTED).
     */
    public void setStatus(EventStatus status) {
        this.status = status;
    }

    /**
     * Sets the turn associated with the event.
     *
     * @param turn The new turn information to set.
     */
    public void setTurn(String turn) {
        this.turn = turn;
    }

    /**
     * Sets the move related to the event (e.g., in a game).
     *
     * @param move The new move to set.
     */
    public void setMove(int move) {
        this.move = move;
    }

    /**
     * Checks if this `Event` object is equal to another object based on the event ID.
     *
     * @param o The object to compare.
     * @return `true` if the objects are equal (have the same event ID), `false` otherwise.
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        return eventId == event.eventId;
    }
}