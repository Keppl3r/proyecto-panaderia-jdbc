package negocio.BOs;

import java.util.List;
import negocio.excepciones.NegocioException;
import persistencia.dominio.Cliente;
import persistencia.dominio.Telefono;

/**
 * BO para Cliente
 */
public interface IClienteBO {
    boolean existeCliente(int idCliente) throws NegocioException;

    Cliente registrarCliente(Cliente cliente) throws NegocioException;

    Cliente obtenerClientePorId(int idUsuario) throws NegocioException;

    boolean actualizarCliente(Cliente cliente) throws NegocioException;

    boolean desactivarCliente(int idUsuario) throws NegocioException;

    List<Telefono> obtenerTelefonos(int idUsuario) throws NegocioException;

    void actualizarTelefonos(int idUsuario, List<Telefono> telefonos) throws NegocioException;
}