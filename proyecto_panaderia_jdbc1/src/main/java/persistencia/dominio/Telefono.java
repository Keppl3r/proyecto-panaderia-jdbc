package persistencia.dominio;

/**
 * Entidad que representa un teléfono de cliente
 *
 */
public class Telefono {

    private int idTelefono;
    private int idUsuario;
    private String etiqueta;
    private String numero;

    
    public Telefono() {
    }

    public Telefono(int idTelefono, int idUsuario, String etiqueta, String numero) {
        this.idTelefono = idTelefono;
        this.idUsuario = idUsuario;
        this.etiqueta = etiqueta;
        this.numero = numero;
    }

    public Telefono(int idUsuario, String etiqueta, String numero) {
        this.idUsuario = idUsuario;
        this.etiqueta = etiqueta;
        this.numero = numero;
    }

    // Getters y Setters
    public int getIdTelefono() {
        return idTelefono;
    }

    public void setIdTelefono(int idTelefono) {
        this.idTelefono = idTelefono;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
 public boolean numeroEsValido() {
             return numero != null && numero.matches("\\d{10}");
         }
    @Override
    public String toString() {
        return "Telefono{etiqueta='" + etiqueta + "', numero='" + numero + "'}";
    }
}
