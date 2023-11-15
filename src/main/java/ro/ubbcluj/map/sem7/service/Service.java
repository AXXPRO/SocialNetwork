package ro.ubbcluj.map.sem7.service;

import ro.ubbcluj.map.sem7.domain.Entity;
import ro.ubbcluj.map.sem7.domain.validators.ValidationException;

import java.util.List;

public interface Service<ID, E extends Entity<ID>> {


    /**
     *
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     *          or null - if there is no entity with the given id
     * @throws IllegalArgumentException
     *                  if id is null.
     */

    E findOne(ID id) throws Exception;

    /**
     *
     * @return all entities
     */
    Iterable<E> findAll();

    /**
     *
     * @param parameters
     *         list of parameters to create an object
     * @return null- if the given entity is saved
     * @throws ValidationException
     *            if the entity is not valid
     *
     */
    E add(List<String> parameters) throws Exception;

    /**
     *  removes the entity with the specified id
     * @param id
     *      id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws IllegalArgumentException
     *                   if the given id is null.
     */
    E delete(ID id) throws Exception;

    /**
     *
     * @param entity
     *          entity must not be null
     * @return null - if the entity is updated,
     *                otherwise  returns the entity  - (e.g id does not exist).
     * @throws IllegalArgumentException
     *             if the given entity is null.
     * @throws ValidationException
     *             if the entity is not valid.
     */

    E update(E entity) throws Exception;

}
