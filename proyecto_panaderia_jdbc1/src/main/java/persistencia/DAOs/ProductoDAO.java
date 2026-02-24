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
 * Implementación de persistencia para la gestión del catálogo de productos.
 * <p>
 * Esta clase proporciona los métodos necesarios para consultar el inventario de la panadería,
 * permitiendo filtrar por disponibilidad, recuperar el catálogo completo para administración
 * y gestionar el estado de los productos en el sistema.
 * </p>
 * @author Adrian Mendoza
 */
public class ProductoDAO implements IProductoDAO {

    private IConexionBD conexion;
    /**
     * Inicializa el DAO con una estrategia de conexión específica.
     * * @param conexion La instancia de {@code IConexionBD} para gestionar el acceso a datos.
     */
    public ProductoDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }
    /**
     * Recupera la lista de productos que están marcados actualmente como disponibles.
     * <p>
     * Este método es utilizado principalmente por la interfaz de cliente para mostrar
     * únicamente los artículos que pueden ser añadidos a un pedido.
     * </p>
     * * @return Una lista de objetos {@code Producto} con {@code DISPONIBLE = true}.
     * @throws PersistenciaException Si ocurre un error en la consulta SQL.
     */
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
                p.setPrecio(rs.getDouble("PRECIO"));
                p.setDisponible(rs.getBoolean("DISPONIBLE"));
                productos.add(p);
            }

        } catch (SQLException error) {
            throw new PersistenciaException("Error al obtener productos", error);
        }

        return productos;
    }
    /**
     * Recupera el catálogo completo de productos, independientemente de su disponibilidad.
     * <p>
     * Los resultados se ordenan alfabéticamente por nombre. Es ideal para módulos 
     * de administración o inventario.
     * </p>
     * * @return Lista de todos los productos registrados en la base de datos.
     * @throws PersistenciaException Si falla la comunicación con el servidor.
     */
    @Override
    public List<Producto> obtenerTodos() throws PersistenciaException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT ID_PRODUCTO, NOMBRE, TIPO, DESCRIPCION, PRECIO, DISPONIBLE FROM PRODUCTOS ORDER BY NOMBRE";
        try (Connection conn = conexion.crearConexion();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("ID_PRODUCTO"));
                p.setNombre(rs.getString("NOMBRE"));
                p.setTipo(rs.getString("TIPO"));
                p.setDescripcion(rs.getString("DESCRIPCION"));
                p.setPrecio(rs.getDouble("PRECIO"));
                p.setDisponible(rs.getBoolean("DISPONIBLE"));
                productos.add(p);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al obtener todos los productos", e);
        }
        return productos;
    }
    /**
     * Modifica el estado de disponibilidad de un producto en el sistema.
     * * @param idProducto El identificador único del producto a actualizar.
     * @param disponible El nuevo estado ({@code true} para activo, {@code false} para ocultar).
     * @return {@code true} si se actualizó el registro; {@code false} si el ID no existe.
     * @throws PersistenciaException Si ocurre un error durante el UPDATE.
     */
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
    /**
     * Busca y retorna la información detallada de un producto específico mediante su ID.
     * * @param idProducto El identificador del producto buscado.
     * @return El objeto {@code Producto} encontrado o {@code null} si no existe coincidencia.
     * @throws PersistenciaException Si hay un error al procesar el ResultSet o la conexión.
     */
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
                    producto.setPrecio(rs.getDouble("PRECIO"));
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
