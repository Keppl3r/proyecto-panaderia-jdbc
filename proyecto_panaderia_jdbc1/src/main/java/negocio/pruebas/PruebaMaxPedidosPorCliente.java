package negocio.pruebas;

import negocio.BOs.IPedidoProgramadoBO;
import negocio.DTOs.PedidoProgramadoNuevoDTO;
import negocio.fabrica.FabricaBOs;
import persistencia.dominio.DetallePedido;
import persistencia.dominio.PedidoProgramado;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
