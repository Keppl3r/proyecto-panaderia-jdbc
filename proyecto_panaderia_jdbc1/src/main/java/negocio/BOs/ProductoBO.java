
package negocio.BOs;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.excepciones.NegocioException;
import persistencia.DAOs.IProductoDAO;
import persistencia.dominio.Producto;
import persistencia.excepciones.PersistenciaException;

/**
 *
 *@author Jazmin
 */
public class ProductoBO implements IProductoBO {
    private IProductoDAO productoDAO;
    private static final Logger LOG = Logger.getLogger(ProductoBO.class.getName());

    public ProductoBO(IProductoDAO productoDAO) {
        this.productoDAO = productoDAO;
    }

   

    @Override
    public List<Producto> obtenerProductoDisponibles() throws NegocioException{
        try {
            return productoDAO.obtenerProductosDisponibles();
        } catch (PersistenciaException e) {
            LOG.log(Level.SEVERE,"Error al consultar productos disponibles", e);
            throw new NegocioException("Ocurrio un error al obtener los productos ");
        }
       
    }

    @Override
    public Producto obtenerPorId(int idProducto) throws NegocioException {
        if(idProducto <=0){
            LOG.warning("ID del producto invalido: " +  idProducto);
            throw new NegocioException("El ID del producto no puede ser negativo");
        }
        try {
            Producto producto = productoDAO.obtenerPorId(idProducto);
            if(producto == null){
                LOG.warning("No se encontro el Producto: " +  idProducto);
            throw new NegocioException("No se encontro ningun Producto con el ID:" + idProducto);
            }
            return producto;
        } catch (PersistenciaException e) {
            LOG.log(Level.SEVERE, "Error al consultar producto por ID: " + idProducto, e);
            throw new NegocioException("Error al obtener el producto con ID: " + idProducto);
        }
    }
    
    
    //este se puede pasar a una clase validador
    private void validarProducto(Producto producto) throws NegocioException{
        if (producto == null) {
            throw new NegocioException("El producto no puede ser nulo.");
        }
        if (producto.getNombre() == null || producto.getNombre().isEmpty()) {
            throw new NegocioException("El nombre del producto es obligatorio.");
        }
        if (producto.getPrecio() <= 0) {
            throw new NegocioException("El precio del producto debe ser mayor a cero.");
        }
    }
    
}
