package com.mycompany.proyecto_panaderia_jdbc1;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;

/**
 * Controlador para el menú principal de empleados.
 * Actúa como un panel de control para derivar al usuario a las distintas 
 * áreas operativas: Cocina, Caja, Historial e Inventario.
 */
public class MenuEmpleadoController {

    /**
     * Configura la vista de pedidos en modo "Cocina".
     * En este modo, el empleado verá pedidos pendientes de preparación.
     */
    @FXML
    private void handleCocina() {
        PedidosEmpleadoController.setModo("Cocina");
        navegar("pedidos_empleado");
    }
    
    /**
     * Configura la vista de pedidos en modo "Caja y Entregas".
     * Enfocado en pedidos listos para ser cobrados o entregados al cliente.
     */
    @FXML
    private void handleCajaEntregas() {
        PedidosEmpleadoController.setModo("Caja y Entregas");
        navegar("pedidos_empleado");
    }
    /**
     * Configura la vista de pedidos en modo "Historial".
     * Permite consultar pedidos finalizados o cancelados.
     */
    @FXML
    private void handleHistorialPedidos() {
        PedidosEmpleadoController.setModo("Historial Pedidos");
        navegar("pedidos_empleado");
    }
    /**
     * Navega directamente a la gestión de stock de productos.
     */
    @FXML
    private void handleInventario() {
        navegar("inventario");
    }
    /**
     * Cierra la sesión del empleado y regresa a la pantalla principal.
     */
    @FXML
    private void handleSalir() {
        navegar("main_panaderia");
    }
    /**
     * Método utilitario para centralizar la lógica de navegación.
     * Incluye manejo de excepciones para fallos en la carga de archivos FXML.
     * @param fxml Nombre del archivo de vista (sin extensión).
     */
    private void navegar(String fxml) {
        try {
            App.setRoot(fxml);
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo cargar la pantalla.");
            alert.showAndWait();
        }
    }
}
