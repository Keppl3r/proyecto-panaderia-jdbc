package persistencia.dominio;



/**
 * Entidad de dominio que representa una línea de artículo dentro de un pedido.
 * <p>
 * Gestiona la relación específica entre un producto y un pedido, almacenando 
 * datos calculados como el subtotal y persistiendo el precio del producto 
 * en el momento exacto de la transacción para evitar inconsistencias por cambios 
 * futuros en el catálogo.
 * </p>
 */
public class DetallePedido {

    private int idDetallePedido;
    private int idPedido;
    private int idProducto;
    private int cantidad;
    private Double precio;
    private Double subtotal;
    private String notas;
   private Producto producto;

    /**
     * Constructor por defecto.
     */
    public DetallePedido() {
    }

    /**
     * Constructor para crear un nuevo detalle antes de ser persistido.
     * <p>
     * Este constructor dispara automáticamente el cálculo del subtotal 
     * basado en la cantidad y el precio proporcionados.
     * </p>
     * * @param idPedido Identificador del pedido padre.
     * @param idProducto Identificador del producto solicitado.
     * @param cantidad Unidades del producto.
     * @param precio Precio unitario pactado para la venta.
     * @param notas Comentarios adicionales (ej. "Sin ajonjolí", "Bien cocido").
     */
    public DetallePedido(int idPedido, int idProducto, int cantidad, Double precio, String notas) {
        this.idPedido = idPedido;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.notas = notas;
        calcularSubtotal();
    }

    /**
     * Constructor completo para la reconstrucción de objetos desde la base de datos.
     * * @param idDetallePedido Identificador único de la línea de detalle.
     * @param idPedido ID del pedido al que pertenece.
     * @param idProducto ID del producto asociado.
     * @param cantidad Cantidad de artículos.
     * @param precio Precio unitario registrado en la base de datos.
     * @param subtotal Valor total de la línea (cantidad * precio).
     * @param notas Instrucciones especiales.
     */
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

    /** @return El identificador único del detalle. */
    public int getIdDetallePedido() {
        return idDetallePedido; 
    }

    /** @param idDetallePedido El identificador a asignar. */
    public void setIdDetallePedido(int idDetallePedido) { 
        this.idDetallePedido = idDetallePedido;
    }

    /** @return El ID del pedido asociado. */
    public int getIdPedido() { 
        return idPedido; 
    }

    /** @param idPedido El ID del pedido a asignar. */
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    /** @return El ID del producto. */
    public int getIdProducto() {
        return idProducto; 
    }

    /** @param idProducto El ID del producto a asignar. */
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto; 
    }

    /** @return La cantidad de unidades. */
    public int getCantidad() { 
        return cantidad;
    }

    /** * Define la cantidad y recalcula automáticamente el subtotal.
     * @param cantidad Unidades solicitadas. 
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        calcularSubtotal();
    }

    /** @return El precio unitario de la línea. */
    public Double getPrecio() { return precio; }

    /** * Define el precio y recalcula automáticamente el subtotal.
     * @param precio Valor unitario. 
     */
    public void setPrecio(Double precio) {
        this.precio = precio;
        calcularSubtotal();
    }

    /** @return El subtotal calculado. */
    public Double getSubtotal() {
        return subtotal;
    }

    /** @param subtotal Valor total de la línea. */
    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal; 
    }

    /** @return Comentarios o notas del detalle. */
    public String getNotas() {
        return notas; 
    }

    /** @param notas Comentarios adicionales. */
    public void setNotas(String notas) { 
        this.notas = notas;
    }

    /** @return El objeto {@link Producto} completo vinculado. */
    public Producto getProducto() { 
        return producto; 
    }

    /** * Asocia un objeto Producto al detalle. 
     * <p>
     * Si el precio de este detalle es nulo, se hereda automáticamente 
     * el precio actual del producto proporcionado.
     * </p>
     * @param producto Objeto producto de la capa de dominio. 
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
        if (producto != null) {
            this.idProducto = producto.getIdProducto();
            if (this.precio == null) {
                this.precio = producto.getPrecio();
            }
        }
    }
    
    /**
     * Realiza la operación aritmética {@code precio * cantidad}.
     * <p>
     * Si los valores no son válidos (nulos o cantidad cero), 
     * el subtotal se establece en 0.0.
     * </p>
     */
    public void calcularSubtotal() {
        if (precio != null && cantidad > 0) {
            this.subtotal = precio * cantidad;
        } else {
            this.subtotal = 0.0;
        }
    }

    /**
     * Valida la integridad de la información comercial del detalle.
     * @return {@code true} si la cantidad es positiva y el precio es mayor a cero.
     */
    public boolean esValido() {
        return cantidad > 0 && precio != null && precio > 0;
    }

    @Override
    public String toString() {
        return "DetallePedido{idDetallePedido=" + idDetallePedido +
               ", cantidad=" + cantidad + ", precio=" + precio +
               ", subtotal=" + subtotal + "}";
    }
}