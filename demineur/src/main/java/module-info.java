module com.example.demineur {
    requires javafx.controls;
    requires javafx.fxml;
    
    opens com.example.demineur to javafx.fxml;
    exports com.example.demineur;
    opens com.example.demineur.Controlleur to javafx.fxml;
    exports com.example.demineur.Controlleur;
    opens com.example.demineur.module to javafx.fxml;
    exports com.example.demineur.module;
    opens com.example.demineur.Vue to javafx.fxml;
    exports com.example.demineur.Vue;

}