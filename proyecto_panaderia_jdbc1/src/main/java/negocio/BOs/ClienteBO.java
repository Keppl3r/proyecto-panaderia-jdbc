package negocio.BOs;

     import java.util.logging.Level;
     import java.util.logging.Logger;
     import negocio.excepciones.NegocioException;
     import persistencia.DAOs.IClienteDAO;
import persistencia.dominio.Cliente;
     import persistencia.excepciones.PersistenciaException;

     /**
      * BO para Cliente - SOLO PARA PEDIDO PROGRAMADO
      */
     public class ClienteBO implements IClienteBO {

         private IClienteDAO clienteDAO;
         private static final Logger LOG = Logger.getLogger(ClienteBO.class.getName());

         public ClienteBO(IClienteDAO clienteDAO) {
             this.clienteDAO = clienteDAO;
         }

         @Override
         public boolean existeCliente(int idCliente) throws NegocioException {
             if (idCliente <= 0) {
                 throw new NegocioException("ID de cliente inválido");
             }

             try {
                 return clienteDAO.existeClienteActivo(idCliente);
             } catch (PersistenciaException ex) {
                 LOG.log(Level.SEVERE, "Error al verificar cliente", ex);
                 throw new NegocioException("Error al verificar cliente");
             }
         }

    @Override
    public Cliente buscarClientePorId(int idCliente) throws NegocioException {
        if(idCliente <= 0){
            throw new NegocioException("ID cliente invalido");
        }
        try {
            Cliente cliente = clienteDAO.buscarPorId(idCliente);
            if(cliente == null){
                throw new NegocioException("Cliente no encontrado");
            }
            return cliente;
        } catch (PersistenciaException e) {
            LOG.log(Level.SEVERE,"Error al obtener cliente", e);
            throw new NegocioException("Error al obtener cliente");
        }
    }

    
     }