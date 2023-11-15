package ro.ubbcluj.map.sem7.domain.exceptions;

/**
 * Clasa de exceptii legate de un utilizator
 */
public class UtilizatorExceptions extends Exception{
    /**
     * Construcotrul default
     * @param message - messajul ce va fi returnat
     */
    public UtilizatorExceptions(String message) {
        super(message);
    }
}
