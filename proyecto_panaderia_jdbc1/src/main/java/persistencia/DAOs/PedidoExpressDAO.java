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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
public class PedidoExpressDAO implements IPedidoExpressDAO {

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

        try (Connection conn = conexion.crearConexion(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int maximoActual = rs.getInt(1);
                return maximoActual + 1;
            }

            return 1;

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al generar número de pedido express", ex);
        }
    }

    @Override
    public PedidoExpress obtenerPorFolio(String folio) throws PersistenciaException {
        String sql = """
        SELECT p.ID_PEDIDO, p.FOLIO, p.PIN, p.TIEMPO_LIMITE, pd.ID_PRODUCTO, pd.CANTIDAD, pd.PRECIO, pd.SUBTOTAL, pd.NOTAS
        FROM PEDIDOS_EXPRESS p
        LEFT JOIN DETALLE_PEDIDOS pd ON p.ID_PEDIDO = pd.ID_PEDIDO
        WHERE p.FOLIO = ?
    """;

        try (Connection conn = conexion.crearConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, folio);

            try (ResultSet rs = ps.executeQuery()) {
                PedidoExpress pedido = null;
                List<DetallePedido> detalles = new ArrayList<>();

                while (rs.next()) {
                    if (pedido == null) {
                        pedido = new PedidoExpress();
                        pedido.setIdPedido(rs.getInt("ID_PEDIDO"));
                        pedido.setFolio(rs.getString("FOLIO"));
                        pedido.setPin(rs.getString("PIN"));
                        pedido.setTiempoLimite(rs.getTimestamp("TIEMPO_LIMITE"));
                    }

                    int idProducto = rs.getInt("ID_PRODUCTO");
                    if (idProducto > 0) { // puede ser null si no tiene detalles
                        DetallePedido detalle = new DetallePedido();
                        detalle.setIdProducto(idProducto);
                        detalle.setCantidad(rs.getInt("CANTIDAD"));
                        detalle.setPrecio(rs.getDouble("PRECIO"));
                        detalle.setSubtotal(rs.getDouble("SUBTOTAL"));
                        detalle.setNotas(rs.getString("NOTAS"));
                        detalles.add(detalle);
                    }
                }

                if (pedido != null) {
                    pedido.setDetalles(detalles);
                }

                return pedido;
            }

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al obtener pedido Express por folio", ex);
        }
    }

    @Override
    public List<PedidoExpress> obtenerPorTelefono(String telefono) throws PersistenciaException {
        String sql = """
        SELECT pe.ID_PEDIDO, pe.FOLIO, pe.PIN, pe.TIEMPO_LIMITE, dp.ID_PRODUCTO, dp.CANTIDAD, dp.PRECIO, dp.SUBTOTAL, dp.NOTAS
        FROM PEDIDOS_EXPRESS pe
        JOIN PEDIDOS p ON pe.ID_PEDIDO = p.ID_PEDIDO
        JOIN TELEFONOS t ON p.ID_USUARIO = t.ID_USUARIO
        LEFT JOIN DETALLE_PEDIDOS dp ON pe.ID_PEDIDO = dp.ID_PEDIDO
        WHERE t.NUMERO = ?
    """;

        try (Connection conn = conexion.crearConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, telefono);
            try (ResultSet rs = ps.executeQuery()) {
                Map<Integer, PedidoExpress> mapaPedidos = new LinkedHashMap<>();

                while (rs.next()) {
                    int idPedido = rs.getInt("ID_PEDIDO");
                    PedidoExpress pedido = mapaPedidos.get(idPedido);

                    if (pedido == null) {
                        pedido = new PedidoExpress();
                        pedido.setIdPedido(idPedido);
                        pedido.setFolio(rs.getString("FOLIO"));
                        pedido.setPin(rs.getString("PIN"));
                        pedido.setTiempoLimite(rs.getTimestamp("TIEMPO_LIMITE"));
                        pedido.setDetalles(new ArrayList<>());
                        mapaPedidos.put(idPedido, pedido);
                    }

                    int idProducto = rs.getInt("ID_PRODUCTO");
                    if (idProducto > 0) {
                        DetallePedido detalle = new DetallePedido();
                        detalle.setIdProducto(idProducto);
                        detalle.setCantidad(rs.getInt("CANTIDAD"));
                        detalle.setPrecio(rs.getDouble("PRECIO"));
                        detalle.setSubtotal(rs.getDouble("SUBTOTAL"));
                        detalle.setNotas(rs.getString("NOTAS"));
                        pedido.getDetalles().add(detalle);
                    }
                }

                return new ArrayList<>(mapaPedidos.values());
            }

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al obtener pedidos Express por teléfono", ex);
        }
    }

    @Override
    public List<PedidoExpress> obtenerPorRangoFechas(Timestamp inicio, Timestamp fin) throws PersistenciaException {
        String sql = """
        SELECT pe.ID_PEDIDO, pe.FOLIO, pe.PIN, pe.TIEMPO_LIMITE, dp.ID_PRODUCTO, dp.CANTIDAD, dp.PRECIO, dp.SUBTOTAL, dp.NOTAS
        FROM PEDIDOS_EXPRESS pe
        JOIN PEDIDOS p ON pe.ID_PEDIDO = p.ID_PEDIDO
        LEFT JOIN DETALLE_PEDIDOS dp ON pe.ID_PEDIDO = dp.ID_PEDIDO
        WHERE p.FECHA_REGISTRO BETWEEN ? AND ?
        ORDER BY p.FECHA_REGISTRO
    """;

        try (Connection conn = conexion.crearConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, inicio);
            ps.setTimestamp(2, fin);

            try (ResultSet rs = ps.executeQuery()) {
                Map<Integer, PedidoExpress> mapaPedidos = new LinkedHashMap<>();

                while (rs.next()) {
                    int idPedido = rs.getInt("ID_PEDIDO");
                    PedidoExpress pedido = mapaPedidos.get(idPedido);

                    if (pedido == null) {
                        pedido = new PedidoExpress();
                        pedido.setIdPedido(idPedido);
                        pedido.setFolio(rs.getString("FOLIO"));
                        pedido.setPin(rs.getString("PIN"));
                        pedido.setTiempoLimite(rs.getTimestamp("TIEMPO_LIMITE"));
                        pedido.setDetalles(new ArrayList<>());
                        mapaPedidos.put(idPedido, pedido);
                    }

                    int idProducto = rs.getInt("ID_PRODUCTO");
                    if (idProducto > 0) {
                        DetallePedido detalle = new DetallePedido();
                        detalle.setIdProducto(idProducto);
                        detalle.setCantidad(rs.getInt("CANTIDAD"));
                        detalle.setPrecio(rs.getDouble("PRECIO"));
                        detalle.setSubtotal(rs.getDouble("SUBTOTAL"));
                        detalle.setNotas(rs.getString("NOTAS"));
                        pedido.getDetalles().add(detalle);
                    }
                }

                return new ArrayList<>(mapaPedidos.values());
            }

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al obtener pedidos Express por rango de fechas", ex);
        }
    }
}
