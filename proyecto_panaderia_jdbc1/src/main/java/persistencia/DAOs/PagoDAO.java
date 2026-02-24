package persistencia.DAOs;

import java.sql.*;
import persistencia.conexion.IConexionBD;
import persistencia.excepciones.PersistenciaException;

/**
 * Implementación JDBC para el registro de transacciones financieras.
 * <p>
 * Esta clase se encarga de insertar los comprobantes de pago en la base de datos,
 * vinculándolos de forma única a un pedido existente. Es una pieza clave para 
 * la auditoría y el cierre de caja de la panadería.
 * </p>
 * @author Adrian Mendoza
 */
public class PagoDAO implements IPagoDAO {

    private IConexionBD conexion;
    /**
     * Inicializa el DAO con un gestor de conexiones.
     * @param conexion Proveedor de conexiones activas a la base de datos.
     */
    public PagoDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }
    /**
     * Registra un nuevo pago en la tabla PAGOS.
     * <p>
     * Se asume que el objeto Connection cierra automáticamente la sentencia al terminar
     * el bloque try-with-resources, garantizando la liberación de recursos.
     * </p>
     * @param idPedido   Referencia al pedido que se está liquidando.
     * @param metodoPago Medio de pago (ej. 'EFECTIVO', 'TARJETA').
     * @param monto      Valor total recibido.
     * @return {@code true} si la inserción fue exitosa.
     * @throws PersistenciaException Si ocurre un error de SQL (ej. llave foránea inexistente).
     */
    @Override
    public boolean registrarPago(int idPedido, String metodoPago, double monto) throws PersistenciaException {
        String sql = "INSERT INTO PAGOS (ID_PEDIDO, METODO_PAGO, MONTO) VALUES (?, ?, ?)";
        try (Connection conn = conexion.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            ps.setString(2, metodoPago);
            ps.setDouble(3, monto);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al registrar pago", ex);
        }
    }
}
