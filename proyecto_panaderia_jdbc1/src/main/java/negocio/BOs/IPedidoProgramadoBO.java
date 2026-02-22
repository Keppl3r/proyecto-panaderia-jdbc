 package negocio.BOs;

     import java.sql.Timestamp;
     import java.util.List;
     import persistencia.dominio.PedidoProgramado;
     import persistencia.dominio.DetallePedido;
     import negocio.excepciones.NegocioException;

     public interface IPedidoProgramadoBO {

       PedidoProgramado programarPedido(negocio.DTOs.PedidoProgramadoNuevoDTO pedidoDTO) throws NegocioException;
     }