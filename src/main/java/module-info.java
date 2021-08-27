module com.example.projektdom {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    opens com.example.projektdom to javafx.fxml;
    exports com.example.projektdom;
}