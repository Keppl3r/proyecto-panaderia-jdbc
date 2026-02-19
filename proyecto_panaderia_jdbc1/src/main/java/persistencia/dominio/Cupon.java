 package persistencia.dominio;

     import java.sql.Timestamp;

     /**
      * Cupón de descuento - SOLO para CU Pedido Programado
      */
     public class Cupon {

         private int idCupon;
         private Double porcentajeDescuento;
         private Timestamp fechaInicio;
         private Timestamp fechaFin;
         private boolean vigencia;
         private int numeroUsos;

         public Cupon() {}

         public Cupon(int idCupon, Double porcentajeDescuento, Timestamp fechaInicio,
                     Timestamp fechaFin, boolean vigencia, int numeroUsos) {
             this.idCupon = idCupon;
             this.porcentajeDescuento = porcentajeDescuento;
             this.fechaInicio = fechaInicio;
             this.fechaFin = fechaFin;
             this.vigencia = vigencia;
             this.numeroUsos = numeroUsos;
         }

         // Getters y Setters
         public int getIdCupon() { return idCupon; }
         public void setIdCupon(int idCupon) { this.idCupon = idCupon; }

         public Double getPorcentajeDescuento() { return porcentajeDescuento; }
         public void setPorcentajeDescuento(Double porcentajeDescuento) {
             this.porcentajeDescuento = porcentajeDescuento;
         }

         public Timestamp getFechaInicio() { return fechaInicio; }
         public void setFechaInicio(Timestamp fechaInicio) { this.fechaInicio = fechaInicio; }

         public Timestamp getFechaFin() { return fechaFin; }
         public void setFechaFin(Timestamp fechaFin) { this.fechaFin = fechaFin; }

         public boolean isVigencia() { return vigencia; }
         public void setVigencia(boolean vigencia) { this.vigencia = vigencia; }

         public int getNumeroUsos() { return numeroUsos; }
         public void setNumeroUsos(int numeroUsos) { this.numeroUsos = numeroUsos; }

         // ÚNICO método de negocio necesario para el CU
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

         @Override
         public String toString() {
             return "Cupon{idCupon=" + idCupon + ", porcentajeDescuento=" + porcentajeDescuento +
                    "%, vigencia=" + vigencia + "}";
         }
     }