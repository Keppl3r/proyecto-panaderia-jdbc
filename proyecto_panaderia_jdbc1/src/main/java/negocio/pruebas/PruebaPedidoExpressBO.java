package negocio.pruebas;

import negocio.BOs.*;
import negocio.DTOs.*;
import negocio.fabrica.*;
import persistencia.dominio.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de prueba funcional para la gestión de Pedidos Express.
 * <p>
 * Esta clase verifica la correcta ejecución del proceso de creación de pedidos rápidos 
 * (sin cliente registrado). Valida los siguientes puntos clave:
 * <ul>
 * <li>Funcionamiento de la {@code FabricaBOs} para obtener la instancia de negocio.</li>
 * <li>Correcto mapeo y transferencia de datos mediante el DTO {@code PedidoExpressNuevoDTO}.</li>
 * <li>Generación automática de atributos de seguridad (Folio y PIN).</li>
 * <li>Cálculo del tiempo límite de recolección por parte de la capa de negocio.</li>
 * </ul>
 * </p>
 * * Nota: La salida de consola recorta el PIN para verificar que el hash se generó 
 * correctamente sin comprometer la visualización completa.
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