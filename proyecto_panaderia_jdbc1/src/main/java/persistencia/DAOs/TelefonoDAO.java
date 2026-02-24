package persistencia.DAOs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.Telefono;
import persistencia.excepciones.PersistenciaException;

/**
 * Implementación de persistencia para la gestión de números telefónicos de los usuarios.
 * <p>
 * Esta clase permite administrar la relación uno-a-muchos entre un Usuario y sus 
 * diversos contactos telefónicos, permitiendo operaciones de registro, consulta por 
 * cliente y eliminación individual.
 * </p>
 * @author Adrian Mendoza
 */
public class TelefonoDAO implements ITelefonoDAO {

    private IConexionBD conexion;
    /**
     * Inicializa el DAO con el gestor de conexión proporcionado.
     * * @param conexion Instancia de {@code IConexionBD} para el acceso a la base de datos.
     */
    public TelefonoDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }
    /**
     * Registra un nuevo número telefónico asociado a un usuario.
     * <p>
     * Tras una inserción exitosa, el objeto {@code Telefono} se actualiza con el 
     * ID generado automáticamente por la base de datos.
     * </p>
     * * @param telefono El objeto {@code Telefono} con los datos a persistir (ID_USUARIO, ETIQUETA, NUMERO).
     * @return El objeto {@code Telefono} con su identificador de base de datos asignado.
     * @throws PersistenciaException Si ocurre un error durante la ejecución del INSERT o la recuperación de llaves.
     */
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
    /**
     * Recupera todos los números telefónicos registrados para un usuario específico.
     * * @param idUsuario Identificador único del usuario/cliente.
     * @return Una lista de objetos {@code Telefono} pertenecientes al usuario; 
     * lista vacía si no existen registros.
     * @throws PersistenciaException Si falla la consulta SQL o la conexión.
     */
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
    /**
     * Elimina un registro telefónico específico de la base de datos.
     * * @param idTelefono Identificador único del teléfono a eliminar.
     * @return {@code true} si se eliminó el registro exitosamente; {@code false} en caso contrario.
     * @throws PersistenciaException Si ocurre un error de integridad o de sintaxis SQL.
     */
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
