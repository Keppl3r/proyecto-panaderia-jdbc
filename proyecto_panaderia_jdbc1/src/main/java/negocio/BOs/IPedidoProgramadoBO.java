 package negocio.BOs;

     import java.sql.Timestamp;
     import java.util.List;
     import persistencia.dominio.PedidoProgramado;
     import persistencia.dominio.DetallePedido;
     import negocio.excepciones.NegocioException;

     public interface IPedidoProgramadoBO {

         PedidoProgramado programarPedido(int idCliente, List<DetallePedido> detalles,
                                        Timestamp fechaEntrega, Integer idCupon) throws NegocioException;
     }