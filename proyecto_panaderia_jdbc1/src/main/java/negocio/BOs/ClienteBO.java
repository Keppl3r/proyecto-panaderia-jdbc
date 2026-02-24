package negocio.BOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.encriptacion.EncriptadorPIN;
import negocio.excepciones.NegocioException;
import persistencia.DAOs.IClienteDAO;
import persistencia.DAOs.IUsuarioDAO;
import persistencia.DAOs.ITelefonoDAO;
import persistencia.dominio.Cliente;
import persistencia.dominio.Telefono;
import persistencia.dominio.Usuario;
import persistencia.excepciones.PersistenciaException;

/**
 * Implementación de la lógica de negocio para la gestión de Clientes.
 * <p>
 * Esta clase valida las reglas de negocio antes de interactuar con la capa de
 * persistencia, gestiona la seguridad mediante encriptación y coordina
 * múltiples DAOs para operaciones complejas como el registro integral de un
 * usuario-cliente.
 * </p>
 *
 * * @author Adrian Mendoza
 */
public class ClienteBO implements IClienteBO {

    private IClienteDAO clienteDAO;
    private IUsuarioDAO usuarioDAO;
    private ITelefonoDAO telefonoDAO;
    private static final Logger LOG = Logger.getLogger(ClienteBO.class.getName());

    /**
     * Constructor para operaciones básicas de consulta de clientes.
     *
     * @param clienteDAO Interfaz de acceso a datos de clientes.
     */
    public ClienteBO(IClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    /**
     * Constructor completo para operaciones que requieren gestión de usuarios y
     * teléfonos.
     *
     * @param clienteDAO Interfaz de acceso a datos de clientes.
     * @param usuarioDAO Interfaz para validación y gestión de cuentas de
     * usuario.
     * @param telefonoDAO Interfaz para el registro de contactos telefónicos.
     */
    public ClienteBO(IClienteDAO clienteDAO, IUsuarioDAO usuarioDAO, ITelefonoDAO telefonoDAO) {
        this.clienteDAO = clienteDAO;
        this.usuarioDAO = usuarioDAO;
        this.telefonoDAO = telefonoDAO;
    }

    /**
     * Verifica si un cliente existe y se encuentra en estado activo.
     *
     * * @param idCliente Identificador único del cliente.
     * @return {@code true} si el cliente existe y está activo, {@code false} en
     * caso contrario.
     * @throws NegocioException Si el ID es inválido o ocurre un error en la
     * base de datos.
     */
    @Override
    public boolean existeCliente(int idCliente) throws NegocioException {
        if (idCliente <= 0) {
            throw new NegocioException("ID de cliente invalido");
        }
        try {
            return clienteDAO.existeClienteActivo(idCliente);
        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error al verificar cliente", ex);
            throw new NegocioException("Error al verificar cliente");
        }
    }

    /**
     * Recupera la información detallada de un cliente a partir de su ID de
     * usuario.
     *
     * * @param idUsuario ID vinculado a la cuenta de usuario.
     * @return Objeto {@link Cliente} con la información recuperada.
     * @throws NegocioException Si no se encuentra el cliente o hay errores de
     * persistencia.
     */
    @Override
    public Cliente obtenerClientePorId(int idUsuario) throws NegocioException {
        try {
            Cliente cliente = clienteDAO.buscarPorId(idUsuario);
            if (cliente == null) {
                throw new NegocioException("Cuenta no encontrada o desactivada");
            }
            return cliente;
        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error al obtener cliente por ID", ex);
            throw new NegocioException("Error al obtener los datos del cliente");
        }
    }

    /**
     * Registra un nuevo cliente en el sistema siguiendo las reglas de
     * validación.
     * <p>
     * El proceso incluye: validación de campos obligatorios, verificación de
     * unicidad del username, encriptación de la contraseña, registro de la
     * entidad cliente y registro de teléfonos asociados.
     * </p>
     *
     * * @param cliente Objeto con los datos del nuevo cliente.
     * @return El objeto {@link Cliente} persistido con su ID generado.
     * @throws NegocioException Si el username ya existe, faltan datos o falla
     * la persistencia.
     */
    @Override
    public Cliente registrarCliente(Cliente cliente) throws NegocioException {
        if (cliente.getUsername() == null || cliente.getUsername().isBlank()) {
            throw new NegocioException("El username es obligatorio");
        }
        if (cliente.getPassword() == null || cliente.getPassword().isBlank()) {
            throw new NegocioException("La contraseña es obligatoria");
        }
        if (cliente.getNombres() == null || cliente.getNombres().isBlank()) {
            throw new NegocioException("El nombre es obligatorio");
        }

        try {
            if (usuarioDAO.existeUsername(cliente.getUsername())) {
                throw new NegocioException("El username ya esta registrado");
            }

            // Encriptar password
            cliente.setPassword(EncriptadorPIN.encriptar(cliente.getPassword()));

            // Registrar en BD
            Cliente registrado = clienteDAO.registrar(cliente);

            // Registrar telefonos si tiene
            if (cliente.getTelefonos() != null) {
                for (Telefono tel : cliente.getTelefonos()) {
                    tel.setIdUsuario(registrado.getIdUsuario());
                    telefonoDAO.agregar(tel);
                }
            }

            LOG.info("Cliente registrado: " + registrado.getUsername());
            return registrado;
        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error al registrar cliente", ex);
            throw new NegocioException("Error al registrar cliente", ex);
        }
    }

  /**
     * Realiza la actualización de los datos personales de un cliente tras validar los campos obligatorios.
     * <p>
     * Este método actúa como filtro de integridad; verifica que los atributos esenciales 
     * (nombres y apellido paterno) no sean nulos ni contengan únicamente espacios en blanco 
     * antes de delegar la persistencia al DAO.
     * </p>
     * * @param cliente El objeto {@link Cliente} con la información actualizada.
     * @return {@code true} si la operación fue exitosa en la base de datos.
     * @throws NegocioException Si se violan las reglas de validación o si ocurre un fallo en la capa de datos.
     */
    @Override
    public boolean actualizarCliente(Cliente cliente) throws NegocioException {
        if (cliente.getNombres() == null || cliente.getNombres().isBlank()) {
            throw new NegocioException("El nombre es obligatorio");
        }
        if (cliente.getApellidoPaterno() == null || cliente.getApellidoPaterno().isBlank()) {
            throw new NegocioException("El apellido paterno es obligatorio");
        }
        try {
            return clienteDAO.actualizar(cliente);
        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error al actualizar cliente", ex);
            throw new NegocioException("Error al actualizar los datos del cliente", ex);
        }
    }
    /**
     * Gestiona la baja lógica de un cliente en el sistema.
     * <p>
     * Valida que el identificador sea un valor positivo coherente con la base de datos.
     * Al desactivar, el cliente pierde acceso a funciones de inicio de sesión y pedidos, 
     * pero su historial se mantiene intacto para fines de auditoría.
     * </p>
     * * @param idUsuario Identificador único del cliente a desactivar.
     * @return {@code true} si el cliente fue encontrado y marcado como inactivo.
     * @throws NegocioException Si el ID es inválido o ocurre un error durante el proceso de desactivación.
     */
    @Override
    public boolean desactivarCliente(int idUsuario) throws NegocioException {
        if (idUsuario <= 0) {
            throw new NegocioException("ID de cliente inválido");
        }
        try {
            return clienteDAO.desactivar(idUsuario);
        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error al desactivar cliente", ex);
            throw new NegocioException("Error al desactivar la cuenta", ex);
        }
    }
}

