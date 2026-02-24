package persistencia.DAOs;

import java.sql.*;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.Cliente;
import persistencia.excepciones.PersistenciaException;

/**
 * Implementación de la persistencia para la entidad Cliente utilizando JDBC.
 * <p>
 * Esta clase gestiona la comunicación directa con la base de datos para realizar
 * operaciones CRUD sobre la tabla CLIENTES, manteniendo la integridad con la 
 * tabla USUARIOS mediante el uso de transacciones manuales.
 * </p>
 * @author Adrian Mendoza
 */
public class ClienteDAO implements IClienteDAO {

    private final IConexionBD conexion;

    /**
     * Inicializa el DAO con un gestor de conexiones.
     * @param conexion Objeto encargado de proveer conexiones activas a la BD.
     */
    public ClienteDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    /**
     * Recupera la información detallada de un cliente y sus credenciales mediante un INNER JOIN.
     * <p>
     * Realiza una consulta que unifica los datos de perfil (CLIENTES) con los de 
     * cuenta (USUARIOS) siempre que el registro se encuentre en estado 'ACTIVO'.
     * </p>
     * @param idUsuario Identificador único del usuario/cliente.
     * @return Objeto {@link Cliente} completamente hidratado si existe, o {@code null} si no se encuentra.
     * @throws PersistenciaException Si ocurre un error de sintaxis SQL o falla la comunicación con el servidor.
     */
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

    /**
     * Verifica de forma expedita la existencia de un cliente operativo en el sistema.
     * <p>
     * Se utiliza para validaciones previas a transacciones de negocio donde solo 
     * importa confirmar la vigencia de la cuenta.
     * </p>
     * @param idUsuario Identificador del usuario a consultar.
     * @return {@code true} si se encontró el registro con estado 'ACTIVO', {@code false} en caso contrario.
     * @throws PersistenciaException Si ocurre un error técnico en el motor de base de datos.
     */
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

    /**
     * Registra un nuevo cliente y su respectiva cuenta de usuario de forma atómica.
     * <p>
     * Implementa un bloque de transacción (Commit/Rollback). Primero inserta las credenciales 
     * en la tabla USUARIOS para obtener el ID generado, y posteriormente inserta la 
     * información personal en la tabla CLIENTES.
     * </p>
     * @param cliente Objeto con los datos del nuevo cliente (sin ID).
     * @return El objeto {@link Cliente} persistido con el ID de usuario asignado.
     * @throws PersistenciaException Si el username ya existe o si falla alguna inserción en el proceso.
     */
    @Override
    public Cliente registrar(Cliente cliente) throws PersistenciaException {
        try (Connection conn = conexion.crearConexion()) {
            conn.setAutoCommit(false);
            try {
                // 1. Inserción en tabla USUARIOS
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

                // 2. Inserción en tabla CLIENTES
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
                    ps.setDate(5, new java.sql.Date(cliente.getFechaNacimiento().getTime()));
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

    /**
     * Actualiza la información demográfica y domiciliaria de un cliente existente.
     * <p>
     * La actualización solo se permite para registros que no han sido desactivados lógicamente.
     * </p>
     * @param cliente Objeto {@link Cliente} con los datos actualizados y ID válido.
     * @return {@code true} si el registro fue localizado y actualizado, {@code false} si no hubo cambios.
     * @throws PersistenciaException Si ocurre una violación de integridad o error de conexión.
     */
    @Override
    public boolean actualizar(Cliente cliente) throws PersistenciaException {
        String sql = """
                    UPDATE CLIENTES SET NOMBRES = ?, APELLIDO_PATERNO = ?, APELLIDO_MATERNO = ?,
                    FECHA_NACIMIENTO = ?, CALLE = ?, NUMERO = ?, COLONIA = ?
                    WHERE ID_USUARIO = ? AND ESTADO = 'ACTIVO'
                """;
        try (Connection conn = conexion.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombres());
            ps.setString(2, cliente.getApellidoPaterno());
            ps.setString(3, cliente.getApellidoMaterno());
            ps.setDate(4, new java.sql.Date(cliente.getFechaNacimiento().getTime()));
            ps.setString(5, cliente.getCalle());
            ps.setString(6, cliente.getNumero());
            ps.setString(7, cliente.getColonia());
            ps.setInt(8, cliente.getIdUsuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al actualizar cliente", ex);
        }
    }

    /**
     * Realiza una baja lógica del cliente en la base de datos.
     * <p>
     * Cambia el atributo ESTADO a 'INACTIVO' para impedir futuras operaciones 
     * sin eliminar el historial de transacciones del cliente.
     * </p>
     * @param idUsuario Identificador del usuario a desactivar.
     * @return {@code true} si la desactivación fue exitosa, {@code false} si no se encontró al cliente activo.
     * @throws PersistenciaException Si ocurre un error en la capa de datos.
     */
    @Override
    public boolean desactivar(int idUsuario) throws PersistenciaException {
        String sql = "UPDATE CLIENTES SET ESTADO = 'INACTIVO' WHERE ID_USUARIO = ? AND ESTADO = 'ACTIVO'";
        try (Connection conn = conexion.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al desactivar cliente", ex);
        }
    }

    /**
     * Extrae y mapea la información del ResultSet actual a un objeto de dominio Cliente.
     * <p>
     * Este proceso de hidratación convierte los tipos de datos de SQL (como {@code DATE} o {@code VARCHAR}) 
     * a los atributos equivalentes en Java.
     * </p>
     * @param rs El {@link ResultSet} posicionado en la fila a extraer.
     * @return Un objeto {@link Cliente} poblado con los datos del cursor.
     * @throws SQLException Si ocurre un error al acceder a las columnas por nombre.
     */
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