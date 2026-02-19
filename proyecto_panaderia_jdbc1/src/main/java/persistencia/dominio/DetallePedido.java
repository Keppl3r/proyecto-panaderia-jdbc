package persistencia.dominio;



/**
 * Entidad que representa el detalle de un pedido
 */
public class DetallePedido {

    private int idDetallePedido;
    private int idPedido;
    private int idProducto;
    private int cantidad;
    private Double precio;
    private Double subtotal; // cantidad * precio
    private String notas;

    private Producto producto;

    // Constructores
    public DetallePedido() {
    }

    public DetallePedido(int idPedido, int idProducto, int cantidad, Double precio, String notas) {
        this.idPedido = idPedido;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.notas = notas;
        calcularSubtotal();
    }

    public DetallePedido(int idDetallePedido, int idPedido, int idProducto, int cantidad,
            Double precio, Double subtotal, String notas) {
        this.idDetallePedido = idDetallePedido;
        this.idPedido = idPedido;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.subtotal = subtotal;
        this.notas = notas;
    }

    // Getters y Setters
    public int getIdDetallePedido() {
        return idDetallePedido;
    }

    public void setIdDetallePedido(int idDetallePedido) {
        this.idDetallePedido = idDetallePedido;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        calcularSubtotal();
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
        calcularSubtotal();
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        if (producto != null) {
            this.idProducto = producto.getIdProducto();
            // Usar el precio actual del producto si no se ha establecido
            if (this.precio == null) {
                this.precio = producto.getPrecio();
            }
        }
        
    }
    
    public void calcularSubtotal() {
        if (precio != null && cantidad > 0) {
            this.subtotal = precio * cantidad;
        } else {
            this.subtotal = 0.0;
        }
    }

    // Validaciones
    public boolean esValido() {
        if (cantidad > 0 && precio != null && precio > 0) return true;
        return false;
    }

         @Override
         public String toString() {
             return "DetallePedido{idDetallePedido=" + idDetallePedido +
                    ", cantidad=" + cantidad + ", precio=" + precio +
                    ", subtotal=" + subtotal + "}";
         }

}
