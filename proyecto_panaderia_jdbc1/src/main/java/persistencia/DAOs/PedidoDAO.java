package persistencia.DAOs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.DetallePedido;
import persistencia.dominio.Pedido;
import persistencia.dominio.Pedido.EstadoPedido;
import persistencia.dominio.Producto;
import persistencia.excepciones.PersistenciaException;

/**
 * Implementación JDBC para la gestión integral de pedidos.
 * <p>
 * Esta clase centraliza las consultas complejas que involucran filtros por folio, 
 * teléfono, fechas y estados. Además, gestiona la recuperación de los detalles 
 * de cada orden (productos y cantidades).
 * </p>
 * @author Adrian Mendoza
 */
public class PedidoDAO implements IPedidoDAO {

    private IConexionBD conexion;

    public PedidoDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }
    /**
     * Actualiza el estado de una orden. Vital para el flujo de trabajo en cocina.
     * @param idPedido Identificador de la orden.
     * @param nuevoEstado Texto que coincide con los valores del Enum EstadoPedido.
     * @return true si se actualizó correctamente.
     */
    @Override
    public boolean cambiarEstado(int idPedido, String nuevoEstado) throws PersistenciaException {
        String sql = "UPDATE PEDIDOS SET ESTADO = ? WHERE ID_PEDIDO = ?";
        try (Connection conn = conexion.crearConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idPedido);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al cambiar estado del pedido", ex);
        }
    }
    
    @Override
    public int contarPedidosActivos(int idCliente) throws PersistenciaException {
        String sql = "SELECT COUNT(*) FROM PEDIDOS WHERE ID_USUARIO = ? AND ESTADO IN ('PENDIENTE', 'LISTO')";
        try (Connection conn = conexion.crearConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getInt(1);
            }
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al contar pedidos activos", ex);
        }
        return 0;
    }

    @Override
    public List<Pedido> buscarPorTelefono(String telefono) throws PersistenciaException {
        String sql = """
                    SELECT p.* FROM PEDIDOS p
                    INNER JOIN TELEFONOS t ON p.ID_USUARIO = t.ID_USUARIO
                    WHERE t.NUMERO = ?
                    ORDER BY p.FECHA_REGISTRO DESC
                """;
        return ejecutarBusqueda(sql, telefono);
    }

    @Override
    public List<Pedido> buscarPorFolio(String folio) throws PersistenciaException {
        String sql = """
                    SELECT p.* FROM PEDIDOS p
                    INNER JOIN PEDIDOS_EXPRESS pe ON p.ID_PEDIDO = pe.ID_PEDIDO
                    WHERE pe.FOLIO = ?
                """;
        return ejecutarBusqueda(sql, folio);
    }

    @Override
    public List<Pedido> buscarPorRangoFechas(Timestamp inicio, Timestamp fin) throws PersistenciaException {
        String sql = "SELECT * FROM PEDIDOS WHERE FECHA_REGISTRO BETWEEN ? AND ? ORDER BY FECHA_REGISTRO DESC";
        List<Pedido> pedidos = new ArrayList<>();
        try (Connection conn = conexion.crearConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, inicio);
            ps.setTimestamp(2, fin);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    pedidos.add(extraerPedido(rs));
            }
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al buscar por rango de fechas", ex);
        }
        return pedidos;
    }

    @Override
    public List<Pedido> obtenerPedidosPendientesYListos() throws PersistenciaException {
        String sql = "SELECT * FROM PEDIDOS WHERE ESTADO IN ('PENDIENTE', 'LISTO') ORDER BY FECHA_REGISTRO ASC";
        List<Pedido> pedidos = new ArrayList<>();
        try (Connection conn = conexion.crearConexion();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                pedidos.add(extraerPedido(rs));
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al obtener pedidos pendientes", ex);
        }
        return pedidos;
    }

    @Override
    public List<Pedido> obtenerHistorialCliente(int idCliente) throws PersistenciaException {
        String sql = "SELECT * FROM PEDIDOS WHERE ID_USUARIO = ? ORDER BY FECHA_REGISTRO DESC";
        List<Pedido> pedidos = new ArrayList<>();
        try (Connection conn = conexion.crearConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    pedidos.add(extraerPedido(rs));
            }
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al obtener historial", ex);
        }
        return pedidos;
    }

    @Override
    public Pedido obtenerPorId(int idPedido) throws PersistenciaException {
        String sql = "SELECT * FROM PEDIDOS WHERE ID_PEDIDO = ?";
        try (Connection conn = conexion.crearConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return extraerPedido(rs);
            }
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al obtener pedido por ID", ex);
        }
        return null;
    }
    /**
     * Recupera el desglose de productos de un pedido mediante un INNER JOIN 
     * con la tabla PRODUCTOS para obtener los nombres actualizados.
     * @param idPedido ID de la orden.
     * @return Lista de {@link DetallePedido} con objetos Producto anidados.
     */
    @Override
    public List<DetallePedido> obtenerDetallesPorPedido(int idPedido) throws PersistenciaException {
        List<DetallePedido> detalles = new ArrayList<>();
        String sql = """
                    SELECT dp.ID_DETALLE_PEDIDO, dp.ID_PEDIDO, dp.ID_PRODUCTO,
                           dp.CANTIDAD, dp.PRECIO, dp.SUBTOTAL, dp.NOTAS,
                           p.NOMBRE AS NOMBRE_PRODUCTO
                    FROM DETALLE_PEDIDOS dp
                    INNER JOIN PRODUCTOS p ON dp.ID_PRODUCTO = p.ID_PRODUCTO
                    WHERE dp.ID_PEDIDO = ?
                """;
        try (Connection conn = conexion.crearConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetallePedido d = new DetallePedido();
                    d.setIdDetallePedido(rs.getInt("ID_DETALLE_PEDIDO"));
                    d.setIdPedido(rs.getInt("ID_PEDIDO"));
                    d.setIdProducto(rs.getInt("ID_PRODUCTO"));
                    d.setCantidad(rs.getInt("CANTIDAD"));
                    d.setPrecio(rs.getDouble("PRECIO"));
                    d.setSubtotal(rs.getDouble("SUBTOTAL"));
                    d.setNotas(rs.getString("NOTAS"));
                    Producto prod = new Producto();
                    prod.setIdProducto(rs.getInt("ID_PRODUCTO"));
                    prod.setNombre(rs.getString("NOMBRE_PRODUCTO"));
                    d.setProducto(prod);
                    detalles.add(d);
                }
            }
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al obtener detalles del pedido", ex);
        }
        return detalles;
    }

    @Override
    public List<Pedido> obtenerHistorialEmpleado() throws PersistenciaException {
        String sql = "SELECT * FROM PEDIDOS ORDER BY FECHA_REGISTRO DESC LIMIT 100";
        List<Pedido> pedidos = new ArrayList<>();
        try (Connection conn = conexion.crearConexion();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                pedidos.add(extraerPedido(rs));
            }
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al obtener historial empleado", ex);
        }
        return pedidos;
    }

    // --- Métodos auxiliares ---

    private List<Pedido> ejecutarBusqueda(String sql, String parametro) throws PersistenciaException {
        List<Pedido> pedidos = new ArrayList<>();
        try (Connection conn = conexion.crearConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    pedidos.add(extraerPedido(rs));
            }
        } catch (SQLException ex) {
            throw new PersistenciaException("Error en búsqueda de pedidos", ex);
        }
        return pedidos;
    }

    private Pedido extraerPedido(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setIdPedido(rs.getInt("ID_PEDIDO"));
        p.setIdUsuario(rs.getObject("ID_USUARIO") != null ? rs.getInt("ID_USUARIO") : null);
        p.setNumPedido(rs.getInt("NUM_PEDIDO"));
        p.setEstado(EstadoPedido.valueOf(rs.getString("ESTADO")));
        p.setFechaRegistro(rs.getTimestamp("FECHA_REGISTRO"));
        p.setFechaEntrega(rs.getTimestamp("FECHA_ENTREGA"));
        p.setTotal(rs.getDouble("TOTAL"));
        return p;
    }
}
