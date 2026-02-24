
package pruebasCasoDeUsoPrincipal;
 import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import negocio.BOs.*;
import negocio.DTOs.*;
import negocio.fabrica.*;
import persistencia.dominio.*;

/**
 * * Clase de prueba para validar el flujo del Caso de Uso: "Programar Pedido".
 * <p>
 * Actúa como un driver de ejecución que simula la interacción de un cliente con el sistema,
 * desde la selección de productos y configuración de entrega hasta la persistencia final
 * a través de la capa de lógica de negocio (BO).
 * </p>
 * @author Adrian Mendoza
 */
public class PruebaPedidoProgramado {
        /**
     * Punto de entrada de la aplicación de prueba.
     * <p>
     * Realiza las siguientes etapas:
     * 1. Inicializa el BO mediante la {@link FabricaBOs}.
     * 2. Crea una lista de {@link DetallePedido} simulando una selección de carrito.
     * 3. Configura un {@link PedidoProgramadoNuevoDTO} con datos de tiempo futuro.
     * 4. Ejecuta la lógica de programación y muestra los resultados por consola.
     * </p>
     * * @param args Argumentos de línea de comandos (no utilizados).
     */
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
















