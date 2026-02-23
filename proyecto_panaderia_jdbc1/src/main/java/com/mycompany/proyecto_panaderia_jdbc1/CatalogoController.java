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
import java.util.ArrayList;
import java.util.List;

public class CatalogoController {

    @FXML
    private GridPane gridProductos;

    @FXML
    private ScrollPane scrollProductos;

    @FXML
    private Button btnTabInicio;

    @FXML
    private Button btnTabPan;

    @FXML
    private Button btnTabPasteles;

    private String categoriaActual = "todos";

    private static final String[][] PRODUCTOS_PAN = {
        {"Concha de Vainilla",   "$15.00"},
        {"Concha de Chocolate",  "$15.00"},
        {"Cuernito",             "$10.00"},
        {"Telera",               "$8.00"},
        {"Bolillo",              "$5.00"},
        {"Pan de Muerto",        "$25.00"},
        {"Orejas",               "$12.00"},
        {"Donas",                "$18.00"},
    };

    private static final String[][] PRODUCTOS_PASTELES = {
        {"Pastel de Chocolate",       "$200.00"},
        {"Pastel de Zanahoria",       "$250.00"},
        {"Pastel de Fresa sin azúcar","$445.00"},
        {"Pastel 3 Leches",           "$235.00"},
        {"Pastel Red Velvet",         "$235.00"},
        {"Pastel de Limón",           "$350.00"},
        {"Pastel de Almendra",        "$295.00"},
        {"Pastel de Chocolate Negro", "$395.00"},
    };

    @FXML
    private void initialize() {
        cargarProductos("todos");
    }

    private void cargarProductos(String categoria) {
        gridProductos.getChildren().clear();

        List<String[]> productos = new ArrayList<>();

        if ("pan".equals(categoria)) {
            for (String[] p : PRODUCTOS_PAN) productos.add(p);
        } else if ("pasteles".equals(categoria)) {
            for (String[] p : PRODUCTOS_PASTELES) productos.add(p);
        } else {
            for (String[] p : PRODUCTOS_PAN) productos.add(p);
            for (String[] p : PRODUCTOS_PASTELES) productos.add(p);
        }

        int col = 0;
        int row = 0;
        int columnas = 4;

        for (String[] producto : productos) {
            VBox tarjeta = crearTarjetaProducto(producto[0], producto[1]);
            gridProductos.add(tarjeta, col, row);
            col++;
            if (col >= columnas) {
                col = 0;
                row++;
            }
        }
    }

    private VBox crearTarjetaProducto(String nombre, String precio) {
        VBox tarjeta = new VBox(8);
        tarjeta.setAlignment(Pos.CENTER);
        tarjeta.setPrefWidth(160.0);
        tarjeta.setPrefHeight(170.0);
        tarjeta.setPadding(new Insets(12));
        tarjeta.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                + " -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 6, 0, 0, 2);");

        // Ícono decorativo del producto (emoji como placeholder)
        Label icono = new Label(nombre.toLowerCase().contains("pastel") ? "🎂" : "🍞");
        icono.setStyle("-fx-font-size: 36px;");

        Label lblNombre = new Label(nombre);
        lblNombre.setStyle("-fx-font-size: 11px; -fx-text-fill: #3a2a1a;"
                + " -fx-font-weight: bold; -fx-text-alignment: center;");
        lblNombre.setWrapText(true);
        lblNombre.setMaxWidth(140);

        Label lblPrecio = new Label(precio);
        lblPrecio.setStyle("-fx-font-size: 12px; -fx-text-fill: #4a3a2a; -fx-font-weight: bold;");

        Button btnAgregar = new Button("🛒 Agregar");
        btnAgregar.setStyle("-fx-background-color: #f4c430; -fx-text-fill: #3a2a1a;"
                + " -fx-font-size: 11px; -fx-font-weight: bold;"
                + " -fx-background-radius: 20; -fx-cursor: hand; -fx-padding: 5 12;");
        String descripcion = nombre.toLowerCase().contains("pastel") ? "Pastel artesanal" : "Pan artesanal";
        double precioDouble = Double.parseDouble(precio.replace("$", "").replace(",", ""));
        btnAgregar.setOnAction(e -> {
            App.agregarAlCarrito(nombre, descripcion, precioDouble);
            btnAgregar.setText("✓ Agregado");
            btnAgregar.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;"
                    + " -fx-font-size: 11px; -fx-font-weight: bold;"
                    + " -fx-background-radius: 20; -fx-cursor: hand; -fx-padding: 5 12;");
        });

        tarjeta.getChildren().addAll(icono, lblNombre, lblPrecio, btnAgregar);
        return tarjeta;
    }

    @FXML
    private void handleTabInicio() {
        categoriaActual = "todos";
        actualizarEstiloTabs("inicio");
        cargarProductos("todos");
    }

    @FXML
    private void handleTabPan() {
        categoriaActual = "pan";
        actualizarEstiloTabs("pan");
        cargarProductos("pan");
    }

    @FXML
    private void handleTabPasteles() {
        categoriaActual = "pasteles";
        actualizarEstiloTabs("pasteles");
        cargarProductos("pasteles");
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
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Carrito vacío");
            alert.setHeaderText(null);
            alert.setContentText("Agrega productos al carrito antes de continuar.");
            alert.showAndWait();
            return;
        }
        try {
            App.setRoot("carrito");
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error al abrir carrito");
            alert.setHeaderText(null);
            alert.setContentText("Detalle: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleRegresar() {
        try {
            App.setRoot("bienvenida");
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al regresar");
            alert.setContentText("No se pudo volver a la pantalla de bienvenida.");
            alert.showAndWait();
        }
    }
}
