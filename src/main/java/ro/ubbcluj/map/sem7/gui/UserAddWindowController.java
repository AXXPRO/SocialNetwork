package ro.ubbcluj.map.sem7.gui;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import ro.ubbcluj.map.sem7.domain.Utilizator;
import ro.ubbcluj.map.sem7.domain.exceptions.UtilizatorExceptions;
import ro.ubbcluj.map.sem7.events.Event;
import ro.ubbcluj.map.sem7.events.UserChangeEvent;
import ro.ubbcluj.map.sem7.events.UserChanges;
import ro.ubbcluj.map.sem7.observer.Observer;
import ro.ubbcluj.map.sem7.service.MasterService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
public class UserAddWindowController implements Observer<Event> {
    String numePrenumeFilter ="";
    String numeFilter ="";
    String prenumeFilter = "";



    public TextField textFieldNume;
    public TextField textFieldPrenume;
    public TextField textFieldMail;
    public TextField textFieldPassword;

    public CheckBox updateCheckBox;
    public Button cancelButton;
    public Button updateButton;
    public Button saveButton;
    public ComboBox<Utilizator> comboBox;

    Stage dialogStage;
    ObservableList<Utilizator> model = FXCollections.observableArrayList();



    MasterService service;
 @FXML
 public void updateCheckBoxHandle(ActionEvent event){

    var state = updateCheckBox.isSelected();
    if(state)
    {
        //E selectat

        comboBox.setVisible(true);
        comboBox.valueProperty().setValue(null);
        comboBox.setDisable(false);
        saveButton.setVisible(false);
        updateButton.setVisible(true);
        fieldNumeHandle(null);
        fieldPrenumeHandle(null);

    }
    else
    {
        //E neselectat
        comboBox.setVisible(false);
        comboBox.setDisable(true);
        saveButton.setVisible(true);
        updateButton.setVisible(false);


    }

 }


   @FXML
   public void cancelButtonHandle(ActionEvent event)
   {
        dialogStage.close();
   }
    @FXML
    public void updateButtonHandle(ActionEvent event)
    {
        Utilizator selectat = comboBox.getValue();
        try {
            service.updateUtilziator(textFieldNume.getText(), textFieldPrenume.getText(),textFieldMail.getText(),
                    textFieldPassword.getText(),  selectat.getId());
        } catch (UtilizatorExceptions e) {
            System.out.println("Nu s-a putut fratele meu..");
        }

        dialogStage.close();
    }
    @FXML
    public void saveButtonHandle(ActionEvent event)
    {
        String nume = textFieldNume.getText();
        String prenume = textFieldPrenume.getText();
        String mail = textFieldMail.getText();
        String password = textFieldPassword.getText();

        try {
            service.addUtilizator(new ArrayList<>(){{
                add(nume);
                add(prenume);
                add(mail);
                add(password);
            }});
            //update();
            ///Maybe show a message  here or somth
            cancelButtonHandle(null);
        } catch (UtilizatorExceptions e) {
            System.out.println("Nu s-a putut adauga..");
        }

    }
    public void comboBoxHandle(ActionEvent event)
    {
        comboBox.setDisable(true);


    }
    @FXML
    public void initialize() {

        comboBox.setItems(model);
//        tableColumnUserID.setCellValueFactory(new PropertyValueFactory<Utilizator, Long>("id"));
//        tableColumnNume.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("firstName"));
//        tableColumnPreunume.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("lastName"));
//
//        tableView.setItems(model);
    }

    private void initModel() {
//        Iterable<Utilizator> messages = service.findAllUsers();
//        List<Utilizator> messageTaskList = StreamSupport.stream(messages.spliterator(), false)
//                .filter(utilizator -> utilizator.getFullName().startsWith(numePrenumeFilter))
//                .collect(Collectors.toList());
//        model.setAll(messageTaskList);
        if(!prenumeFilter.isEmpty())
            numePrenumeFilter = numeFilter + " " + prenumeFilter;
        else numePrenumeFilter = numeFilter;
        List<Utilizator> usersFiltered = service.findAllUsersFiltered(numePrenumeFilter);
        model.setAll(usersFiltered);
    }

//    public void handleSearchMessage(KeyEvent actionEvent){
//        //  tableView.setVisible(!tableView.isVisible());
//        numePrenumeFilter = searchField.getText();
//        update();
//
//
//    }
//    public void handleDeleteMessage(ActionEvent actionEvent) {
//        Utilizator selected = (Utilizator) tableView.getSelectionModel().getSelectedItem();
//        try {
//            service.deleteUser(selected.getId());
//        } catch (Exception e) {
//            System.out.println("Nu exista frt");
//        }
//
//        update();
//    }

//        if (selected != null) {
//            MessageTask deleted = service.deleteMessageTask(selected);
//            if (null != deleted)
//                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Delete", "Studentul a fost sters cu succes!");
//        } else MessageAlert.showErrorMessage(null, "Nu ati selectat nici un student!");


    //    @Override


    public void setService(MasterService servicePrimit, Stage dialogStage) {


        this.service = servicePrimit;
        servicePrimit.addObserver(this);
        this.dialogStage=dialogStage;

        initModel();


    }

    public void fieldNumeHandle(KeyEvent event) {
     if(!comboBox.isDisable())
     {numeFilter = textFieldNume.getText();
    update(new UserChangeEvent(UserChanges.FILTER));
     }

    }

    public void fieldPrenumeHandle(KeyEvent event) {
         if(!comboBox.isDisable())
         {prenumeFilter = textFieldPrenume.getText();
     update(new UserChangeEvent(UserChanges.FILTER));
         }
    }

    @Override
    public void update(Event userChangeEvent) {
        initModel();
    }


//    public void showMessageTaskEditDialog(MessageTask messageTask) {
//        try {
//            // create a new stage for the popup dialog.
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(getClass().getResource("views/editmessagetask-view.fxml"));
//
//
//            AnchorPane root = (AnchorPane) loader.load();
//
//            // Create the dialog Stage.
//            Stage dialogStage = new Stage();
//            dialogStage.setTitle("Edit Message");
//            dialogStage.initModality(Modality.WINDOW_MODAL);
//            //dialogStage.initOwner(primaryStage);
//            Scene scene = new Scene(root);
//            dialogStage.setScene(scene);
//
//            EditMessageTaskController editMessageViewController = loader.getController();
//            editMessageViewController.setService(service, dialogStage, messageTask);
//
//            dialogStage.show();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
