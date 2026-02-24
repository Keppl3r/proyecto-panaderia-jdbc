package com.mycompany.proyecto_panaderia_jdbc1;

import java.io.IOException;
import javafx.fxml.FXML;

/**
 * Controlador para la vista secundaria de la aplicación.
 * Facilita la navegación de regreso hacia la pantalla principal,
 * manteniendo la coherencia en el flujo de usuario.
 */
public class SecondaryController {
    
    /**
     * Gestiona el evento de retorno a la vista primaria.
     * Invoca el método setRoot de la clase App para cargar el FXML "primary",
     * reemplazando el contenido actual de la ventana.
     * * @throws IOException Si existe un error al localizar o leer el archivo FXML.
     */
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}