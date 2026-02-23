package persistencia.DAOs;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.PedidoProgramado;
import persistencia.dominio.DetallePedido;
import persistencia.dominio.Pedido.EstadoPedido;
import persistencia.excepciones.PersistenciaException;

public class PedidoProgramadoDAO implements IPedidoProgramadoDAO {

    private IConexionBD conexion;

    public PedidoProgramadoDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public PedidoProgramado crear(PedidoProgramado pedido) throws PersistenciaException {
        try (Connection conn = conexion.crearConexion()) {
            conn.setAutoCommit(false);

            try {
                // Insertar PEDIDO
                String sqlPedido = """
                         INSERT INTO PEDIDOS (ID_USUARIO, NUM_PEDIDO, ESTADO, FECHA_REGISTRO, FECHA_ENTREGA, TOTAL)
                         VALUES (?, ?, 'PENDIENTE', CURRENT_TIMESTAMP, ?, ?)
                     """;

                try (PreparedStatement ps = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, pedido.getIdUsuario());
                    ps.setInt(2, pedido.getNumPedido());
                    ps.setTimestamp(3, pedido.getFechaEntrega());
                    ps.setDouble(4, pedido.getTotal());

                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            pedido.setIdPedido(rs.getInt(1));
                        }
                    }
                }

                //  Insertar pedido programado
                String sqlProgramado = "INSERT INTO PEDIDOS_PROGRAMADOS (ID_PEDIDO, ID_CUPON) VALUES (?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sqlProgramado)) {
                    ps.setInt(1, pedido.getIdPedido());
                    ps.setObject(2, pedido.getIdCupon(), Types.INTEGER);
                    ps.executeUpdate();
                }

                // Insertar detalles
                if (pedido.getDetalles() != null) {
                    String sqlDetalle = """
                             INSERT INTO DETALLE_PEDIDOS (ID_PEDIDO, ID_PRODUCTO, CANTIDAD, PRECIO, SUBTOTAL, NOTAS)
                             VALUES (?, ?, ?, ?, ?, ?)
                         """;

                    try (PreparedStatement ps = conn.prepareStatement(sqlDetalle)) {
                        for (DetallePedido detalle : pedido.getDetalles()) {
                            ps.setInt(1, pedido.getIdPedido());
                            ps.setInt(2, detalle.getIdProducto());
                            ps.setInt(3, detalle.getCantidad());
                            ps.setDouble(4, detalle.getPrecio());
                            ps.setDouble(5, detalle.getSubtotal());
                            ps.setString(6, detalle.getNotas());
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }
                }

                conn.commit();
                return pedido;

            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al crear pedido programado", ex);
        }
    }

    @Override
    public int generarNumPedido() throws PersistenciaException {
        String sql = "SELECT MAX(NUM_PEDIDO) FROM PEDIDOS";

        try (Connection conn = conexion.crearConexion(); 
                PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int maximoActual = rs.getInt(1);
                return maximoActual + 1;
            }

            return 1;

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al generar número de pedido", ex);
        }
    }

    @Override
    public boolean validarFechaEntrega(Timestamp fechaEntrega) throws PersistenciaException {
        Timestamp ahora = new Timestamp(System.currentTimeMillis());
        long dosHoras = 2 * 60 * 60 * 1000;
        return fechaEntrega.getTime() > (ahora.getTime() + dosHoras);
    }

   @Override
