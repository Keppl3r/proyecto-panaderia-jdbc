
package pruebasCasoDeUsoPrincipal;
 import java.sql.Timestamp;
     import java.util.ArrayList;
     import java.util.List;
     import negocio.BOs.*;
     import persistencia.DAOs.*;
     import persistencia.conexion.*;
     import persistencia.dominio.*;
/**
 *
 * @author Adrian Mendoza
 */
public class PruebaPedidoProgramado {
     public static void main(String[] args) {

             try {
                 // Crear conexion
                 IConexionBD conexion = new ConexionBD();

                 // Crear DAOs
                 IClienteDAO clienteDAO = new ClienteDAO(conexion);
                 IProductoDAO productoDAO = new ProductoDAO(conexion);
                 IPedidoProgramadoDAO pedidoProgramadoDAO = new PedidoProgramadoDAO(conexion);

                 // Crear BOs
                 IClienteBO clienteBO = new ClienteBO(clienteDAO);
                 IPedidoProgramadoBO pedidoProgramadoBO = new PedidoProgramadoBO(
                     pedidoProgramadoDAO, productoDAO, clienteBO
                 );

                 // Datos de prueba
                 int idCliente = 1; // Cliente que debe existir en BD

                 // Crear detalles del pedido
                 List<DetallePedido> detalles = new ArrayList<>();

                 DetallePedido detalle1 = new DetallePedido();
                 detalle1.setIdProducto(1); // Producto que debe existir
                 detalle1.setCantidad(2);
                 detalle1.setNotas("Sin azucar");
                 detalles.add(detalle1);

                 // Fecha de entrega 3 horas en el futuro
                 long tiempoFuturo = System.currentTimeMillis() + (3 * 60 * 60 * 1000);
                 Timestamp fechaEntrega = new Timestamp(tiempoFuturo);

                 // Programar pedido
                 PedidoProgramado pedido = pedidoProgramadoBO.programarPedido(
                     idCliente, detalles, fechaEntrega, null
                 );

                 // Mostrar resultados
                 System.out.println("Pedido programado creado:");
                 System.out.println("ID: " + pedido.getIdPedido());
                 System.out.println("Numero: " + pedido.getNumPedido());
                 System.out.println("Total: " + pedido.getTotal());
                 System.out.println("Fecha entrega: " + pedido.getFechaEntrega());

             } catch (Exception e) {
                 System.out.println("Error: " + e.getMessage());
                 e.printStackTrace();
             }
         }
     }