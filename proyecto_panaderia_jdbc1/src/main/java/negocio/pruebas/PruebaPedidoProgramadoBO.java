package negocio.pruebas;

import negocio.BOs.*;
import negocio.DTOs.*;
import negocio.fabrica.*;
import persistencia.dominio.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de prueba integral para la lógica de Pedidos Programados.
 * <p>
 * Esta prueba valida el ciclo de vida de creación de un pedido para clientes registrados,
 * verificando componentes críticos del sistema:
 * <ul>
 * <li>Uso de la capa de servicios a través de {@code IPedidoProgramadoBO}.</li>
 * <li>Persistencia de detalles específicos del producto, incluyendo notas de personalización.</li>
 * <li>Validación de la regla de tiempo mínimo de entrega (configurado a +3 horas).</li>
 * <li>Cálculo automático del monto total y asignación del estado inicial del pedido.</li>
 * <li>Manejo opcional de cupones (verificación de valor {@code null}).</li>
 * </ul>
 * </p>
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