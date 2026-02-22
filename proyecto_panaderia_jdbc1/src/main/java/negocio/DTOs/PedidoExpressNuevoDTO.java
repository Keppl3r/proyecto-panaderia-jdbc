/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.DTOs;


/**
 *
 * @author Adrian Mendoza
 */
public class PedidoExpressNuevoDTO {

    private java.util.List<persistencia.dominio.DetallePedido> detalles;

    public PedidoExpressNuevoDTO() {
    }

    public PedidoExpressNuevoDTO(java.util.List<persistencia.dominio.DetallePedido> detalles) {
        this.detalles = detalles;
    }

    public java.util.List<persistencia.dominio.DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(java.util.List<persistencia.dominio.DetallePedido> detalles) {
        this.detalles = detalles;
    }
}
