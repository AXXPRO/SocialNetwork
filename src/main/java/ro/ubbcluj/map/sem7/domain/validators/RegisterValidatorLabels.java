package ro.ubbcluj.map.sem7.domain.validators;

import javafx.scene.control.Label;
import ro.ubbcluj.map.sem7.domain.Tuple;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class RegisterValidatorLabels extends RegisterValidator{

    List<Tuple<Method, Tuple<Label, String>>> listaValidariLabel = new ArrayList<>();
   private static  RegisterValidatorLabels instance = new RegisterValidatorLabels();

    public static RegisterValidatorLabels getInstance(){
        return instance;
    }
   private RegisterValidatorLabels() {
       super();
   };



    public  void addPairLabeled(Tuple<Method, Tuple<Label, String>> elem)
    {
        listaValidariLabel.add(elem);
    }
    public boolean validateNume(Label label, String Name) {

        if(!super.validateNume(Name))
        {
            label.setVisible(true);

        }
        return true;

    }
    public boolean  validatePrenume(Label label, String Name) {
        if(!super.validatePrenume(Name))
        {
            label.setVisible(true);

        }
        return true;

    }
    public boolean  validateMail(Label label, String Name) {
        if(!super.validateMail(Name))
        {
            label.setVisible(true);

        }
        return true;

    }
    public boolean  validateParola(Label label, String Name) {

        if(!super.validateParola(Name))
        {
            label.setVisible(true);

        }
        return true;

    }

    @Override
    public boolean validate() {
        succes = true;
        listaValidariLabel.forEach(elemList -> {

            Method M =  elemList.getLeft();
            try {
                M.invoke(this, elemList.getRight().getLeft(), elemList.getRight().getRight());
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }

        });

        listaValidariLabel = new ArrayList<>();
        return succes;
    }
}
