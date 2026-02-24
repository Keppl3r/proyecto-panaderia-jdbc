package persistencia.fabrica;

import persistencia.DAOs.*;
import persistencia.conexion.ConexionBD;
import persistencia.conexion.IConexionBD;

/**
 * Clase de tipo Fábrica (Factory) encargada de centralizar la creación de los DAOs.
 * <p>
 * Proporciona un punto de acceso único para obtener las implementaciones de los 
 * Data Access Objects, asegurando que todos compartan la misma instancia de 
 * conexión a la base de datos y promoviendo el desacoplamiento entre la lógica 
 * de negocio y la persistencia.
 * </p>
 * * @author Adrian Mendoza
 */
public class FabricaDAOs {

    /**
     * Instancia compartida de la conexión a la base de datos.
     * Se mantiene estática para ser reutilizada por todos los DAOs generados.
     */
    private static final IConexionBD conexion = new ConexionBD();

    /**
     * Proporciona la implementación para la gestión de usuarios.
     * @return Una instancia de {@link IUsuarioDAO}.
     */
    public static IUsuarioDAO obtenerUsuarioDAO() {
        return new UsuarioDAO(conexion);
    }

    /**
     * Proporciona la implementación para la gestión de clientes.
     * @return Una instancia de {@link IClienteDAO}.
     */
    public static IClienteDAO obtenerClienteDAO() {
        return new ClienteDAO(conexion);
    }

    /**
     * Proporciona la implementación para la gestión del catálogo de productos.
     * @return Una instancia de {@link IProductoDAO}.
     */
    public static IProductoDAO obtenerProductoDAO() {
        return new ProductoDAO(conexion);
    }

    /**
     * Proporciona la implementación para la gestión de pedidos programados.
     * @return Una instancia de {@link IPedidoProgramadoDAO}.
     */
    public static IPedidoProgramadoDAO obtenerPedidoProgramadoDAO() {
        return new PedidoProgramadoDAO(conexion);
    }

    /**
     * Proporciona la implementación para la gestión de pedidos de tipo express.
     * @return Una instancia de {@link IPedidoExpressDAO}.
     */
    public static IPedidoExpressDAO obtenerPedidoExpressDAO() {
        return new PedidoExpressDAO(conexion);
    }

    /**
     * Proporciona la implementación general para la gestión de pedidos base.
     * @return Una instancia de {@link IPedidoDAO}.
     */
    public static IPedidoDAO obtenerPedidoDAO() {
        return new PedidoDAO(conexion);
    }

    /**
     * Proporciona la implementación para la gestión de números telefónicos.
     * @return Una instancia de {@link ITelefonoDAO}.
     */
    public static ITelefonoDAO obtenerTelefonoDAO() {
        return new TelefonoDAO(conexion);
    }

    /**
     * Proporciona la implementación para la gestión de cupones de descuento.
     * @return Una instancia de {@link ICuponDAO}.
     */
    public static ICuponDAO obtenerCuponDAO() {
        return new CuponDAO(conexion);
    }

    /**
     * Proporciona la implementación para la gestión de transacciones de pago.
     * @return Una instancia de {@link IPagoDAO}.
     */
    public static IPagoDAO obtenerPagoDAO() {
        return new PagoDAO(conexion);
    }
}