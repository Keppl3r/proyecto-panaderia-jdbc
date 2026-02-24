package persistencia.DAOs;

import java.sql.Timestamp;
import persistencia.dominio.PedidoProgramado;
import persistencia.excepciones.PersistenciaException;

/**
 * Interfaz que define las operaciones de persistencia para la gestión de Pedidos Programados.
 * <p>
 * A diferencia de los pedidos express, esta interfaz maneja la lógica de reservación 
 * a futuro, integrando validaciones de tiempos de entrega y asignación de folios 
 * para clientes registrados.
 * </p>
 * * @author Jazmin
 */
public interface IPedidoProgramadoDAO {
    /**
     * Registra un nuevo pedido programado en la base de datos.
     * <p>
     * Debe persistir la información en las tablas PEDIDOS, PEDIDOS_PROGRAMADOS 
     * y DETALLES_PEDIDO de manera atómica mediante una transacción.
     * </p>
     * @param pedido Instancia con los datos del cliente, productos y fecha de entrega.
     * @return El objeto {@link PedidoProgramado} con su ID asignado por el sistema.
     * @throws PersistenciaException Si ocurre un error de integridad o de comunicación SQL.
     */
    PedidoProgramado crear(PedidoProgramado pedido) throws PersistenciaException;
    /**
     * Genera un número de identificación secuencial para el flujo de trabajo interno.
     * @return Un entero representativo para el control de la orden.
     * @throws PersistenciaException Si falla la consulta al contador de pedidos.
     */
    int generarNumPedido() throws PersistenciaException;
    /**
     * Verifica la disponibilidad de la fecha de entrega en la base de datos.
     * <p>
     * Permite validar si la panadería tiene capacidad de producción para el 
     * horario solicitado o si la fecha cumple con las restricciones mínimas 
     * de anticipación (ej. 2 horas).
     * </p>
     * @param fechaEntrega Estampa de tiempo solicitada por el cliente.
     * @return {@code true} si la fecha es válida y está disponible; {@code false} en caso contrario.
     * @throws PersistenciaException Si hay un error técnico en la validación.
     */
    boolean validarFechaEntrega(Timestamp fechaEntrega) throws PersistenciaException;
}