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
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import negocio.BOs.IClienteBO;
import negocio.BOs.IUsuarioBO;
import negocio.excepciones.NegocioException;
import negocio.fabrica.FabricaBOs;
import persistencia.dominio.Cliente;
import persistencia.dominio.Telefono;

/**
 * Controlador para la edición del perfil de usuario. Gestiona la carga de datos
 * del cliente desde la sesión activa, la validación de campos personales
 * (dirección, teléfonos, edad) y la actualización de la cuenta.
 */
public class EditarPerfilController {

    @FXML private TextField txtNombreCompleto;
    @FXML private DatePicker dateFechaNacimiento;
    @FXML private TextField txtEdad;
    @FXML private TextField txtCalle;
    @FXML private TextField txtNumero;
    @FXML private TextField txtColonia;
    @FXML private TextField txtTelefonoCasa;
    @FXML private TextField txtTelefonoTrabajo;
    @FXML private PasswordField txtContrasena;
    @FXML private CheckBox chkMostrarContrasena;

    private IClienteBO clienteBO;
    private IUsuarioBO usuarioBO;

    /**
     * Inicializa el controlador cargando la información del cliente almacenada
     * en SesionActual.
     */
    @FXML
    private void initialize() {
        clienteBO = FabricaBOs.obtenerClienteBO();
        usuarioBO = FabricaBOs.obtenerUsuarioBO();
        cargarDatosCliente();
    }

