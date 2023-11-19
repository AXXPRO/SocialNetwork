package ro.ubbcluj.map.sem7.repository;

import ro.ubbcluj.map.sem7.domain.Utilizator;
import ro.ubbcluj.map.sem7.domain.validators.Validator;

import java.sql.*;
import java.util.*;

public class UserDBRepository implements Repository<Long, Utilizator> {

    private String url;
    private String username;
    private String password;

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
                Utilizator u = new Utilizator(firstName,lastName);
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
                Utilizator user=new Utilizator(firstName,lastName);
                user.setId(id);
                users.add(user);

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
                     " into users(first_name, last_name) VALUES (?,?)");
        ) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
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
                     " update  users set first_name = ?, last_name = ? where id = ?");
        ) {
            var found = this.findOne(entity.getId());
            if(found.isEmpty())
                return Optional.of(entity);
            statementUpdate.setString(1, entity.getFirstName());
            statementUpdate.setString(2, entity.getLastName());
            statementUpdate.setLong(3, entity.getId());
            statementUpdate.executeUpdate();
            //statement2.executeUpdate();

            return Optional.empty();

        } catch (SQLException e) {
            return Optional.of(entity);
        }

    }

    @Override
    public Optional<Utilizator> executeQuerry(String querry) {
        return Optional.empty();
    }

    @Override
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
                Utilizator user=new Utilizator(firstName,lastName);
                user.setId(id);
                users.add(user);

            }
            return users;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
