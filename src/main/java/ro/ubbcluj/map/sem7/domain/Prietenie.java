package ro.ubbcluj.map.sem7.domain;

import java.time.LocalDateTime;


public class Prietenie extends Entity<Tuple<Long,Long>> {

    LocalDateTime friendsFrom;

    public Prietenie(LocalDateTime friendsFrom, Long id1, Long id2) {
        this.friendsFrom = friendsFrom;
        super.setId(new Tuple<>(id1,id2));
    }

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }



    /**
     *
     * @return the date when the friendship was created
     */

}
