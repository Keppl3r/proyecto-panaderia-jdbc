package negocio.BOs;

import negocio.DTOs.PedidoProgramadoNuevoDTO;
import negocio.excepciones.NegocioException;
import persistencia.dominio.PedidoProgramado;

public interface IPedidoProgramadoBO {
  PedidoProgramado programarPedido(PedidoProgramadoNuevoDTO pedidoDTO) throws NegocioException;
}