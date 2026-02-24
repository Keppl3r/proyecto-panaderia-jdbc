package persistencia.dominio;




/**
 * Entidad de dominio que representa un artículo del catálogo de la panadería.
 * <p>
 * Esta clase almacena las propiedades fundamentales de los productos, como su 
 * categorización (tipo), precio de venta y estado de disponibilidad en inventario.
 * Es utilizada transversalmente en la gestión de catálogo, generación de pedidos 
 * y visualización para el cliente.
 * </p>
 * * @author Adrian Mendoza
 */
public class Producto {

    private int idProducto;
    private String nombre;
    private String tipo;
    private String descripcion;
    private Double precio;
    private boolean disponible;

    /**
     * Constructor por defecto.
     * Crea una instancia vacía de producto lista para ser poblada mediante setters.
     */
    public Producto() {
    }

    /**
     * Constructor completo para instanciar productos existentes o nuevos.
     * * @param idProducto  Identificador único del producto en la base de datos.
     * @param nombre      Nombre comercial del pan o artículo.
     * @param tipo        Categoría del producto (ej. "Dulce", "Salado", "Repostería").
     * @param descripcion Detalle de ingredientes o características del artículo.
     * @param precio      Valor unitario del producto para la venta.
     * @param disponible  Estado de visibilidad en el catálogo (true para activo).
     */
    public Producto(int idProducto, String nombre, String tipo, String descripcion, Double precio, boolean disponible) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.disponible = disponible;
    }

    /** @return El identificador numérico del producto. */
    public int getIdProducto() { 
        return idProducto; 
    }

    /** @param idProducto El identificador a asignar. */
    public void setIdProducto(int idProducto) { 
        this.idProducto = idProducto; 
    }

    /** @return El nombre comercial del artículo. */
    public String getNombre() { 
        return nombre;
    }

    /** @param nombre El nombre a asignar. */
    public void setNombre(String nombre) {
        this.nombre = nombre; 
    }

    /** @return La categoría o tipo de pan/producto. */
    public String getTipo() { 
        return tipo; 
    }

    /** @param tipo La categoría a asignar. */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /** @return Una breve descripción del producto. */
    public String getDescripcion() {
        return descripcion;
    }

    /** @param descripcion Los detalles informativos a asignar. */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /** @return El precio unitario actual. */
    public Double getPrecio() {
        return precio; 
    }

    /** @param precio El monto de venta a establecer. */
    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    /** @return {@code true} si el producto puede ser añadido a pedidos. */
    public boolean isDisponible() {
        return disponible; 
    }

    /** @param disponible El estado de disponibilidad a establecer. */
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    /**
     * Genera una cadena descriptiva con todos los atributos del producto.
     * * @return Representación textual detallada del objeto para depuración.
     */
    @Override
    public String toString() {
        return "Producto{" + "idProducto=" + idProducto + ", nombre=" + nombre + 
               ", tipo=" + tipo + ", descripcion=" + descripcion + 
               ", precio=" + precio + ", disponible=" + disponible + '}';
    }
}