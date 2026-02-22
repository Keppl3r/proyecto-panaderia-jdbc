  package negocio.BOs;

     import java.sql.Timestamp;
     import java.util.List;
     import java.util.logging.Level;
     import java.util.logging.Logger;
     import negocio.excepciones.NegocioException;
     import persistencia.DAOs.IPedidoProgramadoDAO;
     import persistencia.DAOs.IProductoDAO;
     import persistencia.dominio.PedidoProgramado;
     import persistencia.dominio.DetallePedido;
     import persistencia.dominio.Producto;
     import persistencia.excepciones.PersistenciaException;

     public class PedidoProgramadoBO implements IPedidoProgramadoBO {

         private IPedidoProgramadoDAO pedidoDAO;
         private IProductoDAO productoDAO;
         private IClienteBO clienteBO; 
         private static final Logger LOG = Logger.getLogger(PedidoProgramadoBO.class.getName());

         // CORREGIR CONSTRUCTOR:
         public PedidoProgramadoBO(IPedidoProgramadoDAO pedidoDAO, IProductoDAO productoDAO, IClienteBO clienteBO) {
             this.pedidoDAO = pedidoDAO;
             this.productoDAO = productoDAO;
             this.clienteBO = clienteBO; 
         }

         @Override
         public PedidoProgramado programarPedido(int idCliente, List<DetallePedido> detalles,
                                               Timestamp fechaEntrega, Integer idCupon) throws NegocioException {

             try {
                 
                 if (!clienteBO.existeCliente(idCliente)) {
                     throw new NegocioException("Cliente no existe o no está activo");
                 }

               
                 if (!pedidoDAO.validarFechaEntrega(fechaEntrega)) {
                     throw new NegocioException("La fecha de entrega debe ser al menos 2 horas en el futuro");
                 }

                
                 if (detalles == null || detalles.isEmpty()) {
                     throw new NegocioException("El pedido debe tener al menos un producto");
                 }

              
                 PedidoProgramado pedido = new PedidoProgramado();
                 pedido.setIdUsuario(idCliente);
                 pedido.setNumPedido(pedidoDAO.generarNumPedido());
                 pedido.setFechaEntrega(fechaEntrega);
                 pedido.setIdCupon(idCupon);

                 
                 for (DetallePedido detalle : detalles) {
                     if (detalle.getCantidad() <= 0) {
                         throw new NegocioException("La cantidad debe ser mayor a 0");
                     }

                     Producto producto = productoDAO.obtenerPorId(detalle.getIdProducto());
                     if (producto == null || !producto.isDisponible()) {
                         throw new NegocioException("Producto no disponible");
                     }

                     detalle.setPrecio(producto.getPrecio());
                     detalle.calcularSubtotal();
                 }

                 pedido.setDetalles(detalles);
                 pedido.calcularTotal();

                 
                 return pedidoDAO.crear(pedido);

             } catch (PersistenciaException ex) {
                 LOG.log(Level.SEVERE, "Error al programar pedido", ex);
                 throw new NegocioException("Error al crear el pedido programado");
             }
         }
     }