package socket;

import model.User;
import model.Event;
import java.util.List;

/**
 * Server response to an UPDATE_PAIRING request. It is a subclass of Response.java.
 */
public class PairingResponse extends Response {
    private List<User> availableUsers;
    private Event invitation;
    private Event invitationResponse;

    /**
     * Default constructor for the class. Must call the constructor of the super class.
     */
    public PairingResponse() {
        super();
    }

    /**
     * Constructor that sets all attributes of this class. Must call the constructor of the super class.
     *
     * @param status             The status of the response.
     * @param message            The message associated with the response.
     * @param availableUsers     A list of available users for game invitations.
     * @param invitation         An object of type Event representing a game invitation from another user.
     * @param invitationResponse An object of type Event representing a response to a game invitation.
     */
    public PairingResponse(ResponseStatus status, String message, List<User> availableUsers,
                           Event invitation, Event invitationResponse) {
        super(status, message);
        this.availableUsers = availableUsers;
        this.invitation = invitation;
        this.invitationResponse = invitationResponse;
    }

    /**
     * Getter for the list of available users.
     *
     * @return The list of available users.
     */
    public List<User> getAvailableUsers() {
        return availableUsers;
    }

    /**
     * Setter for the list of available users.
     *
     * @param availableUsers The list of available users to set.
     */
    public void setAvailableUsers(List<User> availableUsers) {
        this.availableUsers = availableUsers;
    }

    /**
     * Getter for the game invitation.
     *
     * @return The game invitation.
     */
    public Event getInvitation() {
        return invitation;
    }

    /**
     * Setter for the game invitation.
     *
     * @param invitation The game invitation to set.
     */
    public void setInvitation(Event invitation) {
        this.invitation = invitation;
    }

    /**
     * Getter for the invitation response.
     *
     * @return The invitation response.
     */
    public Event getInvitationResponse() {
        return invitationResponse;
    }

    /**
     * Setter for the invitation response.
     *
     * @param invitationResponse The invitation response to set.
     */
    public void setInvitationResponse(Event invitationResponse) {
        this.invitationResponse = invitationResponse;
    }
}
