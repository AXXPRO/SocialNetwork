package ro.ubbcluj.map.sem7.repository;

import ro.ubbcluj.map.sem7.domain.Entity;
import ro.ubbcluj.map.sem7.domain.Message;
import ro.ubbcluj.map.sem7.domain.validators.Validator;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {
    private Validator<E> validator;
    Map<ID,E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities=new HashMap<ID,E>();
    }

    @Override
    public Optional<E> findOne(ID id){
        if (id==null)
            throw new IllegalArgumentException("id must be not null");
        return  Optional.ofNullable(entities.get(id));
    }

    @Override
    public List<E> findAll() {
        return entities.values().stream().toList();
    }

    @Override
    public Optional<E> save(E entity) {
        if (entity==null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
        if(entities.get(entity.getId()) != null) {
            return Optional.of(entity);
        }
        else entities.put(entity.getId(),entity);
        return Optional.empty();
    }

    @Override
    public  Optional<E> delete(ID id) {

        if(id == null)
            throw new IllegalArgumentException("Id cannot be null!");
        return Optional.ofNullable(entities.remove(id));

    }

    @Override
    public Optional<E> update(E entity) {

        if(entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);

       // entities.put(entity.getId(),entity);

        if(entities.get(entity.getId()) != null) {
            entities.put(entity.getId(),entity);
            return Optional.empty();
        }
        return  Optional.of(entity);

    }

//    @Override
//    public Optional<E> executeQuerry(String querry) {
//        return Optional.empty();
//    }
//
//    @Override
//    public List<E> findAllFiltered(String numePrenumeFilter) {
//        return null;
//    }
//
//    @Override
//    public Optional<E> tryLogin(String mail, String password) {
//        return Optional.empty();
//    }
//
//    @Override
//    public Long saveMessage(String  mesaj) {
//    return -1L;
//    }
//
//    @Override
//    public void saveMessageSent(Long id1, Long id2, LocalDateTime date, Long idMessage) {
//
//    }
//
//    @Override
//    public List<Message> getMessages(Long id1, Long id2) {
//        return null;
//    }
}
