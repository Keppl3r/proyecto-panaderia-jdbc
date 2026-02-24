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

/**
 * Controlador para la pantalla de confirmación de pedido.
 * Esta vista informa al usuario sobre el éxito de su transacción y proporciona
 * herramientas de seguimiento: un temporizador para clientes express o acceso 
 * al historial para clientes registrados.
 */
public class PedidoConfirmadoController {

    @FXML private Label  lblNumeroPedido;
    @FXML private Label  lblEstadoPedido;
    @FXML private VBox   vboxTimer;
    @FXML private Label  lblTimer;
    @FXML private VBox   vboxMensaje;
    @FXML private Button btnVerPedidos;

    private static String  numeroPedido  = "#001";
    private static String  estadoPedido  = "Pendiente";
    private static boolean pedidoExpress = false;
    private static String  folioExpress  = null;
    private static String  pinExpress    = null;

    @FXML private Label lblFolioPin;

    private Timeline timeline; // Motor del temporizador
    
    /**
     * Configura los datos básicos del pedido antes de cargar la vista.
     */
    public static void setDatosPedido(String numero, String estado, boolean express) {
        numeroPedido  = numero;
        estadoPedido  = estado;
        pedidoExpress = express;
    }
    /**
     * Inyecta credenciales temporales para pedidos sin cuenta (Modo Express).
     */
    public static void setInfoExpress(String folio, String pin) {
        folioExpress = folio;
        pinExpress   = pin;
    }
    /**
     * Inicializa la vista y alterna entre el diseño "Express" (Temporizador y PIN)
     * y el diseño "Cliente" (Mensaje de éxito y acceso a historial).
     */
    @FXML
    private void initialize() {
        lblNumeroPedido.setText(numeroPedido);
        lblEstadoPedido.setText(estadoPedido);

        if (pedidoExpress) {
            lblEstadoPedido.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;"
                    + " -fx-text-fill: white; -fx-background-color: #f39c12;"
                    + " -fx-background-radius: 20; -fx-padding: 5 18;");

            vboxTimer.setVisible(true);
            vboxTimer.setManaged(true);
            vboxMensaje.setVisible(false);
            vboxMensaje.setManaged(false);
            btnVerPedidos.setVisible(false);
            btnVerPedidos.setManaged(false);

            if (lblFolioPin != null && folioExpress != null) {
                lblFolioPin.setText("Folio: " + folioExpress + "  |  PIN: " + pinExpress);
                lblFolioPin.setVisible(true);
                lblFolioPin.setManaged(true);
            }

            iniciarTimer(20 * 60);
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
    
    /**
     * Implementa un contador regresivo reactivo usando la clase Timeline.
     * @param segundosTotales Duración del contador.
     */
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
    /**
     * Formatea y actualiza la visualización del tiempo restante.
     * Convierte los segundos brutos a un formato de reloj mm:ss.
     * @param segundos Cantidad de segundos a formatear.
     */
    private void actualizarLabelTimer(int segundos) {
        int min = segundos / 60;
        int seg = segundos % 60;
        lblTimer.setText(String.format("%02d:%02d", min, seg));
    }
    /**
    * activo (como el temporizador de entrega) para liberar recursos.
     * * @throws IOException Si el cargador de FXML no logra localizar o parsear 
     * el archivo "mis_pedidos.fxml".
     */
    @FXML
    private void handleVerPedidos() {
        detenerTimer();
        try {
            App.setRoot("mis_pedidos");
        } catch (IOException e) {
            mostrarError("No se pudo cargar Mis Pedidos.");
        }
    }
    /**
     * Gestiona el retorno a la navegación principal.
     * Si el usuario era "Express", limpia el estado global de la aplicación
     * antes de enviarlo al menú raíz.
     */
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
    /**
     * Detiene el proceso del temporizador.
     * Es crucial invocar este método antes de cualquier cambio de escena para
     * evitar fugas de memoria y procesos huérfanos en el JavaFX Application Thread.
     */
    private void detenerTimer() {
        if (timeline != null) timeline.stop();
    }
    /**
     * Despliega una ventana emergente de error (Modal).
     * Este método estandariza la comunicación de fallos críticos, como errores de 
     * lectura de archivos FXML o excepciones de la base de datos, asegurando que 
     * el hilo de la interfaz de usuario permanezca estable.
     * * @param msg Mensaje descriptivo del error que se presentará al usuario.
     */
    private void mostrarError(String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
