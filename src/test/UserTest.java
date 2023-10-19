package test;
import model.User;

/**
 * The `UserTest` class is responsible for testing the functionality of the `User` class.
 */
public class UserTest {

    /**
     * The main method that runs the test methods for the `User` class.
     *
     * @param args Command-line arguments (not used in this example).
     */
    public static void main(String[] args) {
        testDefaultConstructor();
        testParameterizedConstructor();
        testEqualsMethod();
    }

    /**
     * Tests the default constructor of the `User` class.
     */
    public static void testDefaultConstructor() {
        User user = new User();
        assert user.getUsername() == null;
        assert user.getPassword() == null;
        assert user.getDisplayName() == null;
        assert !user.isOnline();
    }

    /**
     * Tests the parameterized constructor of the `User` class.
     */
    public static void testParameterizedConstructor() {
        User user = new User("john_doe", "password123", "John Doe", true);
        assert user.getUsername().equals("john_doe");
        assert user.getPassword().equals("password123");
        assert user.getDisplayName().equals("John Doe");
        assert user.isOnline();
    }

    /**
     * Tests the `equals` method of the `User` class.
     */
    public static void testEqualsMethod() {
        User user1 = new User("john_doe", "password123", "John Doe", true);
        User user2 = new User("jane_smith", "password123", "Jane Smith", true);
        User user3 = new User("john_doe", "password123", "John Doe", true);

        assert !user1.equals(user2);
        assert user1.equals(user3);
    }
}
