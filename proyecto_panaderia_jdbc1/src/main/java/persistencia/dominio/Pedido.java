package persistencia.dominio;

import java.sql.Timestamp;
import java.util.List;

/**
 * Entidad de dominio que representa la estructura base de un Pedido.
 * <p>
 * Esta clase centraliza la información general de una transacción, incluyendo
 * el control de estados, cálculos de totales y la gestión de fechas de registro
 * y entrega. Sirve como base para especializaciones como pedidos express o programados.
 * </p>
 * @author Jazmin
 * @author Adrian Mendoza
 */
public class Pedido {

    private int idPedido;
    private Integer idUsuario;
    private int numPedido;
    private EstadoPedido estado;
    private Timestamp fechaRegistro;
    private Timestamp fechaEntrega;
    private Double total;
    private Cliente cliente;
    private List<DetallePedido> detalles;

    /**
     * Enumeración que define los estados posibles en el ciclo de vida de un pedido.
     */
    public enum EstadoPedido {
        PENDIENTE, LISTO, ENTREGADO, CANCELADO, NO_ENTREGADO;

        /**
         * Proporciona una representación amigable para el usuario del estado actual.
         * @return Cadena de texto con la descripción formateada del estado.
         */
        public String getDescripcion() {
            return switch (this) {
                case PENDIENTE -> "Pendiente";
                case LISTO -> "Listo";
                case ENTREGADO -> "Entregado";
                case CANCELADO -> "Cancelado";
                case NO_ENTREGADO -> "No Entregado";
                default -> name();
            };
        }
    }

    /**
     * Constructor por defecto.
     * Inicializa el pedido con la fecha actual, estado {@code PENDIENTE} y total en cero.
     */
    public Pedido() {
        this.fechaRegistro = new Timestamp(System.currentTimeMillis());
        this.estado = EstadoPedido.PENDIENTE;
        this.total = 0.0;
    }

    /**
     * Constructor para la creación inicial de un pedido con datos de entrega.
     * @param idUsuario Identificador del usuario que realiza el pedido (puede ser nulo para ventas rápidas).
     * @param numPedido Número correlativo del pedido para fines administrativos.
     * @param fechaEntrega Fecha y hora programada para la entrega.
     */
    public Pedido(Integer idUsuario, int numPedido, Timestamp fechaEntrega) {
        this();
        this.idUsuario = idUsuario;
        this.numPedido = numPedido;
        this.fechaEntrega = fechaEntrega;
    }

    /**
     * Constructor completo para reconstrucción de objetos desde la capa de persistencia.
     * @param idPedido Identificador único en base de datos.
     * @param idUsuario ID del usuario asociado.
     * @param numPedido Número de pedido comercial.
     * @param estado Estado actual del ciclo de vida.
     * @param fechaRegistro Marca de tiempo de creación.
     * @param fechaEntrega Marca de tiempo de entrega prevista.
     * @param total Monto económico total del pedido.
     */
    public Pedido(int idPedido, Integer idUsuario, int numPedido, EstadoPedido estado,
                  Timestamp fechaRegistro, Timestamp fechaEntrega, Double total) {
        this.idPedido = idPedido;
        this.idUsuario = idUsuario;
        this.numPedido = numPedido;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
        this.fechaEntrega = fechaEntrega;
        this.total = total;
    }

    /** @return El ID único del pedido. */
    public int getIdPedido() { 
        return idPedido; 
    }

    /** @param idPedido El ID a asignar. */
    public void setIdPedido(int idPedido) { 
        this.idPedido = idPedido;
    }

    /** @return El ID del usuario o cliente asociado. */
    public Integer getIdUsuario() { 
        return idUsuario; 
    }

    /** @param idUsuario El ID de usuario a asignar. */
    public void setIdUsuario(Integer idUsuario) { 
        this.idUsuario = idUsuario;
    }

    /** @return El número de pedido comercial. */
    public int getNumPedido() {
        return numPedido; 
    }

    /** @param numPedido El número de pedido a asignar. */
    public void setNumPedido(int numPedido) { 
        this.numPedido = numPedido; 
    }

    /** @return El estado actual del pedido. */
    public EstadoPedido getEstado() { 
        return estado; 
    }

    /** @param estado El nuevo estado a establecer. */
    public void setEstado(EstadoPedido estado) { 
        this.estado = estado; 
    }

    /** @return La fecha en que se registró el pedido. */
    public Timestamp getFechaRegistro() {
        return fechaRegistro; 
    }

    /** @param fechaRegistro La fecha de registro a asignar. */
    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro; 
    }

    /** @return La fecha programada para la entrega. */
    public Timestamp getFechaEntrega() { 
        return fechaEntrega;
    }

    /** @param fechaEntrega La fecha de entrega a asignar. */
    public void setFechaEntrega(Timestamp fechaEntrega) {
        this.fechaEntrega = fechaEntrega; 
    }

    /** @return El monto total acumulado. */
    public Double getTotal() { 
        return total; 
    }

    /** @param total El monto total a asignar. */
    public void setTotal(Double total) { 
        this.total = total;
    }

    /** @return El objeto {@link Cliente} vinculado al pedido. */
    public Cliente getCliente() { 
        return cliente; 
    }

    /**
     * Asocia un cliente al pedido y sincroniza automáticamente el {@code idUsuario}.
     * @param cliente Objeto cliente.
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        if (cliente != null) {
            this.idUsuario = cliente.getIdUsuario();
        }
    }

    /** @return Lista de {@link DetallePedido} que componen el pedido. */
    public List<DetallePedido> getDetalles() {
        return detalles; 
    }

    /** @param detalles La lista de detalles a asignar. */
    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles; 
    }

    /**
     * Realiza la sumatoria de los subtotales de todos los detalles asociados.
     * <p>
     * Si la lista de detalles es nula o está vacía, el total se reinicia a 0.0.
     * </p>
     */
    public void calcularTotal() {
        this.total = 0.0;
        if (detalles != null && !detalles.isEmpty()) {
            for (DetallePedido detalle : detalles) {
                if (detalle.getSubtotal() != null) {
                    this.total += detalle.getSubtotal();
                }
            }
        }
    }

    /**
     * Determina si el pedido es de naturaleza programada basándose en la fecha de entrega.
     * @return {@code true} si existe una fecha de entrega posterior a la de registro.
     */
    public boolean esProgramado() {
        return fechaEntrega != null && fechaEntrega.after(fechaRegistro);
    }

    /**
     * Verifica si el pedido cumple las condiciones para ser cancelado.
     * @return {@code true} si el estado actual es {@code PENDIENTE}.
     */
    public boolean puedeSerCancelado() {
        return estado == EstadoPedido.PENDIENTE;
    }

    @Override
    public String toString() {
        return "Pedido{idPedido=" + idPedido + ", numPedido=" + numPedido
                + ", estado=" + estado + ", total=" + total + "}";
    }
}