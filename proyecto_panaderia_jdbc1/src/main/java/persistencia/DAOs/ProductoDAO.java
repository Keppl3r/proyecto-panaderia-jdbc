package persistencia.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.Producto;
import persistencia.excepciones.PersistenciaException;

/**
 * Implementación de IProductoDAO.
 *
 * @author Adrian Mendoza
 */
public class ProductoDAO implements IProductoDAO {

    private IConexionBD conexion;

    public ProductoDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setIdProducto(rs.getInt("ID_PRODUCTO"));
        p.setNombre(rs.getString("NOMBRE"));
        p.setTipo(rs.getString("TIPO"));
        p.setDescripcion(rs.getString("DESCRIPCION"));
        p.setPrecio(rs.getDouble("PRECIO"));
        p.setDisponible(rs.getBoolean("DISPONIBLE"));
        p.setImagen(rs.getString("IMAGEN"));
        return p;
    }

    @Override
    public List<Producto> obtenerProductosDisponibles() throws PersistenciaException {
        List<Producto> productos = new ArrayList<>();
        String sql = """
                          SELECT ID_PRODUCTO, NOMBRE, TIPO, DESCRIPCION, PRECIO, DISPONIBLE, IMAGEN
                          FROM PRODUCTOS 
                          WHERE DISPONIBLE = TRUE
                     """;
        try (Connection conn = conexion.crearConexion(); 
                PreparedStatement ps = conn.prepareStatement(sql); 
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }
        } catch (SQLException error) {
            throw new PersistenciaException("Error al obtener productos", error);
        }
        return productos;
    }

    @Override
    public List<Producto> obtenerTodos() throws PersistenciaException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT ID_PRODUCTO, NOMBRE, TIPO, DESCRIPCION, PRECIO, DISPONIBLE, IMAGEN FROM PRODUCTOS ORDER BY NOMBRE";
        try (Connection conn = conexion.crearConexion();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al obtener todos los productos", e);
        }
        return productos;
    }

    @Override
    public boolean actualizarDisponibilidad(int idProducto, boolean disponible) throws PersistenciaException {
        String sql = "UPDATE PRODUCTOS SET DISPONIBLE = ? WHERE ID_PRODUCTO = ?";
        try (Connection conn = conexion.crearConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, disponible);
            ps.setInt(2, idProducto);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new PersistenciaException("Error al actualizar disponibilidad", e);
        }
    }

    @Override
    public Producto obtenerPorId(int idProducto) throws PersistenciaException {
        String sql = """
                     SELECT ID_PRODUCTO, NOMBRE, TIPO, DESCRIPCION, PRECIO, DISPONIBLE, IMAGEN
                     FROM PRODUCTOS WHERE ID_PRODUCTO = ?
                     """;
        try (Connection conn = conexion.crearConexion(); 
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearProducto(rs);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al obtener producto por ID", e);
        }
        return null;
    }
}
