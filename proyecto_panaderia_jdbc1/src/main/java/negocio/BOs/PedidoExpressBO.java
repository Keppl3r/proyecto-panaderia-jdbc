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
 * @author Jazmin
 * @author Adrian Mendoza
 */
public class PedidoExpressBO implements IPedidoExpressBO {

    private IPedidoExpressDAO pedidoDAO;
    private IProductoDAO productoDAO;
    private static final Logger LOG = Logger.getLogger(PedidoExpressBO.class.getName());

    public PedidoExpressBO(IPedidoExpressDAO pedidoDAO, IProductoDAO productoDAO) {
        this.pedidoDAO = pedidoDAO;
        this.productoDAO = productoDAO;
    }

    @Override
    public PedidoExpress crearPedidoExpress(PedidoExpressNuevoDTO pedidoDTO) throws NegocioException {
        try {
            if (pedidoDTO == null) {
                throw new NegocioException("El DTO del pedido no puede ser nulo");
            }
            if (pedidoDTO.getDetalles() == null || pedidoDTO.getDetalles().isEmpty()) {
                throw new NegocioException("El pedido debe tener al menos un producto");
            }

            // Validar productos y calcular precios
            for (DetallePedido d : pedidoDTO.getDetalles()) {
                Producto producto = productoDAO.obtenerPorId(d.getIdProducto());
                if (producto == null || !producto.isDisponible()) {
                    throw new NegocioException("Producto no disponible: " + d.getIdProducto());
                }
                d.setPrecio(producto.getPrecio());
                d.calcularSubtotal();
            }

            // Armar el pedido
            PedidoExpress pedido = new PedidoExpress();
            pedido.setDetalles(pedidoDTO.getDetalles());
            pedido.setNumPedido(pedidoDAO.generarNumPedido());
            pedido.calcularTotal();

            // Folio consecutivo (basado en numPedido)
            pedido.setFolio(String.valueOf(pedido.getNumPedido()));

            // Generar PIN de 8 dígitos
            String pinTextoPlano = String.valueOf(10000000 + new SecureRandom().nextInt(90000000));

            // Guardar encriptado en BD, texto plano para mostrar al usuario
            pedido.setPin(EncriptadorPIN.encriptar(pinTextoPlano));
            pedido.setPinTextoPlano(pinTextoPlano);

            // Persistir
            PedidoExpress creado = pedidoDAO.crear(pedido);
            creado.setPinTextoPlano(pinTextoPlano); // Mantener para la pantalla

            LOG.info("Pedido Express creado - Folio: " + creado.getFolio());
            return creado;

        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error al crear pedido Express", ex);
            throw new NegocioException("No se pudo crear el pedido Express", ex);
        }
    }
}
