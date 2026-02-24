package persistencia.dominio;

import java.sql.Timestamp;

/**
 * Entidad que representa un pedido programado en el sistema de la panadería.
 * <p>
 * Esta clase especializa a {@link Pedido} al permitir la asociación de un beneficio 
 * promocional mediante un {@link Cupon}. Gestiona la lógica de aplicación de 
 * descuentos y la validación de vigencia de los mismos sobre el total del pedido.
 * </p>
 */
public class PedidoProgramado extends Pedido {

    private Integer idCupon; // opcional
    private Cupon cupon;

    /**
     * Constructor por defecto.
     * Invoca la inicialización base de la superclase para establecer estado y fecha.
     */
    public PedidoProgramado() {
        super();
    }

    /**
     * Constructor para la creación inicial de pedidos programados por el cliente.
     * * @param idUsuario Identificador del cliente que realiza la programación.
     * @param numPedido Número correlativo administrativo.
     * @param fechaEntrega Fecha y hora pactada para la entrega futura.
     * @param idCupon Identificador del cupón aplicado (puede ser null).
     */
    public PedidoProgramado(Integer idUsuario, int numPedido, Timestamp fechaEntrega, Integer idCupon) {
        super(idUsuario, numPedido, fechaEntrega);
        this.idCupon = idCupon;
    }

    /**
     * Constructor completo para la reconstrucción de pedidos programados desde persistencia.
     * * @param idPedido Identificador único en la base de datos.
     * @param idUsuario ID del cliente asociado.
     * @param numPedido Número de pedido comercial.
     * @param estado Estado actual del pedido.
     * @param fechaRegistro Marca de tiempo de creación.
     * @param fechaEntrega Marca de tiempo de entrega programada.
     * @param total Monto total bruto del pedido.
     * @param idCupon ID del cupón vinculado.
     */
    public PedidoProgramado(int idPedido, Integer idUsuario, int numPedido, EstadoPedido estado,
            Timestamp fechaRegistro, Timestamp fechaEntrega, Double total, Integer idCupon) {
        super(idPedido, idUsuario, numPedido, estado, fechaRegistro, fechaEntrega, total);
        this.idCupon = idCupon;
    }

    /** @return El identificador del cupón asociado o null si no se aplicó ninguno. */
    public Integer getIdCupon() { 
        return idCupon; 
    }

    /** @param idCupon El ID del cupón a asignar. */
    public void setIdCupon(Integer idCupon) {
        this.idCupon = idCupon; 
    }

    /** @return El objeto {@link Cupon} vinculado al pedido. */
    public Cupon getCupon() { 
        return cupon; 
    }

    /**
     * Asocia un objeto Cupon al pedido y sincroniza automáticamente su identificador.
     * @param cupon Instancia de la entidad Cupon.
     */
    public void setCupon(Cupon cupon) {
        this.cupon = cupon;
        if (cupon != null) {
            this.idCupon = cupon.getIdCupon();
        }
    }

    /**
     * Calcula el monto económico a descontar basándose en el cupón asociado.
     * <p>
     * El descuento solo se calcula si el objeto cupon no es nulo y cumple 
     * con los criterios de vigencia temporal y administrativa.
     * </p>
     * * @return El monto del descuento calculado; {@code 0.0} si el cupón no es válido.
     */
    public Double calcularDescuento() {
        if (cupon != null && cupon.estaVigente()) {
            return (getTotal() * cupon.getPorcentajeDescuento()) / 100.0;
        }
        return 0.0;
    }

    /**
     * Obtiene el monto final a pagar tras aplicar las promociones.
     * @return El resultado de restar el descuento al total bruto del pedido.
     */
    public Double calcularTotalConDescuento() {
        return getTotal() - (calcularDescuento());
    }

    /**
     * Verifica si el pedido tiene una vinculación formal con un cupón.
     * @return {@code true} si tanto el ID como el objeto Cupon están presentes.
     */
    public boolean tieneCupon() {
        return idCupon != null && cupon != null;
    }

    /**
     * Determina si el cupón asociado es apto para su uso actual.
     * @return {@code true} si existe un cupón y este se encuentra vigente.
     */
    public boolean cuponEsValido() {
        return tieneCupon() && cupon.estaVigente();
    }

    /**
     * Representación textual del pedido programado, incluyendo datos de la superclase
     * y detalles de programación.
     * @return Cadena con información de depuración.
     */
    @Override
    public String toString() {
        return "PedidoProgramado{" + super.toString()
                + ", idCupon=" + idCupon
                + ", fechaEntrega=" + getFechaEntrega() + "}";
    }
}