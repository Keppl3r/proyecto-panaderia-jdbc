package persistencia.dominio;

import java.sql.Timestamp;

/**
 * Entidad que representa un pedido de tipo Express en el sistema.
 * <p>
 * Un pedido express se caracteriza por no requerir un usuario registrado obligatoriamente,
 * basando su seguridad en un sistema de Folio y PIN. Además, cuenta con un mecanismo 
 * de tiempo límite para la recolección de los productos.
 * </p>
 * * @author Jazmin
 * @author Adrian Mendoza
 */
public class PedidoExpress extends Pedido {

    private String folio;
    private String pin;              
    private String pinTextoPlano;    
    private Timestamp tiempoLimite;
    private Timestamp tiempoRecoleccion;

    /**
     * Constructor por defecto.
     * Invoca al constructor de la superclase para inicializar los valores base.
     */
    public PedidoExpress() {
        super();
    }

    /**
     * Constructor simplificado para la creación de un nuevo pedido express.
     * <p>
     * Inicializa el pedido sin un usuario asociado ni fecha de entrega programada, 
     * centrando la identidad en el folio y el pin de seguridad.
     * </p>
     * * @param numPedido Número correlativo del pedido en el sistema.
     * @param folio Cadena única de identificación para la recolección.
     * @param pin Código de seguridad (hash) para validar la entrega.
     */
    public PedidoExpress(int numPedido, String folio, String pin) {
        super(null, numPedido, null);
        this.folio = folio;
        this.pin = pin;
    }

    /** @return El folio único de identificación del pedido. */
    public String getFolio() { 
        return folio; 
    }

    /** @param folio El folio a asignar. */
    public void setFolio(String folio) { 
        this.folio = folio;
    }

    /** @return El PIN de seguridad (usualmente encriptado). */
    public String getPin() {
        return pin;
    }

    /** @param pin El PIN de seguridad a asignar. */
    public void setPin(String pin) { 
        this.pin = pin; 
    }

    /** @return El PIN en formato legible (utilizado temporalmente durante la creación). */
    public String getPinTextoPlano() {
        return pinTextoPlano; 
    }

    /** @param pinTextoPlano El PIN en texto claro a asignar. */
    public void setPinTextoPlano(String pinTextoPlano) { 
        this.pinTextoPlano = pinTextoPlano; 
    }

    /** @return La marca de tiempo que indica el límite para recoger el pedido. */
    public Timestamp getTiempoLimite() {
        return tiempoLimite;
    }

    /** @param tiempoLimite El tiempo límite de recolección a asignar. */
    public void setTiempoLimite(Timestamp tiempoLimite) {
        this.tiempoLimite = tiempoLimite; 
    }

    /** @return La fecha y hora exacta en la que se realizó la recolección física. */
    public Timestamp getTiempoRecoleccion() {
        return tiempoRecoleccion; 
    }

    /** @param tiempoRecoleccion La marca de tiempo de la recolección real. */
    public void setTiempoRecoleccion(Timestamp tiempoRecoleccion) { 
        this.tiempoRecoleccion = tiempoRecoleccion; 
    }

    /**
     * Evalúa si el periodo de gracia para la recolección ha concluido.
     * <p>
     * Compara la marca de tiempo actual del sistema contra el {@code tiempoLimite}.
     * </p>
     * * @return {@code true} si la hora actual es posterior al tiempo límite; 
     * {@code false} si aún está dentro del rango o si no hay límite definido.
     */
    public boolean tiempoExpirado() {
        if (tiempoLimite == null) return false;
        return new Timestamp(System.currentTimeMillis()).after(tiempoLimite);
    }

    /**
     * Verifica si el pedido cumple con los requisitos comerciales para ser entregado.
     * <p>
     * Un pedido express solo puede entregarse si su estado es {@code LISTO} 
     * y no ha superado su {@code tiempoLimite}.
     * </p>
     * * @return {@code true} si el estado es válido y hay vigencia temporal; 
     * {@code false} en cualquier otro caso.
     */
    public boolean puedeSerEntregado() {
        return getEstado() == EstadoPedido.LISTO && !tiempoExpirado();
    }

    /**
     * Representación textual que combina los datos de la superclase con los 
     * atributos específicos del pedido express.
     * * @return Cadena con la información de depuración del pedido.
     */
    @Override
    public String toString() {
        return "PedidoExpress{" + super.toString()
                + ", folio='" + folio + "'"
                + ", tiempoLimite=" + tiempoLimite + "}";
    }
}