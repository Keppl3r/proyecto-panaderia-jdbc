package persistencia.DAOs;

import persistencia.dominio.Cupon;
import persistencia.excepciones.PersistenciaException;

/**
 * DAO para cupones de descuento.
 * @author Adrian Mendoza
 */
public interface ICuponDAO {
    Cupon buscarPorId(int idCupon) throws PersistenciaException;
    boolean incrementarUsos(int idCupon) throws PersistenciaException;
}
