package ro.ubbcluj.map.sem7;

import ro.ubbcluj.map.sem7.domain.exceptions.FriendshipException;
import ro.ubbcluj.map.sem7.domain.validators.UtilizatorValidatorDetailed;
import ro.ubbcluj.map.sem7.ui.UI;

public class Main {

    public static void main(String[] args) throws FriendshipException {

        UI ui = new UI(new UtilizatorValidatorDetailed(),args[0]); ///FOR STRATEGY PATTERN
         ui.run();


    }
}
