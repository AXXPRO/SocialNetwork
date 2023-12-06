package ro.ubbcluj.map.sem7.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.ubbcluj.map.sem7.domain.Message;
import ro.ubbcluj.map.sem7.domain.Utilizator;
import ro.ubbcluj.map.sem7.events.Event;
import ro.ubbcluj.map.sem7.events.EventType;
import ro.ubbcluj.map.sem7.events.LoginEvent;
import ro.ubbcluj.map.sem7.events.NewMessageEvent;
import ro.ubbcluj.map.sem7.observer.Observer;
import ro.ubbcluj.map.sem7.service.MasterService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserTextEveryoneController  implements Observer<Event> {


    public TextField toTextField;
    public TextArea chatBox;
    public Button sendButton;
    ObservableList<Utilizator> modelUserFriends = FXCollections.observableArrayList();
    MasterService service;
    Stage userSendMessagesStage;
    Utilizator utilizator;
    public ListView<Utilizator> friendsList;
    List<Utilizator> friends;
    ArrayList<Long> idsFriends = new ArrayList<>();
    @Override
    public void update(Event event) {

    }

    public void setMasterService(MasterService service, Stage dialogStage,Utilizator util) {
        this.service = service;
        userSendMessagesStage = dialogStage;
        utilizator = util;
        // loginViewController = login;

        service.addObserver(this);
        friends = service.findAllFriends(util.getId());

        initModel();
    }

    private void initModel() {
        //Called multiple times

        modelUserFriends.setAll(friends);


    }
    @FXML
    public void initialize(){
        //Called once
        friendsList.setItems(modelUserFriends);


    }


    public void handleAddFriendToText(MouseEvent event) {

       if(!friendsList.getSelectionModel().isEmpty()) {

           Utilizator utilizatorSelectat = friendsList.getSelectionModel().getSelectedItem();
          friends.remove(utilizatorSelectat);
          idsFriends.add(utilizatorSelectat.getId());
          toTextField.appendText(utilizatorSelectat.toString()+ " | ");
          initModel();
       }

    }

    public void handleSend(ActionEvent event) {
        String mesaj = chatBox.getText();
        StringBuilder IDSString= new StringBuilder();
        for (Long ID: idsFriends) {
            IDSString.append(Long.toString(ID)).append(" ");
        }
        IDSString.deleteCharAt(IDSString.length()-1);
        try {
            service.addMessage(new ArrayList<>() {{
                add(Long.toString(utilizator.getId()));
                add(IDSString.toString());
                add(mesaj);
                add(Long.toString(-1));
            }});
        } catch (Exception e) {
            throw new RuntimeException(e); //Not hapenning
        }


        service.emitChange(new NewMessageEvent(utilizator.getLastName()+": "+mesaj));
        chatBox.clear();
        userSendMessagesStage.close();
    }
}
