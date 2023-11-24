package ro.ubbcluj.map.sem7.repository;

import ro.ubbcluj.map.sem7.domain.Message;
import ro.ubbcluj.map.sem7.domain.Utilizator;
import ro.ubbcluj.map.sem7.domain.validators.Validator;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

public class UserDBRepository implements Repository<Long, Utilizator> {

    protected String url;
    protected String username;
    protected String password;

    private Validator<Utilizator> validator;


    public UserDBRepository(String url, String username, String password, Validator<Utilizator> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Optional<Utilizator> findOne(Long longID) {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from users " +
                    "where id = ?");

        ) {
            statement.setInt(1, Math.toIntExact(longID));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String mail = resultSet.getString("mail");
                String password = resultSet.getString("password");
                Utilizator u = new Utilizator(firstName,lastName,mail,password,-1L);
                u.setId(longID);
                return Optional.ofNullable(u);
            }
        } catch (SQLException e) {
            return Optional.empty();
        }

        return Optional.empty();
    }

    @Override
    public List<Utilizator> findAll() {
        ArrayList<Utilizator> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from users order by first_name");
             ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next())
            {
                Long id= resultSet.getLong("id");
                String firstName=resultSet.getString("first_name");
                String lastName=resultSet.getString("last_name");
                String mail = resultSet.getString("mail");
                String password = resultSet.getString("password");
                Utilizator u = new Utilizator(firstName,lastName,mail,password,-1L);
                u.setId(id);
                users.add(u);

            }
            return users;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<Utilizator> save(Utilizator entity) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("insert" +
                     " into users(first_name, last_name,mail,password) VALUES (?,?,?,?)");
        ) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getMail());
            statement.setString(4, entity.getPassword());
            statement.executeUpdate();

            return Optional.empty();

        } catch (SQLException e) {
            return Optional.of(entity);
        }

    }

    @Override
    public Optional<Utilizator> delete(Long aLong) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement1 = connection.prepareStatement(
                             "select * from users where id = ?");
             PreparedStatement statement2 = connection.prepareStatement(
                     "delete from users where id = ?");
        ) {
            statement1.setInt(1, Math.toIntExact(aLong));
            statement2.setInt(1, Math.toIntExact(aLong));
            ResultSet resultSet1 = statement1.executeQuery();
             statement2.executeUpdate();
            if(resultSet1.next()) {
                String firstName = resultSet1.getString("first_name");
                String lastName = resultSet1.getString("last_name");
                Utilizator u = new Utilizator(firstName,lastName);
                u.setId(aLong);
                return Optional.of(u);
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Utilizator> update(Utilizator entity) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementUpdate = connection.prepareStatement(
                     " update  users set first_name = ?, last_name = ?, mail = ?, password = ? where id = ?");
        ) {
            var found = this.findOne(entity.getId());
            if(found.isEmpty())
                return Optional.of(entity);
            statementUpdate.setString(1, entity.getFirstName());
            statementUpdate.setString(2, entity.getLastName());
            statementUpdate.setString(3, entity.getMail());
            statementUpdate.setString(4, entity.getPassword());
            statementUpdate.setLong(5, entity.getId());
            statementUpdate.executeUpdate();
            //statement2.executeUpdate();

            return Optional.empty();

        } catch (SQLException e) {
            return Optional.of(entity);
        }

    }

//    @Override
//    public Optional<Utilizator> executeQuerry(String querry) {
//        return Optional.empty();
//    }


    public List<Utilizator> findAllFiltered(String numePrenumeFilter) {

        ArrayList<Utilizator> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);

             PreparedStatement statement = connection.prepareStatement("select * from users where  CONCAT(first_name, ' ', last_name) LIKE ? order by first_name");

        ) {

            statement.setString(1,numePrenumeFilter+"%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
            {
                Long id= resultSet.getLong("id");
                String firstName=resultSet.getString("first_name");
                String lastName=resultSet.getString("last_name");
                String mail = resultSet.getString("mail");
                String password = resultSet.getString("password");
                Utilizator u = new Utilizator(firstName,lastName,mail,password,-1L);
                u.setId(id);
                users.add(u);

            }
            return users;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public Optional<Utilizator> tryLogin(String mailQuerry, String passwordQuerry) {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from users " +
                    "where mail = ? AND password = ?");

        ) {
            statement.setString(1, mailQuerry);
            statement.setString(2, passwordQuerry);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String mail = resultSet.getString("mail");
                String password = resultSet.getString("password");
                Long ID = resultSet.getLong("id");
                Utilizator u = new Utilizator(firstName,lastName,mail,password,-1L);
                u.setId(ID);
                return Optional.ofNullable(u);
            }
        } catch (SQLException e) {
            return Optional.empty();
        }

        return Optional.empty();
    }

//    @Override
//    public Long saveMessage(String mesaj) {
//        try(Connection connection = DriverManager.getConnection(url, username, password);
//            PreparedStatement statement = connection.prepareStatement("insert into message(mesaj) VALUES (?)RETURNING id");
//
//        ) {
//            statement.setString(1, mesaj);
//
//            ResultSet resultSet = statement.executeQuery();
//            if(resultSet.next()) {
//                return resultSet.getLong("id");
//            }
//        } catch (SQLException e) {
//            return -1L;
//        }
//
//        return -1L;
//    }
//
//    @Override
//    public void saveMessageSent(Long id1, Long id2, LocalDateTime date, Long idMessage) {
//        try (Connection connection = DriverManager.getConnection(url, username, password);
//             PreparedStatement statement = connection.prepareStatement("insert" +
//                     " into \"messageSent\"(\"idFrom\", \"idTo\", date, \"idMessage\") VALUES (?,?,?,?) ");
//        ) {
//            statement.setLong(1,id1);
//            statement.setLong(2,id2);
//            statement.setTimestamp(3,Timestamp.valueOf(date));
//            statement.setLong(4,idMessage);
//            statement.executeUpdate();
//
//        } catch (SQLException e) {
//            System.out.println(e);
//        }
//    }
//
//    @Override
//    public List<Message> getMessages(Long id1, Long id2) {
//        ArrayList<Message> messages = new ArrayList<>();
//
//        try (Connection connection = DriverManager.getConnection(url, username, password);
//             PreparedStatement statement = connection.prepareStatement("select M.id as id, MS.\"idFrom\" as idFrom, " +
//                     " MS.\"idTo\" as idTo, MS.date as date, M.mesaj as mesaj from \"messageSent\" MS FULL JOIN" +
//                     " message M on MS.\"idMessage\" = M.id where (\"idFrom\" = ? and \"idTo\" = ?) or (\"idFrom\" = ? and \"idTo\" = ?) order by MS.date" );
//
//        ) {
//            statement.setLong(1, id1);
//            statement.setLong(2, id2);
//            statement.setLong(3, id2);
//            statement.setLong(4, id1);
//
//            ResultSet resultSet = statement.executeQuery();
//
//            while (resultSet.next())
//            {
//                Long id= resultSet.getLong("id");
//                Long idFrom = resultSet.getLong("idFrom");
//                Long idTo = resultSet.getLong("idTo");
//                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
//                String mesaj = resultSet.getString("mesaj");
//
//                Message M = new Message(id,idFrom,new ArrayList<>(){{add(idTo);}},mesaj,date);
//               messages.add(M);
//
//            }
//            return messages;
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
