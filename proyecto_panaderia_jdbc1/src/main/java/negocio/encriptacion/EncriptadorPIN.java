package negocio.encriptacion;

import java.security.MessageDigest;
import negocio.excepciones.NegocioException;

/**
 * Encripta textos con SHA-256 para guardar contraseñas y PINs seguros.
 * 
 * @author Adrian Mendoza
 */
public class EncriptadorPIN {

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

    public static boolean verificar(String texto, String hashGuardado) throws NegocioException {
        try {
            return encriptar(texto).equals(hashGuardado);
        } catch (Exception e) {
            throw new NegocioException("Error al verificar encriptacion", e);
        }
    }
}
