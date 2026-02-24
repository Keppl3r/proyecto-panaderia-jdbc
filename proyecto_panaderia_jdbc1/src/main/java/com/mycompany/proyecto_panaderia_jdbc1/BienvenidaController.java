package com.mycompany.proyecto_panaderia_jdbc1;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import java.io.IOException;
import java.util.Optional;

/**
 * Controlador para la vista de Bienvenida de la aplicación.
 * Gestiona el menú principal donde el usuario puede elegir navegar 
 * hacia el catálogo, sus pedidos o editar su perfil.
 * 
 */
public class BienvenidaController {
    /** Etiqueta que muestra el nombre del usuario logueado en la pantalla */
    @FXML
    private Label lblNombreUsuario;
    /**
     * Método de inicialización automática de JavaFX.
     * Se encarga de mostrar el nombre del usuario recuperándolo de la Sesión Actual.
     */
    @FXML
    private void initialize() {
        lblNombreUsuario.setText(SesionActual.getNombreDisplay());
    }
    /**
     * Configura la aplicación en modo catálogo normal, limpia el carrito
     * y redirige a la vista del catálogo de productos.
     */
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
    /**
     * Redirige al usuario a la pantalla donde puede visualizar su historial de pedidos.
     */
    @FXML
    private void handleMisPedidos() {
        try {
            App.setRoot("mis_pedidos");
        } catch (IOException e) {
            mostrarError("No se pudo cargar la pantalla de pedidos.");
        }
    }
    /**
     * Redirige al usuario a la pantalla de edición de información personal.
     */
    @FXML
    private void handleMiPerfil() {
        try {
            App.setRoot("editar_perfil");
        } catch (IOException e) {
            mostrarError("No se pudo cargar la pantalla de perfil.");
        }
    }
    /**
     * Muestra una ventana emergente informativa sobre el estado del carrito.
     * Actualmente solo indica que el carrito está vacío.
     */
    @FXML
    private void handleCarrito() {
        try {
            App.setRoot("carrito");
        } catch (IOException e) {
            mostrarError("No se pudo abrir el carrito.");
        }
    }
    /**
     * Método alternativo para navegar al perfil (duplicado funcional de handleMiPerfil).
     */
    @FXML
    private void handlePerfil() {
        try {
            App.setRoot("editar_perfil");
        } catch (IOException e) {
            mostrarError("No se pudo cargar la pantalla de perfil.");
        }
    }

    /**
     * Cierra la sesión del usuario actual con confirmación, limpia el carrito
     * y redirige a la pantalla principal de la aplicación.
     */
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

    /**
     * Utilidad para mostrar alertas de error personalizadas al usuario
     * en caso de fallos en la navegación o carga de vistas.
     * @param mensaje El texto descriptivo del error a mostrar.
     */
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error de navegación");
        alert.setHeaderText("Error");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
