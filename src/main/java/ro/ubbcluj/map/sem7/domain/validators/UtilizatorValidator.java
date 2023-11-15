package ro.ubbcluj.map.sem7.domain.validators;

import ro.ubbcluj.map.sem7.domain.Utilizator;

/**
 * Validator simplu de utilizator
 */
public class UtilizatorValidator implements Validator<Utilizator> {
    /**
     * Entitatea trebuie sa nu aibe ID-ul -1(invalid) si sa nu aibe vreun nume gol
     * @param entity - entitatea de validat
     * @throws ValidationException
     */
    @Override
    public void validate(Utilizator entity) throws ValidationException {

        boolean ok = true;
        if(entity.getId() == -1)
            ok = false;

        if(entity.getFirstName().isBlank()  || entity.getLastName().isBlank() )
            ok=false;

        if(!ok)
            throw new ValidationException("Not a valid user!\n");

    }
}

