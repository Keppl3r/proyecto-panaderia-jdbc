/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.DTOs;

/**
 *
 * @author Adrian Mendoza
 */
public class PedidoProgramadoNuevoDTO {

    private int idCliente;
    private java.sql.Timestamp fechaEntrega;
    private Integer idCupon;
    private java.util.List<persistencia.dominio.DetallePedido> detalles;

    // Constructor vacío
    public PedidoProgramadoNuevoDTO() {
    }

    // Constructor completo
    public PedidoProgramadoNuevoDTO(int idCliente,java.sql.Timestamp fechaEntrega,Integer idCupon,java.util.List<persistencia.dominio.DetallePedido> detalles) {
        this.idCliente = idCliente;
        this.fechaEntrega = fechaEntrega;
        this.idCupon = idCupon;
        this.detalles = detalles;
    }

    // Getters y Setters
    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public java.sql.Timestamp getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(java.sql.Timestamp fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Integer getIdCupon() {
        return idCupon;
    }

    public void setIdCupon(Integer idCupon) {
        this.idCupon = idCupon;
    }

    public java.util.List<persistencia.dominio.DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(java.util.List<persistencia.dominio.DetallePedido> detalles) {
        this.detalles = detalles;
    }
}
