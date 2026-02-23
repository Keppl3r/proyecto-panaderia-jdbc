 package persistencia.pruebas;

     import persistencia.DAOs.*;
     import persistencia.conexion.*;
     import persistencia.dominio.*;
     import java.util.List;

     public class PruebaProductoDAO {

         public static void main(String[] args) {
             System.out.println("Probando ProductoDAO");

             try {
                 IConexionBD conexion = new ConexionBD();
                 IProductoDAO productoDAO = new ProductoDAO(conexion);

                 // Obtener productos disponibles
                 List<Producto> productos = productoDAO.obtenerProductosDisponibles();
                 System.out.println("Productos disponibles: " + productos.size());

                 if (productos.size() > 0) {
                     System.out.println("Lista de productos");

                     // Obtener primer producto por ID
                     Producto producto = productoDAO.obtenerPorId(productos.get(0).getIdProducto());
                     if (producto != null) {
                         System.out.println("Producto por ID");
                         System.out.println("Nombre: " + producto.getNombre());
                         System.out.println("Precio: " + producto.getPrecio());
                     } else {
                         System.out.println("Producto por ID");
                     }
                 } else {
                     System.out.println("No hay productos");
                 }

             } catch (Exception e) {
                 System.out.println("Error: " + e.getMessage());
             }
         }
     }