package ro.ubbcluj.map.sem7.ui;

import ro.ubbcluj.map.sem7.domain.Prietenie;
import ro.ubbcluj.map.sem7.domain.Tuple;
import ro.ubbcluj.map.sem7.domain.Utilizator;
import ro.ubbcluj.map.sem7.domain.exceptions.FriendshipException;
import ro.ubbcluj.map.sem7.domain.exceptions.UtilizatorExceptions;
import ro.ubbcluj.map.sem7.domain.validators.Validator;
import ro.ubbcluj.map.sem7.repository.FriendshipDBRepository;
import ro.ubbcluj.map.sem7.repository.Repository;
import ro.ubbcluj.map.sem7.repository.UserDBRepository;
import ro.ubbcluj.map.sem7.service.MasterService;
import ro.ubbcluj.map.sem7.service.ServicePrietenie;
import ro.ubbcluj.map.sem7.service.ServiceUtilizator;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Clasa de UI de interactiune cu utilziatoru;
 */
public class UI {
    Scanner in = new Scanner(System.in);
    MasterService service;
    Repository<Long, Utilizator> repo;
    Validator<Utilizator> validator;


    /**
     * Constructor ce creeaza un repo si un service
     * @param validator - validatorul pentru un utilizator
     */
    public UI(Validator<Utilizator> validator, String password) {
        this.validator = validator;

        String username = "postgres";

        Repository<Long, Utilizator> UsersRepository = new UserDBRepository("jdbc:postgresql://localhost:5432/socialnetwork", username, password,validator);
        Repository<Tuple<Long, Long>, Prietenie> FriendshipRepository = new FriendshipDBRepository("jdbc:postgresql://localhost:5432/socialnetwork",username,password) ;
        this.service = new MasterService(new ServiceUtilizator(UsersRepository), new ServicePrietenie(FriendshipRepository));

    }

    /**
     * Functie de UI pentru adaugarea utilizatorilor
     */

    public void adaugaUtilizator() throws UtilizatorExceptions {
        System.out.println("Dati numele si prenumele utilziatorului de adaugat(separate prin spatiu): ");
        String numeIntreg = in.nextLine();
        String[] numeIntregSplit = numeIntreg.split(" ", 2);
        var firstName = numeIntregSplit[0];
        var lastName = numeIntregSplit[1];
        service.addUtilizator(new ArrayList<>(){{
            add(firstName);
            add(lastName);
        }});
    }

    /**
     * Functie de ui pentru stergerea unui utilizator
     * @throws UtilizatorExceptions, daca nu exista nicun utilizator ce se potriveste numelui dat
     */
    public void stergeUtilizator()  throws Exception{
        System.out.println("Dati numele si prenumele utilziatorului pe care il stergem(separate prin spatiu): ");
        String numeIntreg = in.nextLine();
        String[] numeIntregSplit = numeIntreg.split(" ", 2);
        var firstName = numeIntregSplit[0];
        var lastName = numeIntregSplit[1];
        long id;
        var elements = service.findAllUsersMatching(firstName,lastName);
        if(elements.size() == 1)
        {
            id = elements.get(0).getId();
        }
        else {
            System.out.println("Mai multi utilizatori au numele identice, scrieti ID-ul caruia il vreti: ");
            printAll(elements);
            int choiceVar = in.nextInt();
            in.nextLine();
            id = choiceVar;
            ArrayList<Long> listIDs = new ArrayList<>();
            elements.forEach(x->listIDs.add(x.getId()));
            if(!listIDs.contains(id))
                throw  new UtilizatorExceptions("Nu se regaseste acest ID in lista!\n");


        }


        service.deleteUser(id);
    }

