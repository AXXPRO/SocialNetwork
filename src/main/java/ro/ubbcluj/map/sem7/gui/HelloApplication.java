package ro.ubbcluj.map.sem7.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ro.ubbcluj.map.sem7.domain.Message;
import ro.ubbcluj.map.sem7.domain.Prietenie;
import ro.ubbcluj.map.sem7.domain.Tuple;
import ro.ubbcluj.map.sem7.domain.Utilizator;
import ro.ubbcluj.map.sem7.domain.validators.UtilizatorValidatorDetailed;
import ro.ubbcluj.map.sem7.paging.*;
import ro.ubbcluj.map.sem7.repository.*;
import ro.ubbcluj.map.sem7.service.MasterService;
import ro.ubbcluj.map.sem7.service.ServiceMessage;
import ro.ubbcluj.map.sem7.service.ServicePrietenie;
import ro.ubbcluj.map.sem7.service.ServiceUtilizator;

import java.io.IOException;

public class HelloApplication extends Application {
    MasterService service;
    @Override
    public void start(Stage stage) throws IOException {

        String username = "postgres";

        UserDBPagingRepository UsersRepository = new UserDBPagingRepository("jdbc:postgresql://localhost:5432/socialnetwork", username, "***REMOVED***",new UtilizatorValidatorDetailed());

        Pageable pageable = new PageableImplementation(4,2);
        Page<Utilizator> utilizatorPage = UsersRepository.findAll(pageable);
      // utilizatorPage.getContent().forEach(System.out::println);

        Repository<Tuple<Long, Long>, Prietenie> FriendshipDBRepository = new FriendshipDBRepository("jdbc:postgresql://localhost:5432/socialnetwork",username,"***REMOVED***") ;
        Repository<Long, Message> MessageDBRepository = new MessageDBPagingRepository("jdbc:postgresql://localhost:5432/socialnetwork",username,"***REMOVED***") ;
        service = new MasterService(new ServiceUtilizator(UsersRepository), new ServicePrietenie((FriendshipDBRepository) FriendshipDBRepository), new ServiceMessage((MessageDBPagingRepository)  MessageDBRepository  ));

//        try {
//            service.addMessage(new ArrayList<>(){{
//                add("21");
//                add("2");
//                add("Ba iesti!?");
//            }});
//
//            service.addMessage(new ArrayList<>(){{
//                add("2");
//                add("21");
//                add("IESTI nesimtita");
//            }});
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        service.getMessages(2L,21L).forEach(System.out::println);



//        try {
//            service.addMessage(new ArrayList<String>(){{
//                add("2");
//                add("11 13");
//                add("asta e un mesaj smech");
//            }});
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//       Long idtest = UsersRepository.saveMessage("Answer to your message..");
//
//       UsersRepository.saveMessageSent(21L,25L, LocalDateTime.now(),idtest);
//
//        List<Message> list = UsersRepository.getMessages(21L,25L);
//        list.forEach(System.out::println);


        initView(stage);
//        stage.setWidth(800);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private void initView(Stage primaryStage) throws IOException {

        FXMLLoader messageLoader = new FXMLLoader();
        messageLoader.setLocation(getClass().getResource("login-view.fxml"));
        AnchorPane messageTaskLayout = messageLoader.load();
        primaryStage.setScene(new Scene(messageTaskLayout));

        LoginViewController messageTaskController = messageLoader.getController();
        messageTaskController.setMasterService(service, primaryStage);

    }
}