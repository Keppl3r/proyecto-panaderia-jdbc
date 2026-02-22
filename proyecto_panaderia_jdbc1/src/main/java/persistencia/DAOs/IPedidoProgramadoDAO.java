 package persistencia.DAOs;

     import java.sql.Timestamp;
     import java.util.List;
     import persistencia.dominio.PedidoProgramado;
     import persistencia.excepciones.PersistenciaException;

     /**
      * DAO para PedidoProgramado - SOLO CASO DE USO
      */
     public interface IPedidoProgramadoDAO {

         PedidoProgramado crear(PedidoProgramado pedido) throws PersistenciaException;
         int generarNumPedido() throws PersistenciaException;
         boolean validarFechaEntrega(Timestamp fechaEntrega) throws PersistenciaException;
     }