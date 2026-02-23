package negocio.BOs;

import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.encriptacion.EncriptadorPIN;
import negocio.excepciones.NegocioException;
import persistencia.DAOs.IClienteDAO;
import persistencia.DAOs.IUsuarioDAO;
import persistencia.DAOs.ITelefonoDAO;
import persistencia.dominio.Cliente;
import persistencia.dominio.Telefono;
import persistencia.excepciones.PersistenciaException;

/**
 * @author Adrian Mendoza
 */
public class ClienteBO implements IClienteBO {

    private IClienteDAO clienteDAO;
    private IUsuarioDAO usuarioDAO;
    private ITelefonoDAO telefonoDAO;
    private static final Logger LOG = Logger.getLogger(ClienteBO.class.getName());

    public ClienteBO(IClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    public ClienteBO(IClienteDAO clienteDAO, IUsuarioDAO usuarioDAO, ITelefonoDAO telefonoDAO) {
        this.clienteDAO = clienteDAO;
        this.usuarioDAO = usuarioDAO;
        this.telefonoDAO = telefonoDAO;
    }

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
}