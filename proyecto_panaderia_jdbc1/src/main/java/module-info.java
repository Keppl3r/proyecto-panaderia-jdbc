module com.mycompany.proyecto_panaderia_jdbc1 {
    requires javafx.controls;
    requires javafx.fxml;
  requires java.sql;
    opens com.mycompany.proyecto_panaderia_jdbc1 to javafx.fxml;
    exports com.mycompany.proyecto_panaderia_jdbc1;
}
