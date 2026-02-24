package com.mycompany.proyecto_panaderia_jdbc1;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import negocio.BOs.IPedidoBO;
import negocio.excepciones.NegocioException;
import negocio.fabrica.FabricaBOs;
import persistencia.dominio.DetallePedido;
import persistencia.dominio.Pedido;

/**
 * Controlador para la vista de historial de pedidos del cliente.
 * Gestiona la carga dinámica de tarjetas de pedido, el filtrado por estado
 * y la lógica de cancelación de pedidos pendientes.
 */
public class MisPedidosController {

    @FXML private GridPane gridPedidos;
    @FXML private ComboBox<String> cmbTipoPedido;
    @FXML private DatePicker dateDesde;
    @FXML private DatePicker dateHasta;

    private List<Pedido> pedidosBD;
    private IPedidoBO pedidoBO;

    /**
     * Inicializa los componentes de la vista, configura el ComboBox de estados
     * y dispara la carga inicial de datos desde la base de datos.
     */
    @FXML
    private void initialize() {
        cmbTipoPedido.getItems().addAll("Todos", "Listo", "Pendiente", "Entregado", "Cancelado");
        cmbTipoPedido.setValue("Todos");

        pedidoBO = FabricaBOs.obtenerPedidoBO();
        cargarPedidos();
    }

    /**
     * Recupera el historial de pedidos del cliente en sesión.
     * Valida la existencia de una sesión activa antes de realizar la consulta al BO.
     */
    private void cargarPedidos() {
        if (!SesionActual.isLogeado() || SesionActual.getCliente() == null) {
            mostrarError("No hay sesión activa de cliente.");
            return;
        }

        try {
            int idCliente = SesionActual.getCliente().getIdUsuario();
            pedidosBD = pedidoBO.obtenerHistorial(idCliente);
            renderizarPedidos(pedidosBD);
        } catch (NegocioException e) {
            e.printStackTrace();
            mostrarError("No se pudieron cargar los pedidos: " + e.getMessage());
        }
    }
    
    /**
     * Construye la interfaz visual en un formato de cuadrícula (Grid).
     * Si la lista está vacía, muestra un mensaje informativo de cortesía.
     * @param lista Lista de pedidos a transformar en componentes visuales.
     */
    private void renderizarPedidos(List<Pedido> lista) {
        gridPedidos.getChildren().clear();
        if (lista == null || lista.isEmpty()) {
            Label vacio = new Label("No tienes pedidos aún.");
            vacio.setStyle("-fx-font-size: 14px; -fx-text-fill: #6b5644;");
            gridPedidos.add(vacio, 0, 0);
            return;
        }

        int col = 0, row = 0;
        for (Pedido p : lista) {
            gridPedidos.add(crearTarjetaPedido(p), col, row);
            col++;
            if (col >= 2) { col = 0; row++; }
        }
    }
    /**
     * Genera un componente VBox personalizado (Tarjeta) para cada pedido.
     * Incluye lógica condicional para mostrar el botón de cancelación solo 
     * en pedidos con estado "PENDIENTE".
     * @param p Objeto Pedido con la información a mostrar.
     * @return El nodo visual de la tarjeta configurado.
     */
    private VBox crearTarjetaPedido(Pedido p) {
        VBox tarjeta = new VBox(8);
        tarjeta.setPrefWidth(310.0);
        tarjeta.setPadding(new Insets(14, 16, 14, 16));
        tarjeta.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                + " -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 6, 0, 0, 2);");

        HBox encabezado = new HBox(10);
        encabezado.setAlignment(Pos.CENTER_LEFT);
        Label lblNumero = new Label("Pedido #" + p.getNumPedido());
        lblNumero.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3a2a1a;");
        Label badge = crearBadge(p.getEstado().getDescripcion());
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        encabezado.getChildren().addAll(lblNumero, spacer, badge);

        HBox filaFecha = new HBox(6);
        filaFecha.setAlignment(Pos.CENTER_LEFT);
        Label iconFecha = new Label("📅");
        iconFecha.setStyle("-fx-font-size: 11px;");
        String fechaStr = p.getFechaRegistro() != null
                ? p.getFechaRegistro().toLocalDateTime().toLocalDate().toString() : "—";
        Label lblFecha = new Label(fechaStr);
        lblFecha.setStyle("-fx-font-size: 12px; -fx-text-fill: #6b5644;");
        filaFecha.getChildren().addAll(iconFecha, lblFecha);

        VBox itemsBox = new VBox(3);
        List<DetallePedido> detalles = cargarDetalles(p.getIdPedido());
        if (detalles.isEmpty()) {
            Label sinItems = new Label("Sin productos registrados.");
            sinItems.setStyle("-fx-font-size: 12px; -fx-text-fill: #9e9e9e;");
            itemsBox.getChildren().add(sinItems);
        } else {
            for (DetallePedido d : detalles) {
                HBox fila = new HBox(6);
                Label punto = new Label("●");
                punto.setStyle("-fx-font-size: 8px; -fx-text-fill: #4a3a2a;");
                String nombreProducto = d.getProducto() != null ? d.getProducto().getNombre() : "Producto";
                Label lbl = new Label(nombreProducto + " x" + d.getCantidad());
                lbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #3a2a1a;");
                fila.setAlignment(Pos.CENTER_LEFT);
                fila.getChildren().addAll(punto, lbl);
                itemsBox.getChildren().add(fila);
            }
        }

        Region sep = new Region();
        sep.setPrefHeight(1);
        sep.setStyle("-fx-background-color: #e0d0b0;");

