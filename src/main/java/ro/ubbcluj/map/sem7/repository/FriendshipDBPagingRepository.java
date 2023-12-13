package ro.ubbcluj.map.sem7.repository;

import ro.ubbcluj.map.sem7.domain.Prietenie;
import ro.ubbcluj.map.sem7.domain.Tuple;
import ro.ubbcluj.map.sem7.paging.Page;
import ro.ubbcluj.map.sem7.paging.PageImplementation;
import ro.ubbcluj.map.sem7.paging.Pageable;
import ro.ubbcluj.map.sem7.paging.PagingRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FriendshipDBPagingRepository extends FriendshipDBRepository implements PagingRepository<Tuple<Long, Long>, Prietenie> {
    public FriendshipDBPagingRepository(String url, String username, String password) {
        super(url, username, password);
    }

    @Override
    public Page<Prietenie> findAll(Pageable pageable) {
       return null;
       }


       public Page<Prietenie>  findAllFriends(Long id, Pageable pageable) {
           ArrayList<Prietenie> users = new ArrayList<>();

           try (Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement("select * from friendships where id1 = ? and status = 'accepted' LIMIT ? OFFSET ? ");

           ) {
               statement.setLong(1, id);
               statement.setInt(2, pageable.getPageSize());
               statement.setInt(3, pageable.getPageSize() * pageable.getPageNumber());
               ResultSet resultSet = statement.executeQuery();
               while (resultSet.next())
               {
                   Long id1 = resultSet.getLong("id1");
                   Long id2 = resultSet.getLong("id2");
                   LocalDateTime date =  resultSet.getDate("friendsfrom").toLocalDate().atStartOfDay();
                   Prietenie prietenie=new Prietenie(date,id1,id2);

                   prietenie.setId(new Tuple<Long,Long>(id1,id2));
                   users.add(prietenie);

               }
               return new PageImplementation<>(pageable, users.stream());

           } catch (SQLException e) {
               throw new RuntimeException(e);
           }

       }


      public Page<Prietenie> getPendingFriends(Long id, Pageable pageable) {
           ArrayList<Prietenie> users = new ArrayList<>();

           try (Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement("select * from friendships where id2 = ? and status = 'pending' LIMIT ? OFFSET ?");

           ) {
               statement.setLong(1, id);
               statement.setInt(2, pageable.getPageSize());
               statement.setInt(3, pageable.getPageSize() * pageable.getPageNumber());
               ResultSet resultSet = statement.executeQuery();
               while (resultSet.next()) {
                   Long id1 = resultSet.getLong("id1");
                   Long id2 = resultSet.getLong("id2");
                   LocalDateTime date = resultSet.getDate("friendsfrom").toLocalDate().atStartOfDay();
                   Prietenie prietenie = new Prietenie(date, id1, id2);

                   prietenie.setId(new Tuple<Long, Long>(id1, id2));
                   users.add(prietenie);

               }
               return new PageImplementation<>(pageable, users.stream());

           } catch (SQLException e) {
               throw new RuntimeException(e);
           }
       }
}
