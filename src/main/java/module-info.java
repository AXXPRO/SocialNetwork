module ro.ubbcluj.map.sem7 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens ro.ubbcluj.map.sem7 to javafx.fxml;
    opens ro.ubbcluj.map.sem7.domain to javafx.base;

    exports ro.ubbcluj.map.sem7;
    opens ro.ubbcluj.map.sem7.gui to javafx.fxml;
    exports ro.ubbcluj.map.sem7.gui;

}