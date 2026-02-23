package negocio.pruebas;

import negocio.BOs.*;
import negocio.DTOs.*;
import negocio.fabrica.*;
import persistencia.dominio.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Prueba simple de PedidoProgramadoBO
 * @author Adrian Mendoza
 */
public class PruebaPedidoProgramadoBO {
    
    public static void main(String[] args) {
        System.out.println("Iniciando Prueba PedidoProgramadoBO");
        
        try {
            IPedidoProgramadoBO pedidoProgramadoBO = FabricaBOs.obtenerPedidoProgramadoBO();
            
            List<DetallePedido> detalles = new ArrayList<>();
            DetallePedido detalle1 = new DetallePedido();
            detalle1.setIdProducto(1);
            detalle1.setCantidad(2);
            detalle1.setNotas("Sin azucar");
            detalles.add(detalle1);
            
            PedidoProgramadoNuevoDTO pedidoDTO = new PedidoProgramadoNuevoDTO();
            pedidoDTO.setIdCliente(1);
            pedidoDTO.setDetalles(detalles);
            long tiempoFuturo = System.currentTimeMillis() + (3 * 60 * 60 * 1000);
            pedidoDTO.setFechaEntrega(new Timestamp(tiempoFuturo));
            pedidoDTO.setIdCupon(null);
            
            System.out.println("Creando pedido programado...");
            PedidoProgramado pedido = pedidoProgramadoBO.programarPedido(pedidoDTO);
            
            if (pedido != null) {
                System.out.println("Prueba exitosa");
                System.out.println("ID Pedido: " + pedido.getIdPedido());
                System.out.println("Numero Pedido: " + pedido.getNumPedido());
                System.out.println("Estado: " + pedido.getEstado().getDescripcion());
                System.out.println("Total: " + pedido.getTotal());
            } else {
                System.out.println("Prueba fallida - Pedido es null");
            }
            
        } catch (Exception e) {
            System.out.println("Error en prueba: " + e.getMessage());
            e.printStackTrace();
        }
    }
}