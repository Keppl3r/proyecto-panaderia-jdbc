
package persistencia.excepciones;

/**
 * Clase de excepcion personalizada de persistencia
 * @author Adrian Mendoza 
 */
public class PersistenciaException extends Exception{

    public PersistenciaException() {
    }

    public PersistenciaException(String message) {
        super(message);
    }

    public PersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
    
}
