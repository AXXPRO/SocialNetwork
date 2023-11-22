package ro.ubbcluj.map.sem7.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.ubbcluj.map.sem7.events.Event;
import ro.ubbcluj.map.sem7.observer.Observer;
import ro.ubbcluj.map.sem7.service.MasterService;

import java.io.IOException;

public class LoginViewController implements Observer<Event> {
    public Pane loginPane;
    public Label LoginLabel;
    public PasswordField textFieldPassword;
    public TextField textFieldMail;
    public Button logInButton;
    public Label errorLabel;
    MasterService service;

    Stage loginStage;
    @Override
    public void update(Event event) {
        initModel();
    }

    public void setMasterService(MasterService servicePrimit, Stage stagePrimit) {
        loginStage = stagePrimit;
        service = servicePrimit;
        service.addObserver(this);

        initModel();
    }

    @FXML
    public void initialize() {

        //tableColumnUserID.setCellValueFactory(new PropertyValueFactory<Utilizator, Long>("id"));
//        tableColumnNume.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("firstName"));
//        tableColumnPreunume.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("lastName"));
//        tableColumnMail.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("mail"));
//        tableView.setItems(model);
    }

    public void initModel() {

        loginStage.show();
        textFieldPassword.setText("");
        textFieldMail.setText("");
//        List<Utilizator> usersFiltered = service.findAllUsersFiltered(numePrenumeFilter);
//
//        model.setAll(usersFiltered);
//        loginPane.setVisible(true);
    }

    public void showUserView() {
//        try {
//            // create a new stage for the popup dialog.
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(getClass().getResource("userAddWindow-view.fxml"));
//
//
//            AnchorPane root = (AnchorPane) loader.load();
//
//            // Create the dialog Stage.
//            Stage dialogStage = new Stage();
//            dialogStage.setTitle("Update Users");
//            dialogStage.initModality(Modality.WINDOW_MODAL);
//            //dialogStage.initOwner(primaryStage);
//            Scene scene = new Scene(root);
//            dialogStage.setScene(scene);
//
//            UserAddWindowController userAddWindowController = loader.getController();
//            userAddWindowController.setService(service, dialogStage);
//
//            dialogStage.show();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }




    public void handleLogInLabel(MouseEvent event)
    {
        textFieldMail.setText("admin@fakemail.com");
        textFieldPassword.setText("admin");
        handleLogInButton(null);
    }
    public void handleLogInButton(ActionEvent event) {

        String mail, password;
        mail = textFieldMail.getText();
        password = textFieldPassword.getText();
        if(mail.equals("admin@fakemail.com"))
        {
            //VERY UNSAFE, NOT FOR ACTUAL USE, JUST TO NOT OVERCOMPLICATE FOR NOW
            if(password.equals("admin"))
            {
                //ADMIN PRIVS

                //adminPane.setVisible(true);
                showAdminView();
               // loginPane.setVisible(false);
            }
            else {
                errorLabel.setVisible(true);
            }
        }
        else {

            Long ID = service.tryLogin(mail, password);
            if (ID < 0) {
                errorLabel.setVisible(true);
                return;
            }
            showUserView();
          //  loginPane.setVisible(false);

        }
    }

    private void showAdminView() {
                try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("admin-view.fxml"));


            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Admin Window");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            UserTableController userAddWindowController = loader.getController();
            userAddWindowController.setMasterService(service, dialogStage, this);

            dialogStage.show();
            loginStage.hide();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
