package negocio.BOs;

import negocio.DTOs.PedidoProgramadoNuevoDTO;
import negocio.excepciones.NegocioException;
import persistencia.dominio.PedidoProgramado;

/**
/**
 * Interfaz de la Capa de Negocio para la gestión de Pedidos Programados.
 * <p>
 * Define el contrato para la creación de órdenes con fecha de entrega diferida.
 * A diferencia de los pedidos inmediatos, esta interfaz coordina la reserva
 * de productos y la validación de ventanas de tiempo para la entrega.
 */
public interface IPedidoProgramadoBO {
    /**
     * Registra y planifica una nueva orden programada en el sistema.
     * <p>
     * La implementación debe validar que la fecha de entrega sea posterior a la 
     * fecha actual, verificar la capacidad de producción para dicha fecha y 
     * asegurar la integridad de los datos de contacto del cliente.
     * </p>
     * * @param pedidoDTO Objeto de transferencia de datos con la información de la 
     * orden, incluyendo fecha/hora de entrega y lista de productos.
     * @return El objeto {@link PedidoProgramado} persistido y validado.
     * @throws NegocioException Si la fecha es inválida (ej. pasado), si se supera el 
     * límite de pedidos para ese día, o si ocurre un error 
     * en la persistencia.
     */
  PedidoProgramado programarPedido(PedidoProgramadoNuevoDTO pedidoDTO) throws NegocioException;
}