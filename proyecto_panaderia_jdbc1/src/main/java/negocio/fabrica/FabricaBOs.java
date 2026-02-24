package negocio.fabrica;

import persistencia.fabrica.FabricaDAOs;
import negocio.BOs.*;

/**
 * Fábrica estática para la creación y gestión de Objetos de Negocio (BOs).
 * <p>
 * Esta clase centraliza la instanciación de todas las implementaciones de la capa de negocio,
 * resolviendo automáticamente la inyección de dependencias necesaria (DAOs y otros BOs).
 * </p>
 * <p>
 * Al utilizar esta fábrica, se asegura que el sistema siga el principio de inversión 
 * de dependencias, permitiendo que la capa de presentación interactúe únicamente 
 * con interfaces.
 * </p>
 * @author Adrian Mendoza
 */
public class FabricaBOs {
    /**
     * Proporciona una instancia de la lógica de usuarios.
     * @return Implementación de {@link IUsuarioBO}.
     */
    public static IUsuarioBO obtenerUsuarioBO() {
        return new UsuarioBO(FabricaDAOs.obtenerUsuarioDAO());
    }
    /**
     * Construye un BO de clientes inyectando DAOs de usuarios, teléfonos y clientes.
     * @return Implementación de {@link IClienteBO}.
     */
    public static IClienteBO obtenerClienteBO() {
        return new ClienteBO(
                FabricaDAOs.obtenerClienteDAO(),
                FabricaDAOs.obtenerUsuarioDAO(),
                FabricaDAOs.obtenerTelefonoDAO());
    }
    /**
     * Proporciona una instancia para la gestión del catálogo de productos.
     * @return Implementación de {@link IProductoBO}.
     */
    public static IProductoBO obtenerProductoBO() {
        return new ProductoBO(FabricaDAOs.obtenerProductoDAO());
    }
    /**
     * Orquesta la creación del BO de Pedidos Programados. 
     * <p>Nótese que inyecta tanto DAOs como otros BOs (ClienteBO) para validaciones complejas.</p>
     * @return Implementación de {@link IPedidoProgramadoBO}.
     */
    public static IPedidoProgramadoBO obtenerPedidoProgramadoBO() {
        return new PedidoProgramadoBO(
                FabricaDAOs.obtenerPedidoProgramadoDAO(),
                FabricaDAOs.obtenerProductoDAO(),
                obtenerClienteBO(),
                FabricaDAOs.obtenerPedidoDAO(),
                FabricaDAOs.obtenerCuponDAO());
    }
    /**
     * Proporciona una instancia para la gestión de pedidos rápidos sin registro.
     * @return Implementación de {@link IPedidoExpressBO}.
     */
    public static IPedidoExpressBO obtenerPedidoExpressBO() {
        return new PedidoExpressBO(
                FabricaDAOs.obtenerPedidoExpressDAO(),
                FabricaDAOs.obtenerProductoDAO());
    }
    /**
     * Proporciona una instancia para la gestión operativa de pedidos (cambio de estados, pagos).
     * @return Implementación de {@link IPedidoBO}.
     */
    public static IPedidoBO obtenerPedidoBO() {
        return new PedidoBO(
                FabricaDAOs.obtenerPedidoDAO(),
                FabricaDAOs.obtenerPedidoExpressDAO(),
                FabricaDAOs.obtenerPagoDAO());
    }
}
