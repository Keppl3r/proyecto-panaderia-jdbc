package persistencia.DAOs;

import java.sql.*;
import persistencia.conexion.IConexionBD;
import persistencia.excepciones.PersistenciaException;

/**
 * @author Adrian Mendoza
 */
public class PagoDAO implements IPagoDAO {

    private IConexionBD conexion;

    public PagoDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

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
