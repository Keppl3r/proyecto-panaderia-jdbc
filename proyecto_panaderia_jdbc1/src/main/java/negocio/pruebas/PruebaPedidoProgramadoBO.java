
package negocio.pruebas;

     import negocio.BOs.*;
     import persistencia.DAOs.*;
     import persistencia.conexion.*;
     import persistencia.dominio.*;
     import java.sql.Timestamp;
     import java.util.ArrayList;
     import java.util.List;
/**
 *
 * @author Adrian Mendoza
 */
public class PruebaPedidoProgramadoBO {
       public static void main(String[] args) {
             System.out.println("Probando PedidoProgramadoBO");

             try {
                 IConexionBD conexion = new ConexionBD();
                 IClienteDAO clienteDAO = new ClienteDAO(conexion);
                 IProductoDAO productoDAO = new ProductoDAO(conexion);
                 IPedidoProgramadoDAO pedidoDAO = new PedidoProgramadoDAO(conexion);

                 IClienteBO clienteBO = new ClienteBO(clienteDAO);
                 IPedidoProgramadoBO pedidoBO = new PedidoProgramadoBO(pedidoDAO, productoDAO, clienteBO);

                 // Crear pedido de prueba
                 List<DetallePedido> detalles = new ArrayList<>();
                 DetallePedido detalle = new DetallePedido();
                 detalle.setIdProducto(1);
                 detalle.setCantidad(1);
                 detalles.add(detalle);

                 Timestamp fecha = new Timestamp(System.currentTimeMillis() + 10800000); // 3 horas

                 PedidoProgramado pedido = pedidoBO.programarPedido(1, detalles, fecha, null);

                 if (pedido != null) {
                     System.out.println("Pedido creado");
                     System.out.println("ID: " + pedido.getIdPedido());
                     System.out.println("Numero: " + pedido.getNumPedido());
                 } else {
                     System.out.println("Pedido no creado");
                 }

             } catch (Exception e) {
                 System.out.println("Error: " + e.getMessage());
             }
         }
}
