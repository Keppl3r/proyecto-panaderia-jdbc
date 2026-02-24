package negocio.BOs;

import java.security.SecureRandom;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.DTOs.PedidoExpressNuevoDTO;
import negocio.encriptacion.EncriptadorPIN;
import negocio.excepciones.NegocioException;
import persistencia.DAOs.IPedidoExpressDAO;
import persistencia.DAOs.IProductoDAO;
import persistencia.dominio.DetallePedido;
import persistencia.dominio.PedidoExpress;
import persistencia.dominio.Producto;
import persistencia.excepciones.PersistenciaException;

/**
 * Implementación de la lógica de negocio para la creación de pedidos en modalidad Express.
 * <p>
 * Esta clase coordina la validación de disponibilidad de productos, el cálculo de costos,
 * la generación segura de PINs y la persistencia de órdenes para clientes no registrados.
 * </p>
 * * @author Jazmin
 * @author Adrian Mendoza
 */
public class PedidoExpressBO implements IPedidoExpressBO {

    private IPedidoExpressDAO pedidoDAO;
    private IProductoDAO productoDAO;
    private static final Logger LOG = Logger.getLogger(PedidoExpressBO.class.getName());
    /**
     * Constructor que inyecta los DAOs necesarios para el procesamiento de pedidos.
     * @param pedidoDAO Componente de persistencia para órdenes express.
     * @param productoDAO Componente de acceso a datos para validación de catálogo.
     */
    public PedidoExpressBO(IPedidoExpressDAO pedidoDAO, IProductoDAO productoDAO) {
        this.pedidoDAO = pedidoDAO;
        this.productoDAO = productoDAO;
    }
    /**
     * Procesa la creación de un nuevo pedido Express bajo reglas estrictas de negocio.
     * <p>
     * El flujo de trabajo incluye:
     * <ol>
     * <li>Validación de integridad del DTO.</li>
     * <li>Verificación de disponibilidad de cada producto en tiempo real.</li>
     * <li>Cálculo de subtotales y totales basado en el precio vigente en DB.</li>
     * <li>Generación de un PIN de 8 dígitos mediante {@link SecureRandom}.</li>
     * <li>Encriptación del PIN antes de la persistencia para protección de datos.</li>
     * </ol>
     * </p>
     * * @param pedidoDTO Contenedor de datos con los productos solicitados.
     * @return {@link PedidoExpress} con folio, PIN y totales generados.
     * @throws NegocioException Si algún producto no existe, no está disponible, 
     * o si ocurre un fallo en la persistencia.
     */
    @Override
    public PedidoExpress crearPedidoExpress(PedidoExpressNuevoDTO pedidoDTO) throws NegocioException {
        try {
            if (pedidoDTO == null) {
                throw new NegocioException("El DTO del pedido no puede ser nulo");
            }
            if (pedidoDTO.getDetalles() == null || pedidoDTO.getDetalles().isEmpty()) {
                throw new NegocioException("El pedido debe tener al menos un producto");
            }

            for (DetallePedido d : pedidoDTO.getDetalles()) {
                Producto producto = productoDAO.obtenerPorId(d.getIdProducto());
                if (producto == null || !producto.isDisponible()) {
                    throw new NegocioException("Producto no disponible: " + d.getIdProducto());
                }
                d.setPrecio(producto.getPrecio());
                d.calcularSubtotal();
            }

            PedidoExpress pedido = new PedidoExpress();
            pedido.setDetalles(pedidoDTO.getDetalles());
            pedido.setNumPedido(pedidoDAO.generarNumPedido());
            pedido.calcularTotal();

            pedido.setFolio(String.valueOf(pedido.getNumPedido()));

            String pinTextoPlano = String.valueOf(10000000 + new SecureRandom().nextInt(90000000));

            pedido.setPin(EncriptadorPIN.encriptar(pinTextoPlano));
            pedido.setPinTextoPlano(pinTextoPlano);

            PedidoExpress creado = pedidoDAO.crear(pedido);
            creado.setPinTextoPlano(pinTextoPlano);

            LOG.info("Pedido Express creado - Folio: " + creado.getFolio());
            return creado;

        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error al crear pedido Express", ex);
            throw new NegocioException("No se pudo crear el pedido Express", ex);
        }
    }
}
