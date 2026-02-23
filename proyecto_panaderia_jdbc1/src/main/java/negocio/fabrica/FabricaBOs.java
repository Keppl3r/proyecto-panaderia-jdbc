package negocio.fabrica;

import persistencia.fabrica.FabricaDAOs;
import negocio.BOs.*;

/**
 * @author Adrian Mendoza
 */
public class FabricaBOs {

    public static IUsuarioBO obtenerUsuarioBO() {
        return new UsuarioBO(FabricaDAOs.obtenerUsuarioDAO());
    }

    public static IClienteBO obtenerClienteBO() {
        return new ClienteBO(FabricaDAOs.obtenerClienteDAO());
    }

    public static IProductoBO obtenerProductoBO() {
        return new ProductoBO(FabricaDAOs.obtenerProductoDAO());
    }

    public static IPedidoProgramadoBO obtenerPedidoProgramadoBO() {
        return new PedidoProgramadoBO(
                FabricaDAOs.obtenerPedidoProgramadoDAO(),
                FabricaDAOs.obtenerProductoDAO(),
                obtenerClienteBO());
    }

    public static IPedidoExpressBO obtenerPedidoExpressBO() {
        return new PedidoExpressBO(
                FabricaDAOs.obtenerPedidoExpressDAO(),
                FabricaDAOs.obtenerProductoDAO());
    }

    public static IPedidoBO obtenerPedidoBO() {
        return new PedidoBO(
                FabricaDAOs.obtenerPedidoDAO(),
                FabricaDAOs.obtenerPedidoExpressDAO(),
                FabricaDAOs.obtenerPagoDAO());
    }
}
