package com.mycompany.proyecto_panaderia_jdbc1;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;
import negocio.BOs.IClienteBO;
import negocio.BOs.IUsuarioBO;
import negocio.excepciones.NegocioException;
import negocio.fabrica.FabricaBOs;
import persistencia.dominio.Cliente;
import persistencia.dominio.Usuario;

public class LoginController {

    @FXML
    private TextField txtNombre;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnEntrar;

    @FXML
    private Button btnVolver;

    @FXML
    private void handleEntrar() {
        String username = txtNombre.getText().trim();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Campos vacíos",
                    "Por favor complete todos los campos",
                    "Debe ingresar su usuario y contraseña.");
            return;
        }

        try {
            IUsuarioBO usuarioBO = FabricaBOs.obtenerUsuarioBO();
            Usuario usuario = usuarioBO.autenticar(username, password);

            if (usuario.esCliente()) {
                IClienteBO clienteBO = FabricaBOs.obtenerClienteBO();
                Cliente cliente = clienteBO.obtenerClientePorId(usuario.getIdUsuario());
                SesionActual.iniciarSesion(usuario, cliente);
                App.setRoot("bienvenida");

            } else if (usuario.esEmpleado()) {
                SesionActual.iniciarSesion(usuario, null);
                App.setRoot("menu_empleado");

            } else {
                mostrarAlerta(AlertType.ERROR, "Acceso denegado",
                        "Rol no reconocido",
                        "Tu cuenta no tiene un rol válido. Contacta al administrador.");
            }

        } catch (NegocioException ex) {
            mostrarAlerta(AlertType.ERROR, "Error de autenticación",
                    "Credenciales incorrectas",
                    ex.getMessage());
            txtPassword.clear();
            txtPassword.requestFocus();

        } catch (IOException ex) {
            ex.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Error de navegación",
                    "No se pudo cargar la pantalla",
                    ex.getMessage());
        }
    }

    @FXML
    private void handleVolver() {
        try {
            App.setRoot("main_panaderia");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        addHoverEffect(btnEntrar);
        addHoverEffect(btnVolver);
        txtNombre.setOnAction(e -> txtPassword.requestFocus());
        txtPassword.setOnAction(e -> handleEntrar());
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String encabezado, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    private void addHoverEffect(Button button) {
        String originalStyle = button.getStyle();
        button.setOnMouseEntered(e -> {
            if (button == btnEntrar) {
                button.setStyle(originalStyle + "; -fx-scale-x: 1.05; -fx-scale-y: 1.05; -fx-opacity: 0.9;");
            } else {
                button.setStyle(originalStyle + "; -fx-text-fill: #8b6f47;");
            }
        });
        button.setOnMouseExited(e -> button.setStyle(originalStyle));
    }
}
