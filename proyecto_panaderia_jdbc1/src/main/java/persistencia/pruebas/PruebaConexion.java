/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.pruebas;
import java.sql.SQLException;
import java.util.List;
import persistencia.DAOs.IProductoDAO;
import persistencia.DAOs.ProductoDAO;
import persistencia.conexion.IConexionBD;
import persistencia.conexion.ConexionBD;
import persistencia.dominio.Producto;
/**
 *
 * @author Adrian Mendoza
 */
public class PruebaConexion {
     public static void main(String[] args) {
             try {
                 IConexionBD conexion = new ConexionBD();
                 System.out.println("Conexion creada exitosamente");

                 IProductoDAO productoDAO = new ProductoDAO(conexion);
                 List<Producto> productos = productoDAO.obtenerProductosDisponibles();

                 System.out.println("Productos encontrados: " + productos.size());

                 for (Producto p : productos) {
                     System.out.println("  - " + p.getNombre() + " ($" + p.getPrecio() + ")");
                 }

             } catch (Exception e) {
                 System.out.println("Error: " + e.getMessage());
                 e.printStackTrace();
             }
         }
}
