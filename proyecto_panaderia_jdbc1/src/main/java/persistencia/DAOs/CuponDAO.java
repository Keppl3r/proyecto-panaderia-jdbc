package persistencia.DAOs;

import java.sql.*;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.Cupon;
import persistencia.excepciones.PersistenciaException;

/**
 * Implementación de la persistencia para la entidad Cupon.
 * <p>
 * Se encarga de la recuperación de reglas de descuento y de la actualización
 * del contador de usos, permitiendo un control estricto sobre las promociones
 * aplicadas a los pedidos programados.
 * </p>
 * @author Adrian Mendoza
 */
public class CuponDAO implements ICuponDAO {

    private IConexionBD conexion;
    /**
     * @param conexion Gestor de conexión a la base de datos.
     */
    public CuponDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }
    /**
     * Recupera la información completa de un cupón mediante su ID.
     * @param idCupon Identificador único del cupón.
     * @return Objeto {@link Cupon} poblado o {@code null} si no se encuentra.
     * @throws PersistenciaException Si ocurre un error en la consulta SQL.
     */
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
    /**
     * Incrementa de forma atómica el contador de usos de un cupón específico.
     * <p>
     * Este método se invoca usualmente después de confirmar exitosamente un pedido
     * que incluye un descuento.
     * </p>
     * @param idCupon ID del cupón a actualizar.
     * @return {@code true} si la actualización fue exitosa, {@code false} si el ID no existe.
     * @throws PersistenciaException Si falla la comunicación con la base de datos.
     */
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
