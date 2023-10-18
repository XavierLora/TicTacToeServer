package test;
import model.Event;

public class EventTest {
    public static void main(String[] args) {
        testDefaultConstructor();
        testParameterizedConstructor();
        testGettersAndSetters();
        testEqualsMethod();
    }

    public static void testDefaultConstructor() {
        Event event = new Event();
        assert event.getEventId() == 0;
        assert event.getSender() == null;
        assert event.getOpponent() == null;
        assert event.getStatus() == Event.EventStatus.PENDING;
        assert event.getTurn() == null;
        assert event.getMove() == 0;
    }

    public static void testParameterizedConstructor() {
        Event event = new Event(1, "john_doe", "jane_smith", Event.EventStatus.ACCEPTED, "john_doe", 5);
        assert event.getEventId() == 1;
        assert event.getSender().equals("john_doe");
        assert event.getOpponent().equals("jane_smith");
        assert event.getStatus() == Event.EventStatus.ACCEPTED;
        assert event.getTurn().equals("john_doe");
        assert event.getMove() == 5;
    }

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

    public static void testEqualsMethod() {
        Event event1 = new Event(1, "john_doe", "jane_smith", Event.EventStatus.ACCEPTED, "john_doe", 5);
        Event event2 = new Event(2, "jane_smith", "john_doe", Event.EventStatus.PENDING, "jane_smith", 2);
        Event event3 = new Event(1, "john_doe", "jane_smith", Event.EventStatus.ACCEPTED, "john_doe", 5);

        assert !event1.equals(event2);
        assert event1.equals(event3);
    }
}
