package pruebasCasoDeUsoPrincipal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import negocio.BOs.*;
import persistencia.DAOs.*;
import persistencia.conexion.*;
import persistencia.dominio.*;

/**
 * @author Adrian Mendoza
 */
public class PruebaPedidoProgramado {

    public static void main(String[] args) {
        System.out.println("=== PRUEBA CASO DE USO: Pedido Programado ===\n");

        try {
            IConexionBD conexion = new ConexionBD();

            // PASO 1: Diagnostico - verificar datos necesarios
            System.out.println("--- PASO 1: Verificando datos en BD ---");
            int idClienteReal = -1;
            int idProductoReal = -1;

            try (Connection conn = conexion.crearConexion()) {
                System.out.println("Conexion OK\n");

                // Buscar un cliente activo real
                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT c.ID_USUARIO, c.NOMBRES, c.ESTADO, u.USERNAME " +
                        "FROM CLIENTES c INNER JOIN USUARIOS u ON c.ID_USUARIO = u.ID_USUARIO " +
                        "LIMIT 5");
                     ResultSet rs = ps.executeQuery()) {
                    System.out.println("CLIENTES con JOIN a USUARIOS:");
                    while (rs.next()) {
                        int id = rs.getInt("ID_USUARIO");
                        String estado = rs.getString("ESTADO");
                        System.out.println("  ID_USUARIO=" + id
                                + ", NOMBRES=" + rs.getString("NOMBRES")
                                + ", ESTADO='" + estado + "'"
                                + ", USERNAME=" + rs.getString("USERNAME"));
                        if (idClienteReal == -1) {
                            idClienteReal = id;
                        }
                    }
                    if (idClienteReal == -1) System.out.println("  (NO hay clientes con usuario asociado)");
                }

                System.out.println();

                // Buscar un producto disponible real
                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT ID_PRODUCTO, NOMBRE, PRECIO, DISPONIBLE FROM PRODUCTOS WHERE DISPONIBLE = TRUE LIMIT 5");
                     ResultSet rs = ps.executeQuery()) {
                    System.out.println("PRODUCTOS disponibles:");
                    while (rs.next()) {
                        int id = rs.getInt("ID_PRODUCTO");
                        System.out.println("  ID_PRODUCTO=" + id
                                + ", NOMBRE=" + rs.getString("NOMBRE")
                                + ", PRECIO=" + rs.getDouble("PRECIO"));
                        if (idProductoReal == -1) {
                            idProductoReal = id;
                        }
                    }
                    if (idProductoReal == -1) System.out.println("  (NO hay productos disponibles)");
                }
            }

            System.out.println();

            if (idClienteReal == -1) {
                System.out.println("ERROR: No hay clientes en la BD. Inserta datos primero.");
                return;
            }
            if (idProductoReal == -1) {
                System.out.println("ERROR: No hay productos disponibles en la BD. Inserta datos primero.");
                return;
            }

            // PASO 2: Verificar que existeClienteActivo funciona
            System.out.println("--- PASO 2: Verificando validaciones ---");
            IClienteDAO clienteDAO = new ClienteDAO(conexion);
            IProductoDAO productoDAO = new ProductoDAO(conexion);
            IPedidoProgramadoDAO pedidoProgramadoDAO = new PedidoProgramadoDAO(conexion);
            IClienteBO clienteBO = new ClienteBO(clienteDAO);

            System.out.println("existeClienteActivo(" + idClienteReal + ") = "
                    + clienteDAO.existeClienteActivo(idClienteReal));

            Producto productoTest = productoDAO.obtenerPorId(idProductoReal);
            System.out.println("obtenerPorId(" + idProductoReal + ") = "
                    + (productoTest != null ? productoTest.getNombre() : "NULL"));

            // PASO 3: Crear el pedido con datos reales
            System.out.println("\n--- PASO 3: Creando pedido programado ---");
            System.out.println("Usando ID_USUARIO=" + idClienteReal + ", ID_PRODUCTO=" + idProductoReal);

            IPedidoProgramadoBO pedidoProgramadoBO = new PedidoProgramadoBO(
                    pedidoProgramadoDAO, productoDAO, clienteBO
            );

            List<DetallePedido> detalles = new ArrayList<>();
            DetallePedido detalle1 = new DetallePedido();
            detalle1.setIdProducto(idProductoReal);
            detalle1.setCantidad(2);
            detalle1.setNotas("Sin azucar");
            detalles.add(detalle1);

            long tiempoFuturo = System.currentTimeMillis() + (3 * 60 * 60 * 1000);
            Timestamp fechaEntrega = new Timestamp(tiempoFuturo);

            PedidoProgramado pedido = pedidoProgramadoBO.programarPedido(
                    idClienteReal, detalles, fechaEntrega, null
            );

            System.out.println("\nPedido programado creado exitosamente:");
            System.out.println("  ID Pedido: " + pedido.getIdPedido());
            System.out.println("  Numero: " + pedido.getNumPedido());
            System.out.println("  Total: $" + pedido.getTotal());
            System.out.println("  Fecha entrega: " + pedido.getFechaEntrega());
            System.out.println("  Detalles: " + pedido.getDetalles().size() + " producto(s)");

        } catch (Exception e) {
            System.out.println("\nERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}