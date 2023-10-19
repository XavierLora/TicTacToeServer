package model;

/**
 * The `User` class represents a user in your application. It includes information about the user's username,
 * password, display name, and online status.
 */
public class User {
    private String username;
    private String password;
    private String displayName;
    private boolean online;

    /**
     * Default constructor for the `User` class.
     */
    public User() {
    }

    /**
     * Parameterized constructor for the `User` class.
     *
     * @param username     The username of the user.
     * @param password     The user's password.
     * @param displayName  The display name of the user.
     * @param online       The online status of the user (true if online, false if offline).
     */
    public User(String username, String password, String displayName, boolean online) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.online = online;
    }

    /**
     * Gets the username of the user.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the user's password.
     *
     * @return The user's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the display name of the user.
     *
     * @return The display name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Checks if the user is online.
     *
     * @return `true` if the user is online, `false` if the user is offline.
     */
    public boolean isOnline() {
        return online;
    }

    /**
     * Sets the username of the user.
     *
     * @param username The new username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the user's password.
     *
     * @param password The new password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the online status of the user.
     *
     * @param online `true` if the user is online, `false` if the user is offline.
     */
    public void setOnline(boolean online) {
        this.online = online;
    }

    /**
     * Checks if this `User` object is equal to another object based on the username.
     *
     * @param o The object to compare.
     * @return `true` if the objects are equal (have the same username), `false` otherwise.
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return username.equals(user.username);
    }
}
