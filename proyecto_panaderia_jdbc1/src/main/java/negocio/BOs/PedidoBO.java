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
import persistencia.dominio.Pedido;
import persistencia.dominio.Pedido.EstadoPedido;
import persistencia.dominio.PedidoExpress;
import persistencia.excepciones.PersistenciaException;

/**
 * @author Adrian Mendoza
 */
public class PedidoBO implements IPedidoBO {

    private IPedidoDAO pedidoDAO;
    private IPedidoExpressDAO pedidoExpressDAO;
    private IPagoDAO pagoDAO;
    private static final Logger LOG = Logger.getLogger(PedidoBO.class.getName());

    public PedidoBO(IPedidoDAO pedidoDAO, IPedidoExpressDAO pedidoExpressDAO, IPagoDAO pagoDAO) {
        this.pedidoDAO = pedidoDAO;
        this.pedidoExpressDAO = pedidoExpressDAO;
        this.pagoDAO = pagoDAO;
    }

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

    @Override
    public List<Pedido> buscarPorTelefono(String tel) throws NegocioException {
        try {
            return pedidoDAO.buscarPorTelefono(tel);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error en búsqueda", ex);
        }
    }

    @Override
    public List<Pedido> buscarPorFolio(String folio) throws NegocioException {
        try {
            return pedidoDAO.buscarPorFolio(folio);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error en búsqueda", ex);
        }
    }

    @Override
    public List<Pedido> buscarPorFechas(Timestamp ini, Timestamp fin) throws NegocioException {
        try {
            return pedidoDAO.buscarPorRangoFechas(ini, fin);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error en búsqueda", ex);
        }
    }

    @Override
    public List<Pedido> obtenerPendientesYListos() throws NegocioException {
        try {
            return pedidoDAO.obtenerPedidosPendientesYListos();
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al obtener pedidos", ex);
        }
    }

    @Override
    public List<Pedido> obtenerHistorial(int idCliente) throws NegocioException {
        try {
            return pedidoDAO.obtenerHistorialCliente(idCliente);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al obtener historial", ex);
        }
    }
}
