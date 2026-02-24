
package persistencia.excepciones;

/**
 * Excepción personalizada para gestionar errores en la capa de persistencia.
 * <p>
 * Esta clase actúa como un envoltorio (wrapper) para capturar fallos específicos 
 * relacionados con el acceso a datos, como errores de SQL, pérdida de conexión 
 * con el servidor o violaciones de integridad, evitando que los detalles 
 * técnicos se propaguen a las capas superiores.
 * </p>
 * * @author Adrian Mendoza 
 */
public class PersistenciaException extends Exception {

    /**
     * Constructor por defecto de la excepción.
     * Crea una instancia sin un mensaje descriptivo específico.
     */
    public PersistenciaException() {
        super();
    }

    /**
     * Constructor que permite definir un mensaje de error personalizado.
     * * @param message Descripción detallada del error ocurrido.
     */
    public PersistenciaException(String message) {
        super(message);
    }

    /**
     * Constructor que permite definir un mensaje y capturar la causa original del error.
     * <p>
     * Es especialmente útil para capturar una {@code SQLException} y relanzarla 
     * bajo este tipo de excepción, manteniendo la traza del error original.
     * </p>
     * * @param message Descripción del error orientado al contexto del sistema.
     * @param cause   La instancia de la excepción raíz (ej. SQL o de conexión).
     */
    public PersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }
}