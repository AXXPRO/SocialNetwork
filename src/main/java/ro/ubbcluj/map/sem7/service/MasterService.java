package ro.ubbcluj.map.sem7.service;

import ro.ubbcluj.map.sem7.domain.*;
import ro.ubbcluj.map.sem7.domain.exceptions.UtilizatorExceptions;
import ro.ubbcluj.map.sem7.events.*;
import ro.ubbcluj.map.sem7.observer.Observable;
import ro.ubbcluj.map.sem7.observer.Observer;
import ro.ubbcluj.map.sem7.paging.Page;
import ro.ubbcluj.map.sem7.paging.Pageable;

import java.util.ArrayList;
import java.util.List;

public class MasterService implements Observable<Event> {
    ServiceUtilizator serviceUtilizator;
    ServicePrietenie servicePrietenie;

    ServiceMessage serviceMessage;


    ArrayList<Observer<Event>> userObservers = new ArrayList<>();

    public MasterService(ServiceUtilizator serviceUtilizator, ServicePrietenie servicePrietenie,ServiceMessage serviceMessage) {
        this.serviceUtilizator = serviceUtilizator;
        this.servicePrietenie = servicePrietenie;
        this.serviceMessage = serviceMessage;

    }
    public Utilizator addUtilizator(List<String> list) throws UtilizatorExceptions {
      var util = serviceUtilizator.add(list);
      notifyObservers(new UserChangeEvent(UserChanges.ADD));
      return util;
    }
    @Deprecated
    //Not in actual USE!
    public ArrayList<Utilizator> findAllUsersMatching(String firstName, String lastName) throws UtilizatorExceptions{
        return serviceUtilizator.findAllMatching(firstName,lastName);
    }

//    private void removeAllFriends(Long ID){
//        String ID_str = ID.toString();
//       String querry = "delete from friendships where id1 = "+ ID_str+ " or id2 = "+ID_str;
//        servicePrietenie.executeQuerry(querry);
//        /*
//        ALTERNATIV, FARA QUERRY
//
//        ArrayList<Prietenie> removeFriends = new ArrayList<Prietenie>();
//        var friendsList = servicePrietenie.findAll();
//        for(var prieten : friendsList)
//        {
//            if(prieten.getId().getLeft().equals(ID) || prieten.getId().getRight().equals(ID))
//                removeFriends.add(prieten);
//        }
//        for(var prieten : removeFriends)
//        {
//            try {
//                servicePrietenie.delete(prieten.getId());
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//
//        }
//         */
//    }
    public Utilizator deleteUser(Long ID) throws Exception {
        //removeAllFriends(ID);
       var util = serviceUtilizator.delete(ID);
        notifyObservers(new UserChangeEvent(UserChanges.DELETE));
       return util;
    }

    public Prietenie addRequest(List<String> list) throws UtilizatorExceptions {
        return servicePrietenie.addRequest(list);
    }
    public Message addMessage(List<String> list) throws Exception{
        return serviceMessage.add(list);
    }
    public Prietenie addFriend(List<String> list) throws UtilizatorExceptions{
        return servicePrietenie.add(list);
    }
    public Prietenie removeFriend(Long ID_entity, Long ID_friendEntity) throws Exception {
        servicePrietenie.delete(new Tuple<>(ID_entity, ID_friendEntity));
        return servicePrietenie.delete(new Tuple<>(ID_friendEntity, ID_entity));
    }
    public ArrayList<Utilizator> CeaMaiSociabilaComunitate() {
        var iterable = servicePrietenie.findAll();
        ArrayList<Prietenie> list = new ArrayList<>();
        for (Prietenie element : iterable) {
            list.add(element);
        }
        var users = serviceUtilizator.findAll();
        int numUsers = 0;
        for (var element : users) {
            numUsers++;
        }
        GrafUtilizatori G = new GrafUtilizatori(list, numUsers);
        ArrayList<Long>  listReturned = G.ceaMaiLungaComponentaConexa();

        //CAST TO UTILZIATORI
        ArrayList<Utilizator> toReturn = new ArrayList<>();

        listReturned.forEach(ID -> {
            Utilizator util;
            try {
              util = serviceUtilizator.findOne(ID);
            } catch (Exception e) {
                throw new RuntimeException(e); //NOT HAPENNING
            }

                toReturn.add(util);

        });

        return toReturn;

    }
    public long numarComunitati() {

        var iterable = servicePrietenie.findAll();
        ArrayList<Prietenie> list = new ArrayList<>();
        for (Prietenie element : iterable) {
            list.add(element);
        }
        var users = serviceUtilizator.findAll();
        int numUsers = 0;
        for (var element : users) {
           numUsers++;
        }
        GrafUtilizatori G = new GrafUtilizatori(list, numUsers);
        return G.componenteConexe();

    }
    public List<Utilizator> findAllUsers() {return serviceUtilizator.findAll();}
    public String utilizatorDetaliat(long ID) throws UtilizatorExceptions {return null;}

