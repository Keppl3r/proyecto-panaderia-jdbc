package com.mycompany.proyecto_panaderia_jdbc1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;

public class MainPanaderiaController {

    @FXML
    private Button btnVerCatalogo;

    @FXML
    private Button btnIniciarSesion;

    @FXML
    private Button btnRegistrarse;

    @FXML
    private void handleVerCatalogo() {
        System.out.println("Ver Catálogo presionado");
        
        // Aquí puedes implementar la navegación al catálogo
        // Por ejemplo: App.setRoot("catalogo");
        
        // Por ahora mostramos un mensaje
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Pedido Express");
        alert.setHeaderText("Ver Catálogo");
        alert.setContentText("Mostrando productos disponibles para entrega inmediata...");
        alert.showAndWait();
    }

    @FXML
    private void handleIniciarSesion() {
        System.out.println("Iniciar Sesión presionado");
        
        try {
            App.setRoot("login");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al cargar pantalla");
            alert.setContentText("No se pudo cargar la pantalla de inicio de sesión.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleRegistrarse() {
        System.out.println("Registrarse presionado");
        
        try {
            App.setRoot("registro");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al cargar pantalla");
            alert.setContentText("No se pudo cargar la pantalla de registro.");
            alert.showAndWait();
        }
    }

    @FXML
    private void initialize() {
        // Método que se ejecuta automáticamente al cargar el FXML
        System.out.println("Pantalla principal de Pantojarte Panadería cargada");
        
        // Aquí puedes agregar efectos hover para los botones
        addHoverEffect(btnVerCatalogo);
        addHoverEffect(btnIniciarSesion);
        addHoverEffect(btnRegistrarse);
    }

    private void addHoverEffect(Button button) {
        String originalStyle = button.getStyle();
        
        button.setOnMouseEntered(e -> {
            button.setStyle(originalStyle + "; -fx-scale-x: 1.05; -fx-scale-y: 1.05; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 15, 0, 0, 3);");
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(originalStyle);
        });
    }
}

