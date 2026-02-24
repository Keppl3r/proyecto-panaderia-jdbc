package negocio.BOs;

import java.sql.Timestamp;
import java.util.List;
import negocio.excepciones.NegocioException;
import persistencia.dominio.DetallePedido;
import persistencia.dominio.Pedido;

/**
 * Interfaz de la Capa de Negocio para la gestión del ciclo de vida de Pedidos.
 * <p>
 * Define las operaciones críticas para el flujo de trabajo de la panadería, 
 * incluyendo el seguimiento de estados (Pendiente, Listo, Entregado, Cancelado), 
 * la gestión de ventas express y la recuperación de historial para clientes y empleados.
 * </p>
 * * @author Jazmin
 */
public interface IPedidoBO {
    /**
     * Cambia el estado de un pedido a "Listo".
     * Indica que la producción en cocina ha finalizado y el pedido puede ser entregado.
     * * @param idPedido Identificador único de la orden.
     * @return {@code true} si la actualización fue exitosa.
     * @throws NegocioException Si el pedido no existe o ya fue cancelado/entregado.
     */
    boolean marcarComoListo(int idPedido) throws NegocioException;
    /**
     * Registra la entrega física y el pago de un pedido de un cliente registrado.
     * * @param idPedido   Identificador único de la orden.
     * @param metodoPago Forma de pago utilizada (Efectivo, Tarjeta, etc.).
     * @return {@code true} si se procesó la entrega correctamente.
     * @throws NegocioException Si hay inconsistencias en el inventario o estado del pedido.
     */
    boolean entregarPedido(int idPedido, String metodoPago) throws NegocioException;
    /**
     * Procesa la entrega de un pedido realizado bajo la modalidad "Express".
     * Requiere validación de credenciales temporales (Folio y PIN).
     * * @param folio      Código único de rastreo de la orden express.
     * @param pin        Clave de seguridad proporcionada al cliente al comprar.
     * @param metodoPago Forma de pago.
     * @return {@code true} si las credenciales son válidas y se completa la entrega.
     * @throws NegocioException Si el PIN es incorrecto o el folio no existe.
     */
    boolean entregarPedidoExpress(String folio, String pin, String metodoPago) throws NegocioException;
    /**
     * Cancela un pedido activo, liberando productos si es necesario.
     * * @param idPedido Identificador de la orden a anular.
     * @return {@code true} si la cancelación fue procedente.
     * @throws NegocioException Si el pedido ya ha sido entregado.
     */
    boolean cancelarPedido(int idPedido) throws NegocioException;
    /**
     * Recupera una lista de pedidos asociados a un número telefónico.
     * Útil para búsquedas rápidas en el mostrador.
     */
    List<Pedido> buscarPorTelefono(String telefono) throws NegocioException;
    /**
     * Localiza un pedido específico mediante su folio alfanumérico.
     */
    List<Pedido> buscarPorFolio(String folio) throws NegocioException;
    /**
     * Genera un reporte de pedidos filtrado por un rango de tiempo específico.
     * * @param inicio Estampa de tiempo inicial.
     * @param fin    Estampa de tiempo final.
     * @return Lista de pedidos realizados en dicho periodo.
     */
    List<Pedido> buscarPorFechas(Timestamp inicio, Timestamp fin) throws NegocioException;
    /**
     * Obtiene los pedidos que requieren atención inmediata (Cola de producción y entrega).
     * Excluye pedidos entregados o cancelados.
     */
    List<Pedido> obtenerPendientesYListos() throws NegocioException;
    /**
     * Recupera todas las compras realizadas por un cliente específico.
     */
    List<Pedido> obtenerHistorial(int idCliente) throws NegocioException;
    /**
     * Obtiene el desglose de productos (items, cantidades, precios) de una orden.
     * * @param idPedido ID del pedido padre.
     * @return Lista de objetos {@link DetallePedido}.
     */
    List<DetallePedido> obtenerDetallesPorPedido(int idPedido) throws NegocioException;
    /**
     * Proporciona una visión global de todos los pedidos procesados en el sistema 
     * para fines administrativos o de auditoría interna.
     */
    List<Pedido> obtenerHistorialEmpleado() throws NegocioException;
}
