package com.mycompany.proyecto_panaderia_jdbc1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 /**
 * Clase principal de la aplicación Pantojarte Panadería.
 * <p>
 * Esta clase orquestal el ciclo de vida de la aplicación JavaFX, gestiona la 
 * navegación centralizada mediante el intercambio de raíces (roots) en la escena 
 * y mantiene el estado global del carrito de compras y el modo de operación.
 */
public class App extends Application {
    /**
     * Referencia a la escena principal de la aplicación.
     */
    private static Scene scene;
    
    /**
     * Estado que indica si el usuario actual ha ingresado mediante el flujo sin registro.
     * {@code true} si es un pedido express; {@code false} si es un usuario autenticado.
     */
    public static boolean modoExpress = false;
    
    /**
     * Representación inmutable de un elemento dentro del carrito de compras.
     * * @param idProducto  Identificador único del producto en la base de datos.
     * @param nombre      Nombre comercial del pan o producto.
     * @param descripcion Detalles adicionales del producto.
     * @param precio      Costo unitario del producto.
     * @param cantidad    Número de unidades seleccionadas.
     */
    public record ItemCarrito(int idProducto, String nombre, String descripcion, double precio, int cantidad) {}
    
    /**
     * Lista global que almacena los productos seleccionados durante la sesión.
     */
    public static final List<ItemCarrito> carrito = new ArrayList<>();

    /**
     * Agrega un producto al carrito de compras o incrementa su cantidad si ya existe.
     * <p>
     * Implementa una búsqueda lineal por ID. Debido a la inmutabilidad de los {@link ItemCarrito},
     * si el producto existe, se reemplaza la entrada antigua por una nueva con la cantidad actualizada.
     * </p>
     * * @param idProducto  ID del producto a agregar.
     * @param nombre      Nombre del producto.
     * @param descripcion Descripción del producto.
     * @param precio      Precio unitario.
     */
    
    public static void agregarAlCarrito(int idProducto, String nombre, String descripcion, double precio) {
        for (int i = 0; i < carrito.size(); i++) {
            if (carrito.get(i).idProducto() == idProducto) {
                ItemCarrito actual = carrito.get(i);
                carrito.set(i, new ItemCarrito(idProducto, actual.nombre(), actual.descripcion(),
                        actual.precio(), actual.cantidad() + 1));
                return;
            }
        }
        carrito.add(new ItemCarrito(idProducto, nombre, descripcion, precio, 1));
    }
    /**
     * Vacía todos los elementos contenidos en el carrito global.
     */
    public static void limpiarCarrito() {
        carrito.clear();
    }
    /**
     * Punto de entrada principal para el inicio de la interfaz gráfica.
     * Configura el escenario (Stage) inicial y carga la vista de bienvenida de la panadería.
     * * @param stage El escenario principal proporcionado por la plataforma JavaFX.
     * @throws IOException Si ocurre un error al cargar el archivo FXML inicial.
     */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("main_panaderia"), 900, 700);
        stage.setTitle("Pantojarte Panadería");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    /**
     * Cambia la raíz de la escena actual por una nueva vista cargada desde FXML.
     * * @param fxml Nombre del archivo .fxml a cargar (sin la extensión).
     * @throws IOException Si el archivo no puede ser localizado o leído.
     */
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    /**
     * Carga un archivo FXML y devuelve su nodo raíz.
     * * @param fxml Nombre del recurso FXML.
     * @return El nodo {@link Parent} que representa la jerarquía visual del FXML.
     * @throws IOException Si falla la carga del recurso.
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    /**
     * Método principal que lanza la aplicación.
     * * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        launch();
    }

}