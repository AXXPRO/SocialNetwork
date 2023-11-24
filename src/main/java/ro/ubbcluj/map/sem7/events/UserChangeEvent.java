package ro.ubbcluj.map.sem7.events;

public class UserChangeEvent implements Event{

    UserChanges change;

    public UserChangeEvent(UserChanges change) {
        this.change = change;
    }

    public UserChanges getUserEventType(){

        return change;
    };

    @Override
    public EventType getEventType() {
       return  EventType.USER;
    }
}
