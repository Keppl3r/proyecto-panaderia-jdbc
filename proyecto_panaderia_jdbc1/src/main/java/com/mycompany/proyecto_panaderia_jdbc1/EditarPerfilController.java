package com.mycompany.proyecto_panaderia_jdbc1;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.util.Optional;

/**
 * Controlador para la edición del perfil de usuario. Gestiona la carga de datos
 * del cliente desde la sesión activa, la validación de campos personales
 * (dirección, teléfonos, edad) y la actualización de la cuenta.
 */
public class EditarPerfilController {

    @FXML
    private TextField txtNombreCompleto;

    @FXML
    private DatePicker dateFechaNacimiento;

    @FXML
    private TextField txtEdad;

    @FXML
    private TextField txtCalle;

    @FXML
    private TextField txtNumero;

    @FXML
    private TextField txtColonia;

    @FXML
    private TextField txtTelefonoCasa;

    @FXML
    private TextField txtTelefonoTrabajo;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private CheckBox chkMostrarContrasena;

    private String contrasenaTexto = "";

    /**
     * Inicializa el controlador cargando la información del cliente almacenada
     * en SesionActual.
     */
    @FXML
    private void initialize() {
        cargarDatosCliente();
    }

    /**
     * Recupera el objeto Cliente de la sesión y rellena los campos del
     * formulario. Calcula automáticamente la edad basada en la fecha de
     * nacimiento y concatena el nombre completo para su visualización.
     */
    private void cargarDatosCliente() {
        persistencia.dominio.Cliente cliente = SesionActual.getCliente();
        if (cliente == null) {
            txtNombreCompleto.setText("Sin sesión");
            return;
        }

        String nombreCompleto = cliente.getNombres() + " "
                + cliente.getApellidoPaterno() + " "
                + cliente.getApellidoMaterno();
        txtNombreCompleto.setText(nombreCompleto.trim());

        if (cliente.getFechaNacimiento() != null) {
            dateFechaNacimiento.setValue(cliente.getFechaNacimiento().toLocalDate());
            int edad = java.time.LocalDate.now().getYear() - cliente.getFechaNacimiento().toLocalDate().getYear();
            txtEdad.setText(String.valueOf(edad));
        }

        if (cliente.getCalle() != null) {
            txtCalle.setText(cliente.getCalle());
        }
        if (cliente.getNumero() != null) {
            txtNumero.setText(cliente.getNumero());
        }
        if (cliente.getColonia() != null) {
            txtColonia.setText(cliente.getColonia());
        }
    }

    @FXML
    private void handleMostrarContrasena() {
        if (chkMostrarContrasena.isSelected()) {
            contrasenaTexto = txtContrasena.getText().isEmpty() ? contrasenaTexto : txtContrasena.getText();
        }
    }

    /**
     * Lógica para alternar la visibilidad de la contraseña. Nota: En esta
     * versión se captura el texto para una posible transición a TextField.
     */
    @FXML
    private void handleAnadirTelefono() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Añadir teléfono");
        alert.setHeaderText(null);
        alert.setContentText("Funcionalidad para agregar un teléfono adicional.");
        alert.showAndWait();
    }

    /**
     * Valida y procesa la actualización de los datos del perfil. Muestra un
     * diálogo de confirmación antes de proceder y redirige a la pantalla de
     * bienvenida tras el éxito.
     */
    @FXML
    private void handleConfirmarCambios() {
        if (txtNombreCompleto.getText().isBlank()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Campo requerido");
            alert.setHeaderText(null);
            alert.setContentText("El nombre completo es obligatorio.");
            alert.showAndWait();
            return;
        }

        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Guardar cambios");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Deseas guardar los cambios realizados?");

        javafx.scene.control.Button btnAceptar = (javafx.scene.control.Button) confirm.getDialogPane().lookupButton(ButtonType.OK);
        btnAceptar.setText("Aceptar");
        btnAceptar.setStyle("-fx-background-color: #3a2a1a; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6;");

        javafx.scene.control.Button btnCancelar = (javafx.scene.control.Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL);
        btnCancelar.setText("Cancelar");
        btnCancelar.setStyle("-fx-background-radius: 6;");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Alert exito = new Alert(AlertType.INFORMATION);
            exito.setTitle("Cambios guardados");
            exito.setHeaderText(null);
            exito.setContentText("Tu perfil ha sido actualizado correctamente.");
            exito.showAndWait();

            try {
                App.setRoot("bienvenida");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Permite al usuario desactivar su cuenta de forma lógica. Redirige al
     * inicio de la aplicación (Modo Express/Main).
     */
    @FXML
    private void handleDesactivarCuenta() {
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Desactivar cuenta");
        confirm.setHeaderText("¿Estás seguro?");
        confirm.setContentText("Esta acción desactivará tu cuenta. Podrás reactivarla iniciando sesión nuevamente.");

        javafx.scene.control.Button btnAceptar
                = (javafx.scene.control.Button) confirm.getDialogPane().lookupButton(ButtonType.OK);
        btnAceptar.setText("Desactivar");
        btnAceptar.setStyle("-fx-background-color: #e05a8a; -fx-text-fill: white;"
                + " -fx-font-weight: bold; -fx-background-radius: 6;");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                App.setRoot("main_panaderia");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
