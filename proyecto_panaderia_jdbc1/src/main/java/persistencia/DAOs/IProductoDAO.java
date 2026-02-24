package persistencia.DAOs;

import java.util.List;
import persistencia.dominio.Producto;
import persistencia.excepciones.PersistenciaException;

/**
 * Interfaz que define las operaciones de persistencia para el catálogo de productos.
 * <p>
 * Gestiona el inventario visual de la panadería, permitiendo filtrar productos 
 * por su estado de disponibilidad actual y administrar la información detallada 
 * de cada artículo disponible para la venta.
 * </p>
 * @author Adrian Mendoza
 */
public interface IProductoDAO {
    /**
     * Recupera únicamente los productos marcados como disponibles para venta inmediata.
     * <p>Utilizado principalmente por el cliente en la vista de selección de productos.</p>
     * @return {@link List} de productos con stock o disponibilidad activa.
     * @throws PersistenciaException Si ocurre un error técnico en la consulta SQL.
     */
    List<Producto> obtenerProductosDisponibles() throws PersistenciaException;
    /**
     * Recupera el catálogo completo de productos, incluyendo aquellos fuera de línea.
     * <p>Ideal para módulos administrativos y de gestión de inventario.</p>
     * @return Lista total de productos registrados.
     * @throws PersistenciaException Si falla la comunicación con la base de datos.
     */
    List<Producto> obtenerTodos() throws PersistenciaException;
    /**
     * Obtiene la información detallada de un producto específico mediante su ID.
     * @param idProducto Identificador único del producto.
     * @return Objeto {@link Producto} o {@code null} si no existe.
     * @throws PersistenciaException Si ocurre un error de acceso a datos.
     */
    Producto obtenerPorId(int idProducto) throws PersistenciaException;
    /**
     * Modifica el estado de disponibilidad de un producto en tiempo real.
     * <p>Permite al panadero ocultar productos que se han agotado durante el turno.</p>
     * @param idProducto Identificador del producto a modificar.
     * @param disponible Nuevo estado (true para mostrar, false para ocultar).
     * @return {@code true} si la actualización fue exitosa.
     * @throws PersistenciaException Si falla la ejecución del UPDATE.
     */
    boolean actualizarDisponibilidad(int idProducto, boolean disponible) throws PersistenciaException;
}
