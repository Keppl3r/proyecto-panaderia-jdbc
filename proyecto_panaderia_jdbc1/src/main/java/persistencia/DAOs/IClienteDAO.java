 package persistencia.DAOs;

     import persistencia.dominio.Cliente;
     import persistencia.excepciones.PersistenciaException;

     /**
      * DAO para Cliente - SOLO PARA PEDIDO PROGRAMADO
      */
     public interface IClienteDAO {
         Cliente buscarPorId(int idUsuario) throws PersistenciaException;
         boolean existeClienteActivo(int idUsuario) throws PersistenciaException;
     }