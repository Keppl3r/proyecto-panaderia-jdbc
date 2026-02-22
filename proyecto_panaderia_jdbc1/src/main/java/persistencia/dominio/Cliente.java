package persistencia.dominio;

import java.sql.Date;
import java.util.List;

/**
 * Entidad que representa un cliente - HEREDA DE USUARIO Solo para CU Pedido
 * Programado
 */
public class Cliente extends Usuario {

    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private Date fechaNacimiento;
    private String estado;
    private String calle;
    private String numero;
    private String colonia;
    private List<Telefono> telefonos; // Mantener por relación con cliente

    public Cliente() {
        super();
    }

    public Cliente(String username, String password, String nombres, String apellidoPaterno,
            String apellidoMaterno, Date fechaNacimiento, String estado,
            String calle, String numero, String colonia) {
        super(username, password, "CLIENTE");
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.fechaNacimiento = fechaNacimiento;
        this.estado = estado;
        this.calle = calle;
        this.numero = numero;
        this.colonia = colonia;
    }

    public Cliente(int idUsuario, String username, String password, String nombres,
            String apellidoPaterno, String apellidoMaterno, Date fechaNacimiento,
            String estado, String calle, String numero, String colonia) {
        super(idUsuario, username, password, "CLIENTE");
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.fechaNacimiento = fechaNacimiento;
        this.estado = estado;
        this.calle = calle;
        this.numero = numero;
        this.colonia = colonia;
    }

    // Getters y Setters
    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public List<Telefono> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(List<Telefono> telefonos) {
        this.telefonos = telefonos;
    }

    public String getNombreCompleto() {
        return nombres + " " + apellidoPaterno + " " + apellidoMaterno;
    }

    @Override
    public String toString() {
        return "Cliente{idUsuario=" + getIdUsuario() + ", nombres='" + nombres
                + "', apellidoPaterno='" + apellidoPaterno + "', estado='" + estado + "'}";
    }
}
