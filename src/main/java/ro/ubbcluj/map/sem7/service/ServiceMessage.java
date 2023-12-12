package ro.ubbcluj.map.sem7.service;

import ro.ubbcluj.map.sem7.domain.Message;
import ro.ubbcluj.map.sem7.domain.Prietenie;
import ro.ubbcluj.map.sem7.domain.exceptions.UtilizatorExceptions;
import ro.ubbcluj.map.sem7.paging.Pageable;
import ro.ubbcluj.map.sem7.repository.MessageDBPagingRepository;
import ro.ubbcluj.map.sem7.repository.MessageDBRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ServiceMessage extends AbstractService<Long, Message> {

    MessageDBPagingRepository repo;
    public ServiceMessage(MessageDBPagingRepository repo) {
        super(repo);
        this.repo = repo;
    }

    @Override
    public Message add(List<String> list) throws Exception {
        Long id, idFrom;
        String msg;
        LocalDateTime date = LocalDateTime.now();

        //ID, FROM, TO, MESAJ ,



        idFrom = Long.valueOf(list.get(0));
        msg = list.get(2);
        Long replyID = Long.valueOf( list.get(3));
        ArrayList<Long> longIDS = new ArrayList<>();
        String[] strSplit =  list.get(1).split(" ");
        for (String s : strSplit) {
            longIDS.add(Long.valueOf(s));

        }
        Optional<Message> MReply =repo.findOne(replyID);
        Message M;
        if(MReply.isEmpty())
         M = new Message(-1L, idFrom ,longIDS, msg,date,null);
        else { M = new Message(-1L, idFrom ,longIDS, msg,date,MReply.get()); }
        if(repo.save(M).isPresent())
            throw new UtilizatorExceptions("Nu s-a putut adauga!\n");
       return  null;

    }

    public List<Message> getMessages(Long ID1, Long ID2){

        return repo.getMessages(ID1, ID2);
    }
    public List<Message> getMessages(Long ID1, Long ID2, Pageable pageable){

        return repo.getMessages(ID1, ID2,pageable);
    }



}