    /**
     * Functie de ui pentru adaugarea unui prieten
     * @throws UtilizatorExceptions, daca nu exista nicun utilizator ce se potriveste numelui dat
     */
    public void adaugaPrieten() throws UtilizatorExceptions {
        System.out.println("Dati numele si prenumele primului utilziator din prietenie: ");
        String numeIntreg = in.nextLine();
        String[] numeIntregSplit = numeIntreg.split(" ", 2);
        var firstName = numeIntregSplit[0];
        var lastName = numeIntregSplit[1];
        long id;
        var elements = service.findAllUsersMatching(firstName,lastName);
        if(elements.size() == 1)
        {
            id = elements.get(0).getId();
        }
        else {
            System.out.println("Mai multi utilizatori au numele identice, scrieti ID-ul caruia il vreti: ");
            printAll(elements);
            int choiceVar = in.nextInt();
            in.nextLine();
            id = choiceVar;
            ArrayList<Long> listIDs = new ArrayList<>();
            elements.forEach(x->listIDs.add(x.getId()));
            if(!listIDs.contains(id))
                throw  new UtilizatorExceptions("Nu se regaseste acest ID in lista!\n");


        }

        System.out.println("Dati numele si prenumele la al doilea utilziator din prietenie: ");
        String numeIntreg2 = in.nextLine();
        String[] numeIntregSplit2 = numeIntreg2.split(" ", 2);
        var firstName2 = numeIntregSplit2[0];
        var lastName2 = numeIntregSplit2[1];
        long id2;
        var elements2 = service.findAllUsersMatching(firstName2,lastName2);
        if(elements2.size() == 1)
        {
            id2 = elements2.get(0).getId();
        }
        else {
            System.out.println("Mai multi utilizatori au numele identice, scrieti ID-ul caruia il vreti: ");
            printAll(elements2);
            int choiceVar = in.nextInt();
            in.nextLine();
            id2 = choiceVar;
            ArrayList<Long> listIDs = new ArrayList<>();
            elements2.forEach(x->listIDs.add(x.getId()));
            if(!listIDs.contains(id2))
                throw  new UtilizatorExceptions("Nu se regaseste acest ID in lista!\n");


        }

        service.addFriend(
                new ArrayList<>(){{

                    add(Long.toString(id));
                    add(Long.toString(id2));
                }}
                );



    }
    /**
     * Functie de ui pentru stergerea
     * @throws UtilizatorExceptions, daca nu exista nicun utilizator ce se potriveste numelui dat
     */
    public void stergePrieten() throws Exception {
        System.out.println("Dati numele si prenumele primului utilziator din prietenia stearsa: ");
        String numeIntreg = in.nextLine();
        String[] numeIntregSplit = numeIntreg.split(" ", 2);
        var firstName = numeIntregSplit[0];
        var lastName = numeIntregSplit[1];
        long id;
        var elements = service.findAllUsersMatching(firstName,lastName);
        if(elements.size() == 1)
        {
            id = elements.get(0).getId();
        }
        else {
            System.out.println("Mai multi utilizatori au numele identice, scrieti ID-ul caruia il vreti: ");
            printAll(elements);
            int choiceVar = in.nextInt();
            in.nextLine();
            id = choiceVar;
            ArrayList<Long> listIDs = new ArrayList<>();
            elements.forEach(x->listIDs.add(x.getId()));
            if(!listIDs.contains(id))
                throw  new UtilizatorExceptions("Nu se regaseste acest ID in lista!\n");


        }

        System.out.println("Dati numele si prenumele la al doilea utilziator din prietenia stearsa: ");
        String numeIntreg2 = in.nextLine();
        String[] numeIntregSplit2 = numeIntreg2.split(" ", 2);
        var firstName2 = numeIntregSplit2[0];
        var lastName2 = numeIntregSplit2[1];
        long id2;
        var elements2 = service.findAllUsersMatching(firstName2,lastName2);
        if(elements2.size() == 1)
        {
            id2 = elements2.get(0).getId();
        }
        else {
            System.out.println("Mai multi utilizatori au numele identice, scrieti ID-ul caruia il vreti: ");
            printAll(elements2);
            int choiceVar = in.nextInt();
            in.nextLine();
            id2 = choiceVar;
            ArrayList<Long> listIDs = new ArrayList<>();
            elements2.forEach(x->listIDs.add(x.getId()));
            if(!listIDs.contains(id2))
                throw  new UtilizatorExceptions("Nu se regaseste acest ID in lista!\n");


        }
        service.removeFriend(id,id2);
    }

    /**
     * Functie de UI ce afiseaza numarul de comunitati din retea
     */
    public void numarComunitati() {

        System.out.println("Numarul de comunitati este "+ service.numarComunitati());

    }

