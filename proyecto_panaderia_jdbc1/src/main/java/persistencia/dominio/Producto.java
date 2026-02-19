package persistencia.dominio;

import java.util.Objects;

/**
 * Entidad que representa un producto de la panadería.
 *
 * @author Adrian Mendoza
 */
public class Producto {

    private int idProducto;
    private String nombre;
    private String tipo;
    private String descripcion;
    private float precio;
    private boolean disponible;

    public Producto() {
    }

    public Producto(int idProducto, String nombre, String tipo, String descripcion, float precio, boolean disponible) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.disponible = disponible;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "Producto{" + "idProducto=" + idProducto + ", nombre=" + nombre + ", tipo=" + tipo + ", descripcion=" + descripcion + ", precio=" + precio + ", disponible=" + disponible + '}';
    }

    
    
}
