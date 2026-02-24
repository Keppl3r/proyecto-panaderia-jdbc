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
 ** Clase de prueba técnica para validar la conectividad con la base de datos.
 * <p>
 * Esta clase verifica que el túnel de comunicación entre la aplicación y el DBMS 
 * esté correctamente configurado. Realiza una prueba de lectura directa sobre 
 * el catálogo de productos para asegurar que el mapeo de {@link Producto} 
 * sea consistente con los registros de la base de datos.
 * </p>
 * @author Adrian Mendoza
 */
public class PruebaConexion {
     public static void main(String[] args) {
             try {
                 
                 Producto producto=new Producto();  
                 Producto concha = new Producto(1,"Concha de Vainilla",  "DULCE","Pan dulce tradicional",15.50,true);
                 IConexionBD conexion = new ConexionBD();
                 System.out.println("Conexion creada exitosamente");

                 IProductoDAO productoDAO = new ProductoDAO(conexion);
                 List<Producto> productos = productoDAO.obtenerProductosDisponibles();

                 System.out.println("Productos encontrados: " + productos.size());

                 for (Producto p : productos) {
                     System.out.println("  - " + p.getNombre() + " ($" + p.getPrecio() + ")");
System.out.println(concha.toString());
                 }

             } catch (Exception e) {
                 System.out.println("Error: " + e.getMessage());
                 
             }
         }
}
