package negocio.BOs;

import negocio.excepciones.NegocioException;
import persistencia.dominio.Cliente;

/**
 * Interfaz de la Capa de Negocio para la gestión de Clientes.
 * <p>
 * Define las operaciones permitidas para el manejo de información de clientes,
 * estableciendo un contrato que debe cumplir cualquier implementación de lógica 
 * de negocio (BO). Se encarga de actuar como puente entre la capa de presentación 
 * y la capa de acceso a datos (DAO).
 */
public interface IClienteBO {
    /**
     * Verifica la existencia de un cliente en el sistema.
     * * @param idCliente El identificador único del cliente a consultar.
     * @return {@code true} si el cliente existe y su cuenta está activa; {@code false} en caso contrario.
     * @throws NegocioException Si ocurre un error en la validación de reglas de negocio 
     * o en la comunicación con la base de datos.
     */
    boolean existeCliente(int idCliente) throws NegocioException;
    /**
     * Procesa el alta de un nuevo cliente en el sistema.
     * <p>
     * La implementación debe validar que los datos obligatorios estén presentes, 
     * gestionar la unicidad de las credenciales y asegurar que la persistencia 
     * se realice correctamente.
     * </p>
     * * @param cliente Objeto con la información del cliente a registrar.
     * @return El objeto {@link Cliente} resultante con su ID generado por el sistema.
     * @throws NegocioException Si los datos son inválidos, el nombre de usuario ya existe 
     * o falla el proceso de registro.
     */
    Cliente registrarCliente(Cliente cliente) throws NegocioException;
    /**
     * Recupera el perfil completo de un cliente mediante su ID de usuario.
     * * @param idUsuario El identificador del usuario vinculado al cliente.
     * @return El objeto {@link Cliente} que contiene la información personal y de cuenta.
     * @throws NegocioException Si no se encuentra un registro asociado al ID proporcionado 
     * o si los datos están corruptos.
     */
    Cliente obtenerClientePorId(int idUsuario) throws NegocioException;
    /**
     * Procesa la solicitud de actualización de los datos de un cliente bajo las reglas de negocio.
     * <p>
     * Este método actúa como intermediario entre la vista y la persistencia. Se encarga de:
     * <ul>
     * <li>Validar que la información obligatoria del cliente esté completa.</li>
     * <li>Gestionar la seguridad de la cuenta mediante el cifrado de la nueva contraseña.</li>
     * <li>Transformar errores técnicos de la base de datos en mensajes comprensibles para el usuario.</li>
     * </ul>
     * </p>
     *
     * @param cliente El objeto {@code Cliente} con los cambios solicitados por el usuario.
     * @return {@code true} si la operación se realizó con éxito y los datos fueron validados y guardados.
     * @throws NegocioException Si los datos del cliente son inválidos, si no se encuentra 
     * la sesión o si ocurre un fallo en el proceso de guardado.
     */
     public boolean actualizarCliente(Cliente cliente)throws NegocioException;
    
  
}