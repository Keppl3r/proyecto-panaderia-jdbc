package negocio.BOs;

import negocio.DTOs.PedidoExpressNuevoDTO;
import negocio.excepciones.NegocioException;
import persistencia.dominio.PedidoExpress;

/**
 * @author Adrian
 */
public interface IPedidoExpressBO {

    PedidoExpress crearPedidoExpress(PedidoExpressNuevoDTO pedidoDTO) throws NegocioException;
}
