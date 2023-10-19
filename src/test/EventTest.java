package test;
import model.Event;

/**
 * The `EventTest` class is responsible for testing the functionality of the `Event` class.
 */
public class EventTest {

    /**
     * The main method that runs the test methods for the `Event` class.
     *
     * @param args Command-line arguments (not used in this example).
     */
    public static void main(String[] args) {
        testDefaultConstructor();
        testParameterizedConstructor();
        testGettersAndSetters();
        testEqualsMethod();
    }

    /**
     * Tests the default constructor of the `Event` class.
     */
    public static void testDefaultConstructor() {
        Event event = new Event();
        assert event.getEventId() == 0;
        assert event.getSender() == null;
        assert event.getOpponent() == null;
        assert event.getStatus() == Event.EventStatus.PENDING;
        assert event.getTurn() == null;
        assert event.getMove() == 0;
    }

    /**
     * Tests the parameterized constructor of the `Event` class.
     */
    public static void testParameterizedConstructor() {
        Event event = new Event(1, "john_doe", "jane_smith", Event.EventStatus.ACCEPTED, "john_doe", 5);
        assert event.getEventId() == 1;
        assert event.getSender().equals("john_doe");
        assert event.getOpponent().equals("jane_smith");
        assert event.getStatus() == Event.EventStatus.ACCEPTED;
        assert event.getTurn().equals("john_doe");
        assert event.getMove() == 5;
    }

    /**
     * Tests the getters and setters of the `Event` class.
     */
    public static void testGettersAndSetters() {
        Event event = new Event();
        event.setEventId(2);
        event.setSender("alice");
        event.setOpponent("bob");
        event.setStatus(Event.EventStatus.PLAYING);
        event.setTurn("alice");
        event.setMove(3);
        assert event.getEventId() == 2;
        assert event.getSender().equals("alice");
        assert event.getOpponent().equals("bob");
        assert event.getStatus() == Event.EventStatus.PLAYING;
        assert event.getTurn().equals("alice");
        assert event.getMove() == 3;
    }

    /**
     * Tests the `equals` method of the `Event` class.
     */
    public static void testEqualsMethod() {
        Event event1 = new Event(1, "john_doe", "jane_smith", Event.EventStatus.ACCEPTED, "john_doe", 5);
        Event event2 = new Event(2, "jane_smith", "john_doe", Event.EventStatus.PENDING, "jane_smith", 2);
        Event event3 = new Event(1, "john_doe", "jane_smith", Event.EventStatus.ACCEPTED, "john_doe", 5);

        assert !event1.equals(event2);
        assert event1.equals(event3);
    }
}
