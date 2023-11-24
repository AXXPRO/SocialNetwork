package ro.ubbcluj.map.sem7.service;

import ro.ubbcluj.map.sem7.domain.Message;
import ro.ubbcluj.map.sem7.domain.Prietenie;
import ro.ubbcluj.map.sem7.domain.exceptions.UtilizatorExceptions;
import ro.ubbcluj.map.sem7.repository.MessageDBRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServiceMessage extends AbstractService<Long, Message> {

    MessageDBRepository repo;
    public ServiceMessage(MessageDBRepository repo) {
        super(repo);
        this.repo = repo;
    }

    @Override
    public Message add(List<String> list) throws Exception {
        Long id, idFrom;
        String msg;
        LocalDateTime date = LocalDateTime.now();

        //ID, FROM, TO, MESAJ ,DATA



        idFrom = Long.valueOf(list.get(0));
        msg = list.get(2);

        ArrayList<Long> longIDS = new ArrayList<>();
        String[] strSplit =  list.get(1).split(" ");
        for (String s : strSplit) {
            longIDS.add(Long.valueOf(s));

        }
        Message M = new Message(-1L, idFrom ,longIDS, msg,date);

        if(repo.save(M).isPresent())
            throw new UtilizatorExceptions("Nu s-a putut adauga!\n");
       return  null;

    }

    public List<Message> getMessages(Long ID1, Long ID2){

        return repo.getMessages(ID1, ID2);
    }


}
