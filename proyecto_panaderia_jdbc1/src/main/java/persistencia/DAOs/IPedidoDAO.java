package persistencia.DAOs;

import java.sql.Timestamp;
import java.util.List;
import persistencia.dominio.DetallePedido;
import persistencia.dominio.Pedido;
import persistencia.excepciones.PersistenciaException;

/**
 * Interfaz que define las operaciones de consulta y gestión de estados para Pedidos.
 * <p>
 * Provee un conjunto robusto de métodos para el rastreo de órdenes, permitiendo
 * búsquedas por diversos criterios (folio, teléfono, fechas) y la gestión del
 * ciclo de vida del pedido (cambios de estado y conteo de actividad).
 * </p>
 * @author Adrian Mendoza
 */
public interface IPedidoDAO {
    /**
     * Actualiza el estado actual de un pedido (ej. de 'PENDIENTE' a 'LISTO').
     * @param idPedido Identificador único de la orden.
     * @param nuevoEstado Cadena que representa el nuevo estado en la BD.
     * @return {@code true} si la actualización fue exitosa.
     * @throws PersistenciaException Si ocurre un error de conectividad.
     */
    boolean cambiarEstado(int idPedido, String nuevoEstado) throws PersistenciaException;
    /**
     * Cuenta cuántos pedidos tiene un cliente en estados operativos (no finalizados).
     * <p>Utilizado por el BO para hacer cumplir la regla de máximo 3 pedidos activos.</p>
     * @param idCliente ID del cliente a consultar.
     * @return Cantidad de pedidos en curso.
     * @throws PersistenciaException Si falla la consulta SQL.
     */
    int contarPedidosActivos(int idCliente) throws PersistenciaException;
    /**
     * Recupera una lista de pedidos asociados a un número telefónico.
     * @param telefono Cadena de contacto del cliente.
     * @return {@link List} de pedidos encontrados.
     * @throws PersistenciaException Si hay un error en la búsqueda.
     */
    List<Pedido> buscarPorTelefono(String telefono) throws PersistenciaException;
    /**
     * Localiza pedidos que coincidan con un folio específico.
     * @param folio Identificador público del pedido.
     * @return Lista de coincidencias (usualmente una sola instancia).
     * @throws PersistenciaException Si falla el filtrado.
     */
    List<Pedido> buscarPorFolio(String folio) throws PersistenciaException;
    /**
     * Filtra el historial de pedidos dentro de una ventana de tiempo específica.
     * @param inicio Fecha/hora inicial del rango.
     * @param fin Fecha/hora final del rango.
     * @return Lista de pedidos realizados en ese intervalo.
     * @throws PersistenciaException Si ocurre un error en la consulta temporal.
     */
    List<Pedido> buscarPorRangoFechas(Timestamp inicio, Timestamp fin) throws PersistenciaException;
    /**
     * Obtiene los pedidos que requieren atención inmediata en cocina o mostrador.
     * @return Lista de pedidos con estados de prioridad alta.
     * @throws PersistenciaException Si falla la recuperación de datos.
     */
    List<Pedido> obtenerPedidosPendientesYListos() throws PersistenciaException;
    /**
     * Recupera todas las órdenes realizadas por un cliente específico.
     * @param idCliente Identificador del cliente.
     * @return Historial completo de compras.
     * @throws PersistenciaException Si hay un error al consultar el historial.
     */
    List<Pedido> obtenerHistorialCliente(int idCliente) throws PersistenciaException;
    /**
     * Obtiene la información de un pedido mediante su ID interno.
     * @param idPedido Identificador único.
     * @return Objeto {@link Pedido} poblado.
     * @throws PersistenciaException Si no se encuentra el registro.
     */
    Pedido obtenerPorId(int idPedido) throws PersistenciaException;
    /**
     * Recupera el desglose de productos incluidos en un pedido.
     * @param idPedido Identificador de la orden.
     * @return Lista de {@link DetallePedido} con productos y cantidades.
     * @throws PersistenciaException Si falla la carga de detalles.
     */
    List<DetallePedido> obtenerDetallesPorPedido(int idPedido) throws PersistenciaException;
    /**
     * Obtiene la lista global de pedidos para la gestión administrativa.
     * @return Lista de todos los pedidos registrados en el sistema.
     * @throws PersistenciaException Si ocurre un error técnico.
     */
    List<Pedido> obtenerHistorialEmpleado() throws PersistenciaException;
}
