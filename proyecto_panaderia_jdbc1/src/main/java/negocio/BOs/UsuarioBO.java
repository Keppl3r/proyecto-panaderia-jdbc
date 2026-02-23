package negocio.BOs;

import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.encriptacion.EncriptadorPIN;
import negocio.excepciones.NegocioException;
import persistencia.DAOs.IUsuarioDAO;
import persistencia.dominio.Usuario;
import persistencia.excepciones.PersistenciaException;

/**
 * @author Adrian Mendoza
 */
public class UsuarioBO implements IUsuarioBO {

    private IUsuarioDAO usuarioDAO;
    private static final Logger LOG = Logger.getLogger(UsuarioBO.class.getName());

    public UsuarioBO(IUsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

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
