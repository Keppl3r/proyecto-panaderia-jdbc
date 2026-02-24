package com.mycompany.proyecto_panaderia_jdbc1;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import negocio.BOs.IPedidoExpressBO;
import negocio.BOs.IPedidoProgramadoBO;
import negocio.DTOs.PedidoExpressNuevoDTO;
import negocio.DTOs.PedidoProgramadoNuevoDTO;
import negocio.excepciones.NegocioException;
import negocio.fabrica.FabricaBOs;
import persistencia.dominio.Cupon;
import persistencia.dominio.DetallePedido;
import persistencia.dominio.PedidoExpress;
import persistencia.dominio.PedidoProgramado;

public class CarritoController {

    @FXML
    private VBox vboxItems;
    @FXML
    private VBox vboxCupon;
    @FXML
    private TextField txtCupon;
    @FXML
    private Label lblSubtotal;
    @FXML
    private Label lblDescuento;
    @FXML
    private Label lblTotal;
    @FXML
    private VBox vboxResumen;
    @FXML
    private CheckBox chkProgramado;
    @FXML
    private CheckBox chkExpress;
    @FXML
    private Label lblTotalResumen;
    @FXML
    private Button btnTarjeta;
    @FXML
    private Button btnEfectivo;

    private double descuentoAplicado = 0.0;
    private Cupon cuponActivo = null;
    private String metodoPago = "TARJETA";

    private static final String BTN_PAGO_NORMAL = "-fx-background-color: white; -fx-background-radius: 10;"
            + " -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3a2a1a;"
            + " -fx-cursor: hand; -fx-padding: 10 16;";
    private static final String BTN_PAGO_SELEC = "-fx-background-color: #3a2a1a; -fx-background-radius: 10;"
            + " -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;"
            + " -fx-cursor: hand; -fx-padding: 10 16;";

    @FXML
    private void initialize() {
        boolean express = App.modoExpress;
        chkProgramado.setSelected(!express);
        chkExpress.setSelected(express);
        vboxCupon.setVisible(!express);
        vboxCupon.setManaged(!express);
        btnTarjeta.setStyle(BTN_PAGO_SELEC);
        btnEfectivo.setStyle(BTN_PAGO_NORMAL);
        renderizarItems();
        actualizarTotales();
    }

    private void renderizarItems() {
        vboxItems.getChildren().clear();
        vboxResumen.getChildren().clear();
        for (int i = 0; i < App.carrito.size(); i++) {
            App.ItemCarrito item = App.carrito.get(i);
            vboxItems.getChildren().add(crearFilaItem(item, i));
            Label resItem = new Label("• " + item.nombre() + " x" + item.cantidad());
            resItem.setStyle("-fx-font-size: 11px; -fx-text-fill: #4a3a2a;");
            vboxResumen.getChildren().add(resItem);
        }
    }

    private HBox crearFilaItem(App.ItemCarrito item, int index) {
        HBox fila = new HBox(10);
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.setStyle("-fx-background-color: #fdf5e6; -fx-background-radius: 10; -fx-padding: 10;");

        Label icono = new Label(item.nombre().toLowerCase().contains("dona")
                || item.nombre().toLowerCase().contains("pastel") ? "🍩" : "🍞");
        icono.setStyle("-fx-font-size: 26px; -fx-min-width: 36;");

        VBox info = new VBox(2);
        Label lblNombre = new Label(item.nombre());
        lblNombre.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #3a2a1a;");
        Label lblDesc = new Label(item.descripcion());
        lblDesc.setStyle("-fx-font-size: 11px; -fx-text-fill: #6b5644;");
        Label lblPrecio = new Label(String.format("$%.2f", item.precio()));
        lblPrecio.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #3a2a1a;");
        info.getChildren().addAll(lblNombre, lblDesc, lblPrecio);
        HBox.setHgrow(info, Priority.ALWAYS);

        HBox controles = new HBox(6);
        controles.setAlignment(Pos.CENTER);
        Button btnMenos = new Button("−");
        btnMenos.setStyle("-fx-background-color: #e0d0b0; -fx-background-radius: 6;"
                + " -fx-font-size: 14px; -fx-cursor: hand; -fx-padding: 2 8;");
        Label lblCantidad = new Label(String.valueOf(item.cantidad()));
        lblCantidad.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-min-width: 22;"
                + " -fx-text-fill: #3a2a1a; -fx-alignment: center;");
        Button btnMas = new Button("+");
        btnMas.setStyle("-fx-background-color: #e0d0b0; -fx-background-radius: 6;"
                + " -fx-font-size: 14px; -fx-cursor: hand; -fx-padding: 2 8;");
        btnMenos.setOnAction(e -> cambiarCantidad(index, -1));
        btnMas.setOnAction(e -> cambiarCantidad(index, +1));
        controles.getChildren().addAll(btnMenos, lblCantidad, btnMas);

        Button btnEliminar = new Button("🗑");
        btnEliminar.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 16px;");
        btnEliminar.setOnAction(e -> {
            App.carrito.remove(index);
            descuentoAplicado = 0;
            cuponActivo = null;
            renderizarItems();
            actualizarTotales();
        });

