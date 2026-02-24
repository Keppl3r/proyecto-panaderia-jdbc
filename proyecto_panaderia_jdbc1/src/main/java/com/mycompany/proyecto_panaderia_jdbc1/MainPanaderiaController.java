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
        App.modoExpress = true;
        App.limpiarCarrito();
        try {
            App.setRoot("catalogo");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al cargar pantalla");
            alert.setContentText("No se pudo cargar el catálogo.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleIniciarSesion() {

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
        addHoverEffect(btnVerCatalogo);
        addHoverEffect(btnIniciarSesion);
        addHoverEffect(btnRegistrarse);
    }

    private void addHoverEffect(Button button) {
        String originalStyle = button.getStyle();

        button.setOnMouseEntered(e -> {
            button.setStyle(originalStyle
                    + "; -fx-scale-x: 1.05; -fx-scale-y: 1.05; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 15, 0, 0, 3);");
        });

        button.setOnMouseExited(e -> {
            button.setStyle(originalStyle);
        });
    }
}
