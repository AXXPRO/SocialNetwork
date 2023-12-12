package ro.ubbcluj.map.sem7.service;

import ro.ubbcluj.map.sem7.domain.Utilizator;
import ro.ubbcluj.map.sem7.domain.UtiliziatorFactory;
import ro.ubbcluj.map.sem7.domain.exceptions.FriendshipException;
import ro.ubbcluj.map.sem7.domain.exceptions.UtilizatorExceptions;
import ro.ubbcluj.map.sem7.paging.Page;
import ro.ubbcluj.map.sem7.paging.Pageable;
import ro.ubbcluj.map.sem7.repository.Repository;
import ro.ubbcluj.map.sem7.repository.UserDBPagingRepository;
import ro.ubbcluj.map.sem7.repository.UserDBRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceUtilizator extends AbstractService<Long, Utilizator> {

    long LatestID = 0;
    UserDBPagingRepository repo;


    /**
     *
     * @param repo the repository used
     */
    public ServiceUtilizator(UserDBPagingRepository repo) {
        super(repo);
        this.repo = repo;

        repo.findAll().forEach(util-> {
            if(util.getId() > LatestID)
                LatestID = util.getId();

        });
//        for (Utilizator util : repo.findAll()) {
//            if (util.getId() > LatestID)
//                LatestID = util.getId();
//        }
        LatestID++;
    }

    /**
     *
     * @param list
     *         list of parameters to create an object
     * @return null if the entity was added
     */

    @Override
    public Utilizator add(List<String> list) throws UtilizatorExceptions{
        String firstName, lastName,mail,password;
        firstName = list.get(0);
        lastName = list.get(1);
        mail = list.get(2);
        password = list.get(3);

        Utilizator util = UtiliziatorFactory.getInstance().getUtilizator(firstName, lastName,mail,password, LatestID);

        if(repo.save(util).isPresent())
            throw new UtilizatorExceptions("Nu s-a putut adauga!\n");
        return null;
    }

    /**
     *
     * @param firstName
     * @param lastName
     * @return a list of users matching the provided firstName and lastName
     * @throws UtilizatorExceptions if none match
     */
    @Deprecated
    public ArrayList<Utilizator> findAllMatching(String firstName, String lastName) throws UtilizatorExceptions {
        ArrayList<Utilizator> list = new ArrayList<>();
        //Iterable<Utilizator> listAll = repo.findAll();

        repo.findAll().forEach(util->{
            if (util.getFirstName().equals(firstName) && util.getLastName().equals(lastName)) {
                list.add(util);
            }

        });
//        for (Utilizator util : listAll) {
//            if (util.getFirstName().equals(firstName) && util.getLastName().equals(lastName)) {
//                list.add(util);
//            }
//        }
//

        if(list.isEmpty())
            throw new UtilizatorExceptions("Nu exista niciun utilizator cu acest nume!\n");
        return list;
    }

    public  Utilizator updateUtilziator(String nume, String prenume,String email, String password, Long ID) throws UtilizatorExceptions{
        Utilizator util = UtiliziatorFactory.getInstance().getUtilizator(nume, prenume,email, password, ID);

        var returnUser = repo.update(util);
        if(returnUser.isPresent())
            throw new UtilizatorExceptions("Nu s-a putut actualiza!");
        return null;

    }

//    /**
//     * @param ID_entity
//     * @param ID_friendEntity
//     * @throws FriendshipException if the 2 entities are already friends
//     */
//    public void addFriend(Long ID_entity, Long ID_friendEntity) throws UtilizatorExceptions {
//        var e1 = repo.findOne(ID_entity);
//        var e2 = repo.findOne(ID_friendEntity);
//
//        if(e1.isEmpty() || e2.isEmpty())
//            throw new UtilizatorExceptions("Nu s-au gasit utilizatorii cu aceste ID-uri!\n");
//        var E1 = e1.get();
//        var E2 = e2.get();
//
//        if(ID_entity.equals(ID_friendEntity))
//            throw new FriendshipException("Exista deja prietenie intre acestia doi!\n");
//
//        for (var util : E1.getFriends()) {
//            if (util.getId().equals(E2.getId()))
//                throw new FriendshipException("Exista deja prietenie intre acestia doi!\n");
//        }
//
//
//        E1.addFriend(UtiliziatorFactory.getInstance().getUtilizator(E2.getFirstName(), E2.getLastName(), E2.getId()));
//        E2.addFriend(UtiliziatorFactory.getInstance().getUtilizator(E1.getFirstName(), E1.getLastName(), E1.getId()));
//        repo.update(E1);
//        repo.update(E2);
//
//    }

    /**
     * @param ID_entity
     * @param ID_friendEntity
     * @throws FriendshipException if the 2 entities aren't friends
     */
    public void removeFriend(Long ID_entity, Long ID_friendEntity) throws UtilizatorExceptions {
        var e1 = repo.findOne(ID_entity);
        var e2 = repo.findOne(ID_friendEntity);

        if(e1.isEmpty() || e2.isEmpty())
            throw new UtilizatorExceptions("Nu s-au gasit utilizatorii cu aceste ID-uri!\n");
        var E1 = e1.get();
        var E2 = e2.get();

        if(ID_entity.equals(ID_friendEntity))
            throw new FriendshipException("Nu exista prietenie intre acestia doi!\n");

        boolean prietenie = false;
        for (var util : E1.getFriends()) {
            if (util.getId().equals(E2.getId())) {
                prietenie = true;
                break;
            }

        }
        if (!prietenie)
            throw new FriendshipException("Nu exista prietenie intre acestia doi!\n");

        E1.removeFriend(UtiliziatorFactory.getInstance().getUtilizator(E2.getFirstName(), E2.getLastName(), E2.getMail(),
                E2.getPassword(), E2.getId()));
        E2.removeFriend(UtiliziatorFactory.getInstance().getUtilizator(E1.getFirstName(), E1.getLastName(), E1.getMail(),
                E1.getPassword(), E1.getId()));
        repo.update(E1);
        repo.update(E2);
    }

    /**
     *
     * @return numarul de comunitati ( grupurile de utilziatori prieteni intre ei )
     */
//    public long numarComunitati() {
//        List<Utilizator> list = new ArrayList<Utilizator>();
//        repo.findAll().forEach(list::add);
////        for (var el : repo.findAll()) {
////            list.add(el);
////        }
//        GrafUtilizatori G = new GrafUtilizatori(list);
//        return G.componenteConexe();
//    }

    /**
     *
     * @return cea mai sociabila comunitate ( cel mai lung lant de utilizatori prieteni)
     */
//    public ArrayList<Utilizator> CeaMaiSociabilaComunitate() {
//
//        List<Utilizator> list = new ArrayList<Utilizator>();
//        repo.findAll().forEach(list::add);
////        for (var el : repo.findAll()) {
////            list.add(el);
////        }
//        GrafUtilizatori G = new GrafUtilizatori(list);
//        ArrayList<Long>  listReturned = G.ceaMaiLungaComponentaConexa();
//
//        //CAST TO UTILZIATORI
//        ArrayList<Utilizator> toReturn = new ArrayList<>();
//
//        listReturned.forEach(ID -> {
//            if(repo.findOne(ID).isPresent())
//                toReturn.add(repo.findOne(ID).get());
//        });
////        for (var ID: listReturned) {
////            toReturn.add(repo.findOne(ID));
////        }
//        return toReturn;
//
//    }

    /**
     *
     * @param util - utilizatorul caruia ii vom sterge toti prietenii
     */
//  private void removeAllFriends(Utilizator util){
//        List<Utilizator> friendList =  new ArrayList<>(util.getFriends());
//        friendList.forEach(prieten -> {
//            try {
//                removeFriend(util.getId(), prieten.getId());
//            } catch (UtilizatorExceptions e) {
//                throw new RuntimeException(e); // Not happening
//            }
//
//        });
////        for(var prieten: friendList)
////        {
////            try {
////                removeFriend(util.getId(), prieten.getId());
////            } catch (FriendshipException e) {
////                throw new RuntimeException(e); // Not happening
////            }
////
////        }
//  }

    /**
     *
     * @param ID
     *      id must be not null
     * @return the removed entity or null if there is no entity with the given id
     */
    @Override
    public Utilizator delete(Long ID) throws Exception {
        var util = repo.findOne(ID);
        if(util.isEmpty())
            throw new UtilizatorExceptions("Utilizatorul nu exista!\n");
        return super.delete(ID);
    }


    public List<Utilizator> findAllFiltered(String numePrenumeFilter) {
       return repo.findAllFiltered(numePrenumeFilter);
    }
    public Page<Utilizator> findAllFiltered(String numePrenumeFilter, Pageable pageable) {
        return repo.findAllFiltered(pageable,numePrenumeFilter);
    }


    public Utilizator tryLogin(String mail, String password) throws UtilizatorExceptions {

       Optional<Utilizator> util = repo.tryLogin(mail, password);

       if(util.isEmpty())
           throw new UtilizatorExceptions("No user Matching");
       return util.get();
    }

    /**
     *
     * @param ID - id-ul utilizatorului pe care il vrem
     * @return un string cu detalii care includ prietenii unui utilizator
     */
//    public String utilizatorDetaliat(long ID) throws UtilizatorExceptions {
//
//        var util = repo.findOne(ID);
//        if(util.isEmpty())
//            throw new UtilizatorExceptions("Niciun utilizator nu are ID-ul dat!\n");
//        return util.get().toStringWithFriends();
//    }


}
