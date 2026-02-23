/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.excepciones.NegocioException;
import persistencia.DAOs.IPedidoExpressDAO;
import persistencia.DAOs.IProductoDAO;
import persistencia.dominio.DetallePedido;
import persistencia.dominio.PedidoExpress;
import persistencia.dominio.Producto;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author Jazmin
 */
public class PedidoExpressBO implements IPedidoExpressBO {

   private IPedidoExpressDAO pedidoDAO;
    private IProductoDAO productoDAO;
    private static final Logger LOG = Logger.getLogger(PedidoProgramadoBO.class.getName());

    public PedidoExpressBO(IPedidoExpressDAO pedidoDAO, IProductoDAO productoDAO) {
        this.pedidoDAO = pedidoDAO;
        this.productoDAO = productoDAO;
    }

    @Override
    public PedidoExpress crearPedidoExpress(negocio.DTOs.PedidoExpressNuevoDTO pedidoDTO) throws NegocioException {
        try {

            if (pedidoDTO == null) {
                throw new NegocioException("El DTO del pedido no puede ser nulo");
            }

            if (pedidoDTO.getDetalles() == null || pedidoDTO.getDetalles().isEmpty()) {
                throw new NegocioException("El pedido debe tener al menos un producto");
            }

            List<DetallePedido> detalles = pedidoDTO.getDetalles();

            for (DetallePedido d : detalles) {
                try {
                    Producto producto = productoDAO.obtenerPorId(d.getIdProducto());
                    if (producto == null || !producto.isDisponible()) {
                        throw new NegocioException("Producto no disponible: " + d.getIdProducto());
                    }
                    d.setPrecio(producto.getPrecio());
                    d.calcularSubtotal();
                } catch (PersistenciaException ex) {
                    LOG.log(Level.SEVERE, "Error al obtener producto", ex);
                    throw new NegocioException("Producto no encontrado", ex);
                }
            }

            PedidoExpress pedidoExpress = new PedidoExpress();
            pedidoExpress.setDetalles(detalles);
            pedidoExpress.setIdPedido(pedidoDAO.generarNumPedido());

            String folio = String.valueOf(pedidoDAO.generarNumPedido());
            pedidoExpress.setFolio(folio);

            SecureRandom secureR = new SecureRandom();
            int pin = 10000000 + secureR.nextInt(90000000);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(String.valueOf(pin).getBytes("UTF-8"));
            String pinSeguro = Base64.getEncoder().encodeToString(hash);
            pedidoExpress.setPin(pinSeguro);

            PedidoExpress pedido = pedidoDAO.crear(pedidoExpress);
            LOG.info("Pedido Express creado: " + pedido.toString());
            return pedido;

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            LOG.log(Level.SEVERE, "Error al generar PIN seguro", ex);
            throw new NegocioException("No se pudo generar un PIN seguro", ex);
        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error al crear pedido Express", ex);
            throw new NegocioException("No se pudo crear el pedido Express", ex);
        }
    }

}
