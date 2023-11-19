package ro.ubbcluj.map.sem7.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.ubbcluj.map.sem7.domain.Utilizator;
import ro.ubbcluj.map.sem7.domain.exceptions.UtilizatorExceptions;
import ro.ubbcluj.map.sem7.events.UserChangeEvent;
import ro.ubbcluj.map.sem7.events.UserChanges;
import ro.ubbcluj.map.sem7.observer.Observer;
import ro.ubbcluj.map.sem7.service.MasterService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserTableController implements Observer<UserChangeEvent> {
    String numePrenumeFilter ="";
    public TextField searchField;
    MasterService service;
    ObservableList<Utilizator> model = FXCollections.observableArrayList();


    @FXML
    TableView<Utilizator> tableView;

    @FXML
    TableColumn<Utilizator, Long> tableColumnUserID;
    @FXML
    TableColumn<Utilizator,String> tableColumnNume;
    @FXML
    TableColumn<Utilizator,String> tableColumnPreunume;


    public void setMasterService(MasterService servicePrimit) {
        service = servicePrimit;
        service.addObserver(this);

        initModel();
    }

    @FXML
    public void initialize() {
        //tableColumnUserID.setCellValueFactory(new PropertyValueFactory<Utilizator, Long>("id"));
        tableColumnNume.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("firstName"));
        tableColumnPreunume.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("lastName"));

        tableView.setItems(model);
    }

    private void initModel() {
        List<Utilizator> usersFiltered = service.findAllUsersFiltered(numePrenumeFilter);

        model.setAll(usersFiltered);
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

            UserAddWindowController userAddWindowController = loader.getController();
            userAddWindowController.setService(service, dialogStage);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void update(UserChangeEvent userChangeEvent) {
        initModel();
    }
}