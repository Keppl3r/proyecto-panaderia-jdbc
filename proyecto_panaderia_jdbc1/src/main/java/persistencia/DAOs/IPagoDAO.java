package persistencia.DAOs;

import persistencia.excepciones.PersistenciaException;

/**
 * DAO para registro de pagos.
 *
 * @author Adrian Mendoza
 */
public interface IPagoDAO {

    boolean registrarPago(int idPedido, String metodoPago, double monto) throws PersistenciaException;
}
