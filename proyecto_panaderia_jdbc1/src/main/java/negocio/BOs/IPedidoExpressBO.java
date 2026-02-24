package negocio.BOs;

import negocio.DTOs.PedidoExpressNuevoDTO;
import negocio.excepciones.NegocioException;
import persistencia.dominio.PedidoExpress;

/**
 * Interfaz de la Capa de Negocio para la gestión de Pedidos Express.
 * <p>
 * Define la operación necesaria para procesar órdenes de clientes no registrados
 * o "invitados". Esta interfaz se centra en la transformación de datos de entrada (DTO)
 * hacia una entidad persistente de Pedido Express.
 * </p>
 * * @author Adrian
 */
public interface IPedidoExpressBO {
    /**
     * Crea y registra un nuevo pedido bajo la modalidad Express.
     * <p>
     * La implementación debe validar la disponibilidad de productos, generar el folio
     * único de rastreo, asignar un PIN de seguridad y persistir la orden en la base de datos.
     * </p>
     * * @param pedidoDTO Objeto de transferencia de datos que contiene la información 
     * necesaria para la nueva orden (lista de productos, cantidades).
     * @return El objeto {@link PedidoExpress} creado con su folio y PIN asignados.
     * @throws NegocioException Si la validación de productos falla, el stock es insuficiente 
     * o ocurre un error en la capa de persistencia.
     */
    PedidoExpress crearPedidoExpress(PedidoExpressNuevoDTO pedidoDTO) throws NegocioException;
}
