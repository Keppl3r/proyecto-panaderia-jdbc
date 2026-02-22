/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.Cliente;
import persistencia.dominio.DetallePedido;
import persistencia.dominio.PedidoExpress;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author Jazmin
 */
public class PedidoExpressDAO implements IPedidoExpressDAO{
    private IConexionBD conexion;

    public PedidoExpressDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }
    
    

    @Override
    
    public PedidoExpress crear(PedidoExpress pedido) throws PersistenciaException {
         try (Connection conn = conexion.crearConexion()) {
            conn.setAutoCommit(false);

            try {
               //insetar pedido express
                String sqlPedido = """
                         INSERT INTO PEDIDOS_EXPRESS (ID_PEDIDO, FOLIO, PIN, TIEMPO_LIMITE)
                                         VALUES (?, ?, ?, ?)
                     """;

                try (PreparedStatement ps = conn.prepareStatement(sqlPedido)) {
                    ps.setInt(1, pedido.getIdPedido());
                    ps.setString(2, pedido.getFolio());
                     ps.setString(3, pedido.getPin());
                    ps.setTimestamp(4, new Timestamp(pedido.getTiempoLimite().getTime()));
                    
                    ps.executeUpdate();
                }   
                     // Insertar detalles
                    if (pedido.getDetalles() != null && !pedido.getDetalles().isEmpty()) {
                        String sqlDetalle = """
                             INSERT INTO DETALLE_PEDIDOS (ID_PEDIDO, ID_PRODUCTO, CANTIDAD, PRECIO, SUBTOTAL, NOTAS)
                             VALUES (?, ?, ?, ?, ?, ?)
                         """;
                        
                        try (PreparedStatement ps = conn.prepareStatement(sqlDetalle)) {
                            for (DetallePedido detalle : pedido.getDetalles()) {
                                ps.setInt(1, pedido.getIdPedido());
                                ps.setInt(2, detalle.getIdProducto());
                                ps.setInt(3, detalle.getCantidad());
                                ps.setDouble(4, detalle.getPrecio());
                                ps.setDouble(5, detalle.getSubtotal());
                                ps.setString(6, detalle.getNotas());
                                ps.addBatch();
                            }
                            ps.executeBatch();
                        }
                    }
                conn.commit();
                return pedido;

            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al crear pedido Express", ex);
        }
    }
    @Override
    public int generarNumPedido() throws PersistenciaException {
        String sql = "SELECT COALESCE(MAX(ID_PEDIDO),0) FROM PEDIDOS_EXPRESS";

        try (Connection conn = conexion.crearConexion(); 
                PreparedStatement ps = conn.prepareStatement(sql); 
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int maximoActual = rs.getInt(1);
                return maximoActual + 1;
            }

            return 1;

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al generar número de pedido express", ex);
        }
    }
}
