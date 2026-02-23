/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.dominio;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * Clase que representa un pedido express.
 * Hereda de Pedido
 * @author Jazmin
 * @author Adrian Mendoza
 */
public class PedidoExpress extends Pedido {

    private String folio;
    private String pin;              
    private String pinTextoPlano;    
    private Timestamp tiempoLimite;
    private Timestamp tiempoRecoleccion;

    public PedidoExpress() {
        super();
    }

    public PedidoExpress(int numPedido, String folio, String pin) {
        super(null, numPedido, null); // Sin usuario, sin fecha entrega
        this.folio = folio;
        this.pin = pin;
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

    public String getPinTextoPlano() {
        return pinTextoPlano;
    }

    public void setPinTextoPlano(String pinTextoPlano) {
        this.pinTextoPlano = pinTextoPlano;
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

    /**
     * Verifica si el tiempo límite de recolección ha expirado.
     * @return true si ya pasaron los 20 minutos
     */
    public boolean tiempoExpirado() {
        if (tiempoLimite == null) return false;
        return new Timestamp(System.currentTimeMillis()).after(tiempoLimite);
    }

    /**
     * Verifica si el pedido express puede ser entregado.
     * Debe estar LISTO y dentro del tiempo límite.
     */
    public boolean puedeSerEntregado() {
        return getEstado() == Pedido.EstadoPedido.LISTO && !tiempoExpirado();
    }

    @Override
    public String toString() {
        return "PedidoExpress{" + super.toString()
                + ", folio='" + folio + "'"
                + ", tiempoLimite=" + tiempoLimite + "}";
    }
}
