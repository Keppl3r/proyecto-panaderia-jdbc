package negocio.BOs;

     import negocio.excepciones.NegocioException;

     /**
      * BO para Cliente - SOLO PARA PEDIDO PROGRAMADO
      */
     public interface IClienteBO {
         boolean existeCliente(int idCliente) throws NegocioException;
         
     }