package negocio.BOs;

import java.sql.Timestamp;
import java.util.List;
import negocio.excepciones.NegocioException;
import persistencia.dominio.Pedido;

public interface IPedidoBO {

    boolean marcarComoListo(int idPedido) throws NegocioException;

    boolean entregarPedido(int idPedido, String metodoPago) throws NegocioException;

    boolean entregarPedidoExpress(String folio, String pin, String metodoPago) throws NegocioException;

    boolean cancelarPedido(int idPedido) throws NegocioException;

    List<Pedido> buscarPorTelefono(String telefono) throws NegocioException;

    List<Pedido> buscarPorFolio(String folio) throws NegocioException;

    List<Pedido> buscarPorFechas(Timestamp inicio, Timestamp fin) throws NegocioException;

    List<Pedido> obtenerPendientesYListos() throws NegocioException;

    List<Pedido> obtenerHistorial(int idCliente) throws NegocioException;
}
