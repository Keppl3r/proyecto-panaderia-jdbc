package persistencia.DAOs;

import persistencia.excepciones.PersistenciaException;

/**
 * Interfaz que define las operaciones de persistencia para el registro de transacciones.
 * <p>
 * Provee el mecanismo necesario para formalizar el cobro de los pedidos, permitiendo
 * registrar el método utilizado y el monto final recibido en la base de datos.
 * </p>
 * @author Adrian Mendoza
 */
public interface IPagoDAO {
    /**
     * Registra un nuevo movimiento financiero asociado a un pedido específico.
     * <p>
     * Este método debe garantizar que el monto coincida con el total calculado 
     * por el sistema y que el estado del pedido se actualice coherentemente 
     * tras la confirmación del pago.
     * </p>
     * @param idPedido   Identificador único del pedido (Express o Programado).
     * @param metodoPago Descripción del medio utilizado (ej. "EFECTIVO", "TARJETA").
     * @param monto      Cantidad total pagada por el cliente.
     * @return {@code true} si el registro fue exitoso; {@code false} en caso contrario.
     * @throws PersistenciaException Si ocurre una falla técnica, como un error 
     * de integridad referencial o pérdida de conexión.
     */
    boolean registrarPago(int idPedido, String metodoPago, double monto) throws PersistenciaException;
}
