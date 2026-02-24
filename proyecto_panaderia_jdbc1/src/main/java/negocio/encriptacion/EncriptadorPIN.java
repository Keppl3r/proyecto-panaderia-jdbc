package negocio.encriptacion;

import java.security.MessageDigest;
import negocio.excepciones.NegocioException;

/**
 * Utilidad criptográfica para el manejo seguro de credenciales y PINs.
 * <p>
 * Implementa el algoritmo SHA-256 para generar resúmenes (hashes) unidireccionales.
 * Esta clase es fundamental para la validación de entregas express y el inicio 
 * de sesión de usuarios, garantizando que la información sensible sea ilegible 
 * incluso en caso de acceso no autorizado a la base de datos.
 * </p>
 * @author Adrian Mendoza
 */
public class EncriptadorPIN {
    /**
     * Transforma una cadena de texto en un hash hexadecimal utilizando SHA-256.
     * <p>
     * El proceso es irreversible; una vez obtenido el hash, no se puede recuperar 
     * el texto original.
     * </p>
     * * @param texto Cadena de texto (PIN o contraseña) a proteger.
     * @return Representación hexadecimal de 64 caracteres del hash generado.
     * @throws NegocioException Si el algoritmo SHA-256 no está disponible en el entorno.
     */
    public static String encriptar(String texto) throws NegocioException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(texto.getBytes());

            // Convertir los bytes a texto hexadecimal
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new NegocioException("Error al encriptar", e);
        }
    }
    /**
     * Compara un texto en plano contra un hash previamente guardado.
     * <p>
     * Re-encripta el texto proporcionado y verifica si el resultado coincide 
     * exactamente con el hash almacenado en la persistencia.
     * </p>
     * * @param texto Texto en plano ingresado por el usuario/empleado.
     * @param hashGuardado Hash recuperado de la base de datos.
     * @return {@code true} si las credenciales coinciden, {@code false} en caso contrario.
     * @throws NegocioException Si ocurre un error durante el proceso de comparación.
     */
    public static boolean verificar(String texto, String hashGuardado) throws NegocioException {
        try {
            return encriptar(texto).equals(hashGuardado);
        } catch (Exception e) {
            throw new NegocioException("Error al verificar encriptacion", e);
        }
    }
}
