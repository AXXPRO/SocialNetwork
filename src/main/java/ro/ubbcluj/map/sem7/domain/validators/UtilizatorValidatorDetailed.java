package ro.ubbcluj.map.sem7.domain.validators;

import ro.ubbcluj.map.sem7.domain.Utilizator;

/**
 * The user's names must be made up of only english alphabetical characters
 */
public class UtilizatorValidatorDetailed implements Validator<Utilizator>{

    /**
     * Entitatea trebuie sa nu aibe ID-ul -1(invalid) si sa nu aibe vreun nume gol sau alcatuit din caractere non alfabetice
     * @param entity - entitatea de validat
     * @throws ValidationException
     */
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        boolean ok = true;
        if(entity.getId() == -1)
            ok = false;

        var firstName = entity.getFirstName().toUpperCase();
        var lastName = entity.getLastName().toUpperCase();
        for(int i =0; i < firstName.length(); i++)
        {
            if(firstName.charAt(i) < 'A' || firstName.charAt(i) > 'Z' )
            {
                ok = false;
                break;
            }

        }

        for(int i =0; i < lastName.length(); i++)
        {
            if(lastName.charAt(i) < 'A' || lastName.charAt(i) > 'Z' )
            {
                ok = false;
                break;
            }

        }

        if(entity.getFirstName().isBlank()  || entity.getLastName().isBlank() )
            ok=false;

        if(!ok)
            throw new ValidationException("Not a valid user!\n");
    }
}
