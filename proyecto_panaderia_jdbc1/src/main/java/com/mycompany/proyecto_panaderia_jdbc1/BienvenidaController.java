package com.mycompany.proyecto_panaderia_jdbc1;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import java.io.IOException;

public class BienvenidaController {

    @FXML
    private Label lblNombreUsuario;

    private static String nombreUsuario = "Juan";

    public static void setNombreUsuario(String nombre) {
        nombreUsuario = nombre;
    }

    @FXML
    private void initialize() {
        lblNombreUsuario.setText(nombreUsuario);
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
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Carrito");
        alert.setHeaderText("Carrito de compras");
        alert.setContentText("Tu carrito está vacío por el momento.");
        alert.showAndWait();
    }

    @FXML
    private void handlePerfil() {
        try {
            App.setRoot("editar_perfil");
        } catch (IOException e) {
            mostrarError("No se pudo cargar la pantalla de perfil.");
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
