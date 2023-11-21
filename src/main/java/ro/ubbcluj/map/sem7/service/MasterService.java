package ro.ubbcluj.map.sem7.service;

import ro.ubbcluj.map.sem7.domain.GrafUtilizatori;
import ro.ubbcluj.map.sem7.domain.Prietenie;
import ro.ubbcluj.map.sem7.domain.Tuple;
import ro.ubbcluj.map.sem7.domain.Utilizator;
import ro.ubbcluj.map.sem7.domain.exceptions.UtilizatorExceptions;
import ro.ubbcluj.map.sem7.events.UserChangeEvent;
import ro.ubbcluj.map.sem7.events.UserChanges;
import ro.ubbcluj.map.sem7.observer.Observable;
import ro.ubbcluj.map.sem7.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class MasterService implements Observable<UserChangeEvent> {
    ServiceUtilizator serviceUtilizator;
    ServicePrietenie servicePrietenie;

    ArrayList<Observer<UserChangeEvent>> userObservers = new ArrayList<>();

    public MasterService(ServiceUtilizator serviceUtilizator, ServicePrietenie servicePrietenie) {
        this.serviceUtilizator = serviceUtilizator;
        this.servicePrietenie = servicePrietenie;

    }
    public Utilizator addUtilizator(List<String> list) throws UtilizatorExceptions {
      var util = serviceUtilizator.add(list);
      notifyObservers(new UserChangeEvent(UserChanges.ADD));
      return util;
    }
    public ArrayList<Utilizator> findAllUsersMatching(String firstName, String lastName) throws UtilizatorExceptions{
        return serviceUtilizator.findAllMatching(firstName,lastName);
    }

    private void removeAllFriends(Long ID){
        String ID_str = ID.toString();
       String querry = "delete from friendships where id1 = "+ ID_str+ " or id2 = "+ID_str;
        servicePrietenie.executeQuerry(querry);
        /*
        ALTERNATIV, FARA QUERRY

        ArrayList<Prietenie> removeFriends = new ArrayList<Prietenie>();
        var friendsList = servicePrietenie.findAll();
        for(var prieten : friendsList)
        {
            if(prieten.getId().getLeft().equals(ID) || prieten.getId().getRight().equals(ID))
                removeFriends.add(prieten);
        }
        for(var prieten : removeFriends)
        {
            try {
                servicePrietenie.delete(prieten.getId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
         */
    }
    public Utilizator deleteUser(Long ID) throws Exception {
        //removeAllFriends(ID);
       var util = serviceUtilizator.delete(ID);
        notifyObservers(new UserChangeEvent(UserChanges.DELETE));
       return util;
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

    public void addObserver(Observer<UserChangeEvent> e) {
        userObservers.add(e);
    }

    @Override
    public void removeObserver(Observer<UserChangeEvent> e) {
        userObservers.remove(e);

    }

    @Override
    public void notifyObservers(UserChangeEvent t) {
       userObservers.forEach(observer -> observer.update(t));
    }

    public List<Utilizator> findAllUsersFiltered(String numePrenumeFilter) {
       return serviceUtilizator.findAllFiltered(numePrenumeFilter);
    }

    public Long tryLogin(String mail, String password) {
        return serviceUtilizator.tryLogin(mail, password);

    }
}
