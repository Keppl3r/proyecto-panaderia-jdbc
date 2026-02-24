package com.mycompany.proyecto_panaderia_jdbc1;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;
import java.sql.Date;
import negocio.BOs.IClienteBO;
import negocio.excepciones.NegocioException;
import negocio.fabrica.FabricaBOs;
import persistencia.dominio.Cliente;

/**
 * Controlador para la gestión de registro de nuevos usuarios en la interfaz JavaFX.
 * <p>
 * Se encarga de capturar la información personal desde la vista, validar la 
 * obligatoriedad de los campos y procesar el alta mediante la capa de negocio.
 * </p>
 * @author Adrian Mendoza
 */
public class RegistroController {

    @FXML
    private TextField txtNombres;
    @FXML
    private TextField txtApellidoP;
    @FXML
    private TextField txtApellidoM;
    @FXML
    private TextField txtDomicilio;
    @FXML
    private DatePicker dateFechaNacimiento;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button btnEntrar;
    @FXML
    private Button btnVolver;

    /**
     * Inicializa la vista configurando efectos visuales y prompts de ayuda.
     */
    @FXML
    private void initialize() {
        addHoverEffect(btnEntrar);
        addHoverEffect(btnVolver);
        dateFechaNacimiento.setPromptText("dd/mm/aaaa");
    }

    /**
     * Procesa el formulario de registro.
     * <p>
     * Realiza una validación visual previa para asegurar que los campos críticos 
     * no estén vacíos. Si la validación pasa, delega el registro al objeto de negocio.
     * </p>
     */
    @FXML
    private void handleEntrar() {
        if (txtNombres.getText().trim().isEmpty() ||
                txtApellidoP.getText().trim().isEmpty() ||
                txtDomicilio.getText().trim().isEmpty() ||
                dateFechaNacimiento.getValue() == null ||
                txtUsername.getText().trim().isEmpty() ||
                txtPassword.getText().trim().isEmpty()) {

            mostrarAlerta(AlertType.WARNING, "Campos incompletos",
                    "Por favor complete todos los campos obligatorios.");
            return;
        }

        try {
            Cliente cliente = new Cliente();
            cliente.setNombres(txtNombres.getText().trim());
            cliente.setApellidoPaterno(txtApellidoP.getText().trim());
            cliente.setApellidoMaterno(txtApellidoM.getText().trim());
            cliente.setCalle(txtDomicilio.getText().trim());
            cliente.setFechaNacimiento(Date.valueOf(dateFechaNacimiento.getValue()));
            cliente.setUsername(txtUsername.getText().trim());
            cliente.setPassword(txtPassword.getText().trim());

            IClienteBO clienteBO = FabricaBOs.obtenerClienteBO();
            clienteBO.registrarCliente(cliente);

            mostrarAlerta(AlertType.INFORMATION, "Registro Exitoso",
                    "Cuenta creada correctamente. Usuario: " + cliente.getUsername());

            App.setRoot("main_panaderia");

        } catch (NegocioException ex) {
            mostrarAlerta(AlertType.ERROR, "Error en registro", ex.getMessage());
        } catch (IOException ex) {
            mostrarAlerta(AlertType.ERROR, "Error de Navegación", "No se pudo cargar la pantalla principal.");
        }
    }

    /**
     * Gestiona el retorno a la pantalla principal.
     */
    @FXML
    private void handleVolver() {
        try {
            App.setRoot("main_panaderia");
        } catch (IOException e) {
            mostrarAlerta(AlertType.ERROR, "Error", "Fallo al intentar volver al menú.");
            e.printStackTrace();
        }
    }

    /**
     * Despliega un cuadro de diálogo configurable para interactuar con el usuario.
     * * @param tipo     Tipo de alerta (ERROR, INFORMATION, WARNING, etc).
     * @param titulo   Título de la ventana de diálogo.
     * @param contenido Mensaje descriptivo del error o notificación.
     */
    private void mostrarAlerta(AlertType tipo, String titulo, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    /**
     * Aplica efectos visuales dinámicos a los botones cuando el cursor entra o sale.
     * <p>
     * Utiliza expresiones lambda para gestionar los eventos de mouse, permitiendo
     * una interfaz reactiva sin dependencia estricta de CSS externo.
     * </p>
     * @param button El componente Button al que se le aplicará la lógica.
     */
    private void addHoverEffect(Button button) {
        String originalStyle = button.getStyle();
        button.setOnMouseEntered(e -> {
            if (button == btnEntrar) {
                button.setStyle(originalStyle + "; -fx-scale-x: 1.05; -fx-scale-y: 1.05;");
            } else {
                button.setStyle(originalStyle + "; -fx-text-fill: #8b6f47;");
            }
        });
        button.setOnMouseExited(e -> button.setStyle(originalStyle));
    }
}