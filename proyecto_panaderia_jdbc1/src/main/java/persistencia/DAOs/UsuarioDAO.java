package persistencia.DAOs;

import java.sql.*;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.Usuario;
import persistencia.excepciones.PersistenciaException;

/**
 * @author Adrian Mendoza
 */
public class UsuarioDAO implements IUsuarioDAO {

    private IConexionBD conexion;

    public UsuarioDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public Usuario login(String username, String passwordEncriptado) throws PersistenciaException {
        String sql = "SELECT ID_USUARIO, USERNAME, PASSWORD, ROL FROM USUARIOS WHERE USERNAME = ? AND PASSWORD = ?";
        try (Connection conn = conexion.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passwordEncriptado);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setIdUsuario(rs.getInt("ID_USUARIO"));
                    u.setUsername(rs.getString("USERNAME"));
                    u.setPassword(rs.getString("PASSWORD"));
                    u.setRol(rs.getString("ROL"));
                    return u;
                }
            }
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al iniciar sesión", ex);
        }
        return null;
    }

    @Override
    public Usuario registrar(Usuario usuario) throws PersistenciaException {
        String sql = "INSERT INTO USUARIOS (USERNAME, PASSWORD, ROL) VALUES (?, ?, ?)";
        try (Connection conn = conexion.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getPassword());
            ps.setString(3, usuario.getRol());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setIdUsuario(rs.getInt(1));
                }
            }
            return usuario;
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al registrar usuario", ex);
        }
    }

    @Override
    public boolean existeUsername(String username) throws PersistenciaException {
        String sql = "SELECT COUNT(*) FROM USUARIOS WHERE USERNAME = ?";
        try (Connection conn = conexion.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al verificar username", ex);
        }
        return false;
    }

    @Override
    public boolean actualizarPassword(int idUsuario, String passwordEncriptado) throws PersistenciaException {
        String sql = "UPDATE USUARIOS SET PASSWORD = ? WHERE ID_USUARIO = ?";
        try (Connection conn = conexion.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, passwordEncriptado);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al actualizar contraseña", ex);
        }
    }
}
