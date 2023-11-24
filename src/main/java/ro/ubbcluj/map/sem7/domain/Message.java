package ro.ubbcluj.map.sem7.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Message extends Entity<Long>{

    Long fromID;
    ArrayList<Long> toIDS;
    String message;
    LocalDateTime dateTime;
    Message reply;

    public Message(Long aLong, Long fromID, ArrayList<Long> toIDS, String message, LocalDateTime dateTime) {
        super(aLong);
        this.fromID = fromID;
        this.toIDS = toIDS;
        this.message = message;
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return
                fromID +
                ": "+ message;
    }

    public Message getReply() {
        return reply;
    }

    public void setReply(Message reply) {
        this.reply = reply;
    }

    public Long getFromID() {
        return fromID;
    }

    public void setFromID(Long fromID) {
        this.fromID = fromID;
    }

    public ArrayList<Long> getToIDS() {
        return toIDS;
    }

    public void setToIDS(ArrayList<Long> toIDS) {
        this.toIDS = toIDS;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
