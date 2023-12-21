package ro.ubbcluj.map.sem7.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ro.ubbcluj.map.sem7.domain.Tuple;
import ro.ubbcluj.map.sem7.domain.exceptions.UtilizatorExceptions;
import ro.ubbcluj.map.sem7.domain.validators.RegisterValidator;
import ro.ubbcluj.map.sem7.domain.validators.RegisterValidatorLabels;
import ro.ubbcluj.map.sem7.events.Event;
import ro.ubbcluj.map.sem7.observer.Observer;
import ro.ubbcluj.map.sem7.service.MasterService;

import java.util.ArrayList;

public class RegisterController implements Observer<Event> {
    public TextField textFieldNume;
    public TextField textFieldPrenume;
    public TextField textFieldMail;
    public TextField textFieldPassword;
    public Label numeInvalidLabel;
    public Label prenumeInvalidLabel;
    public Label mailInvalidLabel;
    public Label parolaInvalidLabel;
    public Button registerButton;
    public Button cancelButton;
    MasterService service;
    Stage registerStage;

    @Override
    public void update(Event event) {

    }

    public void setMasterService(MasterService servicePrimit, Stage stagePrimit) {
        service = servicePrimit;
        registerStage = stagePrimit;
        // loginViewController = login;

        service.addObserver(this);

        initModel();
    }
    private void initModel(){

    }

    @FXML
    public void initialize() {
        //tableColumnUserID.setCellValueFactory(new PropertyValueFactory<Utilizator, Long>("id"));
        mailInvalidLabel.setVisible(false);
        numeInvalidLabel.setVisible(false);
        parolaInvalidLabel.setVisible(false);
        prenumeInvalidLabel.setVisible(false);

    }

    public void cancelButtonHandle(ActionEvent event) {
        registerStage.close();
    }

    public void handleRegisterButton(ActionEvent event) {
        mailInvalidLabel.setVisible(false);
        numeInvalidLabel.setVisible(false);
        parolaInvalidLabel.setVisible(false);
        prenumeInvalidLabel.setVisible(false);
        String nume, prenume, parola, mail;
        nume = textFieldNume.getText();
        prenume = textFieldPrenume.getText();
        mail =textFieldMail.getText();
        parola = textFieldPassword.getText();

        if(service.findOneEmail(mail))
            mailInvalidLabel.setVisible(true);
            RegisterValidatorLabels registerValidator = RegisterValidatorLabels.getInstance();

            try {
                registerValidator.addPairLabeled(new Tuple<>(registerValidator.getClass().getMethod("validateNume",Label.class, String.class) , new Tuple<>(numeInvalidLabel, nume)) );
                registerValidator.addPairLabeled(new Tuple<>(registerValidator.getClass().getMethod("validatePrenume",Label.class, String.class) , new Tuple<>(prenumeInvalidLabel, prenume)) );
                registerValidator.addPairLabeled(new Tuple<>(registerValidator.getClass().getMethod("validateMail",Label.class, String.class) , new Tuple<>(mailInvalidLabel, mail)) );
                registerValidator.addPairLabeled(new Tuple<>(registerValidator.getClass().getMethod("validateParola",Label.class, String.class) , new Tuple<>(parolaInvalidLabel, parola)) );



            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
           if(registerValidator.validate()) {
               try {
                   service.addUtilizator(new ArrayList<>(){{
                       add(nume);
                       add(prenume);
                       add(mail);
                       add(parola);
                   }});

                   cancelButtonHandle(null);
               } catch (UtilizatorExceptions e) {
                   System.out.println("Nu s-a putut adauga..");
               }
           }


    }
}
