package com.mycompany.proyecto_panaderia_jdbc1;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;

public class MenuEmpleadoController {

    @FXML
    private void handleCocina() {
        PedidosEmpleadoController.setModo("Cocina");
        navegar("pedidos_empleado");
    }

    @FXML
    private void handleCajaEntregas() {
        PedidosEmpleadoController.setModo("Caja y Entregas");
        navegar("pedidos_empleado");
    }

    @FXML
    private void handleHistorialPedidos() {
        PedidosEmpleadoController.setModo("Historial Pedidos");
        navegar("pedidos_empleado");
    }

    @FXML
    private void handleInventario() {
        navegar("inventario");
    }

    @FXML
    private void handleSalir() {
        navegar("main_panaderia");
    }

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
