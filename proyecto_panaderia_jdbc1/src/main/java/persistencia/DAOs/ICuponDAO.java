package persistencia.DAOs;

import persistencia.dominio.Cupon;
import persistencia.excepciones.PersistenciaException;

/**
 * Interfaz que define las operaciones de persistencia para la gestión de Cupones.
 * <p>
 * Provee los métodos necesarios para validar la existencia de promociones y 
 * gestionar el ciclo de vida de los cupones mediante el rastreo de su uso 
 * en pedidos programados.
 * </p>
 * @author Adrian Mendoza
 */
public interface ICuponDAO {
    /**
     * Busca y recupera la información completa de un cupón específico.
     * <p>
     * Este método es fundamental para la capa de negocio, ya que permite obtener
     * el porcentaje de descuento y las fechas de vigencia antes de aplicar 
     * una promoción a un pedido.
     * </p>
     * * @param idCupon Identificador único del cupón en la base de datos.
     * @return Objeto {@link Cupon} si se encuentra la coincidencia, 
     * o {@code null} en caso contrario.
     * @throws PersistenciaException Si ocurre un error técnico en la infraestructura de datos.
     */
    Cupon buscarPorId(int idCupon) throws PersistenciaException;
    /**
     * Registra el uso de un cupón incrementando su contador interno en una unidad.
     * <p>
     * Se debe invocar una vez que el pedido ha sido confirmado satisfactoriamente, 
     * permitiendo llevar un control estadístico y de límites de uso.
     * </p>
     * * @param idCupon Identificador del cupón a actualizar.
     * @return {@code true} si la actualización fue exitosa; 
     * {@code false} si el cupón no existe.
     * @throws PersistenciaException Si falla la comunicación con el servidor de persistencia.
     */
    boolean incrementarUsos(int idCupon) throws PersistenciaException;
}
