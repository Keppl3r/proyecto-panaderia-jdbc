
package pruebasCasoDeUsoPrincipal;
 import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import negocio.BOs.*;
import negocio.DTOs.*;
import negocio.fabrica.*;
import persistencia.dominio.*;

/**
 * Caso de uso: Programar un pedido
 * @author Adrian Mendoza
 */
public class PruebaPedidoProgramado {
    
    public static void main(String[] args) {
        System.out.println("Iniciando Caso de Uso: Programar Pedido");
        
        try {
            System.out.println(" Inicializar sistema");
            IPedidoProgramadoBO pedidoProgramadoBO = FabricaBOs.obtenerPedidoProgramadoBO();
            
            System.out.println("Cliente selecciona productos");
            List<DetallePedido> detalles = new ArrayList<>();
            
            DetallePedido detalle1 = new DetallePedido();
            detalle1.setIdProducto(1);
            detalle1.setCantidad(2);
            detalle1.setNotas("Sin azucar");
            detalles.add(detalle1);
            
            System.out.println("Procesar datos del cliente");
            PedidoProgramadoNuevoDTO pedidoDTO = new PedidoProgramadoNuevoDTO();
            pedidoDTO.setIdCliente(1);
            pedidoDTO.setDetalles(detalles);
            
            long tiempoFuturo = System.currentTimeMillis() + (3 * 60 * 60 * 1000);
            Timestamp fechaEntrega = new Timestamp(tiempoFuturo);
            pedidoDTO.setFechaEntrega(fechaEntrega);
            pedidoDTO.setIdCupon(null);
            
            System.out.println("Enviar pedido al sistema");
            PedidoProgramado pedido = pedidoProgramadoBO.programarPedido(pedidoDTO);
            
            System.out.println("\nResultado:");
            System.out.println("ID Pedido: " + pedido.getIdPedido());
            System.out.println("Numero: " + pedido.getNumPedido());
            System.out.println("Cliente: " + pedido.getIdUsuario());
            System.out.println("Estado: " + pedido.getEstado().getDescripcion());
            System.out.println("Total: " + pedido.getTotal());
            System.out.println("Fecha Entrega: " + pedido.getFechaEntrega());
            System.out.println("Productos: " + (pedido.getDetalles() != null ? pedido.getDetalles().size() : 0));
            
            System.out.println("Prueba completada");
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
