public List<PedidoProgramado> obtenerPorTelefono(String telefono) throws PersistenciaException {
    String sql = """
        SELECT p.ID_PEDIDO, p.NUM_PEDIDO, p.ESTADO, p.FECHA_ENTREGA, p.TOTAL,
               pp.ID_CUPON,
               d.ID_PRODUCTO, d.CANTIDAD, d.PRECIO, d.SUBTOTAL, d.NOTAS
        FROM PEDIDOS p
        JOIN CLIENTES c ON p.ID_USUARIO = c.ID_USUARIO
        JOIN TELEFONOS t ON c.ID_USUARIO = t.ID_USUARIO
        LEFT JOIN PEDIDOS_PROGRAMADOS pp ON p.ID_PEDIDO = pp.ID_PEDIDO
        LEFT JOIN DETALLE_PEDIDOS d ON p.ID_PEDIDO = d.ID_PEDIDO
        WHERE t.NUMERO = ?
        ORDER BY p.FECHA_REGISTRO
    """;

    try (Connection conn = conexion.crearConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, telefono);
        try (ResultSet rs = ps.executeQuery()) {

            Map<Integer, PedidoProgramado> mapaPedidos = new HashMap<>();

            while (rs.next()) {
                int idPedido = rs.getInt("ID_PEDIDO");
                PedidoProgramado pedido = mapaPedidos.get(idPedido);

                if (pedido == null) {
                    pedido = new PedidoProgramado();
                    pedido.setIdPedido(idPedido);
                    pedido.setNumPedido(rs.getInt("NUM_PEDIDO"));
                    pedido.setEstado(EstadoPedido.valueOf(rs.getString("ESTADO")));
                    pedido.setFechaEntrega(rs.getTimestamp("FECHA_ENTREGA"));
                    pedido.setTotal(rs.getDouble("TOTAL"));
                    pedido.setIdCupon(rs.getObject("ID_CUPON", Integer.class));
                    pedido.setDetalles(new ArrayList<>());
                    mapaPedidos.put(idPedido, pedido);
                }

                int idProducto = rs.getInt("ID_PRODUCTO");
                if (!rs.wasNull()) {
                    DetallePedido detalle = new DetallePedido();
                    detalle.setIdProducto(idProducto);
                    detalle.setCantidad(rs.getInt("CANTIDAD"));
                    detalle.setPrecio(rs.getDouble("PRECIO"));
                    detalle.setSubtotal(rs.getDouble("SUBTOTAL"));
                    detalle.setNotas(rs.getString("NOTAS"));
                    pedido.getDetalles().add(detalle);
                }
            }

            return new ArrayList<>(mapaPedidos.values());
        }

    } catch (SQLException ex) {
        throw new PersistenciaException("Error al obtener pedidos por teléfono", ex);
    }
}

@Override
public List<PedidoProgramado> obtenerPorRangoFechas(Timestamp inicio, Timestamp fin) throws PersistenciaException {
    String sql = """
        SELECT p.ID_PEDIDO, p.NUM_PEDIDO, p.ESTADO, p.FECHA_ENTREGA, p.TOTAL,
               pp.ID_CUPON,
               d.ID_PRODUCTO, d.CANTIDAD, d.PRECIO, d.SUBTOTAL, d.NOTAS
        FROM PEDIDOS p
        LEFT JOIN PEDIDOS_PROGRAMADOS pp ON p.ID_PEDIDO = pp.ID_PEDIDO
        LEFT JOIN DETALLE_PEDIDOS d ON p.ID_PEDIDO = d.ID_PEDIDO
        WHERE p.FECHA_REGISTRO BETWEEN ? AND ?
        ORDER BY p.FECHA_REGISTRO
    """;

    try (Connection conn = conexion.crearConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setTimestamp(1, inicio);
        ps.setTimestamp(2, fin);

        try (ResultSet rs = ps.executeQuery()) {

            Map<Integer, PedidoProgramado> mapaPedidos = new HashMap<>();

            while (rs.next()) {
                int idPedido = rs.getInt("ID_PEDIDO");
                PedidoProgramado pedido = mapaPedidos.get(idPedido);

                if (pedido == null) {
                    pedido = new PedidoProgramado();
                    pedido.setIdPedido(idPedido);
                    pedido.setNumPedido(rs.getInt("NUM_PEDIDO"));
                    pedido.setEstado(EstadoPedido.valueOf(rs.getString("ESTADO")));
                    pedido.setFechaEntrega(rs.getTimestamp("FECHA_ENTREGA"));
                    pedido.setTotal(rs.getDouble("TOTAL"));
                    pedido.setIdCupon(rs.getObject("ID_CUPON", Integer.class));
                    pedido.setDetalles(new ArrayList<>());
                    mapaPedidos.put(idPedido, pedido);
                }

                int idProducto = rs.getInt("ID_PRODUCTO");
                if (!rs.wasNull()) {
                    DetallePedido detalle = new DetallePedido();
                    detalle.setIdProducto(idProducto);
                    detalle.setCantidad(rs.getInt("CANTIDAD"));
                    detalle.setPrecio(rs.getDouble("PRECIO"));
                    detalle.setSubtotal(rs.getDouble("SUBTOTAL"));
                    detalle.setNotas(rs.getString("NOTAS"));
                    pedido.getDetalles().add(detalle);
                }
            }

            return new ArrayList<>(mapaPedidos.values());
        }

    } catch (SQLException ex) {
        throw new PersistenciaException("Error al obtener pedidos por rango de fechas", ex);
    }
}
}