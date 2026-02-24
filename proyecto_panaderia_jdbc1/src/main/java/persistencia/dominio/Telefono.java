package persistencia.dominio;


/**
 * Entidad de dominio que representa un número telefónico asociado a un usuario.
 * <p>
 * Esta clase permite la multiafiliación de contactos para un mismo cliente, 
 * utilizando etiquetas descriptivas para diferenciar entre distintos tipos 
 * de líneas 
 * </p>
 * @author Jazmin
 * @author Adrian Mendoza
 */
public class Telefono {

    private int idTelefono;
    private int idUsuario;
    private String etiqueta;
    private String numero;

    /**
     * Constructor por defecto.
     * Crea una instancia vacía para ser poblada mediante métodos de acceso.
     */
    public Telefono() {
    }

    /**
     * Constructor completo para la reconstrucción de objetos desde la base de datos.
     * * @param idTelefono Identificador único del registro telefónico.
     * @param idUsuario  Identificador del usuario propietario del número.
     * @param etiqueta   Descripción del tipo de teléfono (ej. Personal, Oficina).
     * @param numero     Cadena de dígitos que compone el número telefónico.
     */
    public Telefono(int idTelefono, int idUsuario, String etiqueta, String numero) {
        this.idTelefono = idTelefono;
        this.idUsuario = idUsuario;
        this.etiqueta = etiqueta;
        this.numero = numero;
    }

    /**
     * Constructor para la creación de nuevos registros (sin ID de base de datos).
     * * @param idUsuario Identificador del usuario al que se vinculará el contacto.
     * @param etiqueta  Etiqueta descriptiva del contacto.
     * @param numero    Cadena de dígitos del teléfono.
     */
    public Telefono(int idUsuario, String etiqueta, String numero) {
        this.idUsuario = idUsuario;
        this.etiqueta = etiqueta;
        this.numero = numero;
    }

    /** @return El identificador único del registro telefónico. */
    public int getIdTelefono() { 
        return idTelefono;
    }

    /** @param idTelefono El identificador a asignar. */
    public void setIdTelefono(int idTelefono) { 
        this.idTelefono = idTelefono;
    }

    /** @return El identificador del usuario asociado. */
    public int getIdUsuario() { 
        return idUsuario; 
    }

    /** @param idUsuario El ID de usuario a asignar. */
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario; 
    }

    /** @return La etiqueta descriptiva del teléfono. */
    public String getEtiqueta() { 
        return etiqueta;
    }

    /** @param etiqueta La descripción a establecer */
    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta; 
    }

    /** @return La cadena del número telefónico. */
    public String getNumero() { 
        return numero;
    }

    /** @param numero El número telefónico a establecer. */
    public void setNumero(String numero) {
        this.numero = numero; 
    }

    /**
     * Valida que el formato del número telefónico sea correcto para el sistema.
     * <p>
     * La validación requiere que el número no sea nulo y que contenga 
     * exactamente 10 caracteres numéricos (0-9).
     * </p>
     * * @return {@code true} si el número cumple con el formato de 10 dígitos; 
     * {@code false} en caso contrario.
     */
    public boolean numeroEsValido() {
        return numero != null && numero.matches("\\d{10}");
    }

    /**
     * Devuelve una representación amigable del teléfono.
     * * @return Una cadena con la etiqueta y el número.
     */
    @Override
    public String toString() {
        return "Telefono{etiqueta='" + etiqueta + "', numero='" + numero + "'}";
    }
}