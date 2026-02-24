package persistencia.DAOs;

import persistencia.dominio.Usuario;
import persistencia.excepciones.PersistenciaException;

/**
 * Interfaz que define las operaciones de persistencia para la gestión de cuentas de usuario.
 * <p>
 * Centraliza las funciones de autenticación y validación de identidad, asegurando 
 * que solo usuarios registrados puedan acceder a funciones sensibles del sistema 
 * de la panadería.
 * </p>
 * @author Adrian Mendoza
 */
public interface IUsuarioDAO {
    /**
     * Valida las credenciales de un usuario contra la base de datos.
     * <p>
     * Se debe recibir la contraseña ya procesada por el {@link EncriptadorPIN} 
     * para realizar una comparación de hashes en el servidor.
     * </p>
     * @param username Nombre de usuario único.
     * @param passwordEncriptado Hash SHA-256 de la contraseña.
     * @return Objeto {@link Usuario} poblado si las credenciales son válidas; 
     * {@code null} en caso contrario.
     * @throws PersistenciaException Si ocurre un fallo técnico en la consulta SQL.
     */
    Usuario login(String username, String passwordEncriptado) throws PersistenciaException;
    /**
     * Crea un registro de usuario básico en el sistema.
     * <p>
     * Este método es utilizado frecuentemente de forma interna por el proceso 
     * de registro de clientes para generar la cuenta de acceso.
     * </p>
     * @param usuario Instancia con username, password (encriptado) y rol.
     * @return El objeto {@link Usuario} con el ID generado.
     * @throws PersistenciaException Si el username ya está en uso o hay error de conexión.
     */
    Usuario registrar(Usuario usuario) throws PersistenciaException;
    /**
     * Comprueba la disponibilidad de un nombre de usuario.
     * <p>
     * Esencial para validaciones en tiempo real durante el registro, 
     * evitando colisiones de identidad en la tabla USUARIOS.
     * </p>
     * @param username El nombre de usuario a verificar.
     * @return {@code true} si el nombre de usuario ya está registrado; 
     * {@code false} si está disponible.
     * @throws PersistenciaException Si falla la comunicación con la BD.
     */
    boolean existeUsername(String username) throws PersistenciaException;
}
