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

    @Override
    public List<Producto> obtenerProductosDisponibles() throws PersistenciaException {
        List<Producto> productos = new ArrayList<>();
        String sql = """
                          SELECT ID_PRODUCTO, NOMBRE, TIPO, DESCRIPCION, PRECIO, DISPONIBLE 
                          FROM PRODUCTOS 
                          WHERE DISPONIBLE = TRUE
                     """;
        try (Connection conn = conexion.crearConexion(); 
                PreparedStatement ps = conn.prepareStatement(sql); 
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("ID_PRODUCTO"));
                p.setNombre(rs.getString("NOMBRE"));
                p.setTipo(rs.getString("TIPO"));
                p.setDescripcion(rs.getString("DESCRIPCION"));
                p.setPrecio(rs.getFloat("PRECIO"));
                p.setDisponible(rs.getBoolean("DISPONIBLE"));
                productos.add(p);
            }

        } catch (SQLException error) {
            throw new PersistenciaException("Error al obtener productos", error);
        }

        return productos;
    }

    @Override
    public Producto obtenerPorId(int idProducto) throws PersistenciaException {
        String sql = """
                     SELECT ID_PRODUCTO, NOMBRE, TIPO, DESCRIPCION, PRECIO, DISPONIBLE FROM PRODUCTOS WHERE ID_PRODUCTO = ?
                     """;

        try (Connection conn = conexion.crearConexion(); 
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Producto producto = new Producto();
                    producto.setIdProducto(rs.getInt("ID_PRODUCTO"));
                    producto.setNombre(rs.getString("NOMBRE"));
                    producto.setTipo(rs.getString("TIPO"));
                    producto.setDescripcion(rs.getString("DESCRIPCION"));
                    producto.setPrecio(rs.getFloat("PRECIO"));
                    producto.setDisponible(rs.getBoolean("DISPONIBLE"));
                    return producto;
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Error al obtener producto por ID", e);
        }

        return null;
    }
}
