/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio.BOs;

import java.util.List;
import negocio.excepciones.NegocioException;
import persistencia.dominio.Producto;

/**
 * Interfaz de la Capa de Negocio para la gestión de Productos.
 * <p>
 * Define las operaciones para la administración del catálogo de la panadería,
 * permitiendo la consulta de existencias y la manipulación del estado de 
 * disponibilidad de los artículos (panes, repostería).
 * @author Adrian Mendoza
 */
public interface IProductoBO {
    /**
     * Recupera únicamente los productos que tienen el marcador de disponibilidad activo.
     * Es el método principal utilizado para poblar la vitrina virtual del cliente.
     * * @return Lista de objetos {@link Producto} listos para la venta.
     * @throws NegocioException Si ocurre un error al consultar el catálogo.
     */
    public List<Producto> obtenerProductoDisponibles() throws NegocioException;
    /**
     * Recupera la totalidad de los productos registrados en el sistema, 
     * independientemente de si están disponibles o no.
     * Útil para módulos de administración e inventario.
     * * @return Lista completa de productos en la base de datos.
     * @throws NegocioException Si hay fallos en la comunicación con la persistencia.
     */
    public List<Producto> obtenerTodos() throws NegocioException;
    /**
     * Localiza un producto específico mediante su identificador único.
     * * @param idProducto ID de la llave primaria del producto.
     * @return El objeto {@link Producto} correspondiente.
     * @throws NegocioException Si el producto no existe o el ID es inválido.
     */
    public Producto obtenerPorId(int idProducto) throws NegocioException;
    /**
     * Cambia el estado de venta de un producto.
     * Permite a los empleados ocultar o mostrar productos en la interfaz de cliente
     * (por ejemplo, cuando se agota una tanda de pan dulce).
     * * @param idProducto  ID del producto a modificar.
     * @param disponible  Nuevo estado (true para visible, false para oculto).
     * @return {@code true} si la actualización fue exitosa.
     * @throws NegocioException Si el ID no se encuentra o hay errores de actualización.
     */
    public boolean actualizarDisponibilidad(int idProducto, boolean disponible) throws NegocioException;
    
}