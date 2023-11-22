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
import ro.ubbcluj.map.sem7.gui.UserTableController;
import ro.ubbcluj.map.sem7.paging.*;
import ro.ubbcluj.map.sem7.repository.FriendshipDBRepository;
import ro.ubbcluj.map.sem7.repository.Repository;
import ro.ubbcluj.map.sem7.repository.UserDBPagingRepository;
import ro.ubbcluj.map.sem7.repository.UserDBRepository;
import ro.ubbcluj.map.sem7.service.MasterService;
import ro.ubbcluj.map.sem7.service.ServicePrietenie;
import ro.ubbcluj.map.sem7.service.ServiceUtilizator;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class HelloApplication extends Application {
    MasterService service;
    @Override
    public void start(Stage stage) throws IOException {

        String username = "postgres";

        PagingRepository<Long, Utilizator> UsersRepository = new UserDBPagingRepository("jdbc:postgresql://localhost:5432/socialnetwork", username, "***REMOVED***",new UtilizatorValidatorDetailed());

        Pageable pageable = new PageableImplementation(4,2);
        Page<Utilizator> utilizatorPage = UsersRepository.findAll(pageable);
       utilizatorPage.getContent().forEach(System.out::println);

        Repository<Tuple<Long, Long>, Prietenie> FriendshipRepository = new FriendshipDBRepository("jdbc:postgresql://localhost:5432/socialnetwork",username,"***REMOVED***") ;
        service = new MasterService(new ServiceUtilizator(UsersRepository), new ServicePrietenie(FriendshipRepository));

       Long idtest = UsersRepository.saveMessage("Answer to your message..");

       UsersRepository.saveMessageSent(21L,25L, LocalDateTime.now(),idtest);

        List<Message> list = UsersRepository.getMessages(21L,25L);
        list.forEach(System.out::println);


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