/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.dominio;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jazmin
 */
public class PedidoExpress {
    private int idPedido;
    private String folio;
    private String pin;
    private Timestamp tiempoLimite;
    private Timestamp tiempoRecoleccion;
    private List<DetallePedido> detalles;

    public PedidoExpress() {
        this.detalles = new ArrayList<>();
       
    }

    public PedidoExpress(int idPedido, String folio, String pin, Timestamp tiempoLimite, Timestamp tiempoRecoleccion) {
        this.idPedido = idPedido;
        this.folio = folio;
        this.pin = pin;
        this.tiempoLimite = tiempoLimite;
        this.tiempoRecoleccion = tiempoRecoleccion;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Timestamp getTiempoLimite() {
        return tiempoLimite;
    }

    public void setTiempoLimite(Timestamp tiempoLimite) {
        this.tiempoLimite = tiempoLimite;
    }

    public Timestamp getTiempoRecoleccion() {
        return tiempoRecoleccion;
    }

    public void setTiempoRecoleccion(Timestamp tiempoRecoleccion) {
        this.tiempoRecoleccion = tiempoRecoleccion;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }
    
  
    @Override
    public String toString() {
        return "PedidoExpress{" + "idPedido=" + idPedido + ", folio=" + folio + ", pin=" + pin + ", tiempoLimite=" + tiempoLimite + ", tiempoRecoleccion=" + tiempoRecoleccion + ", detalles=" + detalles + '}';
    }
    

  
    
    
}
