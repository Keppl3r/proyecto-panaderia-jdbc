package persistencia.DAOs;

import persistencia.dominio.Cliente;
import persistencia.excepciones.PersistenciaException;

/**
 * /**
 * Interfaz que define las operaciones de persistencia para la entidad Cliente.
 * <p>
 * Establece los métodos necesarios para la gestión de cuentas de clientes, 
 * incluyendo la búsqueda de perfiles, validación de estado operativo y 
 * el registro de nuevos usuarios en el sistema.
 */
public interface IClienteDAO {
    /**
     * Recupera la información detallada de un cliente a partir de su ID de usuario.
     * <p>
     * Se espera que la implementación realice un Join con la tabla de Usuarios 
     * para retornar un objeto completo.
     * </p>
     * * @param idUsuario Identificador único del usuario/cliente.
     * @return Objeto {@link Cliente} si existe, o {@code null} si no se encuentra.
     * @throws PersistenciaException Si ocurre un error técnico en la base de datos.
     */
    Cliente buscarPorId(int idUsuario) throws PersistenciaException;
    /**
     * Valida de forma rápida si un cliente existe y tiene permitido realizar operaciones.
     * <p>
     * Un cliente activo es aquel cuyo estado no ha sido suspendido o dado de baja lógica.
     * </p>
     * * @param idUsuario Identificador del usuario a validar.
     * @return {@code true} si el cliente está activo, {@code false} en caso contrario.
     * @throws PersistenciaException Si falla la comunicación con el servidor de datos.
     */
    boolean existeClienteActivo(int idUsuario) throws PersistenciaException;
    /**
     * Almacena un nuevo cliente en el sistema.
     * <p>
     * La implementación debe garantizar la creación atómica tanto del registro de 
     * usuario (credenciales) como del perfil de cliente (datos personales).
     * </p>
     * * @param cliente Objeto con la información del nuevo cliente a registrar.
     * @return El objeto {@link Cliente} persistido, idealmente con su ID generado.
     * @throws PersistenciaException Si hay una violación de restricciones (ej. usuario duplicado) 
     * o error de conexión.
     */
    Cliente registrar(Cliente cliente) throws PersistenciaException;
    /**
     * Realiza la actualización persistente de los datos de un cliente en la base de datos.
     * * Este método comunica la capa de negocio con la base de datos para modificar 
     * los registros existentes. Actualiza tanto la información de la cuenta (credenciales) 
     * como el perfil personal y domicilio, utilizando el identificador único del cliente.
     *
     * @param cliente El objeto {@code Cliente} que contiene los nuevos datos a persistir. 
     * Debe contener un ID válido para localizar el registro.
     * @return {@code true} si se encontró el registro y se actualizó correctamente; 
     * {@code false} si no se realizaron cambios en la base de datos.
     * @throws PersistenciaException Si ocurre un error técnico en el motor de base de datos, 
     * problemas de conexión o violación de restricciones de integridad.
     */
    public boolean actualizar(Cliente cliente) throws PersistenciaException;
}