        HBox filaTotal = new HBox();
        filaTotal.setAlignment(Pos.CENTER_LEFT);
        Label lblTotal = new Label(String.format("Total:  $%.2f", p.getTotal()));
        lblTotal.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #3a2a1a;");
        filaTotal.getChildren().add(lblTotal);

        tarjeta.getChildren().addAll(encabezado, filaFecha, itemsBox, sep, filaTotal);

        if (p.getEstado() == Pedido.EstadoPedido.PENDIENTE) {
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
    
    /**
     * Consulta los ítems específicos de un pedido.
     * Se utiliza durante la construcción de la interfaz para desglosar qué productos 
     * componen cada orden en el historial.
     * * @param idPedido Identificador único de la orden.
     * @return Lista de objetos DetallePedido o una lista vacía en caso de error.
     */
    private List<DetallePedido> cargarDetalles(int idPedido) {
        try {
            return pedidoBO.obtenerDetallesPorPedido(idPedido);
        } catch (NegocioException e) {
            return List.of();
        }
    }
    
    /**
     * Crea un componente visual (Badge) basado en el estado del pedido.
     * Utiliza estilos CSS de JavaFX para generar una etiqueta redondeada 
     * con colores que facilitan la lectura rápida del estado actual.
     * * @param estado El texto descriptivo del estado (ej. "Listo", "Pendiente").
     * @return Un objeto Label configurado estéticamente.
     */
    private Label crearBadge(String estado) {
        Label badge = new Label(estado);
        badge.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;"
                + " -fx-background-radius: 20; -fx-padding: 3 12; -fx-text-fill: white;"
                + " -fx-background-color: " + colorEstado(estado) + ";");
        return badge;
    }
    /**
     * Define la paleta de colores semántica según el flujo de vida del pedido.
     * Implementa una estructura switch moderna para mapear estados a valores hexadecimales.
     * * @param estado Cadena de texto que representa el estado.
     * @return Código de color hexadecimal (String).
     */
    private String colorEstado(String estado) {
        return switch (estado) {
            case "Listo"        -> "#4caf50";
            case "Pendiente"    -> "#2196f3";
            case "Entregado"    -> "#4caf50";
            case "Cancelado"    -> "#9e9e9e";
            case "No Entregado" -> "#e05a5a";
            default             -> "#757575";
        };
    }
    /**
     * Gestiona el proceso de cancelación de una orden.
     * Incluye una confirmación modal para evitar cancelaciones accidentales y
     * estiliza el botón de acción para denotar una operación de "peligro" o crítica.
     * * @param p El objeto Pedido a cancelar.
     */
    private void confirmarCancelacion(Pedido p) {
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Cancelar pedido");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Estás seguro de que deseas cancelar el pedido #" + p.getNumPedido() + "?");

        Button btnOK = (Button) confirm.getDialogPane().lookupButton(ButtonType.OK);
        btnOK.setText("Cancelar pedido");
        btnOK.setStyle("-fx-background-color: #e05a5a; -fx-text-fill: white; -fx-font-weight: bold;");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                pedidoBO.cancelarPedido(p.getIdPedido());
                cargarPedidos();
            } catch (NegocioException e) {
                mostrarError("No se pudo cancelar: " + e.getMessage());
            }
        }
    }
    /**
     * Aplica el filtrado de la lista en memoria (Stream API) basado en la selección
     * del ComboBox, evitando nuevas peticiones a la base de datos.
     */
    @FXML
    private void handleAplicarFiltros() {
        if (pedidosBD == null) return;

        String filtroEstado = cmbTipoPedido.getValue();
        LocalDate desde = dateDesde.getValue();
        LocalDate hasta = dateHasta.getValue();

        if (desde != null && hasta != null && desde.isAfter(hasta)) {
            mostrarError("La fecha 'Desde' no puede ser mayor que la fecha 'Hasta'.");
            return;
        }

        List<Pedido> filtrados = pedidosBD.stream()
                .filter(p -> {
                    if (filtroEstado != null && !"Todos".equals(filtroEstado)) {
                        if (!p.getEstado().getDescripcion().equals(filtroEstado)) return false;
                    }
                    if (p.getFechaRegistro() != null) {
                        LocalDate fechaPedido = p.getFechaRegistro().toLocalDateTime().toLocalDate();
                        if (desde != null && fechaPedido.isBefore(desde)) return false;
                        if (hasta != null && fechaPedido.isAfter(hasta)) return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());

        renderizarPedidos(filtrados);
    }

    @FXML
    private void handleLimpiarFiltros() {
        cmbTipoPedido.setValue("Todos");
        dateDesde.setValue(null);
        dateHasta.setValue(null);
        if (pedidosBD != null) renderizarPedidos(pedidosBD);
    }
    /**
     * Gestiona el retorno a la pantalla de bienvenida del cliente.
     * Este método permite al usuario salir de la vista de historial y volver 
     * a su panel de control principal.
     */
    @FXML
    private void handleRegresar() {
        try {
            App.setRoot("bienvenida");
        } catch (IOException e) {
            mostrarError("No se pudo volver.");
        }
    }
    /**
     * Centraliza la visualización de mensajes de error de tipo modal.
     * Utiliza el componente Alert de JavaFX para detener el flujo de la aplicación
     * hasta que el usuario reconozca el fallo (ej. errores de conexión o sesión).
     * * @param msg Mensaje descriptivo del error a mostrar en el cuerpo de la alerta.
     */
    private void mostrarError(String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error"); alert.setHeaderText(null);
        alert.setContentText(msg); alert.showAndWait();
    }
}
