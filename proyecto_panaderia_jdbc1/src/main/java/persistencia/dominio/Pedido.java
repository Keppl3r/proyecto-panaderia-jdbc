package persistencia.dominio;

import java.sql.Timestamp;
import java.util.List;

/**
 * Entidad que representa un pedido base
 */
public class Pedido {

    private int idPedido;
    private int numPedido;
    private EstadoPedido estado;
    private Timestamp fechaRegistro;
    private Timestamp fechaEntrega;
    private Double total;

    private Cliente cliente;
    private List<DetallePedido> detalles;

    public enum EstadoPedido {
        PENDIENTE, LISTO, ENTREGADO, CANCELADO, NO_RECLAMADO
    }

    public Pedido() {
        this.fechaRegistro = new Timestamp(System.currentTimeMillis());
        this.estado = EstadoPedido.PENDIENTE;
        this.total = 0.0;
    }

    public Pedido(Cliente cliente, int numPedido, Timestamp fechaEntrega) {
        this();
        this.cliente = cliente;
        this.numPedido = numPedido;
        this.fechaEntrega = fechaEntrega;
    }

    public Pedido(int idPedido, Cliente cliente, int numPedido, EstadoPedido estado,
            Timestamp fechaRegistro, Timestamp fechaEntrega, Double total) {
        this.idPedido = idPedido;
        this.numPedido = numPedido;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
        this.fechaEntrega = fechaEntrega;
        this.total = total;
    }

    // Getters y Setters
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public Cliente getcliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public int getNumPedido() {
        return numPedido;
    }

    public void setNumPedido(int numPedido) {
        this.numPedido = numPedido;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Timestamp getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Timestamp fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Cliente getCliente() {
        return cliente;
    }

   
    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }

    // Métodos de negocio necesarios para el CU
    public void calcularTotal() {
        this.total = 0.0;

        if (detalles != null && !detalles.isEmpty()) {
            for (DetallePedido detalle : detalles) {
                this.total += detalle.getSubtotal();
            }
        }
    }

    public boolean esProgramado() {
        return fechaEntrega != null && fechaEntrega.after(fechaRegistro);
    }

    public boolean puedeSerCancelado() {
        return estado == EstadoPedido.PENDIENTE;
    }

    @Override
    public String toString() {
        return "Pedido{idPedido=" + idPedido + ", numPedido=" + numPedido
                + ", estado=" + estado + ", total=" + total + "}";
    }
}
