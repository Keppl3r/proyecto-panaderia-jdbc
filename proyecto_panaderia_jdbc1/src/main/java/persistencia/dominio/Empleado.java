/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.dominio;

/**
 *Representa un empleado del sistema.
 * @author Jazmin
 * @author Adrian
 */
public class Empleado {
    private int idEmpleado;
    private String username;
    private String password;
    

    /**
     * Constructor vacío para crear empleado sin datos.
     */
    public Empleado() {
    }
    /**
     * Constructor completo para empleado.
     * @param idEmpleado identificador único del usuario
     * @param username nombre de usuario para login
     * @param password contraseña del usuario
     */
    public Empleado(int idEmpleado, String username, String password) {
        this.idEmpleado = idEmpleado;
        this.username = username;
        this.password = password;
       
    }

    public Empleado(String username, String password) {
        this.username = username;
        this.password = password;
    
    }

 
    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idUsuario) {
        this.idEmpleado = idUsuario;
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

  
   

    @Override
    public String toString() {
        return "Usuario{idEmpleado=" + idEmpleado + ", username='" + username;
    }
}
