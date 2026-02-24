package com.mycompany.proyecto_panaderia_jdbc1;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.List;
import negocio.BOs.IPedidoBO;
import negocio.excepciones.NegocioException;
import negocio.fabrica.FabricaBOs;
import persistencia.dominio.DetallePedido;
import persistencia.dominio.Pedido;
import persistencia.dominio.Pedido.EstadoPedido;

public class PedidosEmpleadoController {

    @FXML
    private Label lblTitulo;
    @FXML
    private VBox vboxPedidos;

    private static String modo = "Caja y Entregas";
    private IPedidoBO pedidoBO;

    public static void setModo(String m) {
        modo = m;
    }

    @FXML
    private void initialize() {
        lblTitulo.setText(modo);
        pedidoBO = FabricaBOs.obtenerPedidoBO();
        cargarPedidos();
    }

    private void cargarPedidos() {
        try {
            List<Pedido> pedidos;
            if ("Historial Pedidos".equals(modo)) {
                pedidos = pedidoBO.obtenerHistorialEmpleado();
                pedidos = pedidos.stream()
                        .filter(p -> p.getEstado() == EstadoPedido.ENTREGADO
                                || p.getEstado() == EstadoPedido.CANCELADO
                                || p.getEstado() == EstadoPedido.NO_ENTREGADO)
                        .toList();
            } else {
                pedidos = pedidoBO.obtenerPendientesYListos();
            }

            vboxPedidos.getChildren().clear();
            if (pedidos.isEmpty()) {
                Label vacio = new Label("No hay pedidos para mostrar.");
                vacio.setStyle("-fx-font-size: 14px; -fx-text-fill: #6b5644;");
                vboxPedidos.getChildren().add(vacio);
                return;
            }
            for (Pedido p : pedidos) {
                vboxPedidos.getChildren().add(crearTarjetaPedido(p));
            }
        } catch (NegocioException e) {
            e.printStackTrace();
            mostrarError("No se pudieron cargar los pedidos: " + e.getMessage());
        }
    }

    private VBox crearTarjetaPedido(Pedido p) {
        VBox tarjeta = new VBox(10);
        tarjeta.setPadding(new Insets(14, 16, 14, 16));
        tarjeta.setStyle("-fx-background-color: #fdf5e6; -fx-background-radius: 12;"
                + " -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 6, 0, 0, 2);");

        HBox filaSup = new HBox(10);
        filaSup.setAlignment(Pos.CENTER_LEFT);

        VBox infoIzq = new VBox(2);
        Label lblNumero = new Label("Pedido #" + p.getNumPedido());
        lblNumero.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3a2a1a;");
        infoIzq.getChildren().add(lblNumero);

        boolean esExpress = (p.getIdUsuario() == null);
        if (esExpress) {
            Label lblEx = new Label("Express");
            lblEx.setStyle("-fx-font-size: 12px; -fx-text-fill: #e87722; -fx-font-weight: bold;");
            infoIzq.getChildren().add(lblEx);
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        VBox infoDer = new VBox(4);
        infoDer.setAlignment(Pos.CENTER_RIGHT);
        String fechaStr = p.getFechaRegistro() != null
                ? p.getFechaRegistro().toLocalDateTime().toLocalDate().toString()
                : "—";
        Label lblFecha = new Label("Fecha: " + fechaStr);
        lblFecha.setStyle("-fx-font-size: 11px; -fx-text-fill: #4a3a2a;");

        String colorBadge = switch (p.getEstado()) {
            case LISTO -> "#4caf50";
            case PENDIENTE -> "#2196f3";
            case ENTREGADO -> "#9e9e9e";
            case CANCELADO -> "#e05a5a";
            default -> "#757575";
        };
        Label badge = new Label(p.getEstado().getDescripcion());
        badge.setStyle("-fx-background-color: " + colorBadge + "; -fx-text-fill: white;"
                + " -fx-font-size: 11px; -fx-font-weight: bold;"
                + " -fx-background-radius: 20; -fx-padding: 3 14;");

        HBox filaBadge = new HBox(badge);
        filaBadge.setAlignment(Pos.CENTER_RIGHT);
        infoDer.getChildren().addAll(lblFecha, filaBadge);
        filaSup.getChildren().addAll(infoIzq, spacer, infoDer);

        VBox itemsBox = new VBox(4);
        List<DetallePedido> detalles = cargarDetalles(p.getIdPedido());
        if (detalles.isEmpty()) {
            itemsBox.getChildren().add(new Label("Sin productos."));
        } else {
            for (DetallePedido d : detalles) {
                HBox fila = new HBox(16);
                fila.setAlignment(Pos.CENTER_LEFT);
                String nomProd = d.getProducto() != null ? d.getProducto().getNombre() : "Producto";
                Label nombre = new Label(nomProd + " x" + d.getCantidad());
                nombre.setStyle("-fx-font-size: 12px; -fx-text-fill: #3a2a1a; -fx-min-width: 180;");
                String notaTexto = d.getNotas() != null ? d.getNotas() : "";
                Label nota = new Label(notaTexto);
                nota.setStyle("-fx-font-size: 12px; -fx-text-fill: #6b5644; -fx-font-style: italic;");
                fila.getChildren().addAll(nombre, nota);
                itemsBox.getChildren().add(fila);
            }
        }

        Label lblTotal = new Label(String.format("Total: $%.2f", p.getTotal()));
        lblTotal.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #3a2a1a;");

        tarjeta.getChildren().addAll(filaSup, itemsBox, lblTotal);

        if (!"Historial Pedidos".equals(modo)) {
            HBox filaBtn = new HBox();
            filaBtn.setAlignment(Pos.CENTER_RIGHT);
            Button btnCobrar = new Button("Cobrar y entregar");
            btnCobrar.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;"
                    + " -fx-font-size: 12px; -fx-font-weight: bold;"
                    + " -fx-background-radius: 20; -fx-padding: 7 18; -fx-cursor: hand;");
            btnCobrar.setOnAction(e -> entregarPedido(p.getIdPedido(), btnCobrar, badge));
            filaBtn.getChildren().add(btnCobrar);
            tarjeta.getChildren().add(filaBtn);
        }

        return tarjeta;
    }

    private void entregarPedido(int idPedido, Button btnCobrar, Label badge) {
        try {
            try {
                pedidoBO.marcarComoListo(idPedido);
            } catch (NegocioException e) {
            }
            pedidoBO.entregarPedido(idPedido, "EFECTIVO");
            btnCobrar.setText("✓ Entregado");
            btnCobrar.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;"
                    + " -fx-font-size: 12px; -fx-font-weight: bold;"
                    + " -fx-background-radius: 20; -fx-padding: 7 18;");
            btnCobrar.setDisable(true);
            badge.setText("Entregado");
            badge.setStyle("-fx-background-color: #9e9e9e; -fx-text-fill: white;"
                    + " -fx-font-size: 11px; -fx-font-weight: bold;"
                    + " -fx-background-radius: 20; -fx-padding: 3 14;");
        } catch (NegocioException ex) {
            mostrarError("No se pudo entregar: " + ex.getMessage());
        }
    }

    private List<DetallePedido> cargarDetalles(int idPedido) {
        try {
            return pedidoBO.obtenerDetallesPorPedido(idPedido);
        } catch (NegocioException e) {
            return List.of();
        }
    }

    @FXML
    private void handleVolver() {
        try {
            App.setRoot("menu_empleado");
        } catch (IOException e) {
            mostrarError("No se pudo volver al menú.");
        }
    }

    private void mostrarError(String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
