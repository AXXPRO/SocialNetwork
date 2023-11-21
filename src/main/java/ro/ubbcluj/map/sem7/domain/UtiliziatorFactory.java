package ro.ubbcluj.map.sem7.domain;

/**
 * Clasa ce va crea utilizatorii pe baza parametrilor
 * Singleton
 */
public class UtiliziatorFactory {
    private static UtiliziatorFactory factory = new UtiliziatorFactory();

    /**
     * Constructor privat
     */
    private UtiliziatorFactory(){};

    /**
     *
     * @return the one instance of the class
     */
    public static UtiliziatorFactory  getInstance(){
        return factory;
    }

    /**
     *
     * @param firstName - primul parametru al utilziatorului
     * @param lastName - al 2-lea parametru al utilziatorului
     * @param ID - ID-ul unic al utilizatorului
     * @return Utilizator nou cu datele date
     */
    public Utilizator getUtilizator(String firstName, String lastName, String mail, String password, Long ID){

        return new Utilizator(firstName,lastName, mail, password, ID);
    }
}
