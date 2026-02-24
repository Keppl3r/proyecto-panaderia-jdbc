package persistencia.DAOs;

import java.sql.*;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.Usuario;
import persistencia.excepciones.PersistenciaException;

/**
 * Implementación de persistencia para la gestión de cuentas de usuario y seguridad.
 * <p>
 * Esta clase centraliza las operaciones de autenticación, registro básico y validación 
 * de identidad en el sistema, sirviendo como base tanto para clientes como para empleados.
 * </p>
 * @author Adrian Mendoza
 */
public class UsuarioDAO implements IUsuarioDAO {

    private IConexionBD conexion;
    /**
     * Inicializa el DAO con el gestor de conexión configurado.
     * * @param conexion Instancia de {@code IConexionBD} para el acceso a la base de datos.
     */
    public UsuarioDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }
    /**
     * Valida las credenciales de un usuario para permitir el acceso al sistema.
     * <p>
     * La consulta verifica que el nombre de usuario y la contraseña (ya cifrada) coincidan,
     * y además confirma que la cuenta se encuentre en estado 'ACTIVO'.
     * </p>
     * * @param username El nombre de usuario único.
     * @param passwordEncriptado La contraseña procesada mediante el algoritmo de hash.
     * @return Un objeto {@code Usuario} con sus datos de perfil y rol si las credenciales 
     * son válidas; {@code null} en caso contrario.
     * @throws PersistenciaException Si ocurre un fallo técnico en la consulta o conexión.
     */
    @Override
    public Usuario login(String username, String passwordEncriptado) throws PersistenciaException {
        String sql = "SELECT ID_USUARIO, USERNAME, PASSWORD, ROL, ESTADO FROM USUARIOS WHERE USERNAME = ? AND PASSWORD = ? AND ESTADO = 'ACTIVO'";
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
    /**
     * Crea un nuevo registro de usuario en la base de datos.
     * <p>
     * Este método se encarga de la inserción inicial en la tabla USUARIOS y recupera 
     * el ID generado automáticamente para mantener la integridad del objeto en memoria.
     * </p>
     * * @param usuario El objeto {@code Usuario} con las credenciales y el rol asignado.
     * @return El objeto {@code Usuario} persistido con su ID autogenerado.
     * @throws PersistenciaException Si el username ya existe o hay un error de inserción.
     */
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
    /**
     * Verifica si un nombre de usuario ya se encuentra registrado en el sistema.
     * <p>
     * Se utiliza principalmente durante el proceso de registro para prevenir duplicados
     * y asegurar que la restricción de unicidad de la base de datos no sea violada.
     * </p>
     * * @param username El nombre de usuario a buscar.
     * @return {@code true} si el nombre de usuario ya está en uso; {@code false} si está disponible.
     * @throws PersistenciaException Si ocurre un error durante el conteo de registros.
     */
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
}