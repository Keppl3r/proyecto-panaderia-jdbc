package persistencia.DAOs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.Telefono;
import persistencia.excepciones.PersistenciaException;

/**
 * @author Adrian Mendoza
 */
public class TelefonoDAO implements ITelefonoDAO {

    private IConexionBD conexion;

    public TelefonoDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public Telefono agregar(Telefono telefono) throws PersistenciaException {
        String sql = "INSERT INTO TELEFONOS (ID_USUARIO, ETIQUETA, NUMERO) VALUES (?, ?, ?)";
        try (Connection conn = conexion.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, telefono.getIdUsuario());
            ps.setString(2, telefono.getEtiqueta());
            ps.setString(3, telefono.getNumero());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) telefono.setIdTelefono(rs.getInt(1));
            }
            return telefono;
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al agregar teléfono", ex);
        }
    }

    @Override
    public List<Telefono> obtenerPorCliente(int idUsuario) throws PersistenciaException {
        List<Telefono> telefonos = new ArrayList<>();
        String sql = "SELECT * FROM TELEFONOS WHERE ID_USUARIO = ?";
        try (Connection conn = conexion.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    telefonos.add(new Telefono(
                        rs.getInt("ID_TELEFONO"), rs.getInt("ID_USUARIO"),
                        rs.getString("ETIQUETA"), rs.getString("NUMERO")
                    ));
                }
            }
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al obtener teléfonos", ex);
        }
        return telefonos;
    }

    @Override
    public boolean eliminar(int idTelefono) throws PersistenciaException {
        String sql = "DELETE FROM TELEFONOS WHERE ID_TELEFONO = ?";
        try (Connection conn = conexion.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTelefono);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al eliminar teléfono", ex);
        }
    }
}
