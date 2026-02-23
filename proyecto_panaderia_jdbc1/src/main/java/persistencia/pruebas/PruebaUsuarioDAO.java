package persistencia.pruebas;

import persistencia.DAOs.IUsuarioDAO;
import persistencia.DAOs.UsuarioDAO;
import persistencia.conexion.ConexionBD;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.Usuario;
import negocio.encriptacion.EncriptadorPIN;

public class PruebaUsuarioDAO {

    public static void main(String[] args) {
        System.out.println("--- Prueba UsuarioDAO ---");

        try {
            IConexionBD conexion = new ConexionBD();
            IUsuarioDAO usuarioDAO = new UsuarioDAO(conexion);

            // Probar login con usuario existente
            String passEncriptado = EncriptadorPIN.encriptar("cliente123");
            Usuario usuario = usuarioDAO.login("juanito", passEncriptado);

            if (usuario != null) {
                System.out.println("Login exitoso: " + usuario.getUsername() + " - Rol: " + usuario.getRol());
            } else {
                System.out.println("Login fallido");
            }

            // Probar login con password incorrecto
            String passIncorrecto = EncriptadorPIN.encriptar("noexiste");
            Usuario fallido = usuarioDAO.login("juanito", passIncorrecto);
            System.out.println("Login con pass incorrecto: " + (fallido == null ? "rechazado (correcto)" : "aceptado (mal)"));

            // Verificar si existe un username
            boolean existe = usuarioDAO.existeUsername("juanito");
            System.out.println("Existe 'juanito': " + existe);

            boolean noExiste = usuarioDAO.existeUsername("noexiste123");
            System.out.println("Existe 'noexiste123': " + noExiste);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
