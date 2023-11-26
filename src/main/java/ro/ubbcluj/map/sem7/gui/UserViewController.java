package ro.ubbcluj.map.sem7.gui;

import javafx.application.Platform;
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

public class UserViewController implements Observer<Event> {

    public Pane contactsPane;
    public Button logoutButton;
    public Button requestsButton;
    public Button friendsButton;
    public ListView<Utilizator> friendsList;
    public Pane conversationPane;
    public TextArea chatBox;
    public Label friendUsername;
    public Button sendButton;
    public TextArea sendTextArea;
    public Button messagEveryoneButton;


    ObservableList<Utilizator> modelUserFriends = FXCollections.observableArrayList();

    MasterService service;
    Utilizator utilizator;
    Utilizator currentFriend;
    Stage userStage;
    public Label labelUserName;


    public UserViewController(){

    }

    @Override
    public void update(Event event) {
        if(event.getEventType() == EventType.NEWMESSAGE) {
            NewMessageEvent MSEvent = (NewMessageEvent) event;
            chatBox.appendText(MSEvent.getNewMessage()+"\n");
            chatBox.setScrollTop(Double.MAX_VALUE);
        }
    }

    public void setMasterService(MasterService service, Stage dialogStage,Utilizator util) {
        this.service = service;
        userStage = dialogStage;
        utilizator = util;
        // loginViewController = login;

        service.addObserver(this);

        initModel();
    }

    private void initModel() {
        //Called multiple times
        labelUserName.setText("Hello " + utilizator.getFirstName() + " " + utilizator.getLastName() + "!");
        modelUserFriends.setAll(service.findAllFriends(utilizator.getId()));





    }
    @FXML
    public void initialize(){
        //Called once
        friendsList.setItems(modelUserFriends);

        chatBox.clear();
        conversationPane.setVisible(false);






    }

    public void handelLogOut(ActionEvent event) {
        userStage.close();
        modelUserFriends.clear();
        service.emitChange(new LoginEvent());
    }

    public void handleConversationLoad(MouseEvent event) {

        if(!friendsList.getSelectionModel().isEmpty()) {
            conversationPane.setVisible(true);
            chatBox.clear();

            Utilizator friend = friendsList.getSelectionModel().getSelectedItem();
            currentFriend = friend;
            friendUsername.setText(friend.getFirstName() + " " + friend.getLastName());
            List<Message> mesaje = service.getMessages(utilizator.getId(), friend.getId());
            mesaje.forEach(mesaj -> {
                if (mesaj.getFromID().equals(utilizator.getId()))
                    chatBox.appendText(utilizator.getLastName() + ": " + mesaj.getMessage() + "\n");
                else {
                    chatBox.appendText(currentFriend.getLastName() + ": " + mesaj.getMessage() + "\n");
                }
            });

            chatBox.setScrollTop(Double.MAX_VALUE);

        }


    }

    public void handleSend(ActionEvent event) {
        String mesajDeTrimis = sendTextArea.getText();
        sendTextArea.clear();
        //From - to - mesaj
        try {
            service.addMessage(new ArrayList<>() {{
                add(Long.toString(utilizator.getId()));
                add(Long.toString(currentFriend.getId()));
                add(mesajDeTrimis);
            }});
        } catch (Exception e) {
            throw new RuntimeException(e); //Not hapenning
        }
        service.emitChange(new NewMessageEvent(utilizator.getLastName()+": "+mesajDeTrimis));
    }

    public void handleMessageEveryone(ActionEvent event) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("userTextEveryone-view.fxml"));


            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("UserText Window");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            UserTextEveryoneController viewController = loader.getController();
            viewController.setMasterService(service, dialogStage,utilizator);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
