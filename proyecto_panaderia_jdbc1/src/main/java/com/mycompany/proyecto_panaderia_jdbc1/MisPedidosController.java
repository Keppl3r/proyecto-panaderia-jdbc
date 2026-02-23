package com.mycompany.proyecto_panaderia_jdbc1;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MisPedidosController {

    @FXML
    private GridPane gridPedidos;

    @FXML
    private ComboBox<String> cmbTipoPedido;

    private record Pedido(String numero, String estado, String fecha, String[] items, String total, boolean esExpress, String tiempoRestante) {}

    private final List<Pedido> pedidos = new ArrayList<>();

    @FXML
    private void initialize() {
        cmbTipoPedido.getItems().addAll("Todos", "Listo", "Pendiente", "Express", "Cancelado");
        cmbTipoPedido.setValue("Todos");

        pedidos.add(new Pedido("#00123", "Listo",     "12/02/2026",
                new String[]{"Enpanada de Arandanos X2", "Croisant  x2"}, "$35.00", false, null));
        pedidos.add(new Pedido("#00124", "Pendiente", "16/02/2026",
                new String[]{"Concha de Vainilla  X2", "Dona de Chocolate X1"}, "$40.00", false, null));
        pedidos.add(new Pedido("Express", "Express",  "05/02/2026",
                new String[]{"Dona de Chocolate X1"}, "$15.00", true, "19:55"));
        pedidos.add(new Pedido("#00123", "Cancelado", "02/02/2026",
                new String[]{"Dona de Chocolate X1", "Croisant  x1"}, "$35.00", false, null));

        renderizarPedidos(pedidos);
    }

    private void renderizarPedidos(List<Pedido> lista) {
        gridPedidos.getChildren().clear();
        int col = 0;
        int row = 0;
        for (Pedido p : lista) {
            VBox tarjeta = crearTarjetaPedido(p);
            gridPedidos.add(tarjeta, col, row);
            col++;
            if (col >= 2) {
                col = 0;
                row++;
            }
        }
    }

    private VBox crearTarjetaPedido(Pedido p) {
        VBox tarjeta = new VBox(8);
        tarjeta.setPrefWidth(310.0);
        tarjeta.setPadding(new Insets(14, 16, 14, 16));
        tarjeta.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                + " -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 6, 0, 0, 2);");

        // Encabezado: número + badge estado
        HBox encabezado = new HBox(10);
        encabezado.setAlignment(Pos.CENTER_LEFT);

        Label lblNumero = new Label("Pedido " + p.numero());
        lblNumero.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3a2a1a;");

        Label badge = crearBadge(p.estado());
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        encabezado.getChildren().addAll(lblNumero, spacer, badge);

        // Fecha
        HBox filafecha = new HBox(6);
        filafecha.setAlignment(Pos.CENTER_LEFT);
        Label iconFecha = new Label("📅");
        iconFecha.setStyle("-fx-font-size: 11px;");
        Label lblFecha = new Label(p.fecha());
        lblFecha.setStyle("-fx-font-size: 12px; -fx-text-fill: #6b5644;");

        if (p.esExpress() && p.tiempoRestante() != null) {
            Region sp2 = new Region();
            HBox.setHgrow(sp2, javafx.scene.layout.Priority.ALWAYS);
            Label lblTimer = new Label(p.tiempoRestante());
            lblTimer.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #3a2a1a;"
                    + " -fx-background-color: #e0e0e0; -fx-background-radius: 6; -fx-padding: 2 8;");
            filafecha.getChildren().addAll(iconFecha, lblFecha, sp2, lblTimer);
        } else {
            filafecha.getChildren().addAll(iconFecha, lblFecha);
        }

        // Items
        VBox itemsBox = new VBox(3);
        for (String item : p.items()) {
            HBox fila = new HBox(6);
            Label punto = new Label("●");
            punto.setStyle("-fx-font-size: 8px; -fx-text-fill: #4a3a2a;");
            Label lbl = new Label(item);
            lbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #3a2a1a;");
            fila.setAlignment(Pos.CENTER_LEFT);
            fila.getChildren().addAll(punto, lbl);
            itemsBox.getChildren().add(fila);
        }

        // Separador y total
        Region sep = new Region();
        sep.setPrefHeight(1);
        sep.setStyle("-fx-background-color: #e0d0b0;");

        HBox filaTotal = new HBox();
        filaTotal.setAlignment(Pos.CENTER_LEFT);
        Label lblTotal = new Label("Total:  " + p.total());
        lblTotal.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #3a2a1a;");
        filaTotal.getChildren().add(lblTotal);

        tarjeta.getChildren().addAll(encabezado, filafecha, itemsBox, sep, filaTotal);

        // Botón cancelar solo para pedidos pendientes
        if ("Pendiente".equals(p.estado())) {
            Button btnCancelar = new Button("Cancelar");
            btnCancelar.setStyle("-fx-background-color: #e05a5a; -fx-text-fill: white;"
                    + " -fx-font-size: 12px; -fx-font-weight: bold;"
                    + " -fx-background-radius: 20; -fx-padding: 6 20; -fx-cursor: hand;");
            btnCancelar.setOnAction(e -> confirmarCancelacion(p));
            HBox filaCancelar = new HBox();
            filaCancelar.setAlignment(Pos.CENTER_RIGHT);
            filaCancelar.getChildren().add(btnCancelar);
            tarjeta.getChildren().add(filaCancelar);
        }

        return tarjeta;
    }

    private Label crearBadge(String estado) {
        Label badge = new Label(estado);
        badge.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;"
                + " -fx-background-radius: 20; -fx-padding: 3 12; -fx-text-fill: white;"
                + " -fx-background-color: " + colorEstado(estado) + ";");
        return badge;
    }

    private String colorEstado(String estado) {
        return switch (estado) {
            case "Listo"     -> "#4caf50";
            case "Pendiente" -> "#2196f3";
            case "Express"   -> "#f39c12";
            case "Cancelado" -> "#9e9e9e";
            default          -> "#757575";
        };
    }

    private void confirmarCancelacion(Pedido p) {
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Cancelar pedido");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Estás seguro de que desea cancelar el pedido?");

        Button btnAceptar = (Button) confirm.getDialogPane().lookupButton(ButtonType.OK);
        btnAceptar.setText("Aceptar");
        btnAceptar.setStyle("-fx-background-color: #e05a5a; -fx-text-fill: white;"
                + " -fx-font-weight: bold; -fx-background-radius: 6;");

        Button btnCancelar = (Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL);
        btnCancelar.setText("Cancelar");
        btnCancelar.setStyle("-fx-background-radius: 6;");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            pedidos.remove(p);
            pedidos.add(new Pedido(p.numero(), "Cancelado", p.fecha(),
                    p.items(), p.total(), false, null));
            handleAplicarFiltros();
        }
    }

    @FXML
    private void handleAplicarFiltros() {
        String filtro = cmbTipoPedido.getValue();
        if (filtro == null || "Todos".equals(filtro)) {
            renderizarPedidos(pedidos);
        } else {
            List<Pedido> filtrados = pedidos.stream()
                    .filter(p -> filtro.equals(p.estado()))
                    .toList();
            renderizarPedidos(filtrados);
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
