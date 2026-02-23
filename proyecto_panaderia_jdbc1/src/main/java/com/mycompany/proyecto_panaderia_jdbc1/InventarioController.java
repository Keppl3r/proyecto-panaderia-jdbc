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
import java.util.ArrayList;
import java.util.List;

public class InventarioController {

    @FXML private TextField txtBuscar;
    @FXML private VBox      vboxProductos;

    private record Producto(String nombre, double precio, boolean disponible) {}

    private final List<Producto> todos = new ArrayList<>(List.of(
        new Producto("Concha de Vainilla",        15.00,  true),
        new Producto("Concha de Chocolate",       15.00,  true),
        new Producto("Croissant",                 20.00,  true),
        new Producto("Cuernito",                  10.00,  true),
        new Producto("Telera",                     8.00,  true),
        new Producto("Bolillo",                    5.00,  false),
        new Producto("Pan de Muerto",             25.00,  true),
        new Producto("Orejas",                    12.00,  true),
        new Producto("Donas",                     18.00,  true),
        new Producto("Pastel de Chocolate",      200.00,  true),
        new Producto("Pastel de Zanahoria",      250.00,  true),
        new Producto("Pastel Fresa sin azúcar",  445.00,  false),
        new Producto("Pastel 3 Leches",          235.00,  true),
        new Producto("Pastel Red Velvet",        235.00,  true),
        new Producto("Pastel de Limón",          350.00,  true),
        new Producto("Pastel de Almendra",       295.00,  true)
    ));

    @FXML
    private void initialize() {
        renderizar(todos);

        txtBuscar.textProperty().addListener((obs, old, nuevo) -> {
            String filtro = nuevo.trim().toLowerCase();
            if (filtro.isBlank()) {
                renderizar(todos);
            } else {
                List<Producto> filtrados = todos.stream()
                        .filter(p -> p.nombre().toLowerCase().contains(filtro))
                        .toList();
                renderizar(filtrados);
            }
        });
    }

    private void renderizar(List<Producto> lista) {
        vboxProductos.getChildren().clear();
        for (int i = 0; i < lista.size(); i++) {
            vboxProductos.getChildren().add(crearFilaProducto(lista.get(i)));
        }
    }

    private HBox crearFilaProducto(Producto p) {
        HBox fila = new HBox(14);
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.setPadding(new Insets(10, 16, 10, 16));
        fila.setStyle("-fx-background-color: #fdf5e6; -fx-background-radius: 10;"
                + " -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 4, 0, 0, 1);");

        Label lblNombre = new Label(p.nombre());
        lblNombre.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #3a2a1a;"
                + " -fx-min-width: 200;");

        Label lblPrecio = new Label(String.format("$%.2f", p.precio()));
        lblPrecio.setStyle("-fx-font-size: 13px; -fx-text-fill: #4a3a2a;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblDisp = new Label("Disponible");
        lblDisp.setStyle("-fx-font-size: 12px; -fx-text-fill: #4a3a2a; -fx-padding: 0 10 0 0;");

        // Toggle switch estilizado
        ToggleButton toggle = new ToggleButton();
        toggle.setSelected(p.disponible());
        actualizarEstiloToggle(toggle);
        toggle.setOnAction(e -> actualizarEstiloToggle(toggle));

        fila.getChildren().addAll(lblNombre, lblPrecio, spacer, lblDisp, toggle);
        return fila;
    }

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

    @FXML
    private void handleInicio() {
        try {
            App.setRoot("menu_empleado");
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo volver al menú.");
            alert.showAndWait();
        }
    }
}
