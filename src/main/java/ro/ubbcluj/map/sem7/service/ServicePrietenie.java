package ro.ubbcluj.map.sem7.service;

import ro.ubbcluj.map.sem7.domain.Prietenie;
import ro.ubbcluj.map.sem7.domain.Tuple;
import ro.ubbcluj.map.sem7.domain.exceptions.UtilizatorExceptions;
import ro.ubbcluj.map.sem7.repository.FriendshipDBRepository;
import ro.ubbcluj.map.sem7.repository.Repository;

import java.time.LocalDateTime;
import java.util.List;

public class ServicePrietenie extends AbstractService<Tuple<Long,Long>, Prietenie>{

    FriendshipDBRepository repo;
    public ServicePrietenie(FriendshipDBRepository repo) {
        super(repo);
        this.repo = repo;

    }
//    @Override
//    public Prietenie findOne(Tuple<Long, Long> longLongTuple) throws Exception {
//        return null;
//    }

//    @Override
//    public Iterable<Prietenie> findAll() {
//        return null;
//    }

    @Override
    public Prietenie add(List<String> list) throws UtilizatorExceptions {
        Long id1, id2;
        id1 =  Long.valueOf(list.get(0));
        id2 =  Long.valueOf(list.get(1));
        var currentDate = LocalDateTime.now();
        Prietenie prietenie1 = new Prietenie(currentDate, id1,id2);
        Prietenie prietenie2 = new Prietenie(currentDate, id2,id1);
        if(repo.save(prietenie1).isPresent())
            throw new UtilizatorExceptions("Nu s-a putut adauga!\n");
        repo.save(prietenie2);
        return null;
    }

    @Override
    public Prietenie delete(Tuple<Long, Long> longLongTuple) throws Exception {
        var prietenie = repo.findOne(longLongTuple);
        if(prietenie.isEmpty())
            throw new UtilizatorExceptions("Prietenia nu exista!\n");

        return super.delete(longLongTuple);
    }

    @Override
    public Prietenie update(Prietenie entity) throws Exception {
        return null;
    }

    //Return a list of Friendships where first id is id, and second id is friend id
    public List<Prietenie> findAllFriends(Long id) {

      return   repo.findAllFriends(id);
    }
//    public void executeQuerry(String querry){
//        repo.executeQuerry(querry);
//    }
}
