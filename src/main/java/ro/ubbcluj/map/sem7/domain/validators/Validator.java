package ro.ubbcluj.map.sem7.domain.validators;

/**
 * Interfata unui validator
 * @param <T> - elementu pe care il va valida
 */
public interface Validator<T> {
    /**
     *
     * @param entity - Entitatea ce o vol valdia
     * @throws ValidationException daca entitatea nu este valida
     */
    void validate(T entity) throws ValidationException;
}