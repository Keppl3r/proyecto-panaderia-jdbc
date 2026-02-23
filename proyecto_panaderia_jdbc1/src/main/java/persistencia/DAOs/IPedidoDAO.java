package persistencia.DAOs;

import java.sql.Timestamp;
import java.util.List;
import persistencia.dominio.Pedido;
import persistencia.excepciones.PersistenciaException;

/**
 * DAO general para operaciones sobre pedidos (cambio de estado, búsquedas).
 *
 * @author Adrian Mendoza
 */
public interface IPedidoDAO {

    boolean cambiarEstado(int idPedido, String nuevoEstado) throws PersistenciaException;

    int contarPedidosActivos(int idCliente) throws PersistenciaException;

    List<Pedido> buscarPorTelefono(String telefono) throws PersistenciaException;

    List<Pedido> buscarPorFolio(String folio) throws PersistenciaException;

    List<Pedido> buscarPorRangoFechas(Timestamp inicio, Timestamp fin) throws PersistenciaException;

    List<Pedido> obtenerPedidosPendientesYListos() throws PersistenciaException;

    List<Pedido> obtenerHistorialCliente(int idCliente) throws PersistenciaException;

    Pedido obtenerPorId(int idPedido) throws PersistenciaException;
}
