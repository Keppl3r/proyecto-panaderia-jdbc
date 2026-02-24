package negocio.pruebas;

import negocio.encriptacion.EncriptadorPIN;

/**
 * Clase de prueba para validar el funcionamiento del algoritmo de cifrado.
 * <p>
 * Esta clase ejecuta un test de caja blanca sobre la utilidad {@code EncriptadorPIN}, 
 * asegurando que:
 * <ul>
 * <li>El proceso de hashing sea determinista o verificable.</li>
 * <li>La longitud del hash generado sea compatible con el almacenamiento en la BD.</li>
 * <li>El método de verificación distinga correctamente entre credenciales válidas e inválidas.</li>
 * <li>El algoritmo soporte tanto cadenas alfanuméricas como PINs numéricos.</li>
 * </ul>
 * </p>
 */
public class PruebaEncriptador {

    public static void main(String[] args) {
        System.out.println("--- Prueba EncriptadorPIN ---");

        try {
            // Encriptar un texto
            String texto = "cliente123";
            String hash = EncriptadorPIN.encriptar(texto);
            System.out.println("Texto: " + texto);
            System.out.println("Hash: " + hash);
            System.out.println("Largo del hash: " + hash.length());

            // Verificar que coincida
            boolean coincide = EncriptadorPIN.verificar("cliente123", hash);
            System.out.println("Verificar correcto: " + coincide);

            boolean noConcide = EncriptadorPIN.verificar("otrapalabra", hash);
            System.out.println("Verificar incorrecto: " + noConcide);

            // Probar con un PIN de 8 digitos
            String pin = "83492015";
            String pinHash = EncriptadorPIN.encriptar(pin);
            System.out.println("PIN: " + pin);
            System.out.println("PIN encriptado: " + pinHash);
            System.out.println("Verificar PIN: " + EncriptadorPIN.verificar(pin, pinHash));

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
