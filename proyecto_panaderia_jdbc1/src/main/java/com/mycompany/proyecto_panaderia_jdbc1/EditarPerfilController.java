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

    @FXML
    private void initialize() {
        // Datos de ejemplo precargados
        txtNombreCompleto.setText("Juan García López");
        txtEdad.setText("28");
        txtCalle.setText("Av. Juárez");
        txtNumero.setText("142");
        txtColonia.setText("Centro");
        txtTelefonoCasa.setText("555-1234");
        txtTelefonoTrabajo.setText("555-5678");
    }

    @FXML
    private void handleMostrarContrasena() {
        if (chkMostrarContrasena.isSelected()) {
            contrasenaTexto = txtContrasena.getText().isEmpty() ? contrasenaTexto : txtContrasena.getText();
        }
    }

    @FXML
    private void handleAnadirTelefono() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Añadir teléfono");
        alert.setHeaderText(null);
        alert.setContentText("Funcionalidad para agregar un teléfono adicional.");
        alert.showAndWait();
    }

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

        javafx.scene.control.Button btnAceptar =
                (javafx.scene.control.Button) confirm.getDialogPane().lookupButton(ButtonType.OK);
        btnAceptar.setText("Aceptar");
        btnAceptar.setStyle("-fx-background-color: #3a2a1a; -fx-text-fill: white;"
                + " -fx-font-weight: bold; -fx-background-radius: 6;");

        javafx.scene.control.Button btnCancelar =
                (javafx.scene.control.Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL);
        btnCancelar.setText("Cancelar");
        btnCancelar.setStyle("-fx-background-radius: 6;");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            BienvenidaController.setNombreUsuario(txtNombreCompleto.getText().split(" ")[0]);
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

    @FXML
    private void handleDesactivarCuenta() {
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Desactivar cuenta");
        confirm.setHeaderText("¿Estás seguro?");
        confirm.setContentText("Esta acción desactivará tu cuenta. Podrás reactivarla iniciando sesión nuevamente.");

        javafx.scene.control.Button btnAceptar =
                (javafx.scene.control.Button) confirm.getDialogPane().lookupButton(ButtonType.OK);
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
