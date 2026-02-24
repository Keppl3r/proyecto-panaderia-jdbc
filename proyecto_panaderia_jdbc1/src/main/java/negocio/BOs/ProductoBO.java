package negocio.BOs;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.excepciones.NegocioException;
import persistencia.DAOs.IProductoDAO;
import persistencia.dominio.Producto;
import persistencia.excepciones.PersistenciaException;

/**
 * Implementación de la lógica de negocio para la gestión de productos.
 * <p>
 * Esta clase actúa como intermediaria entre la capa de persistencia (DAO) 
 * y la presentación, encargándose de validar IDs, manejar el registro de 
 * errores (logging) y transformar excepciones técnicas en excepciones de negocio.
 * </p>
 * * @author Jazmin
 */
public class ProductoBO implements IProductoBO {
    
    private IProductoDAO productoDAO;
    private static final Logger LOG = Logger.getLogger(ProductoBO.class.getName());
    
    /**
     * Constructor que inyecta la dependencia del DAO de productos.
     * @param productoDAO Interfaz de acceso a datos para productos.
     */
    public ProductoBO(IProductoDAO productoDAO) {
        this.productoDAO = productoDAO;
    }
    /**
     * Obtiene la lista de productos marcados como disponibles para su venta inmediata.
     * @return {@link List} de {@link Producto} con disponibilidad activa.
     * @throws NegocioException Si ocurre un error en la capa de datos.
     */
    @Override
    public List<Producto> obtenerProductoDisponibles() throws NegocioException {
        try {
            return productoDAO.obtenerProductosDisponibles();
        } catch (PersistenciaException e) {
            LOG.log(Level.SEVERE, "Error al consultar productos disponibles", e);
            throw new NegocioException("Ocurrió un error al obtener los productos");
        }
    }
    /**
     * Recupera todos los productos registrados, incluyendo aquellos fuera de inventario.
     * @return Lista completa de productos del catálogo.
     * @throws NegocioException Si falla la comunicación con la persistencia.
     */
    @Override
    public List<Producto> obtenerTodos() throws NegocioException {
        try {
            return productoDAO.obtenerTodos();
        } catch (PersistenciaException e) {
            LOG.log(Level.SEVERE, "Error al obtener todos los productos", e);
            throw new NegocioException("Error al obtener los productos");
        }
    }
    /**
     * Modifica el estado de disponibilidad de un producto específico.
     * @param idProducto Identificador único del producto.
     * @param disponible {@code true} para habilitar, {@code false} para deshabilitar.
     * @return {@code true} si la actualización fue exitosa en la base de datos.
     * @throws NegocioException Si el producto no existe o hay errores técnicos.
     */
    @Override
    public boolean actualizarDisponibilidad(int idProducto, boolean disponible) throws NegocioException {
        try {
            return productoDAO.actualizarDisponibilidad(idProducto, disponible);
        } catch (PersistenciaException e) {
            LOG.log(Level.SEVERE, "Error al actualizar disponibilidad del producto " + idProducto, e);
            throw new NegocioException("Error al actualizar la disponibilidad");
        }
    }
    /**
     * Busca un producto por su ID y valida su existencia antes de retornarlo.
     * @param idProducto Identificador a buscar.
     * @return El objeto {@link Producto} encontrado.
     * @throws NegocioException Si el ID es inválido (<= 0), si el producto 
     * no existe o si hay un error de persistencia.
     */
    @Override
    public Producto obtenerPorId(int idProducto) throws NegocioException {
        if (idProducto <= 0) {
            LOG.warning("ID del producto inválido: " + idProducto);
            throw new NegocioException("El ID del producto no puede ser negativo o cero");
        }

        try {
            Producto producto = productoDAO.obtenerPorId(idProducto);
            if (producto == null) {
                LOG.warning("No se encontró el Producto: " + idProducto);
                throw new NegocioException("No se encontró ningún Producto con el ID: " + idProducto);
            }
            return producto;
        } catch (PersistenciaException e) {
            LOG.log(Level.SEVERE, "Error al consultar producto por ID: " + idProducto, e);
            throw new NegocioException("Error al obtener el producto con ID: " + idProducto);
        }
    }
}