    /**
     * Functie de UI ce afiseaza cea mai sociabila comunitate din retea
     */
    public void sociabilaComunitate() {
        System.out.println("Cea mai sociabila comunitate este alcatuita din: ");
        ArrayList<Utilizator> utilizatori = service.CeaMaiSociabilaComunitate();

        printAll(utilizatori);
    }
    private void prietenieLuna() throws UtilizatorExceptions{
        System.out.println("Dati numele si prenumele utilziatorului caruia ii cautam prieteniile: ");
        String numeIntreg = in.nextLine();
        String[] numeIntregSplit = numeIntreg.split(" ", 2);
        var firstName = numeIntregSplit[0];
        var lastName = numeIntregSplit[1];
        long id;
        var elements = service.findAllUsersMatching(firstName,lastName);
        if(elements.size() == 1)
        {
            id = elements.get(0).getId();
        }
        else {
            System.out.println("Mai multi utilizatori au numele identice, scrieti ID-ul caruia il vreti: ");
            printAll(elements);
            int choiceVar = in.nextInt();
            in.nextLine();
            id = choiceVar;
            ArrayList<Long> listIDs = new ArrayList<>();
            elements.forEach(x->listIDs.add(x.getId()));
            if(!listIDs.contains(id))
                throw  new UtilizatorExceptions("Nu se regaseste acest ID in lista!\n");

        }

        System.out.println("Dati luna in care ii cautam prietenii (numeric, 1 - Ianuarie, 12 - Decembrie): ");
        int luna;
        try {
            luna = in.nextInt();
            in.nextLine();
        }catch (InputMismatchException e) {
            throw new InputMismatchException("Un numar, va rog");

        }

        if(luna < 1 || luna > 12)
            throw new IllegalArgumentException("Luna incorecta!");
        List<String> listaPrieteni = service.prietenieLuna(id, luna);
        listaPrieteni.forEach(System.out::println);


    }

    /**
     * Functie de UI pentru afisarea tuturor utilizatorului din list
     * @param list - lista cu utilizatorii ce dorim sa ii afisam
     */
    private void printAll(List<Utilizator> list)
    {
        list.forEach(System.out::println);
    }

    /**
     * Functie de UI pentru pritnarea tuturor utilziatorilor
     */
    private void printAllFromService()
    {
        service.findAllUsers().forEach(System.out::println);
    }

    /**
     * Functie de UI pentru pritnarea unui utilizator cu toti prietenii acestuia
     * @throws UtilizatorExceptions, daca nu exista un utilziator cu numele dat
     */


    /**
     * Functia de printare a interfatei utilizatorului
     */

    private void interfata() {

        System.out.println("1. Adauga utilizator.");
        System.out.println("2. Sterge utilizator.");
        System.out.println("3. Adauga prieten.");
        System.out.println("4. Sterge prieten.");
        System.out.println("5. Afisare numar de comunitati.");
        System.out.println("6. Afisare cea mai sociabila comunitate.");
        System.out.println("7. Prieteniile unui utilizator intr-o luna.");


        System.out.println("9. Afisare toate elementele.");
        System.out.println("0. Iesire din aplicatie.");

    }


    /**
     * Functia de lansare in executie a programului
     * @throws FriendshipException, pentru orice eroare legate de prietenie.
     */
    public void run() {


        boolean running = true;
        boolean valid;
        while (running) {
            valid = false;
            interfata();
            int choiceVar=-1;
            while(!valid) {
                try {
                    choiceVar = in.nextInt();
                    in.nextLine();
                    valid = true;
                } catch (InputMismatchException e) {
                    System.out.println("Un numar, va rog");
                    in.nextLine();
                }
            }
            try {
                switch (choiceVar) {
                    case 0:
                        running = false;
                        break;
                    case 1:
                        adaugaUtilizator();
                        break;
                    case 2:
                        stergeUtilizator();
                        break;
                    case 3:
                        adaugaPrieten();
                        break;
                    case 4:
                        stergePrieten();
                        break;
                    case 5:
                        numarComunitati();
                        break;
                    case 6:
                        sociabilaComunitate();
                        break;
                    case 7:
                        prietenieLuna();
                        break;

                    case 9:
                        printAllFromService();
                        break;
                    default:
                        System.out.println("Varianta inexistenta!");
                        break;
                }

            }
            catch (IndexOutOfBoundsException e)
            {
                System.out.println("Dati si numele si prenumele in acelasi mesaj!\n");
            }
            catch (Exception e) {

                System.out.println(e.getMessage());
                in.nextLine();
            }
        }

    }


}

