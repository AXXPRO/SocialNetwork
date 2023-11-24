package ro.ubbcluj.map.sem7.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ro.ubbcluj.map.sem7.domain.Utilizator;
import ro.ubbcluj.map.sem7.events.Event;
import ro.ubbcluj.map.sem7.observer.Observer;
import ro.ubbcluj.map.sem7.service.MasterService;

public class UserViewController implements Observer<Event> {

    MasterService service;
    Utilizator utilizator;
    Stage userStage;
    public Label labelUserName;


    public UserViewController(){

    }

    @Override
    public void update(Event event) {
        //TODO
    }

    public void setMasterService(MasterService service, Stage dialogStage,Utilizator util) {
        this.service = service;
        userStage = dialogStage;
        utilizator = util;
        // loginViewController = login;

        service.addObserver(this);

        initModel();
    }

    public void initModel() {
        //Called multiple times
        labelUserName.setText(utilizator.getFirstName() + " " + utilizator.getLastName());
    }
    @FXML
    public void initialize(){
        //Called once


    }
}
