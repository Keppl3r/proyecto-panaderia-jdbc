package persistencia.dominio;

import java.sql.Date;
import java.util.List;

/**
 * Representa a un cliente registrado en el sistema de la panadería.
 * <p>
 * Esta clase extiende de {@code Usuario} para heredar las capacidades de autenticación 
 * y añade los atributos específicos de una persona física, incluyendo su información 
 * demográfica, domicilio y la colección de contactos telefónicos asociados.
 * </p>
 * * @author Jazmin
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
    /**
     * Constructor por defecto. Inicializa una instancia vacía.
     */
    public Cliente() {
        super();
    }
    /**
     * Constructor para la creación de nuevos clientes (sin ID de base de datos).
     * Automáticamente asigna el rol de "CLIENTE" a través del constructor superior.
     * * @param username Nombre de usuario para el sistema.
     * @param password Contraseña del cliente (usualmente ya encriptada).
     * @param nombres Nombre(s) del cliente.
     * @param apellidoPaterno Primer apellido.
     * @param apellidoMaterno Segundo apellido.
     * @param fechaNacimiento Fecha de nacimiento para validaciones de edad.
     * @param estado Entidad federativa de residencia.
     * @param calle Nombre de la vía pública.
     * @param numero Número exterior/interior del domicilio.
     * @param colonia Sector o barrio de la vivienda.
     */
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
    /**
     * Constructor completo para recuperar clientes existentes desde la base de datos.
     * * @param idUsuario Identificador único autogenerado en la tabla de usuarios.
     * @param username Nombre de usuario.
     * @param password Hash de la contraseña.
     * @param nombres Nombre(s) del cliente.
     * @param apellidoPaterno Primer apellido.
     * @param apellidoMaterno Segundo apellido.
     * @param fechaNacimiento Fecha de nacimiento.
     * @param estado Estado de residencia.
     * @param calle Calle del domicilio.
     * @param numero Número de casa.
     * @param colonia Colonia.
     */
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

    /** @return Los nombres del cliente. */
    public String getNombres() {
        return nombres;
    }
    /** @param nombres Los nombres a asignar. */
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    /** @return El primer apellido del cliente. */
    public String getApellidoPaterno() {
        return apellidoPaterno;
    }
    /** @param apellidoPaterno El apellido paterno a asignar. */
    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }
    /** @return El segundo apellido del cliente. */
    public String getApellidoMaterno() {
        return apellidoMaterno;
    }
    /** @param apellidoMaterno El apellido materno a asignar. */
    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }
    /** @return La fecha de nacimiento registrada. */
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }
    /** @param fechaNacimiento La fecha de nacimiento a asignar. */
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    /** @return El estado de residencia. */
    public String getEstado() {
        return estado;
    }
    /** @param estado El estado federativo a asignar. */
    public void setEstado(String estado) {
        this.estado = estado;
    }
    /** @return El nombre de la calle del domicilio. */
    public String getCalle() {
        return calle;
    }
    /** @param calle La calle a asignar. */
    public void setCalle(String calle) {
        this.calle = calle;
    }
    /** @return El número de la casa o local. */
    public String getNumero() {
        return numero;
    }
    /** @param numero El número a asignar. */
    public void setNumero(String numero) {
        this.numero = numero;
    }
    /** @return La colonia o barrio. */
    public String getColonia() {
        return colonia;
    }
    /** @param colonia La colonia a asignar. */
    public void setColonia(String colonia) {
        this.colonia = colonia;
    }
    /** @return La lista de objetos {@link Telefono} asociados. */
    public List<Telefono> getTelefonos() {
        return telefonos;
    }
    /** @param telefonos La colección de teléfonos a asociar al cliente. */
    public void setTelefonos(List<Telefono> telefonos) {
        this.telefonos = telefonos;
    }
    /**
     * Genera una cadena con el nombre, apellido paterno y apellido materno concatenados.
     * @return El nombre completo del cliente separado por espacios.
     */
    public String getNombreCompleto() {
        return nombres + " " + apellidoPaterno + " " + apellidoMaterno;
    }
    /**
     * Devuelve una representación textual simplificada del cliente para depuración.
     * @return Cadena con ID, nombres, apellido paterno y estado.
     */
    @Override
    public String toString() {
        return "Cliente{idUsuario=" + getIdUsuario() + ", nombres='" + nombres
                + "', apellidoPaterno='" + apellidoPaterno + "', estado='" + estado + "'}";
    }
}
