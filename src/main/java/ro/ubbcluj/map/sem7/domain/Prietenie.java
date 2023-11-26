package ro.ubbcluj.map.sem7.domain;

import java.time.LocalDateTime;


public class Prietenie extends Entity<Tuple<Long,Long>> {

    LocalDateTime friendsFrom;
    String status;

    public Prietenie(LocalDateTime friendsFrom, Long id1, Long id2) {
        this.friendsFrom = friendsFrom;
        super.setId(new Tuple<>(id1,id2));
        status = "accepted";
    }

    public Prietenie(LocalDateTime friendsFrom, Long id1, Long id2,String stat) {
        this.friendsFrom = friendsFrom;
        super.setId(new Tuple<>(id1,id2));
        status = stat;
    }



    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    public String getStatus() {
        return status;
    }
    /**
     *
     * @return the date when the friendship was created
     */

}
