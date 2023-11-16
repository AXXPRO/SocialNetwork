package ro.ubbcluj.map.sem7.domain;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entitatea utilizator
 */
public class Utilizator extends Entity<Long> {
    private String firstName;
    private String lastName;
    private List<Utilizator> friends;

    /**
     *
     * @param firstName - primul nume al utilizatorului
     * @param lastName - al 2-lea nume a utilziatorului
     */
    public Utilizator(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        friends= new ArrayList<>();
        super.setId(-1L);
    }

    /**
     *
     * @param firstName - primul nume al utilizatorului
     * @param lastName - al 2-lea nume a utilziatorului
     * @param id - id-ul utilizatorului
     */
    public Utilizator(String firstName, String lastName, Long id ) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        friends= new ArrayList<>();
    }

    /**
     *
     * @return primul nume al utilzatorului
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Schima primul nume al utilziatorului  cu firstName
     * @param firstName - primul nume al utilizatorului
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    /**
     *
     * @return al 2-lea nume al utilzatorului
     */
    public String getLastName() {
        return lastName;
    }
    /**
     * Schima al 2-lea nume al utilziatorului  cu lastName
     * @param lastName - al 2-lea nume al utilizatorului
     */
    public String getFullName(){
        return firstName+" "+lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Va adauga in lista de prieteni a utilziatorului pe entity
     * @param entity - Un utilizator ce va fi adaugat in lista de prieteni
     */
    public void addFriend(Utilizator entity){friends.add(entity);}
    /**
     * Va sterge din lista de prieteni a utilziatorului pe entity
     * @param entity - Un utilizator ce va fi sters din lista de prieteni
     */

    public void removeFriend(Utilizator entity){


       // friends.remove(entity);
        for(Utilizator util : friends)
        {
            if(util.getId().equals(entity.getId()))
            {
                friends.remove(util);
                break;
            }

        }
    }

    /**
     *
     * @return toti prietenii unui utilizator
     */

    public List<Utilizator> getFriends() {
        return friends;
    }

    /**
     *
     * @return String-ul cu detallile utilziatorului
     */
    @Override
    public String toString() {
        return firstName +
                " " + lastName;
    }

    /**
     *
     * @return String-ul cu detallile utilziatorului incluzand prieteniia cestuia
     */
    public String toStringWithFriends() {
        return   "ID='" + id + '\'' +
                "|firstName='" + firstName + '\'' +
                "|lastName='" + lastName + '\''  + "\n" +
                "friends=" + friends;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilizator)) return false;
        Utilizator that = (Utilizator) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriends());
    }
}