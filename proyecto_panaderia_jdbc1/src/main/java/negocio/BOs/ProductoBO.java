 package negocio.BOs;

     import java.util.List;
     import java.util.logging.Level;
     import java.util.logging.Logger;
     import negocio.excepciones.NegocioException;
     import persistencia.DAOs.IProductoDAO;
     import persistencia.dominio.Producto;
     import persistencia.excepciones.PersistenciaException;

     /**
      * BO para Producto - SOLO para CU Pedido Programado
      */
     public class ProductoBO implements IProductoBO {

         private IProductoDAO productoDAO;
         private static final Logger LOG = Logger.getLogger(ProductoBO.class.getName());

         public ProductoBO(IProductoDAO productoDAO) {
             this.productoDAO = productoDAO;
         }

         @Override
         public List<Producto> obtenerProductoDisponibles() throws NegocioException {
             try {
                 return productoDAO.obtenerProductosDisponibles();
             } catch (PersistenciaException e) {
                 LOG.log(Level.SEVERE, "Error al consultar productos disponibles", e);
                 throw new NegocioException("Ocurrió un error al obtener los productos");
             }
         }

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