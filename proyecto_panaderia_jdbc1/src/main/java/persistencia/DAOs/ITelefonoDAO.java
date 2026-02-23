package persistencia.DAOs;

import java.util.List;
import persistencia.dominio.Telefono;
import persistencia.excepciones.PersistenciaException;

/**
 * DAO para teléfonos de clientes.
 * @author Adrian Mendoza
 */
public interface ITelefonoDAO {
    Telefono agregar(Telefono telefono) throws PersistenciaException;
    List<Telefono> obtenerPorCliente(int idUsuario) throws PersistenciaException;
    boolean eliminar(int idTelefono) throws PersistenciaException;
}
