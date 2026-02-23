/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.fabrica;

import negocio.BOs.ClienteBO;
import negocio.BOs.IClienteBO;
import negocio.BOs.IPedidoExpressBO;
import negocio.BOs.IPedidoProgramadoBO;
import negocio.BOs.IProductoBO;
import negocio.BOs.PedidoExpressBO;
import negocio.BOs.PedidoProgramadoBO;
import negocio.BOs.ProductoBO;
import persistencia.fabrica.FabricaDAOs;

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
