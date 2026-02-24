package com.mycompany.proyecto_panaderia_jdbc1;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import java.io.IOException;
import java.util.Optional;

public class BienvenidaController {

    @FXML
    private Label lblNombreUsuario;

    @FXML
    private void initialize() {
        lblNombreUsuario.setText(SesionActual.getNombreDisplay());
    }

    @FXML
    private void handleVerCatalogo() {
        App.modoExpress = false;
        App.limpiarCarrito();
        try {
            App.setRoot("catalogo");
        } catch (IOException e) {
            mostrarError("No se pudo cargar el catálogo de productos.");
        }
    }

    @FXML
    private void handleMisPedidos() {
        try {
            App.setRoot("mis_pedidos");
        } catch (IOException e) {
            mostrarError("No se pudo cargar la pantalla de pedidos.");
        }
    }

    @FXML
    private void handleMiPerfil() {
        try {
            App.setRoot("editar_perfil");
        } catch (IOException e) {
            mostrarError("No se pudo cargar la pantalla de perfil.");
        }
    }

    @FXML
    private void handleCarrito() {
        try {
            App.setRoot("carrito");
        } catch (IOException e) {
            mostrarError("No se pudo abrir el carrito.");
        }
    }

    @FXML
    private void handlePerfil() {
        try {
            App.setRoot("editar_perfil");
        } catch (IOException e) {
            mostrarError("No se pudo cargar la pantalla de perfil.");
        }
    }

    @FXML
    private void handleCerrarSesion() {
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Cerrar sesión");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Estás seguro de que deseas cerrar la sesión?");

        javafx.scene.control.Button btnAceptar = (javafx.scene.control.Button) confirm.getDialogPane()
                .lookupButton(ButtonType.OK);
        btnAceptar.setText("Cerrar sesión");
        btnAceptar.setStyle("-fx-background-color: #e05a5a; -fx-text-fill: white; -fx-font-weight: bold;");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            SesionActual.cerrarSesion();
            App.limpiarCarrito();
            try {
                App.setRoot("main_panaderia");
            } catch (IOException e) {
                mostrarError("No se pudo volver a la pantalla principal.");
            }
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error de navegación");
        alert.setHeaderText("Error");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
