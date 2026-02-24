package persistencia.dominio;

import java.sql.Timestamp;

/**
 * Entidad de dominio que representa un cupón de descuento en el sistema.
 * <p>
 * Esta clase gestiona la información de beneficios promocionales, controlando
 * los porcentajes de reducción de precio y las restricciones temporales
 * de uso mediante marcas de tiempo (timestamps).
 * </p>
 * @author Jazmin
 * @author Adrian Mendoza
 */
public class Cupon {

    private int idCupon;
    private Double porcentajeDescuento;
    private Timestamp fechaInicio;
    private Timestamp fechaFin;
    private boolean vigencia;
    private int numeroUsos;

    /**
     * Constructor por defecto. 
     * Crea una instancia de Cupon sin inicializar sus atributos.
     */
    public Cupon() {
    }

    /**
     * Constructor completo para la creación o recuperación de cupones.
     * @param idCupon Identificador único del cupón en la base de datos.
     * @param porcentajeDescuento Valor decimal del descuento (ej. 0.15 para 15%).
     * @param fechaInicio Marca de tiempo que indica cuándo empieza a ser válido el cupón.
     * @param fechaFin Marca de tiempo que indica el límite de validez del cupón.
     * @param vigencia Estado administrativo del cupón (habilitado/deshabilitado).
     * @param numeroUsos Contador de veces que el cupón ha sido aplicado con éxito.
     */
    public Cupon(int idCupon, Double porcentajeDescuento, Timestamp fechaInicio,
                 Timestamp fechaFin, boolean vigencia, int numeroUsos) {
        this.idCupon = idCupon;
        this.porcentajeDescuento = porcentajeDescuento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.vigencia = vigencia;
        this.numeroUsos = numeroUsos;
    }

    /** @return El identificador único del cupón. */
    public int getIdCupon() { 
        return idCupon; 
    }

    /** @param idCupon El identificador a asignar. */
    public void setIdCupon(int idCupon) { 
        this.idCupon = idCupon; 
    }

    /** @return El porcentaje de descuento asignado. */
    public Double getPorcentajeDescuento() { 
        return porcentajeDescuento; 
    }

    /** @param porcentajeDescuento El valor del descuento a establecer. */
    public void setPorcentajeDescuento(Double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    /** @return La fecha y hora de inicio de validez. */
    public Timestamp getFechaInicio() { 
        return fechaInicio; 
    }

    /** @param fechaInicio La fecha de inicio a establecer. */
    public void setFechaInicio(Timestamp fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /** @return La fecha y hora de vencimiento. */
    public Timestamp getFechaFin() { 
        return fechaFin; 
    }

    /** @param fechaFin La fecha de fin a establecer. */
    public void setFechaFin(Timestamp fechaFin) { 
        this.fechaFin = fechaFin; 
    }

    /** @return {@code true} si el cupón está marcado como activo administrativamente. */
    public boolean isVigencia() {
        return vigencia; 
    }

    /** @param vigencia Define el estado administrativo de activación del cupón. */
    public void setVigencia(boolean vigencia) { 
        this.vigencia = vigencia; 
    }

    /** @return La cantidad de veces que se ha utilizado el cupón. */
    public int getNumeroUsos() {
        return numeroUsos; 
    }

    /** @param numeroUsos El número de usos a registrar. */
    public void setNumeroUsos(int numeroUsos) {
        this.numeroUsos = numeroUsos;
    }

    /**
     * Evalúa si el cupón es apto para ser aplicado en el momento actual.
     * <p>
     * La validación contempla tres filtros:
     * 1. Que el atributo {@code vigencia} sea verdadero.
     * 2. Que la fecha actual sea igual o posterior a la {@code fechaInicio}.
     * 3. Que la fecha actual sea anterior a la {@code fechaFin}.
     * </p>
     * @return {@code true} si cumple con todas las condiciones de tiempo y estado; 
     * {@code false} en caso contrario.
     */
    public boolean estaVigente() {
        if (!vigencia) {
            return false;
        }

        Timestamp ahora = new Timestamp(System.currentTimeMillis());

        if (fechaInicio != null && ahora.before(fechaInicio)) {
            return false;
        }

        if (fechaFin != null && ahora.after(fechaFin)) {
            return false;
        }

        return true;
    }

    /**
     * Genera una representación textual del cupón.
     * @return String con ID, porcentaje y estado de vigencia.
     */
    @Override
    public String toString() {
        return "Cupon{idCupon=" + idCupon + ", porcentajeDescuento=" + (porcentajeDescuento * 100) +
                "%, vigencia=" + vigencia + "}";
    }
}