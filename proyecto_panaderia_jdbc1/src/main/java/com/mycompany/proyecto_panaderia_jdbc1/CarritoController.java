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
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class CarritoController {

    @FXML private VBox     vboxItems;
    @FXML private VBox     vboxCupon;
    @FXML private Separator sepCupon;
    @FXML private TextField txtCupon;
    @FXML private Label    lblSubtotal;
    @FXML private Label    lblDescuento;
    @FXML private Label    lblTotal;
    @FXML private VBox     vboxResumen;
    @FXML private CheckBox chkProgramado;
    @FXML private CheckBox chkExpress;
    @FXML private Label    lblTotalResumen;
    @FXML private Button   btnTarjeta;
    @FXML private Button   btnEfectivo;

    private static final String CUPON_VALIDO   = "DESCUENTO10";
    private static final double DESCUENTO_PCT  = 0.10;

    private double descuentoAplicado = 0.0;
    private String metodoPago = "Tarjeta";

    private static final String BTN_PAGO_NORMAL =
            "-fx-background-color: white; -fx-background-radius: 10;"
            + " -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3a2a1a;"
            + " -fx-cursor: hand; -fx-padding: 10 16;";
    private static final String BTN_PAGO_SELEC =
            "-fx-background-color: #3a2a1a; -fx-background-radius: 10;"
            + " -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;"
            + " -fx-cursor: hand; -fx-padding: 10 16;";

    @FXML
    private void initialize() {
        boolean express = App.modoExpress;

        // Modo de pago: checkboxes del resumen
        chkProgramado.setSelected(!express);
        chkExpress.setSelected(express);

        // Sección cupón solo en modo programado
        vboxCupon.setVisible(!express);
        vboxCupon.setManaged(!express);
        sepCupon.setVisible(!express);
        sepCupon.setManaged(!express);

        // Selección inicial: Tarjeta
        btnTarjeta.setStyle(BTN_PAGO_SELEC);
        btnEfectivo.setStyle(BTN_PAGO_NORMAL);

        // Ítems de ejemplo si el carrito está vacío
        if (App.carrito.isEmpty()) {
            App.agregarAlCarrito("Concha de Vainilla", "Sin relleno, espolvoreada con azúcar", 15.00);
            App.agregarAlCarrito("Dona de Chocolate", "Clásica, sin relleno", 15.00);
        }

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

        // Ícono producto
        Label icono = new Label(item.nombre().toLowerCase().contains("dona")
                || item.nombre().toLowerCase().contains("pastel") ? "🍩" : "🍞");
        icono.setStyle("-fx-font-size: 26px; -fx-min-width: 36;");

        // Info
        VBox info = new VBox(2);
        Label lblNombre = new Label(item.nombre());
        lblNombre.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #3a2a1a;");
        Label lblDesc = new Label(item.descripcion());
        lblDesc.setStyle("-fx-font-size: 11px; -fx-text-fill: #6b5644;");
        Label lblPrecio = new Label(String.format("$%.2f", item.precio()));
        lblPrecio.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #3a2a1a;");
        info.getChildren().addAll(lblNombre, lblDesc, lblPrecio);
        HBox.setHgrow(info, Priority.ALWAYS);

        // Controles cantidad
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

        // Botón eliminar
        Button btnEliminar = new Button("🗑");
        btnEliminar.setStyle("-fx-background-color: transparent; -fx-cursor: hand;"
                + " -fx-font-size: 16px;");
        btnEliminar.setOnAction(e -> {
            App.carrito.remove(index);
            descuentoAplicado = 0;
            renderizarItems();
            actualizarTotales();
        });

        fila.getChildren().addAll(icono, info, controles, btnEliminar);
        return fila;
    }

    private void cambiarCantidad(int index, int delta) {
        if (index < 0 || index >= App.carrito.size()) return;
        App.ItemCarrito item = App.carrito.get(index);
        int nueva = item.cantidad() + delta;
        if (nueva <= 0) {
            App.carrito.remove(index);
        } else {
            App.carrito.set(index, new App.ItemCarrito(
                    item.nombre(), item.descripcion(), item.precio(), nueva));
        }
        renderizarItems();
        actualizarTotales();
    }

    private void actualizarTotales() {
        double subtotal = App.carrito.stream()
                .mapToDouble(i -> i.precio() * i.cantidad())
                .sum();
        double descuento = subtotal * descuentoAplicado;
        double total = subtotal - descuento;

        lblSubtotal.setText(String.format("$%.2f", subtotal));
        lblDescuento.setText(String.format("- $%.2f", descuento));
        lblTotal.setText(String.format("$%.2f", total));
        lblTotalResumen.setText(String.format("$%.2f", total));
    }

    @FXML
    private void handleAplicarCupon() {
        String codigo = txtCupon.getText().trim().toUpperCase();

        if (CUPON_VALIDO.equals(codigo)) {
            descuentoAplicado = DESCUENTO_PCT;
            actualizarTotales();
            mostrarDialogoCupon(true);
        } else {
            descuentoAplicado = 0;
            actualizarTotales();
            mostrarDialogoCupon(false);
        }
    }

    private void mostrarDialogoCupon(boolean valido) {
        Alert alert = new Alert(valido ? AlertType.INFORMATION : AlertType.ERROR);
        alert.setTitle("Validación del cupón");
        alert.setHeaderText(null);

        if (valido) {
            alert.setContentText("✅  Cupón Válido\nSe aplicó un 10% de descuento.");
            Button btnAceptar = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            if (btnAceptar != null) {
                btnAceptar.setText("Aceptar");
                btnAceptar.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;"
                        + " -fx-font-weight: bold; -fx-background-radius: 6;");
            }
        } else {
            alert.setContentText("❌  El cupón ingresado no es válido o ha expirado.");
            Button btnAceptar = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            if (btnAceptar != null) {
                btnAceptar.setText("Aceptar");
                btnAceptar.setStyle("-fx-background-color: #e05a5a; -fx-text-fill: white;"
                        + " -fx-font-weight: bold; -fx-background-radius: 6;");
            }
        }
        alert.showAndWait();
    }

    @FXML
    private void handleSeleccionarTarjeta() {
        metodoPago = "Tarjeta";
        btnTarjeta.setStyle(BTN_PAGO_SELEC);
        btnEfectivo.setStyle(BTN_PAGO_NORMAL);
    }

    @FXML
    private void handleSeleccionarEfectivo() {
        metodoPago = "Efectivo";
        btnEfectivo.setStyle(BTN_PAGO_SELEC);
        btnTarjeta.setStyle(BTN_PAGO_NORMAL);
    }

    @FXML
    private void handleConfirmar() {
        if (App.carrito.isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Carrito vacío");
            alert.setHeaderText(null);
            alert.setContentText("Agrega al menos un producto antes de confirmar.");
            alert.showAndWait();
            return;
        }
        PedidoConfirmadoController.setDatosPedido(
                "#00" + (int)(Math.random() * 900 + 100),
                App.modoExpress ? "Express" : "Pendiente",
                App.modoExpress
        );
        App.limpiarCarrito();
        try {
            App.setRoot("pedido_confirmado");
        } catch (IOException e) {
            mostrarError("No se pudo cargar la pantalla de confirmación.");
        }
    }

    @FXML
    private void handleVolver() {
        try {
            App.setRoot("catalogo");
        } catch (IOException e) {
            mostrarError("No se pudo volver al catálogo.");
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
