package ro.ubbcluj.map.sem7.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.ubbcluj.map.sem7.domain.Utilizator;
import ro.ubbcluj.map.sem7.events.*;
import ro.ubbcluj.map.sem7.observer.Observer;
import ro.ubbcluj.map.sem7.paging.Page;
import ro.ubbcluj.map.sem7.paging.PageImplementation;
import ro.ubbcluj.map.sem7.paging.PageableImplementation;
import ro.ubbcluj.map.sem7.service.MasterService;

import java.io.IOException;
import java.util.List;

public class AdminController implements Observer<Event> {
    public Button logInButton;
    public Label errorLabel;
    public PasswordField textFieldPassword;
    public TextField textFieldMail;
    public SplitPane adminPane;

    public Label LoginLabel;
    public Button logoutButton;
    public Button rightArrowButton;
    public Button leftArrowButton;
    public TextField elementsPerPageArea;
    String numePrenumeFilter ="";
    public TextField searchField;
    MasterService service;
    ObservableList<Utilizator> model = FXCollections.observableArrayList();
    Stage adminStage;
 //   LoginViewController loginViewController;

    @FXML
    TableView<Utilizator> tableView;


    @FXML
    TableColumn<Utilizator,String> tableColumnNume;
    @FXML
    TableColumn<Utilizator,String> tableColumnPreunume;
    @FXML
    TableColumn<Utilizator,String > tableColumnMail;

    private final int pageNumber=0;
    private int pageSize= 5;

    Page<Utilizator> pageImplementation = new PageImplementation<Utilizator>( new PageableImplementation(pageNumber, pageSize) ,null);




    public void setMasterService(MasterService servicePrimit, Stage stagePrimit) {
        service = servicePrimit;
        adminStage = stagePrimit;
       // loginViewController = login;

        service.addObserver(this);

        initModel();
    }

    @FXML
    public void initialize() {
        //tableColumnUserID.setCellValueFactory(new PropertyValueFactory<Utilizator, Long>("id"));
        tableColumnNume.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("firstName"));
        tableColumnPreunume.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("lastName"));
        tableColumnMail.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("mail"));
        tableView.setItems(model);


    }

    private void initModel() {
        this.pageImplementation = service.findAllUsersFiltered(numePrenumeFilter, this.pageImplementation.getPageable());
        List<Utilizator> users = this.pageImplementation.getContent().toList();



        if(users.isEmpty())
        {
            this.pageImplementation = service.findAllUsersFiltered(numePrenumeFilter, this.pageImplementation.previousPageable());
            users = this.pageImplementation.getContent().toList();
        }



        model.setAll(users);

//        loginPane.setVisible(true);
    }



    public void handleSearchMessage(KeyEvent actionEvent){
      //  tableView.setVisible(!tableView.isVisible());
        numePrenumeFilter = searchField.getText();
        update(new UserChangeEvent(UserChanges.FILTER));


    }
    public void handleDeleteMessage(ActionEvent actionEvent) {
        Utilizator selected = (Utilizator) tableView.getSelectionModel().getSelectedItem();
        try {
            service.deleteUser(selected.getId());
        } catch (Exception e) {
            System.out.println("Nu exista frt");
        }



//        if (selected != null) {
//            MessageTask deleted = service.deleteMessageTask(selected);
//            if (null != deleted)
//                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Delete", "Studentul a fost sters cu succes!");
//        } else MessageAlert.showErrorMessage(null, "Nu ati selectat nici un student!");
    }

//    @Override






    public void showMessageTaskEditDialog() {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("userAddWindow-view.fxml"));


            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Update Users");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            AdminAddWindowController adminAddWindowController = loader.getController();
            adminAddWindowController.setService(service, dialogStage);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void update(Event userChangeEvent) {
        initModel();
    }

    public void handleLogOut(ActionEvent event) {
        adminStage.close();
        model.clear();
        service.emitChange(new LoginEvent());
      //  loginViewController.initModel();

    }

    public void handleLeftArrow(ActionEvent actionEvent) {
        if(this.pageImplementation.getPageable().getPageNumber() == 0)
            return;
        this.pageImplementation = new PageImplementation<Utilizator>(this.pageImplementation.previousPageable(), null);

        initModel();
    }

    public void handleRightArrow(ActionEvent actionEvent) {


        this.pageImplementation = new PageImplementation<Utilizator>(this.pageImplementation.nextPageable(), null);
        initModel();
    }


    public void handlePageNumberChange(KeyEvent actionEvent) {
        String nrPagesString = elementsPerPageArea.getText();
        int nrPages;
        try {
            nrPages = Integer.parseInt(nrPagesString);


            pageSize = nrPages;
        }
        catch (Exception E)
        {
            pageSize = 5;
        }

        this.pageImplementation = new PageImplementation<Utilizator>( new PageableImplementation(pageNumber, pageSize),null);
        initModel();

    }
}