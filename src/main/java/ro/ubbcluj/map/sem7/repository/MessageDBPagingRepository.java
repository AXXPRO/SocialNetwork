package ro.ubbcluj.map.sem7.repository;

import ro.ubbcluj.map.sem7.domain.Message;
import ro.ubbcluj.map.sem7.paging.Page;
import ro.ubbcluj.map.sem7.paging.Pageable;
import ro.ubbcluj.map.sem7.paging.PagingRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MessageDBPagingRepository extends MessageDBRepository implements PagingRepository<Long, Message> {
    public MessageDBPagingRepository(String url, String username, String password) {
        super(url, username, password);
    }

    @Override
    public Page<Message> findAll(Pageable pageable) {
        return null;
    }

    public List<Message> getMessages(Long id1, Long id2, Pageable pageable) {
        ArrayList<Message> messages = new ArrayList<>();
        ArrayList<Message> messagesTemp = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select M.id as id, MS.\"idFrom\" as idFrom, " +
                     " MS.\"idTo\" as idTo, MS.date as date, M.mesaj as mesaj, MS.replyid as replyid from \"messageSent\" MS FULL JOIN" +
                     " message M on MS.\"idMessage\" = M.id where (\"idFrom\" = ? and \"idTo\" = ?) or (\"idFrom\" = ? and \"idTo\" = ?) order by MS.date desc LIMIT ?",
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY);

        ) {
            statement.setLong(1, id1);
            statement.setLong(2, id2);
            statement.setLong(3, id2);
            statement.setLong(4, id1);
            statement.setLong(5, pageable.getPageSize());

            ResultSet resultSet = statement.executeQuery();
            resultSet.afterLast();

            while (resultSet.previous()) {
                Long id = resultSet.getLong("id");
                Long idFrom = resultSet.getLong("idFrom");
                Long idTo = resultSet.getLong("idTo");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                String mesaj = resultSet.getString("mesaj");
                Long idReply = resultSet.getLong("replyid");

                Message M = null;


                if(idReply != 0)
                {
                    for ( Message mesajPanaAcum : messages) {
                        if(mesajPanaAcum.getId().equals(idReply))
                        {
                            M = new Message(id,idFrom,new ArrayList<>(){{add(idTo);}},mesaj,date,mesajPanaAcum);

                            break;
                        }

                    }
                    if(M == null)
                        M = new Message(id,idFrom,new ArrayList<>(){{add(idTo);}},mesaj,date,new Message(-1L,-1L,null,"Reply not yet loaded",null,null));

                    messages.add(M);

                }
                else {
                    M = new Message(id,idFrom,new ArrayList<>(){{add(idTo);}},mesaj,date,null);
                    messages.add(M);
                }




            }

            return messages;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
