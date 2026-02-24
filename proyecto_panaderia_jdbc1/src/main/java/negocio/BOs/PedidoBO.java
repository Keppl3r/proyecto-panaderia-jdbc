package negocio.BOs;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.encriptacion.EncriptadorPIN;
import negocio.excepciones.NegocioException;
import persistencia.DAOs.IPagoDAO;
import persistencia.DAOs.IPedidoDAO;
import persistencia.DAOs.IPedidoExpressDAO;
import persistencia.dominio.DetallePedido;
import persistencia.dominio.Pedido;
import persistencia.dominio.Pedido.EstadoPedido;
import persistencia.dominio.PedidoExpress;
import persistencia.excepciones.PersistenciaException;

/**
 * * Implementación de la lógica de negocio para la gestión de pedidos en Pantojarte Panadería.
 * <p>
 * Coordina las operaciones entre los DAOs de Pedidos, Pedidos Express y Pagos. 
 * Implementa validaciones de estado, seguridad por PIN para entregas rápidas 
 * y reglas de expiración de tiempo.
 * @author Adrian Mendoza
 */
public class PedidoBO implements IPedidoBO {

    private IPedidoDAO pedidoDAO;
    private IPedidoExpressDAO pedidoExpressDAO;
    private IPagoDAO pagoDAO;
    private static final Logger LOG = Logger.getLogger(PedidoBO.class.getName());
    /**
     * Constructor que inyecta las dependencias necesarias para la operación de pedidos.
     */
    public PedidoBO(IPedidoDAO pedidoDAO, IPedidoExpressDAO pedidoExpressDAO, IPagoDAO pagoDAO) {
        this.pedidoDAO = pedidoDAO;
        this.pedidoExpressDAO = pedidoExpressDAO;
        this.pagoDAO = pagoDAO;
    }
    /**
     * Transiciona un pedido de PENDIENTE a LISTO.
     * @throws NegocioException Si el pedido no existe o no está en estado PENDIENTE.
     */
    @Override
    public boolean marcarComoListo(int idPedido) throws NegocioException {
        try {
            Pedido pedido = pedidoDAO.obtenerPorId(idPedido);
            if (pedido == null) {
                throw new NegocioException("Pedido no encontrado");
            }
            if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
                throw new NegocioException("Solo pedidos PENDIENTES pueden marcarse como LISTO");
            }
            return pedidoDAO.cambiarEstado(idPedido, "LISTO");
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al marcar como listo", ex);
        }
    }
    /**
     * Finaliza un pedido registrando el pago y cambiando su estado a ENTREGADO.
     * @param metodoPago Vía de pago (Efectivo, Tarjeta, etc.).
     */
    @Override
    public boolean entregarPedido(int idPedido, String metodoPago) throws NegocioException {
        try {
            Pedido pedido = pedidoDAO.obtenerPorId(idPedido);
            if (pedido == null) {
                throw new NegocioException("Pedido no encontrado");
            }
            if (pedido.getEstado() != EstadoPedido.LISTO) {
                throw new NegocioException("Solo pedidos en estado LISTO pueden entregarse");
            }
            pagoDAO.registrarPago(idPedido, metodoPago, pedido.getTotal());
            return pedidoDAO.cambiarEstado(idPedido, "ENTREGADO");
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al entregar pedido", ex);
        }
    }
    /**
     * Procesa la entrega express validando el Folio, el PIN y el tiempo de recolección.
     * <p>
     * Si el tiempo de recolección (20 min) ha expirado, el pedido se marca como 
     * NO_ENTREGADO automáticamente.
     */
    @Override
    public boolean entregarPedidoExpress(String folio, String pin, String metodoPago) throws NegocioException {
        try {
            PedidoExpress pedido = pedidoExpressDAO.buscarPorFolio(folio);
            if (pedido == null) {
                throw new NegocioException("No se encontró pedido con ese folio");
            }
            if (pedido.getEstado() != EstadoPedido.LISTO) {
                throw new NegocioException("El pedido no está en estado LISTO");
            }
            // Verificar PIN
            if (!EncriptadorPIN.verificar(pin, pedido.getPin())) {
                throw new NegocioException("PIN incorrecto");
            }
            // Verificar tiempo (20 min)
            if (pedido.tiempoExpirado()) {
                pedidoDAO.cambiarEstado(pedido.getIdPedido(), "NO_ENTREGADO");
                throw new NegocioException("El tiempo de recolección ha expirado");
            }
            pagoDAO.registrarPago(pedido.getIdPedido(), metodoPago, pedido.getTotal());
            return pedidoDAO.cambiarEstado(pedido.getIdPedido(), "ENTREGADO");
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al entregar pedido express", ex);
        }
    }
    /**
     * Cancela un pedido activo siempre que no haya iniciado su proceso de preparación.
     * <p>
     * Regla de Negocio: Solo se permite la cancelación de pedidos en estado {@code PENDIENTE}.
     * Si el pedido ya está {@code LISTO} o {@code ENTREGADO}, la operación es rechazada 
     * para evitar mermas en la producción.
     * </p>
     * @param idPedido Identificador único de la orden a cancelar.
     * @return {@code true} si la transición al estado CANCELADO fue exitosa.
     * @throws NegocioException Si el pedido no existe, o si su estado actual impide la cancelación.
     */
    @Override
    public boolean cancelarPedido(int idPedido) throws NegocioException {
        try {
            Pedido pedido = pedidoDAO.obtenerPorId(idPedido);
            if (pedido == null) {
                throw new NegocioException("Pedido no encontrado");
            }
            if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
                throw new NegocioException("Solo pedidos PENDIENTES pueden cancelarse");
            }
            return pedidoDAO.cambiarEstado(idPedido, "CANCELADO");
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al cancelar pedido", ex);
        }
    }
    /**
     * Recupera el historial de pedidos vinculados a un número telefónico.
     * Útil para identificar rápidamente a clientes frecuentes o pedidos express.
     */
    @Override
    public List<Pedido> buscarPorTelefono(String tel) throws NegocioException {
        try {
            return pedidoDAO.buscarPorTelefono(tel);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error en búsqueda", ex);
        }
    }
    /**
     * Localiza un pedido específico o un conjunto de coincidencias mediante su folio único.
     * <p>
     * Este método es el punto de contacto principal para la entrega de pedidos Express
     * y Programados en el mostrador, permitiendo al empleado recuperar la información
     * de la orden sin necesidad de consultar datos personales del cliente.
     * </p>
     * @param folio El código alfanumérico identificador de la orden.
     * @return Una lista de {@link Pedido} que coinciden con el folio proporcionado.
     * @throws NegocioException Si ocurre un error técnico en la capa de persistencia 
     * o si el formato del folio es inválido.
     */
    @Override
    public List<Pedido> buscarPorFolio(String folio) throws NegocioException {
        try {
            return pedidoDAO.buscarPorFolio(folio);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error en búsqueda", ex);
        }
    }
    /**
     * Filtra pedidos dentro de un rango temporal específico.
     * Utilizado principalmente para el cierre de caja y reportes de ventas diarios/mensuales.
     */
    @Override
    public List<Pedido> buscarPorFechas(Timestamp ini, Timestamp fin) throws NegocioException {
        try {
            return pedidoDAO.buscarPorRangoFechas(ini, fin);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error en búsqueda", ex);
        }
    }
    /**
     * Obtiene la lista de pedidos que requieren atención inmediata en mostrador o cocina.
     * Filtra los estados PENDIENTE y LISTO para la gestión del flujo de trabajo en tiempo real.
     */
    @Override
    public List<Pedido> obtenerPendientesYListos() throws NegocioException {
        try {
            return pedidoDAO.obtenerPedidosPendientesYListos();
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al obtener pedidos", ex);
        }
    }
    /**
     * Recupera la relación completa de pedidos realizados por un cliente específico.
     * <p>
     * Este método consulta la capa de persistencia para obtener todas las órdenes
     * (independientemente de su estado) vinculadas al ID único del cliente. Es 
     * fundamental para la transparencia del servicio y la gestión de reclamos.
     * </p>
     * @param idCliente Identificador único (Primary Key) del cliente en el sistema.
     * @return Una {@link List} de objetos {@link Pedido} pertenecientes al cliente.
     * @throws NegocioException Si ocurre un fallo en la conexión con la base de datos
     * o si el ID proporcionado no tiene un formato válido.
     */
    @Override
    public List<Pedido> obtenerHistorial(int idCliente) throws NegocioException {
        try {
            return pedidoDAO.obtenerHistorialCliente(idCliente);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al obtener historial", ex);
        }
    }
    /**
     * Recupera el desglose de productos asociados a un pedido.
     * Permite visualizar la "orden de trabajo" (qué panes y cuántos) de una compra específica.
     */
    @Override
    public List<DetallePedido> obtenerDetallesPorPedido(int idPedido) throws NegocioException {
        try {
            return pedidoDAO.obtenerDetallesPorPedido(idPedido);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al obtener detalles del pedido", ex);
        }
    }
    /**
     * Recupera el historial universal de pedidos registrados en el sistema.
     * <p>
     * Proporciona acceso a todos los pedidos (Express, Programados y de Clientes) 
     * para la gestión administrativa del empleado. Este método es esencial para 
     * realizar arqueos de caja, seguimiento de producción histórica y 
     * análisis de ventas globales.
     * </p>
     * @return Una {@link List} que contiene todos los objetos {@link Pedido} en la base de datos.
     * @throws NegocioException Si ocurre una falla en la capa de persistencia 
     * o problemas de conectividad con el servidor de datos.
     */
    @Override
    public List<Pedido> obtenerHistorialEmpleado() throws NegocioException {
        try {
            return pedidoDAO.obtenerHistorialEmpleado();
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al obtener historial de empleado", ex);
        }
    }
}
