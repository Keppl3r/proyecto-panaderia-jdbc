/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.fabrica;
import persistencia.fabrica.FabricaDAOs;
import negocio.BOs.*;


/**
 *
 * @author Adrian Mendoza
 */
public class FabricaBOs {
     public static IPedidoProgramadoBO obtenerPedidoProgramadoBO() {
        return new PedidoProgramadoBO(
            FabricaDAOs.obtenerPedidoProgramadoDAO(),
            FabricaDAOs.obtenerProductoDAO(),
            obtenerClienteBO()
        );
    }
    
  public static IPedidoExpressBO obtenerPedidoExpressBO() {
    return new PedidoExpressBO(
        FabricaDAOs.obtenerPedidoExpressDAO(),
        FabricaDAOs.obtenerProductoDAO()
    );
}
    
    public static IClienteBO obtenerClienteBO() {
        return new ClienteBO(FabricaDAOs.obtenerClienteDAO());
    }
    
    public static IProductoBO obtenerProductoBO() {
        return new ProductoBO(FabricaDAOs.obtenerProductoDAO());
    }

}
