package persistencia.DAOs;

import java.sql.*;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.Cliente;
import persistencia.excepciones.PersistenciaException;

/**
 * @author Adrian Mendoza
 */
public class ClienteDAO implements IClienteDAO {

    private IConexionBD conexion;

    public ClienteDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public Cliente buscarPorId(int idUsuario) throws PersistenciaException {
        String sql = """
                    SELECT c.NOMBRES, c.APELLIDO_PATERNO, c.APELLIDO_MATERNO,
                           c.FECHA_NACIMIENTO, c.ESTADO, c.CALLE, c.NUMERO, c.COLONIA,
                           u.ID_USUARIO, u.USERNAME, u.PASSWORD, u.ROL
                    FROM CLIENTES c
                    INNER JOIN USUARIOS u ON c.ID_USUARIO = u.ID_USUARIO
                    WHERE c.ID_USUARIO = ? AND c.ESTADO = 'ACTIVO'
                """;
        try (Connection conn = conexion.crearConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extraerCliente(rs);
                }
            }
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al buscar cliente por ID", ex);
        }
        return null;
    }

    @Override
    public boolean existeClienteActivo(int idUsuario) throws PersistenciaException {
        String sql = "SELECT COUNT(*) FROM CLIENTES WHERE ID_USUARIO = ? AND ESTADO = 'ACTIVO'";
        try (Connection conn = conexion.crearConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al verificar cliente activo", ex);
        }
        return false;
    }

    @Override
    public Cliente registrar(Cliente cliente) throws PersistenciaException {
        try (Connection conn = conexion.crearConexion()) {
            conn.setAutoCommit(false);
            try {
                // Insertar usuario
                String sqlUsuario = "INSERT INTO USUARIOS (USERNAME, PASSWORD, ROL) VALUES (?, ?, 'CLIENTE')";
                try (PreparedStatement ps = conn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, cliente.getUsername());
                    ps.setString(2, cliente.getPassword());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            cliente.setIdUsuario(rs.getInt(1));
                        }
                    }
                }

                // Insertar cliente
                String sqlCliente = """
                            INSERT INTO CLIENTES (ID_USUARIO, NOMBRES, APELLIDO_PATERNO, APELLIDO_MATERNO,
                            FECHA_NACIMIENTO, ESTADO, CALLE, NUMERO, COLONIA)
                            VALUES (?, ?, ?, ?, ?, 'ACTIVO', ?, ?, ?)
                        """;
                try (PreparedStatement ps = conn.prepareStatement(sqlCliente)) {
                    ps.setInt(1, cliente.getIdUsuario());
                    ps.setString(2, cliente.getNombres());
                    ps.setString(3, cliente.getApellidoPaterno());
                    ps.setString(4, cliente.getApellidoMaterno());
                    ps.setDate(5, cliente.getFechaNacimiento());
                    ps.setString(6, cliente.getCalle());
                    ps.setString(7, cliente.getNumero());
                    ps.setString(8, cliente.getColonia());
                    ps.executeUpdate();
                }

                conn.commit();
                return cliente;
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al registrar cliente", ex);
        }
    }

    private Cliente extraerCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setIdUsuario(rs.getInt("ID_USUARIO"));
        cliente.setUsername(rs.getString("USERNAME"));
        cliente.setPassword(rs.getString("PASSWORD"));
        cliente.setRol(rs.getString("ROL"));
        cliente.setNombres(rs.getString("NOMBRES"));
        cliente.setApellidoPaterno(rs.getString("APELLIDO_PATERNO"));
        cliente.setApellidoMaterno(rs.getString("APELLIDO_MATERNO"));
        cliente.setFechaNacimiento(rs.getDate("FECHA_NACIMIENTO"));
        cliente.setEstado(rs.getString("ESTADO"));
        cliente.setCalle(rs.getString("CALLE"));
        cliente.setNumero(rs.getString("NUMERO"));
        cliente.setColonia(rs.getString("COLONIA"));
        return cliente;
    }
}