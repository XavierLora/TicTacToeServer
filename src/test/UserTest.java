package test;
import model.User;


public class UserTest {
    public static void main(String[] args) {
        testDefaultConstructor();
        testParameterizedConstructor();
        testEqualsMethod();
    }

    public static void testDefaultConstructor() {
        User user = new User();
        assert user.getUsername() == null;
        assert user.getPassword() == null;
        assert user.getDisplayName() == null;
        assert !user.isOnline();
    }

    public static void testParameterizedConstructor() {
        User user = new User("john_doe", "password123", "John Doe", true);
        assert user.getUsername().equals("john_doe");
        assert user.getPassword().equals("password123");
        assert user.getDisplayName().equals("John Doe");
        assert user.isOnline();
    }

    public static void testEqualsMethod() {
        User user1 = new User("john_doe", "password123", "John Doe", true);
        User user2 = new User("jane_smith", "password123", "Jane Smith", true);
        User user3 = new User("john_doe", "password123", "John Doe", true);

        assert !user1.equals(user2);
        assert user1.equals(user3);
    }
}

