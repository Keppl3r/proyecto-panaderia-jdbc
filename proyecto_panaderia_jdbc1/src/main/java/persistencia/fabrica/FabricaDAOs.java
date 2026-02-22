/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.fabrica;
import persistencia.DAOs.*;
import persistencia.conexion.ConexionBD;
import persistencia.conexion.IConexionBD;
/**
 *
 * @author Adrian Mendoza
 */
public class FabricaDAOs {
    // Una sola instancia de conexión compartida
    private static final IConexionBD conexion = new ConexionBD();
    
    // Métodos para obtener cada DAO
    public static IPedidoProgramadoDAO obtenerPedidoProgramadoDAO() {
        return new PedidoProgramadoDAO(conexion);
    }
    
    public static IPedidoExpressDAO obtenerPedidoExpressDAO() {
        return new PedidoExpressDAO(conexion);
    }
    
    public static IClienteDAO obtenerClienteDAO() {
        return new ClienteDAO(conexion);
    }
    
    public static IProductoDAO obtenerProductoDAO() {
        return new ProductoDAO(conexion);
    }
}