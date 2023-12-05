package ro.ubbcluj.map.sem7.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.ubbcluj.map.sem7.domain.Message;
import ro.ubbcluj.map.sem7.domain.Utilizator;
import ro.ubbcluj.map.sem7.domain.exceptions.UtilizatorExceptions;
import ro.ubbcluj.map.sem7.events.*;
import ro.ubbcluj.map.sem7.observer.Observer;
import ro.ubbcluj.map.sem7.service.MasterService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserViewController implements Observer<Event> {

    public Pane contactsPane;
    public Pane requestsPane;
    public Button logoutButton;
    public Button requestsButton;
    public Button friendsButton;
    public ListView<Utilizator> friendsList;
    public Pane conversationPane;
 //   public TextArea chatBox;
    public Label friendUsername;
    public Button sendButton;
    public TextArea sendTextArea;
    public Button messagEveryoneButton;
    public GridPane gridPane;
    public Label alreadyFriendsLabel;
    public Button sendRequestButton;
    public TextField searchRequestBar;
    public ComboBox<Utilizator> friendRequestComboBox;
    public ScrollPane scrollPane;
    public VBox scrollVBox = new VBox();
    public AnchorPane scrollAnchorPane;
    ArrayList<HBox> listaBox = new ArrayList<>();

    ObservableList<Utilizator> modelUserRequests = FXCollections.observableArrayList();
    ObservableList<Utilizator> modelUserFriends = FXCollections.observableArrayList();

    MasterService service;
    Utilizator utilizator;
    Utilizator currentFriend;
    Stage userStage;
    public Label labelUserName;
    private String numePrenumeFiltru="";


    public UserViewController(){

    }

    @Override
    public void update(Event event) {
        if(event.getEventType() == EventType.NEWMESSAGE) {
    //        NewMessageEvent MSEvent = (NewMessageEvent) event;

            handleConversationLoad(null);
    //        chatBox.appendText(MSEvent.getNewMessage()+"\n");
     //       chatBox.setScrollTop(Double.MAX_VALUE);
        }
        if(event.getEventType() == EventType.REQUESTPROCESSED)
        {
            loadFriendshipRequests();
            initModel();
        }
        if( event.getEventType() == EventType.FRIENDSHIP)
        {
            initModel();
        }

    }

    public void setMasterService(MasterService service, Stage dialogStage,Utilizator util) {
        this.service = service;
        userStage = dialogStage;
        utilizator = util;
        // loginViewController = login;

        service.addObserver(this);
        loadFriendshipRequests();
        initModel();



    }
    private void loadFriendshipRequests(){
        listaBox.clear();
        gridPane.getChildren().clear();

        var frens = service.findAllFriendRequests(utilizator.getId());
        frens.forEach(fren -> {
                    var box = (new HBox());
                    box.setSpacing(5);


                    box.getChildren().add(new Label(fren.getFirstName() + " " + fren.getLastName()));
                    Button buttonAccept = new Button("√");
                    Button buttonDecline = new Button("X");


                    buttonAccept.setOnAction(this::handleAcceptRequest);
                    buttonAccept.setUserData(fren.getId());
                    buttonDecline.setOnAction(this::handleDeclineRequest);
                    buttonDecline.setUserData(fren.getId());
                    box.getChildren().add(buttonAccept);
                    box.getChildren().add(buttonDecline);

                    box.setAlignment(Pos.CENTER_RIGHT);

                    listaBox.add(box);
                    gridPane.addRow(listaBox.indexOf(box), box);

                }
        );
    }

    private void initModel() {
        //Called multiple times
        labelUserName.setText("Hello " + utilizator.getFirstName() + " " + utilizator.getLastName() + "!");
        modelUserFriends.setAll(service.findAllFriends(utilizator.getId()));
        modelUserRequests.setAll(service.findAllUsersFiltered(numePrenumeFiltru));

    }
    @FXML
    public void initialize(){
        //Called once
        friendsList.setItems(modelUserFriends);
        friendRequestComboBox.setItems(modelUserRequests);

     //   chatBox.clear();
        contactsPane.setVisible(true);
        requestsPane.setVisible(false);
        conversationPane.setVisible(false);
        scrollVBox.setSpacing(5);
        scrollPane.setContent(scrollVBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);



    }

    public void handelLogOut(ActionEvent event) {
        userStage.close();
        modelUserFriends.clear();
        service.emitChange(new LoginEvent());
    }

    public void handleAcceptRequest(ActionEvent event){
          Button trigger = (Button)event.getSource();
          Long id = (Long) trigger.getUserData();
          service.acceptRequest(id,utilizator.getId());
          service.emitChange(new RequestProcessedEvent());

    }

    public void handleDeclineRequest(ActionEvent event) {
         Button trigger = (Button)event.getSource();
         Long id = (Long) trigger.getUserData();
        service.denyRequest(id,utilizator.getId());
        service.emitChange(new RequestProcessedEvent());

    }
    public void handleConversationLoad(MouseEvent event) {



        if(!friendsList.getSelectionModel().isEmpty()) {
            conversationPane.setVisible(true);
         //   chatBox.clear();
            scrollVBox.getChildren().clear();

            Utilizator friend = friendsList.getSelectionModel().getSelectedItem();
            currentFriend = friend;
            friendUsername.setText(friend.getFirstName() + " " + friend.getLastName());
            List<Message> mesaje = service.getMessages(utilizator.getId(), friend.getId());

            mesaje.forEach(mesaj -> {
                HBox HBoxCurrent = new HBox();
                Region spacer= new Region();
                spacer.setMinWidth(scrollPane.getWidth()/2);
                if (mesaj.getFromID().equals(utilizator.getId()))
                {//    chatBox.appendText(utilizator.getLastName() + ": " + mesaj.getMessage() + "\n");
                    HBoxCurrent.setPrefWidth(scrollPane.getWidth()-20);
                    Label currentLabel = new Label( mesaj.getMessage() );

                    currentLabel.setStyle(" -fx-font-size: 20px; -fx-background-color: #d27514;  -fx-text-fill: #000000;   -fx-background-radius: 10;");
                    currentLabel.setWrapText(true);
                    HBoxCurrent.getChildren().addAll( spacer, currentLabel);
                    HBoxCurrent.setAlignment(Pos.CENTER_RIGHT);


                    scrollVBox.getChildren().add(HBoxCurrent);
                }
                else {
                    //chatBox.appendText(currentFriend.getLastName() + ": " + mesaj.getMessage() + "\n");
                    HBoxCurrent.setPrefWidth(scrollPane.getWidth()-20);
                    Label currentLabel = new Label( mesaj.getMessage());
                    currentLabel.setStyle(" -fx-font-size: 20px; -fx-background-color: #eada6d;  -fx-text-fill: #000000;   -fx-background-radius: 10;");
                    currentLabel.setWrapText(true);
                    HBoxCurrent.getChildren().addAll(currentLabel, spacer);
                    scrollVBox.getChildren().add(HBoxCurrent);
                }
            });

          //  chatBox.setScrollTop(Double.MAX_VALUE);

        }

//
//        Label testLabel = new Label("HEEEY");
//        Label testLabel2 = new Label("HEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEY2");
//        testLabel2.setWrapText(true);
//        testLabel.setFont(new Font(20));
//        //HBox hboxtest = new HBox();
//        hboxtest.setPrefWidth(scrollPane.getWidth());
//        scrollVBox.getChildren().add(hboxtest);
//
//        hboxtest.getChildren().addAll(  testLabel2,spacer1);
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

    public void handleRequestsButton(ActionEvent event) {
        contactsPane.setVisible(false);
        requestsPane.setVisible(true);


    }

    public void handleFriendsButton(ActionEvent event) {

      //  Button trigger = (Button)event.getSource();

       // System.out.println(trigger.getUserData());
        contactsPane.setVisible(true);
        requestsPane.setVisible(false);
    }

    public void handleNameFilter(KeyEvent event) {
        numePrenumeFiltru = searchRequestBar.getText();
        modelUserRequests.setAll(service.findAllUsersFiltered(numePrenumeFiltru));
    }

    public void handleSendRequest(ActionEvent event) {
        Utilizator util = friendRequestComboBox.getValue();
        if(util != null){
            try {
                service.addRequest(new ArrayList<>(){{
                    add(utilizator.getId().toString());
                    add(util.getId().toString());
                }});
                friendRequestComboBox.getSelectionModel().clearSelection();
            } catch (UtilizatorExceptions e) {
                throw new RuntimeException(e);
            }

        }

    }

    public void handleSelectedUser(ActionEvent event) {
        Utilizator util = friendRequestComboBox.getValue();
        if(util != null){
            if(service.findFriendship(utilizator.getId(), util.getId()) != null || service.findFriendship(util.getId(), utilizator.getId()) != null
            || utilizator.getId().equals(util.getId()))
            {
                alreadyFriendsLabel.setVisible(true);
                sendRequestButton.setDisable(true);
            }
            else {
                alreadyFriendsLabel.setVisible(false);
                sendRequestButton.setDisable(false);
            }
        }

    }
}
