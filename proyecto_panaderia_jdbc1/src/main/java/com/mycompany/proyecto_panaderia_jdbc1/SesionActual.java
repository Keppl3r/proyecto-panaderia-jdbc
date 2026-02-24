package com.mycompany.proyecto_panaderia_jdbc1;

import persistencia.dominio.Cliente;
import persistencia.dominio.Usuario;

/**
 * @author Adrian Mendoza
 */
public class SesionActual {

    private static Usuario usuarioActual;
    private static Cliente clienteActual;

    public static void iniciarSesion(Usuario usuario, Cliente cliente) {
        usuarioActual = usuario;
        clienteActual = cliente;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
        clienteActual = null;
    }

    public static Usuario getUsuario() {
        return usuarioActual;
    }

    public static Cliente getCliente() {
        return clienteActual;
    }

    public static boolean isLogeado() {
        return usuarioActual != null;
    }

    public static boolean esCliente() {
        return usuarioActual != null && usuarioActual.esCliente();
    }

    public static boolean esEmpleado() {
        return usuarioActual != null && usuarioActual.esEmpleado();
    }

    public static String getNombreDisplay() {
        if (clienteActual != null) {
            return clienteActual.getNombres();
        }
        return usuarioActual != null ? usuarioActual.getUsername() : "Usuario";
    }

    public static int getIdUsuario() {
        return usuarioActual != null ? usuarioActual.getIdUsuario() : -1;
    }
}
