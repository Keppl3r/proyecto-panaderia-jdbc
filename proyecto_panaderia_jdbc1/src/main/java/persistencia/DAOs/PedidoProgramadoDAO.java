package persistencia.DAOs;

import java.sql.*;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.PedidoProgramado;
import persistencia.dominio.DetallePedido;
import persistencia.excepciones.PersistenciaException;

/**
 * Implementación de persistencia para Pedidos Programados asociados a clientes registrados.
 * <p>
 * Esta clase gestiona el almacenamiento atómico de pedidos que requieren una fecha de entrega
 * futura, permitiendo la asociación con cupones de descuento y el registro detallado de productos.
 * </p>
 */
public class PedidoProgramadoDAO implements IPedidoProgramadoDAO {

    private IConexionBD conexion;
    /**
     * Constructor que inicializa la conexión para el acceso a datos.
     * * @param conexion Implementación de la interfaz de conexión a la base de datos.
     */
    public PedidoProgramadoDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }
    /**
     * Registra un nuevo pedido programado en la base de datos de manera transaccional.
     * <p>
     * El método asegura que el registro en las tablas PEDIDOS, PEDIDOS_PROGRAMADOS 
     * y DETALLE_PEDIDOS se realice con éxito total; de lo contrario, se aplica un rollback.
     * </p>
     * * @param pedido El objeto {@code PedidoProgramado} con la información del cliente y productos.
     * @return El objeto {@code PedidoProgramado} con su ID generado por la base de datos.
     * @throws PersistenciaException Si ocurre un error de SQL durante la inserción o el volcado de lotes.
     */
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
    /**
     * Recupera el valor máximo de la columna NUM_PEDIDO para asignar el siguiente folio correlativo.
     * * @return El siguiente número de pedido disponible; devuelve 1 si no hay registros previos.
     * @throws PersistenciaException Si falla la consulta a la tabla PEDIDOS.
     */
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
    /**
     * Valida que la fecha de entrega solicitada cumpla con las políticas de la empresa.
     * <p>
     * Actualmente verifica que la entrega sea programada con al menos 2 horas de 
     * anticipación respecto al tiempo actual del sistema.
     * </p>
     * * @param fechaEntrega {@code Timestamp} de la fecha y hora de entrega deseada.
     * @return {@code true} si la fecha es válida (futura + 2 horas); {@code false} en caso contrario.
     * @throws PersistenciaException Si hay problemas con el manejo de tiempos o nulos.
     */
    @Override
    public boolean validarFechaEntrega(Timestamp fechaEntrega) throws PersistenciaException {
        Timestamp ahora = new Timestamp(System.currentTimeMillis());
        long dosHoras = 2 * 60 * 60 * 1000;
        return fechaEntrega.getTime() > (ahora.getTime() + dosHoras);
    }

    
}
