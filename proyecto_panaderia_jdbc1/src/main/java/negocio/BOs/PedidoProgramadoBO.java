package negocio.BOs;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.excepciones.NegocioException;
import persistencia.DAOs.IPedidoProgramadoDAO;
import persistencia.DAOs.IProductoDAO;
import persistencia.dominio.PedidoProgramado;
import persistencia.dominio.DetallePedido;
import persistencia.dominio.Producto;
import persistencia.excepciones.PersistenciaException;

public class PedidoProgramadoBO implements IPedidoProgramadoBO {

    private IPedidoProgramadoDAO pedidoDAO;
    private IProductoDAO productoDAO;
    private IClienteBO clienteBO;
    private static final Logger LOG = Logger.getLogger(PedidoProgramadoBO.class.getName());

    // CORREGIR CONSTRUCTOR:
    public PedidoProgramadoBO(IPedidoProgramadoDAO pedidoDAO, IProductoDAO productoDAO, IClienteBO clienteBO) {
        this.pedidoDAO = pedidoDAO;
        this.productoDAO = productoDAO;
        this.clienteBO = clienteBO;
    }

    @Override
    public PedidoProgramado programarPedido(negocio.DTOs.PedidoProgramadoNuevoDTO pedidoDTO) throws NegocioException {
        try {

            if (pedidoDTO == null) {
                throw new NegocioException("El DTO del pedido no puede ser nulo");
            }

            if (!clienteBO.existeCliente(pedidoDTO.getIdCliente())) {
                throw new NegocioException("Cliente no existe o no está activo");
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

            // Validar y procesar detalles
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

            // Crear en BD
            PedidoProgramado pedidoCreado = pedidoDAO.crear(pedido);
            LOG.info("Pedido programado creado: " + pedidoCreado.toString());
            return pedidoCreado;

        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error al crear pedido programado", ex);
            throw new NegocioException(ex.getMessage(), ex);
        }
    }
}
