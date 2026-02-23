package persistencia.DAOs;

import java.sql.*;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.DetallePedido;
import persistencia.dominio.Pedido;
import persistencia.dominio.PedidoExpress;
import persistencia.excepciones.PersistenciaException;

/**
 * @author Jazmin
 * @author Adrian Mendoza
 */
public class PedidoExpressDAO implements IPedidoExpressDAO {

    private IConexionBD conexion;

    public PedidoExpressDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public PedidoExpress crear(PedidoExpress pedido) throws PersistenciaException {
        try (Connection conn = conexion.crearConexion()) {
            conn.setAutoCommit(false);

            try {
                String sqlPedido = """
                            INSERT INTO PEDIDOS (ID_USUARIO, NUM_PEDIDO, ESTADO, TOTAL)
                            VALUES (NULL, ?, 'PENDIENTE', ?)
                        """;
                try (PreparedStatement ps = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, pedido.getNumPedido());
                    ps.setDouble(2, pedido.getTotal());
                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            pedido.setIdPedido(rs.getInt(1));
                        }
                    }
                }

                String sqlExpress = "INSERT INTO PEDIDOS_EXPRESS (ID_PEDIDO, FOLIO, PIN) VALUES (?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sqlExpress)) {
                    ps.setInt(1, pedido.getIdPedido());
                    ps.setString(2, pedido.getFolio());
                    ps.setString(3, pedido.getPin());
                    ps.executeUpdate();
                }

                if (pedido.getDetalles() != null && !pedido.getDetalles().isEmpty()) {
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
            throw new PersistenciaException("Error al crear pedido Express", ex);
        }
    }

    @Override
    public int generarNumPedido() throws PersistenciaException {
        String sql = "SELECT COALESCE(MAX(NUM_PEDIDO), 0) FROM PEDIDOS";
        try (Connection conn = conexion.crearConexion();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
            return 1;
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al generar número de pedido", ex);
        }
    }

    @Override
    public PedidoExpress buscarPorFolio(String folio) throws PersistenciaException {
        String sql = """
                    SELECT p.*, pe.FOLIO, pe.PIN, pe.TIEMPO_LIMITE, pe.TIEMPO_RECOLECCION
                    FROM PEDIDOS p
                    INNER JOIN PEDIDOS_EXPRESS pe ON p.ID_PEDIDO = pe.ID_PEDIDO
                    WHERE pe.FOLIO = ?
                """;
        try (Connection conn = conexion.crearConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, folio);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PedidoExpress pe = new PedidoExpress();
                    pe.setIdPedido(rs.getInt("ID_PEDIDO"));
                    pe.setNumPedido(rs.getInt("NUM_PEDIDO"));
                    pe.setEstado(Pedido.EstadoPedido.valueOf(rs.getString("ESTADO")));
                    pe.setFechaRegistro(rs.getTimestamp("FECHA_REGISTRO"));
                    pe.setTotal(rs.getDouble("TOTAL"));
                    pe.setFolio(rs.getString("FOLIO"));
                    pe.setPin(rs.getString("PIN"));
                    pe.setTiempoLimite(rs.getTimestamp("TIEMPO_LIMITE"));
                    pe.setTiempoRecoleccion(rs.getTimestamp("TIEMPO_RECOLECCION"));
                    return pe;
                }
            }
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al buscar pedido por folio", ex);
        }
        return null;
    }

}
