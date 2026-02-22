/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.dominio;

/**
 *Representa un usuario del sistema.
 *Clase padre para clientes y empleados
 * @author Jazmin
 * @author Adrian
 */
public class Usuario {

    private int idUsuario;
    private String username;
    private String password;
    private String rol; // CLIENTE, EMPLEADO

    /**
     * Constructor vacío para crear usuario sin datos.
     */
    public Usuario() {
    }
    /**
     * Constructor completo para usuario.
     * @param idUsuario identificador único del usuario
     * @param username nombre de usuario para login
     * @param password contraseña del usuario
     * @param rol tipo de usuario CLIENTE o EMPLEADO
     */
    public Usuario(int idUsuario, String username, String password, String rol) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    public Usuario(String username, String password, String rol) {
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

 
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    
    public boolean esCliente() {
        return "CLIENTE".equals(rol);
    }

    public boolean esEmpleado() {
        return "EMPLEADO".equals(rol);
    }

    @Override
    public String toString() {
        return "Usuario{idUsuario=" + idUsuario + ", username='" + username + "', rol='" + rol + "'}";
    }
}
