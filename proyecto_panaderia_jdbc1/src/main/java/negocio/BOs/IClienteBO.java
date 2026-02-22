package negocio.BOs;

     import negocio.excepciones.NegocioException;
import persistencia.dominio.Cliente;

     /**
      * BO para Cliente - SOLO PARA PEDIDO PROGRAMADO
      */
     public interface IClienteBO {
        public boolean existeCliente(int idCliente) throws NegocioException;
        public Cliente buscarClientePorId(int idCliente) throws NegocioException;
     }