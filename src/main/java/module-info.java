module com.ricochetrobots {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.ricochetrobots to javafx.fxml;
    exports com.ricochetrobots.main;
    opens com.ricochetrobots.main to javafx.fxml;
}