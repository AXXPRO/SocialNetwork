package ro.ubbcluj.map.sem7.repository;



import ro.ubbcluj.map.sem7.domain.Message;
import ro.ubbcluj.map.sem7.domain.Utilizator;
import ro.ubbcluj.map.sem7.domain.validators.Validator;
import ro.ubbcluj.map.sem7.paging.Page;
import ro.ubbcluj.map.sem7.paging.PageImplementation;
import ro.ubbcluj.map.sem7.paging.Pageable;
import ro.ubbcluj.map.sem7.paging.PagingRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class UserDBPagingRepository extends UserDBRepository  implements PagingRepository<Long, Utilizator> {


    public UserDBPagingRepository(String url, String username, String password, Validator<Utilizator> val) {
        super(url, username, password, val);
    }


    @Override
    public Page<Utilizator> findAll(Pageable pageable) {
//        Stream<Utilizator> result = StreamSupport.stream(this.findAll().spliterator(), false)
//                .skip(pageable.getPageNumber()  * pageable.getPageSize())
//                .limit(pageable.getPageSize());
//        return new PageImplementation<>(pageable, result);
        ArrayList<Utilizator> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);


        ) {
            PreparedStatement statement = connection.prepareStatement("select * from users order by first_name limit ? offset ?");
            statement.setInt(1, pageable.getPageSize());
            statement.setInt(2, pageable.getPageNumber() * pageable.getPageSize());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Utilizator user = new Utilizator(firstName, lastName);
                user.setId(id);
                users.add(user);

            }
            return new PageImplementation<>(pageable, users.stream());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }



    public Page<Utilizator> findAllFiltered(Pageable pageable, String filtru) {

        ArrayList<Utilizator> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);

             PreparedStatement statement = connection.prepareStatement("select * from users where  CONCAT(first_name, ' ', last_name) LIKE ? order by first_name limit ? offset ?");

        ) {

            statement.setString(1, filtru + "%");
            statement.setInt(2, pageable.getPageSize());
            statement.setInt(3, pageable.getPageNumber() * pageable.getPageSize());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String mail = resultSet.getString("mail");
                String password = resultSet.getString("password");
                Utilizator u = new Utilizator(firstName, lastName, mail, password, -1L);
                u.setId(id);
                users.add(u);

            }
            return new PageImplementation<>(pageable, users.stream());


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