    private String prietenieToString(Prietenie prietenie) {
        long idInteresat = prietenie.getId().getRight();
        Utilizator util;
        try {
           util = serviceUtilizator.findOne(idInteresat);
        } catch (Exception e) {
            throw new RuntimeException(e); //Not hapenning
        }
        String answer;
       return answer = util.getFirstName()+"|"+util.getLastName()+"|"+prietenie.getFriendsFrom();
    }
    public List<String> prietenieLuna(long id, int luna) {
        String stringContained;
        if(luna < 10)
            stringContained = "-0" + Integer.toString(luna) + "-";
        else stringContained = "-"+ Integer.toString(luna)+"-";

        ArrayList<String> list = new ArrayList<>();
        Iterable<Prietenie> prietenii = servicePrietenie.findAll();
        ArrayList<Prietenie> prieteniPosibili = new ArrayList<>();
        ArrayList<Prietenie> prieteniSiguri = new ArrayList<>();

        prietenii.forEach(  x-> {
            //Nu e nevoie sa verificam si in dreapta, daca exista una, exista si cealalta
            if(x.getId().getLeft() == id)
                prieteniPosibili.add(x);
        });
        prieteniPosibili.stream().filter(x -> x.getFriendsFrom().toString().contains(stringContained)
        ).forEach(prieteniSiguri::add);

        //prieteniSiguri.forEach(x -> System.out.println(x.getFriendsFrom()));

        prieteniSiguri.forEach(x->list.add(prietenieToString(x)));
        return list;
    }

    public  Utilizator updateUtilziator(String nume, String prenume, String mail, String password, Long ID) throws UtilizatorExceptions
    {
        var util = serviceUtilizator.updateUtilziator(nume,prenume,mail,password,ID);
        notifyObservers(new UserChangeEvent(UserChanges.UPDATE));
        return util;
    }


    @Override

    public void addObserver(Observer<Event> e) {
        userObservers.add(e);
    }

    @Override
    public void removeObserver(Observer<Event> e) {
        userObservers.remove(e);

    }

    @Override
    public void notifyObservers(Event t) {
       userObservers.forEach(observer -> observer.update(t));
    }

    public List<Utilizator> findAllUsersFiltered(String numePrenumeFilter) {
       return serviceUtilizator.findAllFiltered(numePrenumeFilter);
    }

    public Page<Utilizator> findAllUsersFiltered(String numePrenumeFilter, Pageable pageable) {
        return serviceUtilizator.findAllFiltered( numePrenumeFilter, pageable);
    }



    public Utilizator tryLogin(String mail, String password) throws UtilizatorExceptions {
        return serviceUtilizator.tryLogin(mail, password);

    }

    public List<Message> getMessages(Long id1, Long id2){
        return serviceMessage.getMessages(id1,id2);
    }
    public List<Message> getMessages(Long id1, Long id2, Pageable pageable){
        return serviceMessage.getMessages(id1,id2,pageable);
    }

    public void emitChange(Event eventType) {

        notifyObservers(eventType);
    }

    public List<Utilizator> findAllFriendRequests(Long id){
        List<Prietenie> listaPrieteni = servicePrietenie.findAllFriendRequests(id);
        ArrayList<Utilizator> utilizatori = new ArrayList<>();
        listaPrieteni.forEach(prietenie -> {
            try {
                utilizatori.add(serviceUtilizator.findOne(prietenie.getId().getLeft()));
            } catch (Exception e) {
                throw new RuntimeException(e); //Not hapenning
            }
        });

        return utilizatori;
    }
    public List<Utilizator> findAllFriends(Long id) {


        List<Prietenie> listaPrieteni = servicePrietenie.findAllFriends(id);
        ArrayList<Utilizator> utilizatori = new ArrayList<>();
        listaPrieteni.forEach(prietenie -> {
            try {
                utilizatori.add(serviceUtilizator.findOne(prietenie.getId().getRight()));
            } catch (Exception e) {
                throw new RuntimeException(e); //Not hapenning
            }
        });

        return utilizatori;
    }

    public void acceptRequest(Long id, Long id1) {
        try {
            servicePrietenie.delete(new Tuple<>(id, id1));
            servicePrietenie.add(new ArrayList<>(){{
                add(Long.toString(id));
                add(Long.toString(id1));
            }});
            emitChange(new FriendshipEvent());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void denyRequest(Long id, Long id1) {
        try {
            servicePrietenie.delete(new Tuple<>(id, id1));
            emitChange(new FriendshipEvent());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //returns prietenie de la id la id1
    public Prietenie findFriendship(Long id, Long id1) {
        try {
           return servicePrietenie.findOne(new Tuple <>(id,id1));
        } catch (Exception e) {
            return null;
        }
    }
}