        fila.getChildren().addAll(icono, info, controles, btnEliminar);
        return fila;
    }

    private void cambiarCantidad(int index, int delta) {
        if (index < 0 || index >= App.carrito.size())
            return;
        App.ItemCarrito item = App.carrito.get(index);
        int nueva = item.cantidad() + delta;
        if (nueva <= 0) {
            App.carrito.remove(index);
        } else {
            App.carrito.set(index, new App.ItemCarrito(
                    item.idProducto(), item.nombre(), item.descripcion(), item.precio(), nueva));
        }
        renderizarItems();
        actualizarTotales();
    }

    private void actualizarTotales() {
        double subtotal = App.carrito.stream().mapToDouble(i -> i.precio() * i.cantidad()).sum();
        double descuento = subtotal * descuentoAplicado;
        double total = subtotal - descuento;
        lblSubtotal.setText(String.format("$%.2f", subtotal));
        lblDescuento.setText(String.format("- $%.2f", descuento));
        lblTotal.setText(String.format("$%.2f", total));
        lblTotalResumen.setText(String.format("$%.2f", total));
    }

    @FXML
    private void handleAplicarCupon() {
        String codigoStr = txtCupon.getText().trim();
        if (codigoStr.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Cupón vacío", "Ingresa el número del cupón.");
            return;
        }

        int idCupon;
        try {
            idCupon = Integer.parseInt(codigoStr);
        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.ERROR, "Cupón inválido", "Ingresa el número de ID del cupón (ej: 1, 2, 4).");
            return;
        }

        try {
            IPedidoProgramadoBO pedidoProgramadoBO = FabricaBOs.obtenerPedidoProgramadoBO();
            Cupon cupon = pedidoProgramadoBO.validarCupon(idCupon);

            cuponActivo = cupon;
            descuentoAplicado = cupon.getPorcentajeDescuento() / 100.0;
            mostrarAlertaOK(String.format("✅ Cupón válido\nSe aplicó un %.0f%% de descuento.",
                    cupon.getPorcentajeDescuento()));

        } catch (NegocioException ex) {
            mostrarAlerta(AlertType.ERROR, "Cupón inválido", ex.getMessage());
            descuentoAplicado = 0;
            cuponActivo = null;
        }

        actualizarTotales();
    }

    @FXML
    private void handleSeleccionarTarjeta() {
        metodoPago = "TARJETA";
        btnTarjeta.setStyle(BTN_PAGO_SELEC);
        btnEfectivo.setStyle(BTN_PAGO_NORMAL);
    }

    @FXML
    private void handleSeleccionarEfectivo() {
        metodoPago = "EFECTIVO";
        btnEfectivo.setStyle(BTN_PAGO_SELEC);
        btnTarjeta.setStyle(BTN_PAGO_NORMAL);
    }

    @FXML
    private void handleConfirmar() {
        if (App.carrito.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Carrito vacío", "Agrega al menos un producto.");
            return;
        }

        try {
            List<DetallePedido> detalles = construirDetalles();

            if (App.modoExpress) {
                crearPedidoExpress(detalles);
            } else {
                crearPedidoProgramado(detalles);
            }

        } catch (NegocioException ex) {
            ex.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Error al crear pedido", ex.getMessage());
        }
    }

    private List<DetallePedido> construirDetalles() {
        List<DetallePedido> detalles = new ArrayList<>();
        for (App.ItemCarrito item : App.carrito) {
            DetallePedido d = new DetallePedido();
            d.setIdProducto(item.idProducto());
            d.setCantidad(item.cantidad());
            detalles.add(d);
        }
        return detalles;
    }

    private void crearPedidoExpress(List<DetallePedido> detalles) throws NegocioException {
        IPedidoExpressBO bo = FabricaBOs.obtenerPedidoExpressBO();
        PedidoExpressNuevoDTO dto = new PedidoExpressNuevoDTO(detalles);
        PedidoExpress pedido = bo.crearPedidoExpress(dto);

        PedidoConfirmadoController.setDatosPedido(
                "#" + pedido.getNumPedido(),
                pedido.getEstado().getDescripcion(),
                true);
        PedidoConfirmadoController.setInfoExpress(pedido.getFolio(), pedido.getPinTextoPlano());

        App.limpiarCarrito();
        try {
            App.setRoot("pedido_confirmado");
        } catch (IOException e) {
            mostrarAlerta(AlertType.ERROR, "Error", "No se pudo cargar la confirmación.");
        }
    }

    private void crearPedidoProgramado(List<DetallePedido> detalles) throws NegocioException {
        if (!SesionActual.isLogeado() || SesionActual.getCliente() == null) {
            mostrarAlerta(AlertType.ERROR, "Sin sesión", "Debes iniciar sesión para hacer un pedido programado.");
            return;
        }

        // Fecha de entrega: 2 horas desde ahora (mínimo requerido)
        Timestamp fechaEntrega = new Timestamp(System.currentTimeMillis() + (2L * 60 * 60 * 1000) + (5L * 60 * 1000));
        Integer idCupon = cuponActivo != null ? cuponActivo.getIdCupon() : null;

        IPedidoProgramadoBO bo = FabricaBOs.obtenerPedidoProgramadoBO();
        PedidoProgramadoNuevoDTO dto = new PedidoProgramadoNuevoDTO(
                SesionActual.getCliente().getIdUsuario(), fechaEntrega, idCupon, detalles);
        PedidoProgramado pedido = bo.programarPedido(dto);

        PedidoConfirmadoController.setDatosPedido(
                "#" + pedido.getNumPedido(),
                pedido.getEstado().getDescripcion(),
                false);
        PedidoConfirmadoController.setInfoExpress(null, null);

        App.limpiarCarrito();
        try {
            App.setRoot("pedido_confirmado");
        } catch (IOException e) {
            mostrarAlerta(AlertType.ERROR, "Error", "No se pudo cargar la confirmación.");
        }
    }

    @FXML
    private void handleVolver() {
        try {
            App.setRoot("catalogo");
        } catch (IOException e) {
            mostrarAlerta(AlertType.ERROR, "Error", "No se pudo volver al catálogo.");
        }
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    private void mostrarAlertaOK(String contenido) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Cupón aplicado");
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        Button btn = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        if (btn != null) {
            btn.setText("Aceptar");
            btn.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-weight: bold;");
        }
        alert.showAndWait();
    }
}
