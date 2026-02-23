package persistencia.DAOs;

import java.sql.Timestamp;
import persistencia.dominio.PedidoProgramado;
import persistencia.excepciones.PersistenciaException;

/**
 * @author Jazmin
 */
public interface IPedidoProgramadoDAO {
    PedidoProgramado crear(PedidoProgramado pedido) throws PersistenciaException;

    int generarNumPedido() throws PersistenciaException;

    boolean validarFechaEntrega(Timestamp fechaEntrega) throws PersistenciaException;
}