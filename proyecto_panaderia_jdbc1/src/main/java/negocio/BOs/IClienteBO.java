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
     * Coordina la actualización de la información de un cliente existente.
     * <p>
     * Este método valida que los campos críticos (nombres y apellido paterno) cumplan 
     * con las reglas de negocio antes de proceder a la persistencia. Si la validación 
     * falla, se detiene el proceso para evitar datos inconsistentes.
     * </p>
     * * @param cliente Objeto {@link Cliente} con los datos actualizados.
     * @return {@code true} si la actualización se realizó con éxito en el sistema.
     * @throws NegocioException Si los datos obligatorios están ausentes o si ocurre 
     * un error técnico en la capa de persistencia.
     */
    boolean actualizarCliente(Cliente cliente) throws NegocioException;
    /**
     * Ejecuta el proceso de baja lógica de un cliente en el sistema.
     * <p>
     * Verifica la validez del identificador proporcionado y solicita al DAO el 
     * cambio de estado del cliente. Este método es preferible a una eliminación 
     * física para mantener la integridad referencial de pedidos históricos.
     * </p>
     * * @param idUsuario Identificador único del usuario/cliente a desactivar.
     * @return {@code true} si el cliente fue desactivado correctamente.
     * @throws NegocioException Si el ID es inválido o el servicio de persistencia 
     * no puede completar la operación.
     */
    boolean desactivarCliente(int idUsuario) throws NegocioException;

}