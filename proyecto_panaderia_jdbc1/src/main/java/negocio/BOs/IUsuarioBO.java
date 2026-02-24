package negocio.BOs;

import negocio.excepciones.NegocioException;
import persistencia.dominio.Usuario;

/**
 * Interfaz de la Capa de Negocio para la gestión de Cuentas de Usuario.
 * <p>
 * Se encarga de las operaciones de seguridad, identidad y acceso al sistema.
 * Es el punto de control principal para verificar la legitimidad de las 
 * credenciales antes de permitir el acceso a las funciones protegidas de 
 * la panadería.
 */
public interface IUsuarioBO {
    /**
     * Valida la identidad de un usuario basándose en sus credenciales.
     * <p>
     * La implementación debe verificar que el usuario exista, que la contraseña 
     * (previamente encriptada) coincida con el registro en la base de datos y 
     * determinar el rol asociado (Cliente o Empleado) para la navegación inicial.
     * </p>
     * * @param username Nombre de usuario único en el sistema.
     * @param password Contraseña proporcionada por el usuario (texto plano).
     * @return Objeto {@link Usuario} cargado con los datos de sesión si la 
     * autenticación es exitosa.
     * @throws NegocioException Si las credenciales son incorrectas, el usuario 
     * está bloqueado o hay un error de comunicación.
     */
    Usuario autenticar(String username, String password) throws NegocioException;

    boolean actualizarPassword(int idUsuario, String nuevaPassword) throws NegocioException;
}
