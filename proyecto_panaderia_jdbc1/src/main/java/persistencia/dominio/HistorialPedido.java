/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.dominio;

import java.sql.Date;

/**
 *
 * @author Jazmin
 */
public class HistorialPedido {
    private int idHistorialPedido;
    private Pedido idPedido;
    private String estado;
    private Date fechaHora;

    public HistorialPedido() {
    }

    public HistorialPedido(int idHistorialPedido, Pedido idPedido, String estado, Date fechaHora) {
        this.idHistorialPedido = idHistorialPedido;
        this.idPedido = idPedido;
        this.estado = estado;
        this.fechaHora = fechaHora;
    }

    public int getIdHistorialPedido() {
        return idHistorialPedido;
    }

    public void setIdHistorialPedido(int idHistorialPedido) {
        this.idHistorialPedido = idHistorialPedido;
    }

    public Pedido getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Pedido idPedido) {
        this.idPedido = idPedido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    @Override
    public String toString() {
        return "HistorialPedido{" + "idHistorialPedido=" + idHistorialPedido + ", idPedido=" + idPedido + ", estado=" + estado + ", fechaHora=" + fechaHora + '}';
    }
    
    
    
}
