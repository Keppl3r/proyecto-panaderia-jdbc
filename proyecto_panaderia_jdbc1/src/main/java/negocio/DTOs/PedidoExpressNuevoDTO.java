/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.DTOs;


/**
 ** Data Transfer Object para la creación de nuevos Pedidos Express.
 * <p>
 * Transporta exclusivamente la lista de productos y cantidades seleccionadas
 * por el cliente en el mostrador o carrito rápido, sin requerir información
 * de sesión o perfiles de usuario.
 * @author Adrian Mendoza
 */
public class PedidoExpressNuevoDTO {

    private java.util.List<persistencia.dominio.DetallePedido> detalles;
    /**
     * Constructor por defecto requerido para serialización y frameworks.
     */
    public PedidoExpressNuevoDTO() {
    }
    /**
     * Constructor para inicializar el DTO con una lista de productos.
     * @param detalles Lista de objetos DetallePedido que contienen ID de producto y cantidad.
     */
    public PedidoExpressNuevoDTO(java.util.List<persistencia.dominio.DetallePedido> detalles) {
        this.detalles = detalles;
    }
    /**
     * Obtiene los productos que conforman la solicitud de pedido.
     * @return Lista de detalles del pedido.
     */
    public java.util.List<persistencia.dominio.DetallePedido> getDetalles() {
        return detalles;
    }
    /**
     * Establece los productos para el nuevo pedido.
     * @param detalles Lista de detalles a procesar.
     */
    public void setDetalles(java.util.List<persistencia.dominio.DetallePedido> detalles) {
        this.detalles = detalles;
    }
}
