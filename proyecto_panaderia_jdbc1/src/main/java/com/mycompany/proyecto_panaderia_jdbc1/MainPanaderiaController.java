package com.mycompany.proyecto_panaderia_jdbc1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;

/**
 * Controlador de la pantalla de inicio principal (Landing Page).
 * Proporciona el punto de entrada para los flujos de invitado, inicio de sesión 
 * y registro de nuevos clientes.
 */
public class MainPanaderiaController {

    @FXML
    private Button btnVerCatalogo;

    @FXML
    private Button btnIniciarSesion;

    @FXML
    private Button btnRegistrarse;
    
    /**
     * Inicializa la vista aplicando efectos visuales dinámicos a los botones.
     * Se ejecuta automáticamente al cargar el archivo FXML.
     */
    @FXML
    private void initialize() {
        // Log de confirmación de carga
        System.out.println("Pantalla principal de Pantojarte Panadería cargada");
        
        // Aplicar efectos visuales dinámicos
        addHoverEffect(btnVerCatalogo);
        addHoverEffect(btnIniciarSesion);
        addHoverEffect(btnRegistrarse);
    }

    /**
     * Configura el acceso como invitado.
     * Activa el 'modoExpress' en la aplicación y limpia el carrito de compras 
     * previo para asegurar una nueva sesión de compra limpia.
     */
    @FXML
    private void handleVerCatalogo() {
        App.modoExpress = true;
        App.limpiarCarrito();
        try {
            App.setRoot("catalogo");
        } catch (IOException e) {
            e.printStackTrace();
            mostrarErrorCarga("catalogo");
        }
    }

    /**
     * Redirige al usuario a la pantalla de autenticación.
     */
    @FXML
    private void handleIniciarSesion() {
        try {
            App.setRoot("login");
        } catch (IOException e) {
            e.printStackTrace();
            mostrarErrorCarga("inicio de sesión");
        }
    }

    /**
     * Redirige al usuario al formulario de registro.
     */
    @FXML
    private void handleRegistrarse() {
        try {
            App.setRoot("registro");
        } catch (IOException e) {
            e.printStackTrace();
            mostrarErrorCarga("registro");
        }
    }

    /**
     * Implementa efectos visuales dinámicos para mejorar la experiencia de usuario (UX).
     * Utiliza listeners de eventos de ratón para modificar el estilo CSS del botón 
     * en tiempo de ejecución, creando una sensación de profundidad y respuesta.
     * * Acciones realizadas:
     * 1. MouseEntered: Aumenta la escala del botón un 5% y aplica una sombra (dropshadow).
     * 2. MouseExited: Revierte el botón a su estilo original definido en el FXML.
     * * @param button El componente de la interfaz al que se le aplicará el efecto.
     */
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

    /**
     * Método auxiliar para centralizar el despliegue de errores al cambiar de vista.
     * @param vista El nombre de la vista que falló al cargar para mostrar al usuario.
     */
    private void mostrarErrorCarga(String vista) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error al cargar pantalla");
        alert.setContentText("No se pudo cargar la pantalla de " + vista + ".");
        alert.showAndWait();
    }
}