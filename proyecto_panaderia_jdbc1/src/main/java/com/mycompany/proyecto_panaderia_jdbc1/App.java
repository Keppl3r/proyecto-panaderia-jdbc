package com.mycompany.proyecto_panaderia_jdbc1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    private static Scene scene;

    /** true = entró por Pedido Express (sin login), false = usuario con sesión iniciada */
    public static boolean modoExpress = false;

    public record ItemCarrito(int idProducto, String nombre, String descripcion, double precio, int cantidad) {}

    public static final List<ItemCarrito> carrito = new ArrayList<>();

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

    public static void limpiarCarrito() {
        carrito.clear();
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("main_panaderia"), 900, 700);
        stage.setTitle("Pantojarte Panadería");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}