package ro.ubbcluj.map.sem7.events;

public class RequestProcessedEvent implements Event{
    @Override
    public EventType getEventType() {
        return EventType.REQUESTPROCESSED;
    }
}
