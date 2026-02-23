package persistencia.DAOs;

import persistencia.dominio.PedidoExpress;
import persistencia.excepciones.PersistenciaException;

/**
 * @author Jazmin
 */
public interface IPedidoExpressDAO {

    public PedidoExpress crear(PedidoExpress pedido) throws PersistenciaException;

    public int generarNumPedido() throws PersistenciaException;

    public PedidoExpress buscarPorFolio(String folio) throws PersistenciaException;
}
