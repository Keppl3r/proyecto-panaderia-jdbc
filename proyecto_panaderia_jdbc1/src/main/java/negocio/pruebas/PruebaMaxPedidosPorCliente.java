package negocio.pruebas;

import negocio.BOs.IPedidoProgramadoBO;
import negocio.DTOs.PedidoProgramadoNuevoDTO;
import negocio.fabrica.FabricaBOs;
import persistencia.dominio.DetallePedido;
import persistencia.dominio.PedidoProgramado;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

    
/**
 * Clase de prueba funcional para validar la restricción de pedidos máximos por cliente.
 * <p>
 * El objetivo de esta prueba es asegurar que el sistema respete el límite de 3 pedidos 
 * programados activos por usuario. La prueba realiza un ciclo de 4 iteraciones:
 * <ul>
 * <li>Las primeras 3 iteraciones deberían resultar en la creación exitosa de pedidos.</li>
 * <li>La 4ta iteración debe ser rechazada por la capa de negocio, lanzando una excepción controlada.</li>
 * </ul>
 * </p>
 * <p>
 * Además, valida la correcta integración del DTO {@code PedidoProgramadoNuevoDTO} y el 
 * cálculo de tiempos de entrega (configurados a +3 horas en esta prueba).
 * </p>
 */
public class PruebaMaxPedidosPorCliente {

    public static void main(String[] args) {
        System.out.println("--- Prueba Max 3 Pedidos ---");

        try {
            IPedidoProgramadoBO pedidoBO = FabricaBOs.obtenerPedidoProgramadoBO();

            for (int i = 1; i <= 4; i++) {
                try {
                    List<DetallePedido> detalles = new ArrayList<>();
                    DetallePedido det = new DetallePedido();
                    det.setIdProducto(1);
                    det.setCantidad(1);
                    detalles.add(det);

                    PedidoProgramadoNuevoDTO dto = new PedidoProgramadoNuevoDTO();
                    dto.setIdCliente(3);
                    dto.setDetalles(detalles);
                    long futuro = System.currentTimeMillis() + (3 * 60 * 60 * 1000);
                    dto.setFechaEntrega(new Timestamp(futuro));

                    PedidoProgramado pedido = pedidoBO.programarPedido(dto);
                    System.out.println("Pedido " + i + " creado - ID: " + pedido.getIdPedido());

                } catch (Exception e) {
                    System.out.println("Pedido " + i + " rechazado: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println("Error general: " + e.getMessage());
        }
    }
}
