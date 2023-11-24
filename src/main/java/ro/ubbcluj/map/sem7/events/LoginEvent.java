package ro.ubbcluj.map.sem7.events;

public class LoginEvent implements Event{

    @Override
    public EventType getEventType() {
        return  EventType.LOGIN;
    }
}
