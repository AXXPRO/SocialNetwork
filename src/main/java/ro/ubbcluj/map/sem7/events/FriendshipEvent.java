package ro.ubbcluj.map.sem7.events;

public class FriendshipEvent implements Event{
    @Override
    public EventType getEventType() {
        return EventType.FRIENDSHIP;
    }
}
