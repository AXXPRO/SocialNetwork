package ro.ubbcluj.map.sem7.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.ubbcluj.map.sem7.domain.Message;
import ro.ubbcluj.map.sem7.domain.Utilizator;
import ro.ubbcluj.map.sem7.domain.exceptions.UtilizatorExceptions;
import ro.ubbcluj.map.sem7.events.*;
import ro.ubbcluj.map.sem7.observer.Observer;
import ro.ubbcluj.map.sem7.paging.Page;
import ro.ubbcluj.map.sem7.paging.PageImplementation;
import ro.ubbcluj.map.sem7.paging.PageableImplementation;
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
    public AnchorPane AnchorPaneRoot;
    public ScrollPane friendshipScrollPane;
    public TextField requestsNumberTextArea;
    public TextField friendshipNumberTextArea;
    ArrayList<HBox> listaBox = new ArrayList<>();

    ObservableList<Utilizator> modelUserRequests = FXCollections.observableArrayList();
    ObservableList<Utilizator> modelUserFriends = FXCollections.observableArrayList();

    MasterService service;
    Utilizator utilizator;
    Utilizator currentFriend;
    Stage userStage;
    public Label labelUserName;
    private String numePrenumeFiltru="";
    private int numberOfMessages=5;

    private final int pageNumberRequests =0;
    private int pageSizeRequests = 5;

    Page<Utilizator> requestsPageImplementation = new PageImplementation<Utilizator>( new PageableImplementation(pageNumberRequests, pageSizeRequests) ,null);

    private final int pageNumberFriendships =0;
    private int pageSizeFriendShips = 5;

    Page<Utilizator> friendshipPageImplementation = new PageImplementation<Utilizator>( new PageableImplementation(pageNumberFriendships, pageSizeFriendShips) ,null);


    boolean scrolledAlready = false;


    public UserViewController(){

    }

    @Override
    public void update(Event event) {
        if(event.getEventType() == EventType.NEWMESSAGE) {
            //        NewMessageEvent MSEvent = (NewMessageEvent) event;

            handleConversationLoad(null);
            Platform.runLater(() -> scrollPane.setVvalue(1.0));

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

        friendshipScrollPane.setContent(gridPane);

       List<Utilizator> frens;
        this.requestsPageImplementation = service.findAllFriendRequests(utilizator.getId(), requestsPageImplementation.getPageable());
        frens = this.requestsPageImplementation.getContent().toList();



        if(frens.isEmpty())
        {
            this.requestsPageImplementation =service.findAllFriendRequests(utilizator.getId(), requestsPageImplementation.previousPageable());
            frens = this.requestsPageImplementation.getContent().toList();
        }
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
       Platform.runLater(()  -> friendshipScrollPane.setHvalue(1.0D));
    }

    private void initModel() {
        //Called multiple times
        labelUserName.setText("Hello " + utilizator.getFirstName() + " " + utilizator.getLastName() + "!");

        //this.page = service.findAllFriends(utilizator.getId(),this.requestsPageImplementation.getPageable()).getContent().toList();


        this.friendshipPageImplementation = service.findAllFriends(utilizator.getId(),this.friendshipPageImplementation.getPageable());

        List<Utilizator> users = this.friendshipPageImplementation.getContent().toList();



        if(users.isEmpty())
        {
            this.friendshipPageImplementation = service.findAllFriends(utilizator.getId(),this.friendshipPageImplementation.previousPageable());
            users = this.friendshipPageImplementation.getContent().toList();
        }



        modelUserFriends.setAll(users);


    //    modelUserFriends.setAll(service.findAllFriends(utilizator.getId(),this.friendshipPageImplementation.getPageable()).getContent().toList());


        modelUserRequests.setAll(service.findAllUsersFiltered(numePrenumeFiltru));

        AnchorPaneRoot.setStyle("-fx-background: #757a7a");
        //scrollVBox.setStyle("-fx-background-color: transparent");
        // scrollPane.setStyle("-fx-background-image: url(starry.jpg)");
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
//        scrollVBox.heightProperty().addListener(listener -> {
//            scrollPane.setVmax(scrollVBox.heightProperty().doubleValue());
//        });
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        scrollPane.setOnScroll( (ScrollEvent event) -> {



            if(event.getDeltaY() >  0 && !scrolledAlready)
            {
                scrolledAlready = true;


                Double currentScrollPos = scrollPane.getVmax();
                this.numberOfMessages += 5;



               double contentLength = this.scrollVBox.getBoundsInLocal().getHeight();
                handleConversationLoad(null);
//
                Platform.runLater(() ->{
                   double newContentLength = this.scrollVBox.getBoundsInLocal().getHeight();




                   scrollPane.setVvalue(1 -contentLength / newContentLength );
                    scrolledAlready = false;
                });

            }

        });


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

        if(event != null) //Triggered not by me
        {this.numberOfMessages = 5;}

        if(!friendsList.getSelectionModel().isEmpty()) {
            conversationPane.setVisible(true);
            //   chatBox.clear();


            scrollVBox.getChildren().clear();



            Utilizator friend = friendsList.getSelectionModel().getSelectedItem();
            currentFriend = friend;
            friendUsername.setText(friend.getFirstName() + " " + friend.getLastName());
            List<Message> mesaje = service.getMessages(utilizator.getId(), friend.getId(),new PageableImplementation(0, this.numberOfMessages)).getContent().toList();
            currentReplyLabel=null;
            defaultLabelStyle="";
            currentMessageID = -1L;


            mesaje.forEach(mesaj -> {
                HBox HBoxCurrent = new HBox();
                Region spacer= new Region();
                spacer.setMinWidth(scrollPane.getWidth()/2);
                if (mesaj.getFromID().equals(utilizator.getId()))
                {//    chatBox.appendText(utilizator.getLastName() + ": " + mesaj.getMessage() + "\n");
                    HBoxCurrent.setPrefWidth(scrollPane.getWidth()-20);
                    HBoxCurrent.setStyle("-fx-background-color: transparent");
                    Label currentLabel = new Label( mesaj.getMessage() );

                    currentLabel.setStyle(" -fx-font-size: 20px; -fx-padding: 6px; -fx-background-color: #085c4c;  -fx-text-fill: #ffffff;   -fx-background-radius: 10;");

                    currentLabel.setOnMousePressed((MouseEvent M) -> {
                        if(M.getButton().equals(MouseButton.SECONDARY))
                        {
                            handleReplySelected(currentLabel, mesaj);
                            // currentLabel.setStyle("-fx-font-size: 20px; -fx-background-color: #052d25;  -fx-text-fill: #ffffff;   -fx-background-radius: 10;");
                        }

                    });

                    currentLabel.setWrapText(true);
                    if(mesaj.getReply()!=null)
                    { Label currentReplyLabel = new Label( mesaj.getReply().getMessage() );
                        currentReplyLabel.setStyle(" -fx-font-size: 20px; -fx-background-color: transparent;  -fx-text-fill: #ffffff;   -fx-background-radius: 10;");
                        HBox ReplyHbox = new HBox();
                        ReplyHbox.setPrefWidth(HBoxCurrent.getPrefWidth());
                        currentReplyLabel.setWrapText(true);


                        ReplyHbox.setAlignment(Pos.CENTER_RIGHT);
                        // System.out.println(mesaj.getReply().getMessage());
                        Region spacer2= new Region();
                        spacer2.setMinWidth(scrollPane.getWidth()/2);
                        ReplyHbox.getChildren().addAll(spacer2, currentReplyLabel);
                        scrollVBox.getChildren().add(ReplyHbox);
                    }

                    HBoxCurrent.getChildren().addAll( spacer, currentLabel);
                    HBoxCurrent.setAlignment(Pos.CENTER_RIGHT);


                    scrollVBox.getChildren().add(HBoxCurrent);
                }
                else {
                    //chatBox.appendText(currentFriend.getLastName() + ": " + mesaj.getMessage() + "\n");
                    HBoxCurrent.setPrefWidth(scrollPane.getWidth()-20);
                    Label currentLabel = new Label( mesaj.getMessage());
                    currentLabel.setStyle(" -fx-font-size: 20px; -fx-padding: 6px; -fx-background-color: #342f2f;  -fx-text-fill: #ffffff;   -fx-background-radius: 10;");
                    currentLabel.setOnMousePressed((MouseEvent M) -> {
                        if(M.getButton().equals(MouseButton.SECONDARY))
                        {
                            handleReplySelected(currentLabel, mesaj);
                            //currentLabel.setStyle(" -fx-font-size: 20px; -fx-background-color: #2c0909;  -fx-text-fill: #ffffff;   -fx-background-radius: 10;");
                        }

                    });

                    currentLabel.setWrapText(true);
                    HBoxCurrent.setStyle("-fx-background-color: transparent");
                    if(mesaj.getReply()!=null)
                    {
                        Label currentReplyLabel = new Label( mesaj.getReply().getMessage() );
                        currentReplyLabel.setStyle(" -fx-font-size: 20px; -fx-background-color: transparent;  -fx-text-fill: #ffffff;   -fx-background-radius: 10;");
                        HBox ReplyHbox = new HBox();
                        ReplyHbox.setPrefWidth(HBoxCurrent.getPrefWidth());
                        currentReplyLabel.setWrapText(true);


                        // System.out.println(mesaj.getReply().getMessage());
                        Region spacer2= new Region();
                        spacer2.setMinWidth(scrollPane.getWidth()/2);
                        ReplyHbox.getChildren().addAll(currentReplyLabel, spacer2);
                        scrollVBox.getChildren().add(ReplyHbox);
                    }
                    HBoxCurrent.getChildren().addAll(currentLabel, spacer);

                    scrollVBox.getChildren().add(HBoxCurrent);
                }
            });

            // scrollPane.setVvalue(1.0);
            //  chatBox.setScrollTop(Double.MAX_VALUE);

        }

        else {

            conversationPane.setVisible(false);
        }


        if(event != null)
            Platform.runLater(() ->scrollPane.setVvalue(Double.MAX_VALUE)); ;
    }


    Label currentReplyLabel=null;
    String defaultLabelStyle="";
    Long currentMessageID=-1L;
    private void handleReplySelected(Label currentLabel, Message mesaj) {
        if(currentReplyLabel != null)
        {
            currentReplyLabel.setStyle(defaultLabelStyle);
        }

        if(currentLabel.equals(currentReplyLabel))
        {
            currentReplyLabel=null;
            defaultLabelStyle="";
            currentMessageID=-1L;
            return;
        }
        currentReplyLabel = currentLabel;
        defaultLabelStyle = currentLabel.getStyle();
        currentMessageID = mesaj.getId();

        currentReplyLabel.setStyle(" -fx-font-size: 20px; -fx-padding: 6px; -fx-background-color: #5c0855;  -fx-text-fill: #ffffff;   -fx-background-radius: 10;");
        System.out.println(currentMessageID);
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
                add(Long.toString(currentMessageID));
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
    @FunctionalInterface
    interface Function {
        void apply();
    }


    public void handleRequestsNumberTextArea(KeyEvent keyEvent) {
        String nrPagesString = requestsNumberTextArea.getText();
        int nrPages;
        try {
            nrPages = Integer.parseInt(nrPagesString);
            if(nrPages < 0)
                throw  new Exception("No negatives");

            pageSizeRequests = nrPages;
        }
        catch (Exception E)
        {
            if(pageSizeRequests == 5)
                return;
            pageSizeRequests = 5;

        }

        this.requestsPageImplementation = new PageImplementation<Utilizator>( new PageableImplementation(pageNumberRequests, pageSizeRequests),null);


       loadFriendshipRequests();

    }
    public void handleRequestsLeftArrow(ActionEvent actionEvent) {
        if(this.requestsPageImplementation.getPageable().getPageNumber() == 0)
            return;
        this.requestsPageImplementation = new PageImplementation<Utilizator>(this.requestsPageImplementation.previousPageable(), null);

        loadFriendshipRequests();
    }

    public void handleRequestsRightArrow(ActionEvent actionEvent) {


        this.requestsPageImplementation = new PageImplementation<Utilizator>(this.requestsPageImplementation.nextPageable(), null);
        loadFriendshipRequests();
    }

    public void handleFriendsLeftArrow(ActionEvent actionEvent) {
        if(this.friendshipPageImplementation.getPageable().getPageNumber() == 0)
            return;
        this.friendshipPageImplementation = new PageImplementation<Utilizator>(this.friendshipPageImplementation.previousPageable(), null);

        initModel();
        handleConversationLoad(null);
    }

    public void handleFriendsRightArrow(ActionEvent actionEvent) {


        this.friendshipPageImplementation = new PageImplementation<Utilizator>(this.friendshipPageImplementation.nextPageable(), null);
        initModel();
        handleConversationLoad(null);
    }

    public void handleFriendsNumberTextArea(KeyEvent keyEvent) {

        String nrPagesString = friendshipNumberTextArea.getText();
        int nrPages;
        try {
            nrPages = Integer.parseInt(nrPagesString);
            if(nrPages < 0)
                throw  new Exception("No negatives");

            pageSizeFriendShips = nrPages;
        }
        catch (Exception E)
        {
            if(pageSizeFriendShips == 5)
                return;
            pageSizeFriendShips = 5;

        }

        this.friendshipPageImplementation = new PageImplementation<Utilizator>( new PageableImplementation(pageNumberFriendships, pageSizeFriendShips),null);

        initModel();

        handleConversationLoad(null);

    }
}
