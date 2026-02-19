package negocio.pruebas;

import negocio.BOs.*;
import persistencia.DAOs.*;
import persistencia.conexion.*;
import persistencia.dominio.*;
import java.util.List;

public class PruebaProductoBO {

    public static void main(String[] args) {
        System.out.println("Probando ProductoBO");

        try {
            IConexionBD conexion = new ConexionBD();
            IProductoDAO productoDAO = new ProductoDAO(conexion);
            IProductoBO productoBO = new ProductoBO(productoDAO);

            // Obtener productos
            List<Producto> productos = productoBO.obtenerProductoDisponibles();
            System.out.println("Productos encontrados: " + productos.size());

            if (productos.size() > 0) {
                System.out.println("Hay productos disponibles ");

                // Probar obtener por ID
                Producto producto = productoBO.obtenerPorId(productos.get(0).getIdProducto());
                if (producto != null) {
                    System.out.println("Producto por ID encontrado");
                } else {
                    System.out.println("Producto por ID no encontrado");
                }
            } else {
                System.out.println("No hay productos");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
