package persistencia.pruebas;

import persistencia.DAOs.CuponDAO;
import persistencia.DAOs.ICuponDAO;
import persistencia.conexion.ConexionBD;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.Cupon;
/**
* Clase de prueba unitaria para validar las operaciones de persistencia de la entidad Cupon.
 * <p>
 * Esta clase se encarga de verificar que el {@link ICuponDAO} recupere correctamente 
 * la información de los beneficios desde la base de datos, asegurando que el mapeo 
 * de los campos de fecha (timestamps) y estados booleanos sea preciso para el 
 * correcto cálculo de descuentos.
 * </p>
*/
public class PruebaCuponDAO {

    public static void main(String[] args) {
        System.out.println("--- Prueba CuponDAO ---");

        try {
            IConexionBD conexion = new ConexionBD();
            ICuponDAO cuponDAO = new CuponDAO(conexion);

            Cupon cupon = cuponDAO.buscarPorId(1);
            if (cupon != null) {
                System.out.println("Cupon encontrado: " + cupon.getPorcentajeDescuento() + "%");
                System.out.println("Vigente: " + cupon.estaVigente());
                System.out.println("Usos: " + cupon.getNumeroUsos());
            } else {
                System.out.println("No se encontro cupon con ID 1");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
