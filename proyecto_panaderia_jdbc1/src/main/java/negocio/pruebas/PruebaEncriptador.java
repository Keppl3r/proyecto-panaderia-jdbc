package negocio.pruebas;

import negocio.encriptacion.EncriptadorPIN;

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
