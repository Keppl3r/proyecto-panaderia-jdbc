package com.mycompany.proyecto_panaderia_jdbc1;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PedidosEmpleadoController {

    @FXML private Label lblTitulo;
    @FXML private VBox  vboxPedidos;

    private static String modo = "Caja y Entregas";

    public static void setModo(String m) { modo = m; }

    private record PedidoEmp(String numero, boolean express, List<String[]> items, int minutosTranscurridos, String estado) {}

    @FXML
    private void initialize() {
        lblTitulo.setText(modo);

        List<PedidoEmp> pedidos = obtenerPedidosPorModo();
        for (PedidoEmp p : pedidos) {
            vboxPedidos.getChildren().add(crearTarjetaPedido(p));
        }
    }

    private List<PedidoEmp> obtenerPedidosPorModo() {
        List<PedidoEmp> lista = new ArrayList<>();

        if ("Historial Pedidos".equals(modo)) {
            lista.add(new PedidoEmp("#00997", false,
                    List.<String[]>of(new String[]{"Concha de Vainilla x2", "Sin relleno"},
                            new String[]{"Telera x1", ""}),
                    35, "Entregado"));
            lista.add(new PedidoEmp("#00998 Express", true,
                    List.<String[]>of(new String[]{"Dona de Chocolate x3", "Extra azúcar"}),
                    12, "Entregado"));
            lista.add(new PedidoEmp("#00996", false,
                    List.<String[]>of(new String[]{"Croissant x2", "Sin azúcar"},
                            new String[]{"Bolillo x4", ""}),
                    60, "Cancelado"));
        } else {
            lista.add(new PedidoEmp("#00999", false,
                    List.<String[]>of(new String[]{"Croissant", "Con sudor y sangre"},
                            new String[]{"Croissant", "Sin azúcar"}),
                    2, "Listo"));
            lista.add(new PedidoEmp("#01000 Express", true,
                    List.<String[]>of(new String[]{"Croissant", "Sin azúcar"}),
                    2, "Listo"));
        }
        return lista;
    }

    private VBox crearTarjetaPedido(PedidoEmp p) {
        VBox tarjeta = new VBox(10);
        tarjeta.setPadding(new Insets(14, 16, 14, 16));
        tarjeta.setStyle("-fx-background-color: #fdf5e6; -fx-background-radius: 12;"
                + " -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 6, 0, 0, 2);");

        // ── Fila superior ──
        HBox filaSup = new HBox(10);
        filaSup.setAlignment(Pos.CENTER_LEFT);

        VBox infoIzq = new VBox(2);
        Label lblNumero = new Label("Pedido " + p.numero());
        lblNumero.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3a2a1a;");
        infoIzq.getChildren().add(lblNumero);
        if (p.express()) {
            Label lblExpress = new Label("Express");
            lblExpress.setStyle("-fx-font-size: 12px; -fx-text-fill: #e87722; -fx-font-weight: bold;");
            infoIzq.getChildren().add(lblExpress);
        }

        // Campo notas
        TextField txtNotas = new TextField();
        txtNotas.setPromptText("Notas");
        txtNotas.setPrefWidth(130);
        txtNotas.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent #6b5644 transparent;"
                + " -fx-font-size: 12px; -fx-padding: 2 4;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Tiempo + estado
        VBox infoDer = new VBox(4);
        infoDer.setAlignment(Pos.CENTER_RIGHT);
        Label lblTiempo = new Label("Tiempo transcurrido:  " + p.minutosTranscurridos() + ":00 minutos");
        lblTiempo.setStyle("-fx-font-size: 11px; -fx-text-fill: #4a3a2a;");

        String colorBadge = "Entregado".equals(p.estado()) ? "#9e9e9e"
                : "Cancelado".equals(p.estado()) ? "#e05a5a" : "#4caf50";
        Label badge = new Label(p.estado());
        badge.setStyle("-fx-background-color: " + colorBadge + "; -fx-text-fill: white;"
                + " -fx-font-size: 11px; -fx-font-weight: bold;"
                + " -fx-background-radius: 20; -fx-padding: 3 14;");

        HBox filaBadge = new HBox(badge);
        filaBadge.setAlignment(Pos.CENTER_RIGHT);
        infoDer.getChildren().addAll(lblTiempo, filaBadge);

        filaSup.getChildren().addAll(infoIzq, txtNotas, spacer, infoDer);

        // ── Ítems ──
        VBox itemsBox = new VBox(4);
        for (String[] item : p.items()) {
            HBox fila = new HBox(16);
            fila.setAlignment(Pos.CENTER_LEFT);
            Label nombre = new Label(item[0]);
            nombre.setStyle("-fx-font-size: 12px; -fx-text-fill: #3a2a1a; -fx-min-width: 140;");
            Label nota = new Label(item[1]);
            nota.setStyle("-fx-font-size: 12px; -fx-text-fill: #6b5644; -fx-font-style: italic;");
            fila.getChildren().addAll(nombre, nota);
            itemsBox.getChildren().add(fila);
        }

        tarjeta.getChildren().addAll(filaSup, itemsBox);

        // Botón "Cobrar y entregar" (solo en Cocina y Caja y Entregas, no en Historial)
        if (!"Historial Pedidos".equals(modo)) {
            HBox filaBtn = new HBox();
            filaBtn.setAlignment(Pos.CENTER_RIGHT);
            Button btnCobrar = new Button("Cobrar y entregar");
            btnCobrar.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;"
                    + " -fx-font-size: 12px; -fx-font-weight: bold;"
                    + " -fx-background-radius: 20; -fx-padding: 7 18; -fx-cursor: hand;");
            btnCobrar.setOnAction(e -> {
                btnCobrar.setText("✓ Entregado");
                btnCobrar.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;"
                        + " -fx-font-size: 12px; -fx-font-weight: bold;"
                        + " -fx-background-radius: 20; -fx-padding: 7 18;");
                btnCobrar.setDisable(true);
                badge.setText("Entregado");
                badge.setStyle("-fx-background-color: #9e9e9e; -fx-text-fill: white;"
                        + " -fx-font-size: 11px; -fx-font-weight: bold;"
                        + " -fx-background-radius: 20; -fx-padding: 3 14;");
            });
            filaBtn.getChildren().add(btnCobrar);
            tarjeta.getChildren().add(filaBtn);
        }

        return tarjeta;
    }

    @FXML
    private void handleVolver() {
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
