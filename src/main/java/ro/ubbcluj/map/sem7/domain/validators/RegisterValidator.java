package ro.ubbcluj.map.sem7.domain.validators;

import javafx.scene.control.Label;
import ro.ubbcluj.map.sem7.domain.Tuple;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class RegisterValidator {

    protected boolean succes;

    protected RegisterValidator() {

    };


//    public void addPair(Tuple<Method,  String> elem)
//    {
//        listaValidari.add(elem);
//    }

    public boolean validateNume( String Name) {
        if(!Name.matches("^[a-zA-Z]{3,}$"))
        { succes = false;
            return false;
        }

        return true;

    }
    public boolean  validatePrenume(String Name) {
        if(!Name.matches("^[a-zA-Z]{3,}$"))
        { succes = false;
            return false;
        }

        return true;

    }
    public boolean  validateMail( String Name) {

        if(!Name.matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$"))
        { succes = false;
            return false;
        }

        return true;


    }
    public boolean  validateParola(String Name) {
        int literaMare =0, literaMica = 0, cifra = 0;
        var caractere = Name.toCharArray();
        for (char c : caractere) {
            if (c > 'A' && c < 'Z')
                literaMare = 1;
            else if (c > 'a' && c < 'z') {
                literaMica = 1;
            } else if (c > '0' && c < '9') {
                cifra = 1;
            }

        }
        if(!( literaMica + literaMare + cifra == 3 ) || Name.length() < 6)
        { succes = false;
            return false;
        }

        return true;



    }





   abstract public boolean validate();






}
