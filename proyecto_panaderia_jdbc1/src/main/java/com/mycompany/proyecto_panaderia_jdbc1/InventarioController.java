package com.mycompany.proyecto_panaderia_jdbc1;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import negocio.BOs.IProductoBO;
import negocio.excepciones.NegocioException;
import negocio.fabrica.FabricaBOs;
import persistencia.dominio.Producto;

/**
 * Controlador para la gestión del inventario de productos.
 * Permite a los empleados visualizar el catálogo completo, realizar búsquedas
 * filtradas por nombre y activar/desactivar la disponibilidad de productos
 * de manera inmediata.
 */
public class InventarioController {

    @FXML private TextField txtBuscar;
    @FXML private VBox      vboxProductos;

    private List<Producto> todosLosProductos;
    private IProductoBO productoBO;
    
    /**
     * Inicializa el controlador, establece la conexión con la capa de negocio
     * y configura el Listener para la búsqueda reactiva.
     */
    @FXML
    private void initialize() {
        productoBO = FabricaBOs.obtenerProductoBO();
        cargarProductos();

        txtBuscar.textProperty().addListener((obs, old, nuevo) -> {
            String filtro = nuevo.trim().toLowerCase();
            if (filtro.isBlank()) {
                renderizar(todosLosProductos);
            } else {
                List<Producto> filtrados = todosLosProductos.stream()
                        .filter(p -> p.getNombre().toLowerCase().contains(filtro))
                        .collect(Collectors.toList());
                renderizar(filtrados);
            }
        });
    }
    /**
     * Recupera la lista completa de productos desde la base de datos
     * y los muestra en la interfaz.
     */
    private void cargarProductos() {
        try {
            todosLosProductos = productoBO.obtenerTodos();
            renderizar(todosLosProductos);
        } catch (NegocioException e) {
            e.printStackTrace();
            mostrarError("No se pudieron cargar los productos: " + e.getMessage());
            todosLosProductos = List.of();
        }
    }
    /**
     * Limpia el contenedor visual y genera una fila por cada producto en la lista.
     * @param lista Lista de productos a mostrar.
     */
    private void renderizar(List<Producto> lista) {
        vboxProductos.getChildren().clear();
        for (Producto p : lista) {
            vboxProductos.getChildren().add(crearFilaProducto(p));
        }
    }
    /**
     * Construye dinámicamente un componente HBox para representar un producto.
     * Incluye nombre, precio, una etiqueta de tipo con color (Badge) y 
     * un interruptor (Toggle) de disponibilidad.
     * @param p El producto a representar.
     * @return El nodo visual configurado.
     */
    private HBox crearFilaProducto(Producto p) {
        HBox fila = new HBox(14);
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.setPadding(new Insets(10, 16, 10, 16));
        fila.setStyle("-fx-background-color: #fdf5e6; -fx-background-radius: 10;"
                + " -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 4, 0, 0, 1);");

        Label lblNombre = new Label(p.getNombre());
        lblNombre.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #3a2a1a;"
                + " -fx-min-width: 200;");

        Label lblTipo = new Label(p.getTipo());
        lblTipo.setStyle("-fx-font-size: 11px; -fx-text-fill: white; -fx-background-radius: 10;"
                + " -fx-padding: 2 8; -fx-background-color: "
                + tipoBadgeColor(p.getTipo()) + ";");

        Label lblPrecio = new Label(String.format("$%.2f", p.getPrecio()));
        lblPrecio.setStyle("-fx-font-size: 13px; -fx-text-fill: #4a3a2a; -fx-min-width: 60;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblDisp = new Label("Disponible");
        lblDisp.setStyle("-fx-font-size: 12px; -fx-text-fill: #4a3a2a; -fx-padding: 0 10 0 0;");

        ToggleButton toggle = new ToggleButton();
        toggle.setSelected(p.isDisponible());
        actualizarEstiloToggle(toggle);
        toggle.setOnAction(e -> {
            boolean nuevoEstado = toggle.isSelected();
            try {
                productoBO.actualizarDisponibilidad(p.getIdProducto(), nuevoEstado);
                p.setDisponible(nuevoEstado);
                actualizarEstiloToggle(toggle);
            } catch (NegocioException ex) {
                toggle.setSelected(!nuevoEstado);
                actualizarEstiloToggle(toggle);
                mostrarError("No se pudo actualizar: " + ex.getMessage());
            }
        });

        fila.getChildren().addAll(lblNombre, lblTipo, lblPrecio, spacer, lblDisp, toggle);
        return fila;
    }
    /**
     * Define el color hexadecimal según el tipo de producto.
     */
    private String tipoBadgeColor(String tipo) {
        return switch (tipo) {
            case "DULCE"    -> "#e87722";
            case "SALADO"   -> "#4a7a3a";
            case "INTEGRAL" -> "#8b6f47";
            default         -> "#9e9e9e";
        };
    }
    /**
     * Aplica estilos CSS personalizados para simular un "Switch" moderno
     * basado en el estado del ToggleButton.
     */
    private void actualizarEstiloToggle(ToggleButton t) {
        if (t.isSelected()) {
            t.setText("●");
            t.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;"
                    + " -fx-background-radius: 20; -fx-min-width: 46; -fx-min-height: 24;"
                    + " -fx-font-size: 10px; -fx-cursor: hand; -fx-alignment: CENTER_RIGHT;"
                    + " -fx-padding: 0 4 0 0;");
        } else {
            t.setText("●");
            t.setStyle("-fx-background-color: #bdbdbd; -fx-text-fill: white;"
                    + " -fx-background-radius: 20; -fx-min-width: 46; -fx-min-height: 24;"
                    + " -fx-font-size: 10px; -fx-cursor: hand; -fx-alignment: CENTER_LEFT;"
                    + " -fx-padding: 0 0 0 4;");
        }
    }
    /**
     * Regresa al usuario al menú principal de empleados.
     */
    @FXML
    private void handleInicio() {
        try {
            App.setRoot("menu_empleado");
        } catch (IOException e) {
            mostrarError("No se pudo volver al menú.");
        }
    }
    /**
     * Despliega una ventana emergente de error (Modal).
     * Este método centraliza la notificación de excepciones de la capa de negocio
     * o persistencia, asegurando que el usuario reciba retroalimentación clara
     * ante fallos críticos.
     * * @param msg Mensaje descriptivo del error que se mostrará en el cuerpo de la alerta.
     */
    private void mostrarError(String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error"); alert.setHeaderText(null);
        alert.setContentText(msg); alert.showAndWait();
    }
}
