package ro.ubbcluj.map.sem7.service;

import ro.ubbcluj.map.sem7.domain.Entity;
import ro.ubbcluj.map.sem7.domain.validators.ValidationException;
import ro.ubbcluj.map.sem7.repository.Repository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @param <ID> - type for ID of any given Entity
 * @param <E> - The object the service will handle
 */
public abstract class AbstractService<ID, E extends Entity<ID>> implements Service<ID, E>{

    Repository<ID,E> repo;

    /**
     * Default constructor
     * @param repo - repo-ul asociat Serviceului
     */
    public AbstractService(Repository<ID, E> repo) {
        this.repo = repo;
    }

    /**
     *
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return Enity matching the ID
     */
    @Override
    public E findOne(ID id) throws Exception {
        if(repo.findOne(id).isEmpty())
            throw  new Exception("No entity was found!\n");
        return repo.findOne(id).get();
    }

    /**
     *
     * @return all the elements
     */
    @Override
    public Iterable<E> findAll() {
        return repo.findAll();
    }

    /**
     *
     * @param list
     *         list of parameters to create an object
     * @return null, if entity was added
     */
    @Override
   abstract public E add(List<String> list) throws Exception;

    /**
     *
     * @param id
     *      id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws IllegalArgumentException
     *                   if the given id is null.
     */
    @Override
    public E delete(ID id) throws Exception{
        Optional<E> deleted = repo.delete(id);
        if(deleted.isEmpty())
            throw new Exception("Entity doesn't exist!\n");
        return deleted.get();
    }

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
    @Override
    public E update(E entity) throws Exception {

       if(repo.update(entity).isPresent())
           throw new Exception("Entity could not be updated!\n");
       return repo.update(entity).get();
    }
}
