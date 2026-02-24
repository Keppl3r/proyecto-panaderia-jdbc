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
            mostrarAlerta(AlertType.ERROR, "Error", "No se pudo cargar la pantalla principal.");
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
        dateFechaNacimiento.setPromptText("dd/mm/aaaa");
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

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
