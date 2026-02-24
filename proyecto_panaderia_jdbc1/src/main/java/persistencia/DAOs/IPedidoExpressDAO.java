package persistencia.DAOs;

import persistencia.dominio.PedidoExpress;
import persistencia.excepciones.PersistenciaException;

/**
 * Interfaz que define las operaciones de persistencia exclusivas para Pedidos Express.
 * <p>
 * Un pedido express se caracteriza por ser una venta inmediata en mostrador, 
 * por lo cual esta interfaz se enfoca en la creación ágil y la recuperación 
 * rápida mediante folios generados dinámicamente.
 * </p>
 * @author Jazmin
 */
public interface IPedidoExpressDAO {
    /**
     * Registra un nuevo pedido express en la base de datos.
     * <p>
     * La implementación debe realizar la inserción tanto en la tabla maestra 
     * de pedidos como en la tabla específica de pedidos express y sus detalles, 
     * preferiblemente bajo una estructura transaccional.
     * </p>
     * @param pedido Objeto con la información de la venta inmediata.
     * @return El objeto {@link PedidoExpress} persistido, incluyendo su ID generado.
     * @throws PersistenciaException Si ocurre un error de integridad o de conexión.
     */
    public PedidoExpress crear(PedidoExpress pedido) throws PersistenciaException;
    /**
     * Genera un número de pedido secuencial o aleatorio según la lógica de negocio.
     * <p>
     * Este número se utiliza como identificador visual para el ticket del cliente 
     * y para el monitoreo en las pantallas de entrega rápida.
     * </p>
     * @return Un entero que representa el siguiente número de pedido disponible.
     * @throws PersistenciaException Si falla la consulta al contador de la base de datos.
     */
    public int generarNumPedido() throws PersistenciaException;
    /**
     * Localiza un pedido express utilizando su folio único.
     * <p>
     * Este método es vital para que el empleado pueda validar la orden 
     * cuando el cliente presenta su ticket en el mostrador.
     * </p>
     * @param folio Cadena de texto única que identifica la orden.
     * @return El objeto {@link PedidoExpress} encontrado o {@code null} si no existe.
     * @throws PersistenciaException Si ocurre un error técnico durante la búsqueda.
     */
    public PedidoExpress buscarPorFolio(String folio) throws PersistenciaException;
}
