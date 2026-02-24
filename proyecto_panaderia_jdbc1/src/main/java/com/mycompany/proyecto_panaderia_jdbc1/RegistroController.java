package com.mycompany.proyecto_panaderia_jdbc1;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Controlador para la gestión de registro de nuevos usuarios.
 * Se encarga de capturar la información personal, validar la obligatoriedad 
 * de los campos y procesar el alta en el sistema.
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
    private TextField txtApellidoP2;

    @FXML
    private TextField txtApellidoM2;

    @FXML
    private Button btnEntrar;

    @FXML
    private Button btnVolver;
    /**
     * Procesa el formulario de registro.
     * Realiza una validación previa para asegurar que los campos críticos 
     * (Nombres, Apellido Paterno, Domicilio y Fecha) no estén vacíos.
     */
    @FXML
    private void handleEntrar() {
        // Validar campos
        if (txtNombres.getText().isEmpty() || 
            txtApellidoP.getText().isEmpty() || 
            txtDomicilio.getText().isEmpty() || 
            dateFechaNacimiento.getValue() == null) {
            
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Campos incompletos");
            alert.setHeaderText("Por favor complete los campos obligatorios");
            alert.setContentText("Nombres, Apellido paterno, Domicilio y Fecha de nacimiento son obligatorios.");
            alert.showAndWait();
            return;
        }

        // Aquí implementarías la lógica de registro en la base de datos
        String nombres = txtNombres.getText();
        String apellidoP = txtApellidoP.getText();
        String apellidoM = txtApellidoM.getText();
        String domicilio = txtDomicilio.getText();
        LocalDate fechaNacimiento = dateFechaNacimiento.getValue();

        System.out.println("Registro exitoso:");
        System.out.println("Nombres: " + nombres);
        System.out.println("Apellido P: " + apellidoP);
        System.out.println("Apellido M: " + apellidoM);
        System.out.println("Domicilio: " + domicilio);
        System.out.println("Fecha de nacimiento: " + fechaNacimiento);

        // Mostrar mensaje de éxito
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Registro Exitoso");
        alert.setHeaderText("¡Bienvenido a Pantojarte Panadería!");
        alert.setContentText("Su cuenta ha sido creada exitosamente.\nNombre: " + nombres + " " + apellidoP);
        alert.showAndWait();

        // Volver a la pantalla principal
        try {
            App.setRoot("main_panaderia");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Gestiona el retorno a la pantalla principal desde el formulario de registro.
     * Intenta reasignar la raíz de la escena al FXML "main_panaderia".
     * * En caso de un fallo en la carga del archivo (IOException), despliega una 
     * alerta de tipo ERROR para notificar al usuario sobre la inconsistencia.
     */
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
    /**
     * Inicializa la vista configurando efectos visuales y prompts.
     */
    @FXML
    private void initialize() {
        System.out.println("Pantalla de Registro cargada");
        
        // Agregar efectos hover a los botones
        addHoverEffect(btnEntrar);
        addHoverEffect(btnVolver);
        
        // Configurar el formato de fecha
        dateFechaNacimiento.setPromptText("dd/mm/aaaa");
    }
    /**
     * Aplica efectos visuales dinámicos a los botones cuando el cursor entra o sale.
     * Utiliza expresiones lambda para gestionar los eventos de mouse, permitiendo
     * una interfaz más viva y reactiva sin necesidad de archivos CSS externos.
     * * @param button El componente Button al que se le aplicará la lógica de hover.
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
        
        button.setOnMouseExited(e -> {
            button.setStyle(originalStyle);
        });
    }
}


