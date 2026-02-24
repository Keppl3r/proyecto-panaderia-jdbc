package negocio.BOs;

import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.encriptacion.EncriptadorPIN;
import negocio.excepciones.NegocioException;
import persistencia.DAOs.IUsuarioDAO;
import persistencia.dominio.Usuario;
import persistencia.excepciones.PersistenciaException;

/**
 * /**
 * Implementación de la lógica de negocio para la gestión de seguridad y usuarios.
 * <p>
 * Esta clase se encarga de validar el acceso al sistema, coordinando la encriptación
 * de contraseñas y la verificación de credenciales contra la capa de persistencia.
 * Actúa como el punto de entrada principal para establecer sesiones de usuario.
 * </p>
 * @author Adrian Mendoza
 */
public class UsuarioBO implements IUsuarioBO {

    private IUsuarioDAO usuarioDAO;
    private static final Logger LOG = Logger.getLogger(UsuarioBO.class.getName());
    /**
     * Constructor que inyecta la dependencia del DAO de usuarios.
     * @param usuarioDAO Interfaz de acceso a datos para operaciones de usuario.
     */
    public UsuarioBO(IUsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }
    /**
     * Realiza el proceso de autenticación de un usuario en el sistema.
     * <p>
     * El flujo de autenticación sigue estos pasos:
     * <ol>
     * <li>Valida que los campos no estén vacíos o nulos.</li>
     * <li>Encripta la contraseña proporcionada utilizando el estándar de la aplicación.</li>
     * <li>Consulta al DAO para verificar la coincidencia de credenciales.</li>
     * <li>Registra el éxito o fracaso de la operación en el log.</li>
     * </ol>
     * </p>
     * * @param username Nombre de usuario único registrado.
     * @param password Contraseña en texto plano proporcionada en el formulario.
     * @return Objeto {@link Usuario} si las credenciales son válidas.
     * @throws NegocioException Si los datos son incompletos, las credenciales son 
     * incorrectas o ocurre un error en la base de datos.
     */
    @Override
    public Usuario autenticar(String username, String password) throws NegocioException {
        if (username == null || username.isBlank()) {
            throw new NegocioException("El nombre de usuario es obligatorio");
        }
        if (password == null || password.isBlank()) {
            throw new NegocioException("La contraseña es obligatoria");
        }

        try {
            String passwordEncriptado = EncriptadorPIN.encriptar(password);
            Usuario usuario = usuarioDAO.login(username, passwordEncriptado);

            if (usuario == null) {
                throw new NegocioException("Usuario o contraseña incorrectos");
            }

            LOG.info("Login exitoso: " + username);
            return usuario;
        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error en autenticación", ex);
            throw new NegocioException("Error al iniciar sesión", ex);
        }
    }
}
