package persistencia.DAOs;

import java.util.List;
import persistencia.dominio.Telefono;
import persistencia.excepciones.PersistenciaException;

/**
 * Interfaz que define las operaciones de persistencia para los números de contacto.
 * <p>
 * Gestiona la relación de uno a muchos entre un cliente y sus teléfonos, 
 * permitiendo mantener múltiples vías de comunicación para la confirmación 
 * y seguimiento de pedidos.
 * @author Adrian Mendoza
 */
public interface ITelefonoDAO {
    /**
     * Registra un nuevo número telefónico vinculado a un usuario.
     * @param telefono Objeto con el número y el ID del usuario propietario.
     * @return El objeto {@link Telefono} con el ID generado por la base de datos.
     * @throws PersistenciaException Si el número ya existe o falla la conexión.
     */
    Telefono agregar(Telefono telefono) throws PersistenciaException;
    /**
     * Recupera todos los números de contacto asociados a un cliente específico.
     * @param idUsuario Identificador único del cliente/usuario.
     * @return {@link List} de teléfonos vinculados a la cuenta.
     * @throws PersistenciaException Si ocurre un error en la consulta SQL.
     */
    List<Telefono> obtenerPorCliente(int idUsuario) throws PersistenciaException;
    /**
     * Elimina de forma permanente un registro telefónico del sistema.
     * @param idTelefono Identificador único del teléfono a remover.
     * @return {@code true} si la eliminación fue exitosa.
     * @throws PersistenciaException Si el teléfono está siendo referenciado por pedidos activos.
     */
    boolean eliminar(int idTelefono) throws PersistenciaException;
}
