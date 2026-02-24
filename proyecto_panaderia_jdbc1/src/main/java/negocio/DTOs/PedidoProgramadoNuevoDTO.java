/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.DTOs;

import java.sql.Timestamp;
import java.util.List;
import persistencia.dominio.DetallePedido;

/**
 * Data Transfer Object para la planificación de Pedidos Programados.
 * <p>
 * Este objeto transporta la información necesaria para que la panadería 
 * reserve productos y organice su producción futura. Incluye la vinculación 
 * obligatoria con un cliente registrado y opcionalmente un cupón de descuento.
 * </p>
 * @author Adrian Mendoza
 */
public class PedidoProgramadoNuevoDTO {

    private int idCliente;
    private Timestamp fechaEntrega;
    private Integer idCupon;
    private List<DetallePedido> detalles;

    /**
     * Constructor por defecto.
     * Útil para frameworks de serialización y vinculación de datos en la UI.
     */
    public PedidoProgramadoNuevoDTO() {
    }

    /**
     * Constructor completo para inicializar todos los campos del pedido.
     * @param idCliente    Identificador del cliente que realiza la reserva.
     * @param fechaEntrega Fecha y hora pactada para la recolección.
     * @param idCupon      ID del cupón a aplicar (puede ser null).
     * @param detalles     Lista de productos y cantidades que componen la orden.
     */
    public PedidoProgramadoNuevoDTO(int idCliente, Timestamp fechaEntrega, Integer idCupon, List<DetallePedido> detalles) {
        this.idCliente = idCliente;
        this.fechaEntrega = fechaEntrega;
        this.idCupon = idCupon;
        this.detalles = detalles;
    }

    /**
     * Obtiene el identificador del cliente que realiza la programación.
     * @return ID único del cliente (llave primaria de la tabla Clientes).
     */
    public int getIdCliente() {
        return idCliente;
    }

    /**
     * Asigna el cliente al pedido. 
     * <p>Esta relación es obligatoria para la trazabilidad de pedidos programados.</p>
     * @param idCliente Identificador del cliente autenticado en la sesión.
     */
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * Recupera la fecha y hora pactada para la recolección del pedido.
     * @return Objeto {@link Timestamp} con la precisión de entrega.
     */
    public Timestamp getFechaEntrega() {
        return fechaEntrega;
    }

    /**
     * Establece el momento en que el cliente recogerá sus productos.
     * <p>Importante: La capa de negocio (BO) validará que este valor sea 
     * al menos 2 horas mayor a la hora actual del sistema.</p>
     * @param fechaEntrega Estampa de tiempo para la planificación de producción.
     */
    public void setFechaEntrega(Timestamp fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    /**
     * Obtiene el ID del cupón de descuento aplicado, si existe.
     * @return El ID del cupón o {@code null} si el pedido se procesa a precio regular.
     */
    public Integer getIdCupon() {
        return idCupon;
    }

    /**
     * Asigna un cupón promocional al pedido.
     * <p>Se utiliza la clase {@link Integer} envolvente para permitir 
     * valores nulos en compras sin promoción activa.</p>
     * @param idCupon Identificador del beneficio comercial.
     */
    public void setIdCupon(Integer idCupon) {
        this.idCupon = idCupon;
    }

    /**
     * Recupera la lista de productos y cantidades que componen la orden.
     * @return Lista de {@link DetallePedido}.
     */
    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    /**
     * Define los artículos seleccionados por el cliente.
     * <p>Esta lista será procesada por el BO para calcular totales y 
     * validar la disponibilidad de stock para la fecha futura.</p>
     * @param detalles Colección de productos para la orden.
     */
    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }
}