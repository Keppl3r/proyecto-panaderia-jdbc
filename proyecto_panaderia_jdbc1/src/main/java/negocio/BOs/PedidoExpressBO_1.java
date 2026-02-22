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
public class PedidoExpressBO_1 implements IPedidoExpressBO {

    private IPedidoExpressDAO pedidoDAO;
    private IProductoDAO productoDAO;
    private static final Logger LOG = Logger.getLogger(PedidoProgramadoBO.class.getName());

    public PedidoExpressBO_1(IPedidoExpressDAO pedidoDAO, IProductoDAO productoDAO) {
        this.pedidoDAO = pedidoDAO;
        this.productoDAO = productoDAO;
    }

    @Override
    public PedidoExpress programarPedidoExpress(List<DetallePedido> detalles) throws NegocioException {

        try {
            for (DetallePedido d : detalles) {
                try {
                    Producto producto = productoDAO.obtenerPorId(d.getIdProducto());
                    if (producto == null || !producto.isDisponible()) {
                        throw new NegocioException("Producto no disponible: " + d.getIdProducto());
                    }
                    d.setPrecio(producto.getPrecio());
                    d.calcularSubtotal();
                } catch (PersistenciaException ex) {
                    Logger.getLogger(PedidoExpressBO_1.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            PedidoExpress pedidoExpress = new PedidoExpress();
            pedidoExpress.setDetalles(detalles);
            pedidoExpress.setIdPedido(pedidoDAO.generarNumPedido());
            //folio consecutivo
            String folio = String.valueOf(pedidoDAO.generarNumPedido());
            pedidoExpress.setFolio(folio);
            //pin aleatorio y de 8 digitos
            SecureRandom secureR = new SecureRandom();
            int pin = 10000000 + secureR.nextInt(90000000);

            //Convierte el PIN en un hash seguro usando SHA-256
            //Lo codifica en Base64 para poder guardarlo en la base de datos.
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(String.valueOf(pin).getBytes("UTF-8"));
            String pinSeguro = Base64.getEncoder().encodeToString(hash);
            pedidoExpress.setPin(pinSeguro);
            
            PedidoExpress pedido = pedidoDAO.crear(pedidoExpress);
            return pedido;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
           LOG.log(Level.SEVERE, "Error al generar Pin seguro", ex);
             throw new NegocioException("No se pudo generar un Pin seguro");
       
        } catch (PersistenciaException ex) {
             LOG.log(Level.SEVERE, "Error al crear pedido Express", ex);
             throw new NegocioException("No se pudo crear el pedido Express");
        }
    }

    }
