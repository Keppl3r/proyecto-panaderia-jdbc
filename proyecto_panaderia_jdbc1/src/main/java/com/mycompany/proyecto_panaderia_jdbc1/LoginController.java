package com.mycompany.proyecto_panaderia_jdbc1;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;

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
        String nombre = txtNombre.getText();
        String password = txtPassword.getText();

        // Validar que los campos no estén vacíos
        if (nombre.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Campos vacíos");
            alert.setHeaderText("Por favor complete todos los campos");
            alert.setContentText("Debe ingresar su nombre de usuario y contraseña.");
            alert.showAndWait();
            return;
        }

        // Aquí implementarías la lógica de autenticación con la base de datos
        System.out.println("Intento de inicio de sesión:");
        System.out.println("Usuario: " + nombre);
        System.out.println("Contraseña: " + password);

        // Simulación de validación (reemplazar con lógica real de BD)
        if (validarCredenciales(nombre, password)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Inicio de Sesión Exitoso");
            alert.setHeaderText("¡Bienvenido!");
            alert.setContentText("Has iniciado sesión correctamente, " + nombre + ".");
            alert.showAndWait();

            // Aquí podrías navegar a la pantalla principal del usuario autenticado
            // Por ejemplo: App.setRoot("dashboard");
            
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error de autenticación");
            alert.setHeaderText("Credenciales incorrectas");
            alert.setContentText("El nombre de usuario o la contraseña son incorrectos.\nPor favor, intente nuevamente.");
            alert.showAndWait();
            
            // Limpiar el campo de contraseña
            txtPassword.clear();
        }
    }

    /**
     * Método temporal para validar credenciales
     * Reemplazar con consulta a base de datos
     */
    private boolean validarCredenciales(String nombre, String password) {
        // Simulación simple - REEMPLAZAR CON CONSULTA A BD
        // Por ahora acepta cualquier usuario con contraseña "123456"
        return password.equals("123456");
    }

    @FXML
    private void handleVolver() {
        try {
            App.setRoot("main_panaderia");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al volver");
            alert.setContentText("No se pudo regresar a la pantalla principal.");
            alert.showAndWait();
        }
    }

    @FXML
    private void initialize() {
        System.out.println("Pantalla de Inicio de Sesión cargada");
        
        // Agregar efectos hover a los botones
        addHoverEffect(btnEntrar);
        addHoverEffect(btnVolver);
        
        // Configurar acción Enter en campos de texto
        txtNombre.setOnAction(e -> txtPassword.requestFocus());
        txtPassword.setOnAction(e -> handleEntrar());
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
        
        button.setOnMouseExited(e -> {
            button.setStyle(originalStyle);
        });
    }
}


