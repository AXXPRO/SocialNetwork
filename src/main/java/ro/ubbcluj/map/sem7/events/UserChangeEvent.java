package ro.ubbcluj.map.sem7.events;

public class UserChangeEvent implements Event{

    UserChanges change;

    public UserChangeEvent(UserChanges change) {
        this.change = change;
    }

    public UserChanges getEventType(){

        return change;
    };
}
