/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.dominio;

/**
 *
 * @author Jazmin
 */
public class Usuario {
    private int idUsuario;
    private String usurname;
    private String contrasenia;
    private String rol;

    public Usuario() {
    }
    
    
    public Usuario(int idUsuario, String usurname, String contrasenia, String rol) {
        this.idUsuario = idUsuario;
        this.usurname = usurname;
        this.contrasenia = contrasenia;
        this.rol = rol;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsurname() {
        return usurname;
    }

    public void setUsurname(String usurname) {
        this.usurname = usurname;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Usuario{" + "idUsuario=" + idUsuario + ", usurname=" + usurname + ", contrasenia=" + contrasenia + ", rol=" + rol + '}';
    }
    
    
    
}
