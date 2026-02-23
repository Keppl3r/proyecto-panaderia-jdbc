package persistencia.DAOs;

import java.sql.*;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.Cupon;
import persistencia.excepciones.PersistenciaException;

/**
 * @author Adrian Mendoza
 */
public class CuponDAO implements ICuponDAO {

    private IConexionBD conexion;

    public CuponDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public Cupon buscarPorId(int idCupon) throws PersistenciaException {
        String sql = "SELECT * FROM CUPONES WHERE ID_CUPON = ?";
        try (Connection conn = conexion.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCupon);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Cupon(
                        rs.getInt("ID_CUPON"),
                        rs.getDouble("PORCENTAJE_DESCUENTO"),
                        rs.getTimestamp("FECHA_INICIO"),
                        rs.getTimestamp("FECHA_FIN"),
                        rs.getBoolean("VIGENCIA"),
                        rs.getInt("NUMERO_USOS")
                    );
                }
            }
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al buscar cupón", ex);
        }
        return null;
    }

    @Override
    public boolean incrementarUsos(int idCupon) throws PersistenciaException {
        String sql = "UPDATE CUPONES SET NUMERO_USOS = NUMERO_USOS + 1 WHERE ID_CUPON = ?";
        try (Connection conn = conexion.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCupon);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al incrementar usos del cupón", ex);
        }
    }
}
