package persistencia.DAOs;

import persistencia.dominio.Usuario;
import persistencia.excepciones.PersistenciaException;

/**
 * DAO para autenticación y registro de usuarios.
 *
 * @author Adrian Mendoza
 */
public interface IUsuarioDAO {

    Usuario login(String username, String passwordEncriptado) throws PersistenciaException;

    Usuario registrar(Usuario usuario) throws PersistenciaException;

    boolean existeUsername(String username) throws PersistenciaException;
}
