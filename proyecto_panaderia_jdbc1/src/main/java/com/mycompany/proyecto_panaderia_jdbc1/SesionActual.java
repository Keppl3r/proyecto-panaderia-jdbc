package com.mycompany.proyecto_panaderia_jdbc1;

import persistencia.dominio.Cliente;
import persistencia.dominio.Usuario;

/**
 * Almacén de estado persistente para la sesión del usuario.
 * Utiliza el patrón Singleton (vía métodos estáticos) para garantizar que 
 * la información del usuario y su perfil de cliente estén disponibles
 * en todo el ciclo de vida de la aplicación.
 */
public class SesionActual {

    private static Usuario usuarioActual;
    private static Cliente clienteActual;
    
    /**
     * Vincula los objetos de usuario y cliente a la sesión activa.
     * Se invoca tras una autenticación exitosa en el LoginController.
     * @param usuario Datos de cuenta.
     * @param cliente Datos personales del cliente (puede ser null si es empleado).
     */
    public static void iniciarSesion(Usuario usuario, Cliente cliente) {
        usuarioActual = usuario;
        clienteActual = cliente;
    }
    
    /**
     * Limpia las referencias de memoria, cerrando el acceso a funciones protegidas.
     */
    public static void cerrarSesion() {
        usuarioActual = null;
        clienteActual = null;
    }
    /**
     * Proporciona acceso al objeto Usuario completo en la sesión activa.
     * Útil para recuperar credenciales, roles o configuraciones de cuenta.
     * * @return El objeto Usuario actual o null si no se ha iniciado sesión.
     */
    public static Usuario getUsuario() {
        return usuarioActual;
    }
    /**
     * Proporciona acceso al perfil de Cliente asociado a la sesión.
     * Se utiliza para extraer datos específicos de la persona, como domicilio 
     * o fecha de nacimiento, necesarios en la personalización de la interfaz.
     * * @return El objeto Cliente vinculado o null si el usuario no tiene perfil de cliente.
     */
    public static Cliente getCliente() {
        return clienteActual;
    }
    /**
     * Comprueba si existe una sesión activa en el sistema.
     * @return true si el objeto usuarioActual ha sido instanciado.
     */
    public static boolean isLogeado() {
        return usuarioActual != null;
    }
    /**
     * Verifica si el usuario activo tiene privilegios de Cliente.
     * Combina la verificación de existencia de sesión con el rol interno del usuario.
     * @return true solo si hay sesión y el rol corresponde a cliente.
     */
    public static boolean esCliente() {
        return usuarioActual != null && usuarioActual.esCliente();
    }
    /**
     * Verifica si el usuario activo tiene privilegios de Empleado (Administrativo/Operativo).
     * @return true solo si hay sesión y el rol corresponde a empleado.
     */
    public static boolean esEmpleado() {
        return usuarioActual != null && usuarioActual.esEmpleado();
    }

    /**
     * Determina el nombre a mostrar en la barra de navegación o bienvenida.
     * Prioriza el nombre real del cliente sobre el nombre de usuario técnico.
     * @return Cadena con el nombre para mostrar.
     */
    public static String getNombreDisplay() {
        if (clienteActual != null) {
            return clienteActual.getNombres();
        }
        return usuarioActual != null ? usuarioActual.getUsername() : "Usuario";
    }
    /**
     * Recupera el ID único del usuario en sesión.
     * Este valor es fundamental para operaciones de persistencia (Foreign Keys)
     * en la base de datos, como al registrar una venta o asociar un pedido.
     * * @return El ID del usuario si hay sesión activa; de lo contrario, 
     * devuelve -1 como indicador de estado inválido o sesión inexistente.
     */
    public static int getIdUsuario() {
        return usuarioActual != null ? usuarioActual.getIdUsuario() : -1;
    }
}
