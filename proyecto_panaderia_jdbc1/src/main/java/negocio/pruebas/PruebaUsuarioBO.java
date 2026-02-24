package negocio.pruebas;

import negocio.BOs.IUsuarioBO;
import negocio.fabrica.FabricaBOs;
import persistencia.dominio.Usuario;

/**
 * Clase de prueba para validar el sistema de autenticación y gestión de sesiones.
 * <p>
 * Esta clase realiza un test de integración sobre la capa de negocio de Usuarios, 
 * verificando los siguientes flujos de seguridad:
 * <ul>
 * <li>Autenticación exitosa de clientes registrados con credenciales correctas.</li>
 * <li>Validación de rechazo ante contraseñas incorrectas o usuarios inexistentes.</li>
 * <li>Correcta identificación y recuperación del Rol del usuario (CLIENTE/EMPLEADO).</li>
 * <li>Integración con la fábrica de objetos de negocio {@code FabricaBOs}.</li>
*/
public class PruebaUsuarioBO {

    public static void main(String[] args) {
        System.out.println("--- Prueba UsuarioBO ---");

        try {
            IUsuarioBO usuarioBO = FabricaBOs.obtenerUsuarioBO();

            // Login correcto
            Usuario usuario = usuarioBO.autenticar("juanito", "cliente123");
            System.out.println("Login exitoso: " + usuario.getUsername() + " (" + usuario.getRol() + ")");

        } catch (Exception e) {
            System.out.println("Error esperado o login fallido: " + e.getMessage());
        }

        try {
            IUsuarioBO usuarioBO = FabricaBOs.obtenerUsuarioBO();

            // Login incorrecto
            Usuario fallido = usuarioBO.autenticar("juanito", "passwordmal");
            System.out.println("Esto no deberia imprimirse");

        } catch (Exception e) {
            System.out.println("Login rechazado correctamente: " + e.getMessage());
        }

        try {
            IUsuarioBO usuarioBO = FabricaBOs.obtenerUsuarioBO();

            // Login empleado
            Usuario empleado = usuarioBO.autenticar("admin1", "empleado123");
            System.out.println("Login empleado: " + empleado.getUsername() + " (" + empleado.getRol() + ")");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
