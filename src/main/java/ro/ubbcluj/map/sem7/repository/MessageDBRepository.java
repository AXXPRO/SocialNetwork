package ro.ubbcluj.map.sem7.repository;

import ro.ubbcluj.map.sem7.domain.Message;
import ro.ubbcluj.map.sem7.domain.Utilizator;
import ro.ubbcluj.map.sem7.domain.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageDBRepository implements Repository<Long, Message>{

    protected String url;
    protected String username;
    protected String password;

    public MessageDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;

    }

    @Override
    public Optional<Message> findOne(Long longID) {
        Message M;
        ArrayList<Long> toIDS = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from \"messageSent\" " +
                    "where \"idMessage\" = ?");
            PreparedStatement statementMSG = connection.prepareStatement("select mesaj from message where id = ?")

        ) {
            statementMSG.setLong(1, longID);
            statement.setInt(1, Math.toIntExact(longID));
            ResultSet resultSet = statement.executeQuery();
            ResultSet mesajSet = statementMSG.executeQuery();
            String mesaj="";
            if(mesajSet.next())
            {
                mesaj = mesajSet.getString("mesaj");
            }
            long senderID;
            long receiverID;
            if(resultSet.next()) {
                 senderID = resultSet.getLong("idFrom");
                receiverID = resultSet.getLong("idTo");
                toIDS.add(receiverID);
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                M = new Message(longID,senderID,toIDS,mesaj,date);
                while(resultSet.next()) {
                    receiverID = resultSet.getLong("idTo");
                    toIDS.add(receiverID);
                }
                M.setToIDS(toIDS);
                return Optional.of(M);

            }


        } catch (SQLException e) {
            return Optional.empty();
        }

        return Optional.empty();
    }

    @Override
    public List<Message> findAll() {
        return null;
    }

    @Override
    public Optional<Message> save(Message entity) {

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("insert into message(mesaj) VALUES (?)RETURNING id");
            PreparedStatement statement2 = connection.prepareStatement("insert into \"messageSent\"" +
                    "(\"idFrom\", \"idTo\", date, \"idMessage\") VALUES (?,?,?,?) ");

        ) {
            statement.setString(1, entity.getMessage());
            Long id;
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                id = resultSet.getLong("id");
                statement2.setLong(1, entity.getFromID());
                statement2.setTimestamp(3,Timestamp.valueOf(entity.getDateTime()));
                statement2.setLong(4, id);
                entity.getToIDS().forEach(IDFrom -> {
                    try {
                        statement2.setLong(2,IDFrom);
                        statement2.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                });
            }


        } catch (SQLException e) {
            return Optional.empty();
        }

       return Optional.empty();
    }

    @Override
    public Optional<Message> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Message> update(Message entity) {
        return Optional.empty();
    }

        public List<Message> getMessages(Long id1, Long id2) {
        ArrayList<Message> messages = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select M.id as id, MS.\"idFrom\" as idFrom, " +
                     " MS.\"idTo\" as idTo, MS.date as date, M.mesaj as mesaj from \"messageSent\" MS FULL JOIN" +
                     " message M on MS.\"idMessage\" = M.id where (\"idFrom\" = ? and \"idTo\" = ?) or (\"idFrom\" = ? and \"idTo\" = ?) order by MS.date" );

        ) {
            statement.setLong(1, id1);
            statement.setLong(2, id2);
            statement.setLong(3, id2);
            statement.setLong(4, id1);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                Long id= resultSet.getLong("id");
                Long idFrom = resultSet.getLong("idFrom");
                Long idTo = resultSet.getLong("idTo");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                String mesaj = resultSet.getString("mesaj");

                Message M = new Message(id,idFrom,new ArrayList<>(){{add(idTo);}},mesaj,date);
               messages.add(M);

            }
            return messages;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
