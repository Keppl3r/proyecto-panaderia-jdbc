 module com.mycompany.proyecto_panaderia_jdbc1 {
         requires javafx.controls;
         requires javafx.fxml;
         requires java.sql;
         requires java.logging;

         opens com.mycompany.proyecto_panaderia_jdbc1 to javafx.fxml;

         exports com.mycompany.proyecto_panaderia_jdbc1;
         exports persistencia.dominio;
         exports persistencia.DAOs;
         exports persistencia.conexion;
         exports persistencia.excepciones;
         exports negocio.BOs;
         exports negocio.excepciones;
     }