package persistencia.pruebas;

import java.sql.Timestamp;
import java.util.List;
import persistencia.DAOs.IPedidoDAO;
import persistencia.DAOs.PedidoDAO;
import persistencia.conexion.ConexionBD;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.Pedido;

/**
 * Clase de prueba funcional para validar las operaciones de consulta del PedidoDAO.
 * <p>
 * Esta suite de pruebas verifica la capacidad del sistema para filtrar y recuperar 
 * información crítica de ventas bajo distintos criterios de negocio, asegurando 
 * que el motor de persistencia responda correctamente a las necesidades del panel 
 * de administración y del historial del cliente.
 * </p>
 */
public class PruebaPedidoDAO {

    public static void main(String[] args) {
        System.out.println("--- Prueba PedidoDAO ---");

        try {
            IConexionBD conexion = new ConexionBD();
            IPedidoDAO pedidoDAO = new PedidoDAO(conexion);

            // Contar pedidos activos del cliente 1
            int activos = pedidoDAO.contarPedidosActivos(1);
            System.out.println("Pedidos activos del cliente 1: " + activos);

            // Buscar por telefono
            List<Pedido> porTelefono = pedidoDAO.buscarPorTelefono("6451234567");
            System.out.println("Pedidos encontrados por telefono: " + porTelefono.size());

            // Obtener pendientes y listos
            List<Pedido> pendientes = pedidoDAO.obtenerPedidosPendientesYListos();
            System.out.println("Pedidos pendientes/listos: " + pendientes.size());
            for (Pedido p : pendientes) {
                System.out.println("  - Pedido #" + p.getNumPedido() + " | Estado: " + p.getEstado() + " | Total: $" + p.getTotal());
            }

            // Buscar por rango de fechas (ultimo mes)
            Timestamp inicio = Timestamp.valueOf("2025-01-01 00:00:00");
            Timestamp fin = new Timestamp(System.currentTimeMillis());
            List<Pedido> porFechas = pedidoDAO.buscarPorRangoFechas(inicio, fin);
            System.out.println("Pedidos en rango de fechas: " + porFechas.size());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
