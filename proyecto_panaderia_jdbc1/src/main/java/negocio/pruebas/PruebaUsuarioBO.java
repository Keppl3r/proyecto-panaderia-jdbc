package negocio.pruebas;

import negocio.BOs.IUsuarioBO;
import negocio.fabrica.FabricaBOs;
import persistencia.dominio.Usuario;

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
