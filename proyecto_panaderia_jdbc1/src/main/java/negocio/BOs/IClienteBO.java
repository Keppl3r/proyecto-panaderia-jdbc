package negocio.BOs;

import negocio.excepciones.NegocioException;

/**
 * BO para Cliente
 */
public interface IClienteBO {
    boolean existeCliente(int idCliente) throws NegocioException;

}