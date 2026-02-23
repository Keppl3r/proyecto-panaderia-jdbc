package com.mycompany.proyecto_panaderia_jdbc1;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import java.io.IOException;

public class PedidoConfirmadoController {

    @FXML private Label  lblNumeroPedido;
    @FXML private Label  lblEstadoPedido;
    @FXML private VBox   vboxTimer;
    @FXML private Label  lblTimer;
    @FXML private VBox   vboxMensaje;
    @FXML private Button btnVerPedidos;

    private static String  numeroPedido  = "#00124";
    private static String  estadoPedido  = "Pendiente";
    private static boolean pedidoExpress = false;

    private Timeline timeline;

    /** Llamado desde CarritoController antes de navegar */
    public static void setDatosPedido(String numero, String estado, boolean express) {
        numeroPedido  = numero;
        estadoPedido  = estado;
        pedidoExpress = express;
    }

    @FXML
    private void initialize() {
        lblNumeroPedido.setText(numeroPedido);
        lblEstadoPedido.setText(estadoPedido);

        if (pedidoExpress) {
            // Badge naranja para Express
            lblEstadoPedido.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;"
                    + " -fx-text-fill: white; -fx-background-color: #f39c12;"
                    + " -fx-background-radius: 20; -fx-padding: 5 18;");

            // Mostrar timer, ocultar mensaje de mis pedidos
            vboxTimer.setVisible(true);
            vboxTimer.setManaged(true);
            vboxMensaje.setVisible(false);
            vboxMensaje.setManaged(false);
            btnVerPedidos.setVisible(false);
            btnVerPedidos.setManaged(false);

            iniciarTimer(20 * 60); // 20 minutos
        } else {
            // Badge amarillo para Pendiente
            lblEstadoPedido.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;"
                    + " -fx-text-fill: #4a3a2a; -fx-background-color: #f4c430;"
                    + " -fx-background-radius: 20; -fx-padding: 5 18;");

            vboxTimer.setVisible(false);
            vboxTimer.setManaged(false);
            vboxMensaje.setVisible(true);
            vboxMensaje.setManaged(true);
            btnVerPedidos.setVisible(true);
            btnVerPedidos.setManaged(true);
        }
    }

    private void iniciarTimer(int segundosTotales) {
        final int[] segundos = {segundosTotales};
        actualizarLabelTimer(segundos[0]);

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            segundos[0]--;
            if (segundos[0] < 0) {
                timeline.stop();
                lblTimer.setText("¡Listo!");
                lblTimer.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;"
                        + " -fx-text-fill: #4caf50; -fx-background-color: #e8f5e9;"
                        + " -fx-background-radius: 10; -fx-padding: 8 24;");
            } else {
                actualizarLabelTimer(segundos[0]);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void actualizarLabelTimer(int segundos) {
        int min = segundos / 60;
        int seg = segundos % 60;
        lblTimer.setText(String.format("%02d:%02d", min, seg));
    }

    @FXML
    private void handleVerPedidos() {
        detenerTimer();
        try {
            App.setRoot("mis_pedidos");
        } catch (IOException e) {
            mostrarError("No se pudo cargar Mis Pedidos.");
        }
    }

    @FXML
    private void handleVolverInicio() {
        detenerTimer();
        try {
            if (pedidoExpress) {
                App.modoExpress = false;
                App.setRoot("main_panaderia");
            } else {
                App.setRoot("bienvenida");
            }
        } catch (IOException e) {
            mostrarError("No se pudo volver al inicio.");
        }
    }

    private void detenerTimer() {
        if (timeline != null) timeline.stop();
    }

    private void mostrarError(String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
