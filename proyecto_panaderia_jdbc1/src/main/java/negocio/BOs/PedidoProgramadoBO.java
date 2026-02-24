package negocio.BOs;

import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.DTOs.PedidoProgramadoNuevoDTO;
import negocio.excepciones.NegocioException;
import persistencia.DAOs.ICuponDAO;
import persistencia.DAOs.IPedidoDAO;
import persistencia.DAOs.IPedidoProgramadoDAO;
import persistencia.DAOs.IProductoDAO;
import persistencia.dominio.Cupon;
import persistencia.dominio.DetallePedido;
import persistencia.dominio.PedidoProgramado;
import persistencia.dominio.Producto;
import persistencia.excepciones.PersistenciaException;

/**
 * Implementación de la lógica de negocio para Pedidos Programados.
 * <p>
 * Esta clase orquestra el flujo de creación de pedidos diferidos, aplicando 
 * reglas de negocio restrictivas como el límite de pedidos por cliente, 
 * validación de anticipación de tiempo, disponibilidad de stock y 
 * aplicación de beneficios por cupones.
 * </p>
 * * @author Adrian Mendoza
 */
public class PedidoProgramadoBO implements IPedidoProgramadoBO {

    private IPedidoProgramadoDAO pedidoDAO;
    private IProductoDAO productoDAO;
    private IClienteBO clienteBO;
    private IPedidoDAO pedidoBusquedaDAO;
    private ICuponDAO cuponDAO;
    private static final Logger LOG = Logger.getLogger(PedidoProgramadoBO.class.getName());

    /**
     * Constructor con inyección de dependencias para la gestión integral de pedidos.
     * * @param pedidoDAO DAO específico para la persistencia de pedidos programados.
     * @param productoDAO DAO para consultar precios y disponibilidad real.
     * @param clienteBO BO para validar el estado del cliente.
     * @param pedidoBusquedaDAO DAO para consultar límites de pedidos activos.
     * @param cuponDAO DAO para la gestión y validación de descuentos.
     */
    public PedidoProgramadoBO(IPedidoProgramadoDAO pedidoDAO, IProductoDAO productoDAO,
            IClienteBO clienteBO, IPedidoDAO pedidoBusquedaDAO, ICuponDAO cuponDAO) {
        this.pedidoDAO = pedidoDAO;
        this.productoDAO = productoDAO;
        this.clienteBO = clienteBO;
        this.pedidoBusquedaDAO = pedidoBusquedaDAO;
        this.cuponDAO = cuponDAO;
    }
    
    /**
     * Registra un nuevo pedido programado tras validar un conjunto de reglas de negocio.
     * <p>
     * <b>Reglas aplicadas:</b>
     * <ul>
     * <li>El cliente debe existir y estar activo en el sistema.</li>
     * <li>Un cliente no puede tener más de 3 pedidos en estado "activo" simultáneamente.</li>
     * <li>La fecha de entrega debe cumplir con el tiempo mínimo de anticipación (2 horas).</li>
     * <li>Los productos deben estar marcados como disponibles en el catálogo.</li>
     * <li>Si se incluye un cupón, este debe ser válido y estar vigente.</li>
     * </ul>
     * </p>
     * * @param pedidoDTO DTO con la información de la orden y el cliente.
     * @return {@link PedidoProgramado} persistido con cálculos de total y descuentos aplicados.
     * @throws NegocioException Si se infringe alguna regla de validación o falla la persistencia.
     */
    @Override
    public PedidoProgramado programarPedido(PedidoProgramadoNuevoDTO pedidoDTO) throws NegocioException {
        try {
            if (pedidoDTO == null) {
                throw new NegocioException("El pedido no puede ser nulo");
            }

            if (!clienteBO.existeCliente(pedidoDTO.getIdCliente())) {
                throw new NegocioException("Cliente no existe o no esta activo");
            }

            // Validar maximo 3 pedidos activos
            int activos = pedidoBusquedaDAO.contarPedidosActivos(pedidoDTO.getIdCliente());
            if (activos >= 3) {
                throw new NegocioException("El cliente ya tiene 3 pedidos activos");
            }

            if (!pedidoDAO.validarFechaEntrega(pedidoDTO.getFechaEntrega())) {
                throw new NegocioException("La fecha de entrega debe ser al menos 2 horas en el futuro");
            }

            if (pedidoDTO.getDetalles() == null || pedidoDTO.getDetalles().isEmpty()) {
                throw new NegocioException("El pedido debe tener al menos un producto");
            }

            // Crear pedido
            PedidoProgramado pedido = new PedidoProgramado();
            pedido.setIdUsuario(pedidoDTO.getIdCliente());
            pedido.setNumPedido(pedidoDAO.generarNumPedido());
            pedido.setFechaEntrega(pedidoDTO.getFechaEntrega());
            pedido.setIdCupon(pedidoDTO.getIdCupon());

            // Validar productos y calcular precios
            for (DetallePedido detalle : pedidoDTO.getDetalles()) {
                if (detalle.getCantidad() <= 0) {
                    throw new NegocioException("La cantidad debe ser mayor a 0");
                }
                Producto producto = productoDAO.obtenerPorId(detalle.getIdProducto());
                if (producto == null || !producto.isDisponible()) {
                    throw new NegocioException("Producto no disponible");
                }
                detalle.setPrecio(producto.getPrecio());
                detalle.calcularSubtotal();
            }

            pedido.setDetalles(pedidoDTO.getDetalles());
            pedido.calcularTotal();

            // Validar cupon si tiene
            if (pedidoDTO.getIdCupon() != null) {
                Cupon cupon = cuponDAO.buscarPorId(pedidoDTO.getIdCupon());
                if (cupon == null) {
                    throw new NegocioException("Cupon no encontrado");
                }
                if (!cupon.estaVigente()) {
                    throw new NegocioException("El cupon no esta vigente");
                }
                pedido.setCupon(cupon);
                double totalConDescuento = pedido.calcularTotalConDescuento();
                pedido.setTotal(totalConDescuento);
                cuponDAO.incrementarUsos(pedidoDTO.getIdCupon());
            }

            PedidoProgramado creado = pedidoDAO.crear(pedido);
            LOG.info("Pedido programado creado: " + creado.toString());
            return creado;

        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error al crear pedido programado", ex);
            throw new NegocioException(ex.getMessage(), ex);
        }
    }
}
