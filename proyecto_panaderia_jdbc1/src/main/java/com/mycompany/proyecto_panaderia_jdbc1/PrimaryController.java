package com.mycompany.proyecto_panaderia_jdbc1;

import java.io.IOException;
import javafx.fxml.FXML;

/**
 * Controlador para la vista inicial (Primary) de la aplicación.
 * Su única responsabilidad actual es gestionar la transición hacia 
 * la vista secundaria.
 */
public class PrimaryController {
    /**
     * Maneja el evento de acción para cambiar la escena actual.
     * Utiliza el método estático setRoot de la clase App para cargar 
     * el archivo FXML correspondiente a la vista secundaria.
     * * @throws IOException Si el archivo "secondary.fxml" no se encuentra 
     * o no puede ser cargado.
     */
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
