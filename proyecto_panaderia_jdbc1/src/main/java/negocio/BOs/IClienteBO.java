package negocio.BOs;

import negocio.excepciones.NegocioException;
import persistencia.dominio.Cliente;

/**
 * BO para Cliente
 */
public interface IClienteBO {
    boolean existeCliente(int idCliente) throws NegocioException;

    Cliente registrarCliente(Cliente cliente) throws NegocioException;
}