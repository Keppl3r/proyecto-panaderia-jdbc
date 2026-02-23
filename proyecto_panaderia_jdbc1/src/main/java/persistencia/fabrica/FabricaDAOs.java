package persistencia.fabrica;

import persistencia.DAOs.*;
import persistencia.conexion.ConexionBD;
import persistencia.conexion.IConexionBD;

/**
 * @author Adrian Mendoza
 */
public class FabricaDAOs {

    private static final IConexionBD conexion = new ConexionBD();

    public static IUsuarioDAO obtenerUsuarioDAO() {
        return new UsuarioDAO(conexion);
    }

    public static IClienteDAO obtenerClienteDAO() {
        return new ClienteDAO(conexion);
    }

    public static IProductoDAO obtenerProductoDAO() {
        return new ProductoDAO(conexion);
    }

    public static IPedidoProgramadoDAO obtenerPedidoProgramadoDAO() {
        return new PedidoProgramadoDAO(conexion);
    }

    public static IPedidoExpressDAO obtenerPedidoExpressDAO() {
        return new PedidoExpressDAO(conexion);
    }

    public static IPedidoDAO obtenerPedidoDAO() {
        return new PedidoDAO(conexion);
    }

    public static ITelefonoDAO obtenerTelefonoDAO() {
        return new TelefonoDAO(conexion);
    }

    public static ICuponDAO obtenerCuponDAO() {
        return new CuponDAO(conexion);
    }

    public static IPagoDAO obtenerPagoDAO() {
        return new PagoDAO(conexion);
    }
}
