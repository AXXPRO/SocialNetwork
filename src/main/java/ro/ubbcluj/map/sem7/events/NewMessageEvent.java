package ro.ubbcluj.map.sem7.events;

public class NewMessageEvent implements Event{
    String newMessage;
  public NewMessageEvent(String mesaj){
      newMessage = mesaj;
  }
  public String getNewMessage(){
      return newMessage;
  }
    @Override
    public EventType getEventType() {
        return EventType.NEWMESSAGE;
    }
}
