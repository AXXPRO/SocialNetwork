package ro.ubbcluj.map.sem7.repository;

import ro.ubbcluj.map.sem7.domain.Utilizator;
import ro.ubbcluj.map.sem7.domain.validators.Validator;

import java.util.List;
import java.util.Optional;

public class UtilizatorFileRepository extends AbstractFileRepository<Long, Utilizator> {

    public UtilizatorFileRepository(String fileName, Validator<Utilizator> validator) {
        super(fileName, validator);
    }

    @Override
    public Utilizator extractEntity(List<String> attributes) {
        //TODO: implement method
        Utilizator user = new Utilizator(attributes.get(1),attributes.get(2));
        user.setId(Long.parseLong(attributes.get(0)));

        return user;
    }

    @Override
    protected String createEntityAsString(Utilizator entity) {
        return entity.getId()+";"+entity.getFirstName()+";"+entity.getLastName();
    }

//    @Override
//    public Optional<Utilizator> tryLogin(String mail, String password) {
//        return Optional.empty();
//    }
}
