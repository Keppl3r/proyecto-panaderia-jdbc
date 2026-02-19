package persistencia.DAOs;

import java.util.List;
import persistencia.dominio.Producto;
import persistencia.excepciones.PersistenciaException;

/**
 * Interfaz para operaciones de Producto en BD.
 *
 * @author Adrian Mendoza
 */
public interface IProductoDAO {

    List<Producto> obtenerProductosDisponibles() throws PersistenciaException;

    Producto obtenerPorId(int idProducto) throws PersistenciaException;
}
