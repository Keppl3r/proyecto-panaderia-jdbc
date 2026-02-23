package negocio.pruebas;

import negocio.BOs.*;
import negocio.DTOs.*;
import negocio.fabrica.*;
import persistencia.dominio.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Prueba simple de PedidoExpressBO
 * @author Jazmin
 */
public class PruebaPedidoExpressBO {
    
    public static void main(String[] args) {
        System.out.println("Iniciando Prueba PedidoExpressBO");
        
        try {
            IPedidoExpressBO pedidoExpressBO = FabricaBOs.obtenerPedidoExpressBO();
            
            List<DetallePedido> detalles = new ArrayList<>();
            DetallePedido detalle = new DetallePedido();
            detalle.setIdProducto(1);
            detalle.setCantidad(1);
            detalles.add(detalle);
            
            System.out.println("Creando pedido express...");
            PedidoExpressNuevoDTO pedidoDTO = new PedidoExpressNuevoDTO();
            pedidoDTO.setDetalles(detalles);
            
            PedidoExpress pedido = pedidoExpressBO.crearPedidoExpress(pedidoDTO);
            
            if (pedido != null) {
                System.out.println("Prueba exitosa");
                System.out.println("ID Pedido: " + pedido.getIdPedido());
                System.out.println("Folio: " + pedido.getFolio());
                System.out.println("PIN generado: " + pedido.getPin().substring(0, 20));
                System.out.println("Tiempo Limite: " + pedido.getTiempoLimite());
            } else {
                System.out.println("Prueba fallida - Pedido es null");
            }
            
        } catch (Exception e) {
            System.out.println("Error en prueba: " + e.getMessage());
            e.printStackTrace();
        }
    }
}