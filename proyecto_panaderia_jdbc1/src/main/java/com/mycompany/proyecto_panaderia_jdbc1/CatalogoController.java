package com.mycompany.proyecto_panaderia_jdbc1;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import negocio.BOs.IProductoBO;
import negocio.excepciones.NegocioException;
import negocio.fabrica.FabricaBOs;
import persistencia.dominio.Producto;

public class CatalogoController {

    @FXML private GridPane gridProductos;
    @FXML private ScrollPane scrollProductos;
    @FXML private Button btnTabInicio;
    @FXML private Button btnTabPan;
    @FXML private Button btnTabPasteles;

    private String categoriaActual = "todos";
    private List<Producto> todosLosProductos;

    @FXML
    private void initialize() {
        cargarDesdeDB();
        actualizarEstiloTabs("inicio");
    }

    private void cargarDesdeDB() {
        try {
            IProductoBO productoBO = FabricaBOs.obtenerProductoBO();
            todosLosProductos = productoBO.obtenerProductoDisponibles();
        } catch (NegocioException e) {
            e.printStackTrace();
            mostrarError("No se pudieron cargar los productos: " + e.getMessage());
            todosLosProductos = List.of();
        }
        renderizarGrid(todosLosProductos);
    }

    private void renderizarGrid(List<Producto> productos) {
        gridProductos.getChildren().clear();
        int col = 0, row = 0, columnas = 4;
        for (Producto p : productos) {
            gridProductos.add(crearTarjeta(p), col, row);
            col++;
            if (col >= columnas) { col = 0; row++; }
        }
    }

    private VBox crearTarjeta(Producto producto) {
        VBox tarjeta = new VBox(8);
        tarjeta.setAlignment(Pos.CENTER);
        tarjeta.setPrefWidth(160.0);
        tarjeta.setPrefHeight(170.0);
        tarjeta.setPadding(new Insets(12));
        tarjeta.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                + " -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 6, 0, 0, 2);");

        String emoji = "DULCE".equals(producto.getTipo()) ? "🍰"
                : "INTEGRAL".equals(producto.getTipo()) ? "🌾" : "🍞";
        Label icono = new Label(emoji);
        icono.setStyle("-fx-font-size: 36px;");

        Label lblNombre = new Label(producto.getNombre());
        lblNombre.setStyle("-fx-font-size: 11px; -fx-text-fill: #3a2a1a;"
                + " -fx-font-weight: bold; -fx-text-alignment: center;");
        lblNombre.setWrapText(true);
        lblNombre.setMaxWidth(140);

        Label lblPrecio = new Label(String.format("$%.2f", producto.getPrecio()));
        lblPrecio.setStyle("-fx-font-size: 12px; -fx-text-fill: #4a3a2a; -fx-font-weight: bold;");

        Button btnAgregar = new Button("🛒 Agregar");
        btnAgregar.setStyle("-fx-background-color: #f4c430; -fx-text-fill: #3a2a1a;"
                + " -fx-font-size: 11px; -fx-font-weight: bold;"
                + " -fx-background-radius: 20; -fx-cursor: hand; -fx-padding: 5 12;");

        // Marcar como ya agregado si ya está en el carrito
        boolean yaEnCarrito = App.carrito.stream()
                .anyMatch(i -> i.idProducto() == producto.getIdProducto());
        if (yaEnCarrito) {
            btnAgregar.setText("✓ Agregado");
            btnAgregar.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;"
                    + " -fx-font-size: 11px; -fx-font-weight: bold;"
                    + " -fx-background-radius: 20; -fx-cursor: hand; -fx-padding: 5 12;");
        }

        btnAgregar.setOnAction(e -> {
            App.agregarAlCarrito(producto.getIdProducto(), producto.getNombre(),
                    producto.getDescripcion(), producto.getPrecio());
            btnAgregar.setText("✓ Agregado");
            btnAgregar.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;"
                    + " -fx-font-size: 11px; -fx-font-weight: bold;"
                    + " -fx-background-radius: 20; -fx-cursor: hand; -fx-padding: 5 12;");
        });

        tarjeta.getChildren().addAll(icono, lblNombre, lblPrecio, btnAgregar);
        return tarjeta;
    }

    @FXML private void handleTabInicio() {
        categoriaActual = "todos";
        actualizarEstiloTabs("inicio");
        renderizarGrid(todosLosProductos);
    }

    @FXML private void handleTabPan() {
        categoriaActual = "pan";
        actualizarEstiloTabs("pan");
        if (todosLosProductos != null) {
            List<Producto> filtrados = todosLosProductos.stream()
                    .filter(p -> "SALADO".equals(p.getTipo()) || "INTEGRAL".equals(p.getTipo()))
                    .collect(Collectors.toList());
            renderizarGrid(filtrados);
        }
    }

    @FXML private void handleTabPasteles() {
        categoriaActual = "pasteles";
        actualizarEstiloTabs("pasteles");
        if (todosLosProductos != null) {
            List<Producto> filtrados = todosLosProductos.stream()
                    .filter(p -> "DULCE".equals(p.getTipo()))
                    .collect(Collectors.toList());
            renderizarGrid(filtrados);
        }
    }

    private void actualizarEstiloTabs(String activo) {
        String normal  = "-fx-background-color: transparent; -fx-text-fill: #6b5644;"
                       + " -fx-font-size: 13px; -fx-cursor: hand; -fx-padding: 3 8;";
        String activos = "-fx-background-color: transparent; -fx-text-fill: #e87722;"
                       + " -fx-font-size: 13px; -fx-font-weight: bold;"
                       + " -fx-cursor: hand; -fx-padding: 3 8;";
        btnTabInicio.setStyle("inicio".equals(activo) ? activos : normal);
        btnTabPan.setStyle("pan".equals(activo) ? activos : normal);
        btnTabPasteles.setStyle("pasteles".equals(activo) ? activos : normal);
    }

    @FXML
    private void handleCarrito() {
        if (App.carrito.isEmpty()) {
            mostrarInfo("Carrito vacío", "Agrega productos al carrito antes de continuar.");
            return;
        }
        try {
            App.setRoot("carrito");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("No se pudo abrir el carrito: " + e.getMessage());
        }
    }

    @FXML
    private void handleRegresar() {
        try {
            if (App.modoExpress) {
                App.setRoot("main_panaderia");
            } else {
                App.setRoot("bienvenida");
            }
        } catch (IOException e) {
            mostrarError("No se pudo regresar.");
        }
    }

    private void mostrarError(String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error"); alert.setHeaderText(null);
        alert.setContentText(msg); alert.showAndWait();
    }

    private void mostrarInfo(String titulo, String msg) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo); alert.setHeaderText(null);
        alert.setContentText(msg); alert.showAndWait();
    }
}
