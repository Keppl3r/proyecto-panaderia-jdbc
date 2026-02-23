package persistencia.DAOs;

import java.sql.*;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.PedidoProgramado;
import persistencia.dominio.DetallePedido;
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

    
}
