package persistencia.conexion;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interfaz que define el los metodos para establecer una conexión con la base de datos.
 *
 * @author Adrian Mendoza
 */
public interface IConexionBD {

    /**
     * Crea y retorna una conexión activa con la base de datos.
     *
     * @return una instancia de {@Connection} lista para ser utilizada
     * @throws SQLException si ocurre un error al intentar establecer la conexión
     */
    Connection crearConexion() throws SQLException;

}