    /**
     * Recupera el objeto Cliente de la sesión y rellena los campos del
     * formulario. Calcula automáticamente la edad basada en la fecha de
     * nacimiento y concatena el nombre completo para su visualización.
     */
    private void cargarDatosCliente() {
        Cliente cliente = SesionActual.getCliente();
        if (cliente == null) {
            txtNombreCompleto.setText("Sin sesión");
            return;
        }

        String nombreCompleto = cliente.getNombres() + " "
                + cliente.getApellidoPaterno() + " "
                + cliente.getApellidoMaterno();
        txtNombreCompleto.setText(nombreCompleto.trim());

        if (cliente.getFechaNacimiento() != null) {
            LocalDate fechaNac = cliente.getFechaNacimiento().toLocalDate();
            dateFechaNacimiento.setValue(fechaNac);
            int edad = Period.between(fechaNac, LocalDate.now()).getYears();
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

        cargarTelefonos(cliente.getIdUsuario());
    }

    /**
     * Carga los teléfonos del cliente desde la base de datos y los muestra en los campos correspondientes.
     * @param idUsuario ID del cliente cuyos teléfonos se cargarán.
     */
    private void cargarTelefonos(int idUsuario) {
        try {
            List<Telefono> telefonos = clienteBO.obtenerTelefonos(idUsuario);
            for (Telefono t : telefonos) {
                String etiqueta = t.getEtiqueta() != null ? t.getEtiqueta().toUpperCase() : "";
                switch (etiqueta) {
                    case "CASA"    -> txtTelefonoCasa.setText(t.getNumero());
                    case "TRABAJO" -> txtTelefonoTrabajo.setText(t.getNumero());
                }
            }
        } catch (NegocioException e) {
            mostrarAlerta(AlertType.WARNING, "Teléfonos", "No se pudieron cargar los teléfonos: " + e.getMessage());
        }
    }

    /**
     * Lógica para la visibilidad de la contraseña.
     * El PasswordField de JavaFX siempre oculta el texto, por lo que este método no requiere acción.
     */
    @FXML
    private void handleMostrarContrasena() {
        // No hay acción especial necesaria; el campo PasswordField siempre oculta el texto
    }

    @FXML
    private void handleAnadirTelefono() {
        mostrarAlerta(AlertType.INFORMATION, "Añadir teléfono",
                "Edita los campos de teléfono (Casa / Trabajo) y presiona 'Confirmar cambios'.");
    }

    @FXML
    private void handleRegresar() {
        try {
            App.setRoot("bienvenida");
        } catch (IOException e) {
            mostrarAlerta(AlertType.ERROR, "Error", "No se pudo volver a la pantalla anterior.");
        }
    }

    /**
     * Valida y procesa la actualización de los datos del perfil. Muestra un
     * diálogo de confirmación antes de proceder y redirige a la pantalla de
     * bienvenida tras el éxito.
     */
    @FXML
    private void handleConfirmarCambios() {
        if (txtNombreCompleto.getText().isBlank()) {
            mostrarAlerta(AlertType.WARNING, "Campo requerido", "El nombre completo es obligatorio.");
            return;
        }

        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Guardar cambios");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Deseas guardar los cambios realizados?");

        // Estilización de botones de confirmación
        javafx.scene.control.Button btnAceptar = (javafx.scene.control.Button) confirm.getDialogPane().lookupButton(ButtonType.OK);
        btnAceptar.setText("Aceptar");
        btnAceptar.setStyle("-fx-background-color: #3a2a1a; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6;");

        javafx.scene.control.Button btnCancelar = (javafx.scene.control.Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL);
        btnCancelar.setText("Cancelar");
        btnCancelar.setStyle("-fx-background-radius: 6;");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Cliente cliente = SesionActual.getCliente();
                if (cliente == null) {
                    mostrarAlerta(AlertType.ERROR, "Error", "No hay sesión activa.");
                    return;
                }

                // Parseo simple del nombre completo
                String[] partes = txtNombreCompleto.getText().trim().split("\\s+");
                cliente.setNombres(partes.length > 0 ? partes[0] : "");
                cliente.setApellidoPaterno(partes.length > 1 ? partes[1] : "");
                cliente.setApellidoMaterno(partes.length > 2 ? partes[2] : "");

                if (dateFechaNacimiento.getValue() != null) {
                    cliente.setFechaNacimiento(Date.valueOf(dateFechaNacimiento.getValue()));
                }
                cliente.setCalle(txtCalle.getText().trim());
                cliente.setNumero(txtNumero.getText().trim());
                cliente.setColonia(txtColonia.getText().trim());

                clienteBO.actualizarCliente(cliente);

                List<Telefono> telefonos = new ArrayList<>();
                String numCasa = txtTelefonoCasa.getText().trim();
                String numTrabajo = txtTelefonoTrabajo.getText().trim();
                if (!numCasa.isBlank()) {
                    telefonos.add(new Telefono(cliente.getIdUsuario(), "CASA", numCasa));
                }
                if (!numTrabajo.isBlank()) {
                    telefonos.add(new Telefono(cliente.getIdUsuario(), "TRABAJO", numTrabajo));
                }
                clienteBO.actualizarTelefonos(cliente.getIdUsuario(), telefonos);

                String nuevaContrasena = txtContrasena.getText();
                if (!nuevaContrasena.isBlank()) {
                    usuarioBO.actualizarPassword(SesionActual.getIdUsuario(), nuevaContrasena);
                }

                mostrarAlerta(AlertType.INFORMATION, "Cambios guardados",
                        "Tu perfil ha sido actualizado correctamente.");

                App.setRoot("bienvenida");

            } catch (NegocioException ex) {
                mostrarAlerta(AlertType.ERROR, "Error al guardar", ex.getMessage());
            } catch (IOException ex) {
                mostrarAlerta(AlertType.ERROR, "Error", "No se pudo cargar la pantalla.");
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
        confirm.setContentText("Tu cuenta será desactivada. ¿Deseas continuar?");

        javafx.scene.control.Button btnAceptar = (javafx.scene.control.Button) confirm.getDialogPane().lookupButton(ButtonType.OK);
        btnAceptar.setText("Desactivar");
        btnAceptar.setStyle("-fx-background-color: #e05a8a; -fx-text-fill: white;"
                + " -fx-font-weight: bold; -fx-background-radius: 6;");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Cliente cliente = SesionActual.getCliente();
                if (cliente == null) {
                    mostrarAlerta(AlertType.ERROR, "Error", "No hay sesión activa.");
                    return;
                }

                clienteBO.desactivarCliente(cliente.getIdUsuario());

                SesionActual.cerrarSesion();
                App.setRoot("main_panaderia");

            } catch (NegocioException ex) {
                mostrarAlerta(AlertType.ERROR, "Error al desactivar", ex.getMessage());
            } catch (IOException ex) {
                mostrarAlerta(AlertType.ERROR, "Error", "No se pudo cargar la pantalla principal.");
            }
        }
    }

    /**
     * Despliega una alerta genérica configurable.
     * @param tipo Tipo de alerta (Error, Warning, Info).
     * @param titulo Título de la ventana.
     * @param contenido Mensaje detallado.
     */
    private void mostrarAlerta(AlertType tipo, String titulo, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
