package ro.ubbcluj.map.sem7.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ro.ubbcluj.map.sem7.domain.Prietenie;
import ro.ubbcluj.map.sem7.domain.Tuple;
import ro.ubbcluj.map.sem7.domain.Utilizator;
import ro.ubbcluj.map.sem7.domain.validators.UtilizatorValidatorDetailed;
import ro.ubbcluj.map.sem7.gui.UserTableController;
import ro.ubbcluj.map.sem7.repository.FriendshipDBRepository;
import ro.ubbcluj.map.sem7.repository.Repository;
import ro.ubbcluj.map.sem7.repository.UserDBRepository;
import ro.ubbcluj.map.sem7.service.MasterService;
import ro.ubbcluj.map.sem7.service.ServicePrietenie;
import ro.ubbcluj.map.sem7.service.ServiceUtilizator;

import java.io.IOException;

public class HelloApplication extends Application {
    MasterService service;
    @Override
    public void start(Stage stage) throws IOException {

        String username = "postgres";

        Repository<Long, Utilizator> UsersRepository = new UserDBRepository("jdbc:postgresql://localhost:5432/socialnetwork", username, "***REMOVED***",new UtilizatorValidatorDetailed());
        Repository<Tuple<Long, Long>, Prietenie> FriendshipRepository = new FriendshipDBRepository("jdbc:postgresql://localhost:5432/socialnetwork",username,"***REMOVED***") ;
        service = new MasterService(new ServiceUtilizator(UsersRepository), new ServicePrietenie(FriendshipRepository));



        initView(stage);
        stage.setWidth(800);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private void initView(Stage primaryStage) throws IOException {

        FXMLLoader messageLoader = new FXMLLoader();
        messageLoader.setLocation(getClass().getResource("user-view.fxml"));
        AnchorPane messageTaskLayout = messageLoader.load();
        primaryStage.setScene(new Scene(messageTaskLayout));

        UserTableController messageTaskController = messageLoader.getController();
        messageTaskController.setMasterService(service);

    }
}