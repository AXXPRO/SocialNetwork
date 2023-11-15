package ro.ubbcluj.map.sem7.domain.exceptions;

/**
 * Clasa de exceptie in caz de erori la folosirea functiiolor legate de prietenie intre utilziatori
 */
public class FriendshipException extends UtilizatorExceptions{

    /**
     *
     * @param message - message to be returned by the error
     */
    public FriendshipException(String message) {
        super(message);
    }

}
