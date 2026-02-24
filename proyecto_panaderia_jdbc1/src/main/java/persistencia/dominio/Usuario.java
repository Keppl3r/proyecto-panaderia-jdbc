/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.dominio;

/**
 * Clase base que representa la identidad de un usuario dentro del sistema.
 * <p>
 * Define las credenciales de acceso y el perfil de seguridad (rol) necesario 
 * para la autenticación y autorización. Esta clase sirve como superclase para 
 * entidades especializadas como {@code Cliente} y posibles perfiles administrativos.
 * </p>
 * * @author Jazmin
 * @author Adrian
 */
public class Usuario {

    private int idUsuario;
    private String username;
    private String password;
    private String rol; // CLIENTE, EMPLEADO

    /**
     * Constructor por defecto.
     * Crea una instancia de usuario sin credenciales iniciales.
     */
    public Usuario() {
    }

    /**
     * Constructor completo para la reconstrucción de usuarios desde persistencia.
     * * @param idUsuario Identificador único asignado por la base de datos.
     * @param username  Nombre de usuario único para el inicio de sesión.
     * @param password  Contraseña del usuario (almacenada como hash).
     * @param rol       Perfil de acceso asignado (ej. "CLIENTE", "EMPLEADO").
     */
    public Usuario(int idUsuario, String username, String password, String rol) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    /**
     * Constructor para el registro de nuevos usuarios en el sistema.
     * * @param username Nombre de identificación del usuario.
     * @param password Contraseña de seguridad.
     * @param rol      Nivel de acceso inicial.
     */
    public Usuario(String username, String password, String rol) {
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    /** @return El identificador numérico único del usuario. */
    public int getIdUsuario() { 
        return idUsuario; 
    }

    /** @param idUsuario El identificador a asignar. */
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario; 
    }

    /** @return El nombre de usuario (login) del sistema. */
    public String getUsername() {
        return username;
    }

    /** @param username El nombre de usuario a establecer. */
    public void setUsername(String username) {
        this.username = username; 
    }

    /** @return La contraseña almacenada. */
    public String getPassword() { 
        return password;
    }

    /** @param password La contraseña (previamente cifrada) a establecer. */
    public void setPassword(String password) {
        this.password = password; 
    }

    /** @return El rol o perfil de seguridad asignado. */
    public String getRol() {
        return rol; 
    }

    /** @param rol El rol a asignar (se recomienda usar constantes o enums). */
    public void setRol(String rol) { 
        this.rol = rol; 
    }

    /**
     * Verifica si el usuario posee privilegios de nivel Cliente.
     * @return {@code true} si el rol es exactamente "CLIENTE"; {@code false} en caso contrario.
     */
    public boolean esCliente() {
        return "CLIENTE".equals(rol);
    }

    /**
     * Verifica si el usuario posee privilegios de nivel Empleado.
     * @return {@code true} si el rol es exactamente "EMPLEADO"; {@code false} en caso contrario.
     */
    public boolean esEmpleado() {
        return "EMPLEADO".equals(rol);
    }

    /**
     * Proporciona una representación textual simplificada del usuario para logs.
     * @return String con el ID, username y rol del objeto.
     */
    @Override
    public String toString() {
        return "Usuario{idUsuario=" + idUsuario + ", username='" + username + "', rol='" + rol + "'}";
    }
}