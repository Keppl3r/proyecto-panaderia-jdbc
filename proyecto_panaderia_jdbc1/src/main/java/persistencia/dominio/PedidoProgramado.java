package persistencia.dominio;

import java.sql.Timestamp;

/**
 * Entidad que representa un pedido programado hereda de PEDIDOS
 */
public class PedidoProgramado extends Pedido {

    private Integer idCupon; // opcional
    private Cupon cupon;

    // Constructores
    public PedidoProgramado() {
        super();
    }

    public PedidoProgramado(Integer idUsuario, int numPedido, Timestamp fechaEntrega, Integer idCupon) {
        super(idUsuario, numPedido, fechaEntrega);
        this.idCupon = idCupon;
    }

    public PedidoProgramado(int idPedido, Integer idUsuario, int numPedido, EstadoPedido estado,
            Timestamp fechaRegistro, Timestamp fechaEntrega, Double total, Integer idCupon) {
        super(idPedido, idUsuario, numPedido, estado, fechaRegistro, fechaEntrega, total);
        this.idCupon = idCupon;
    }

    // Getters y Setters específicos
    public Integer getIdCupon() {
        return idCupon;
    }

    public void setIdCupon(Integer idCupon) {
        this.idCupon = idCupon;
    }

    public Cupon getCupon() {
        return cupon;
    }

    public void setCupon(Cupon cupon) {
        this.cupon = cupon;
        if (cupon != null) {
            this.idCupon = cupon.getIdCupon();
        }
    }

    // Métodos de negocio específicos
    public Double calcularDescuento() {
        if (cupon != null && cupon.estaVigente()) {
            Double descuento = 
                    (getTotal()*cupon.getPorcentajeDescuento())
                    /100.0;
                   
            return descuento;
        }
        return 0.0;
    }

    public Double calcularTotalConDescuento() {
        return getTotal()-(calcularDescuento());
    }

    public boolean tieneCupon() {
        if(idCupon != null && cupon != null) return true;
        return false;
    }

    public boolean cuponEsValido() {
        return tieneCupon() && cupon.estaVigente();
    }
/**
 * PENDIENTE
 */
//    // Validaciones específicas de pedido programado
//    public boolean puedeSerModificado() {
//        Timestamp ahora = new Timestamp(System.currentTimeMillis());
//        // Se puede modificar si faltan más de 2 horas para la entrega
//        long dosHorasEnMs = 2 * 60 * 60 * 1000;
//        return getFechaEntrega().getTime() - ahora.getTime() > dosHorasEnMs;
//    }
//
//    public long horasParaEntrega() {
//        Timestamp ahora = new Timestamp(System.currentTimeMillis());
//        long diferencia = getFechaEntrega().getTime() - ahora.getTime();
//        return diferencia / (60 * 60 * 1000); // Convertir a horas
//    }

    @Override
    public String toString() {
        return "PedidoProgramado{" + super.toString()
                + ", idCupon=" + idCupon
                + ", fechaEntrega=" + getFechaEntrega() + "}";
    }
}
