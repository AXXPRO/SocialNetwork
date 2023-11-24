package ro.ubbcluj.map.sem7.repository;

import ro.ubbcluj.map.sem7.domain.Message;
import ro.ubbcluj.map.sem7.domain.Prietenie;
import ro.ubbcluj.map.sem7.domain.Tuple;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

public class FriendshipDBRepository implements Repository<Tuple<Long,Long>,Prietenie> {

private String url;
private String username;
private String password;

//private Validator<Prietenie> validator;


    public FriendshipDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

        public Optional<Prietenie> executeQuerry(String querry){
                try(Connection connection = DriverManager.getConnection(url, username, password);
                    PreparedStatement statement = connection.prepareStatement(querry);

                ) {
                       statement.executeUpdate();
                } catch (SQLException e) {
                        throw new RuntimeException(e);
                }

                return Optional.empty();
        }

//        @Override
//        public List<Prietenie> findAllFiltered(String numePrenumeFilter) {
//                return null;
//        }
//
//        @Override
//        public Optional<Prietenie> tryLogin(String mail, String password) {
//                return Optional.empty();
//        }
//
//        @Override
//        public Long saveMessage(String mesaj) {
//        return -1L;
//        }
//
//    @Override
//    public void saveMessageSent(Long id1, Long id2, LocalDateTime date, Long idMessage) {
//
//    }
//
//    @Override
//    public List<Message> getMessages(Long id1, Long id2) {
//        return null;
//    }

    @Override
        public Optional<Prietenie> findOne(Tuple<Long,Long> longID) {
                try(Connection connection = DriverManager.getConnection(url, username, password);
                    PreparedStatement statement = connection.prepareStatement("select * from friendships " +
                            "where id1 = ? and id2 = ?");

                ) {
                        statement.setInt(1, Math.toIntExact(longID.getLeft()));
                        statement.setInt(2, Math.toIntExact(longID.getRight()));
                        ResultSet resultSet = statement.executeQuery();
                        if(resultSet.next()) {
                                Long id1 = resultSet.getLong("id1");
                                Long id2 = resultSet.getLong("id2");

                                LocalDateTime date = resultSet.getDate("friendsfrom").toLocalDate().atStartOfDay();
                                Prietenie u = new Prietenie(date,id1,id2);
                                u.setId(longID);
                                return Optional.ofNullable(u);
                        }
                } catch (SQLException e) {
                        throw new RuntimeException(e);
                }

                return Optional.empty();
        }


        @Override
public List<Prietenie> findAll() {
        ArrayList<Prietenie> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
        PreparedStatement statement = connection.prepareStatement("select * from friendships");
        ResultSet resultSet = statement.executeQuery()
        ) {

        while (resultSet.next())
        {
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                LocalDateTime date =  resultSet.getDate("friendsfrom").toLocalDate().atStartOfDay();
        Prietenie prietenie=new Prietenie(date,id1,id2);

        prietenie.setId(new Tuple<Long,Long>(id1,id2));
        users.add(prietenie);

        }
        return users;

        } catch (SQLException e) {
        throw new RuntimeException(e);
        }

        }

@Override
public Optional<Prietenie> save(Prietenie entity) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("insert" +
                     " into friendships(id1,id2,friendsfrom) VALUES (?,?,?)");
        ) {
                statement.setInt(1, Math.toIntExact(entity.getId().getLeft()));
                statement.setInt(2, Math.toIntExact(entity.getId().getRight()));

                statement.setDate(3,  Date.valueOf(entity.getFriendsFrom().toLocalDate()));
                statement.executeUpdate();

                return Optional.empty();

        } catch (SQLException e) {
                return Optional.of(entity);
        }

}

@Override
public Optional<Prietenie> delete(Tuple<Long,Long> aLong) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement1 = connection.prepareStatement(
                     "select * from friendships where id1 = ? and id2 = ?");
             PreparedStatement statement2 = connection.prepareStatement(
                     "delete from friendships where id1 = ? and id2 = ?");
        ) {
                statement1.setInt(1, Math.toIntExact(aLong.getLeft()));
                statement1.setInt(2, Math.toIntExact(aLong.getRight()));
                statement2.setInt(1, Math.toIntExact(aLong.getLeft()));
                statement2.setInt(2, Math.toIntExact(aLong.getRight()));
                ResultSet resultSet1 = statement1.executeQuery();
                statement2.executeUpdate();
                if(resultSet1.next()) {
                        Long id1 = resultSet1.getLong("id1");
                        Long id2 = resultSet1.getLong("id2");
                        LocalDateTime dateTime =  resultSet1.getDate("friendsfrom").toLocalDate().atStartOfDay();
                        Prietenie u = new Prietenie(dateTime,id1,id2);
                        u.setId(aLong);
                        return Optional.of(u);
                }
        } catch (SQLException e) {
                return Optional.empty();
        }
        return Optional.empty();
        }

@Override
public Optional<Prietenie> update(Prietenie entity) {
        return Optional.of(entity);
        }
}
