package persistencia.DAOs;

import java.sql.*;
import negocio.excepciones.NegocioException;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.Cliente;
import persistencia.excepciones.PersistenciaException;

/**
 * * Implementación de la persistencia para la entidad Cliente utilizando JDBC.
 * <p>
 * Esta clase gestiona la comunicación directa con la base de datos para realizar
 * operaciones CRUD sobre la tabla CLIENTES, manteniendo la integridad con la 
 * tabla USUARIOS mediante el uso de transacciones.
 * </p>
 * @author Adrian Mendoza
 */
public class ClienteDAO implements IClienteDAO {

    private IConexionBD conexion;
    /**
     * Inicializa el DAO con un gestor de conexiones.
     * @param conexion Objeto encargado de proveer conexiones activas a la BD.
     */
    public ClienteDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }
    /**
     * Recupera un cliente y sus credenciales de usuario mediante un INNER JOIN.
     * @param idUsuario ID único del usuario/cliente.
     * @return Objeto {@link Cliente} completo o {@code null} si no se encuentra.
     * @throws PersistenciaException Si ocurre un error de comunicación con el servidor.
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
     * Verifica si existe un registro de cliente asociado a un ID de usuario 
     * que se encuentre en estado 'ACTIVO'.
     * <p>
     * Este método es utilizado principalmente por la capa de negocio (BO) antes 
     * de procesar pedidos programados o transacciones financieras, asegurando 
     * que no se preste servicio a cuentas suspendidas o dadas de baja.
     * </p>
     * @param idUsuario Identificador único del usuario a consultar.
     * @return {@code true} si el cliente existe y está activo; {@code false} en caso contrario.
     * @throws PersistenciaException Si ocurre un error técnico al ejecutar la consulta SQL.
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
     * Registra un nuevo cliente y su cuenta de usuario de forma atómica.
     * <p>
     * Utiliza el manejo de transacciones (Commit/Rollback) para asegurar que 
     * ambas inserciones ocurran exitosamente. Si falla la inserción del cliente,
     * se deshace la creación del usuario para evitar datos huérfanos.
     * </p>
     * @param cliente Objeto con la información personal y de cuenta.
     * @return El objeto {@link Cliente} con su ID generado por la BD.
     * @throws PersistenciaException Si hay un error SQL o violación de restricciones.
     */
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
    /**
     * Extrae la información del registro actual del ResultSet y la mapea a un objeto Cliente.
     * <p>
     * Este proceso, conocido como "hidratación", convierte los datos relacionales de las tablas 
     * USUARIOS y CLIENTES en una instancia de objeto utilizable por la capa de negocio.
     * </p>
     * @param rs El {@link ResultSet} posicionado en la fila que se desea extraer.
     * @return Un objeto {@link Cliente} poblado con los datos de la fila actual.
     * @throws SQLException Si alguna de las columnas no existe en el ResultSet o hay un fallo de acceso.
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
   /**
     * Actualiza la información personal y de acceso de un cliente en la base de datos.
     * * Este método realiza un UPDATE en la tabla USUARIOS utilizando el ID_USUARIO 
     * como identificador. Se actualizan campos de autenticación (username, password),
     * datos personales (nombres, apellidos, fecha de nacimiento) y datos de 
     * contacto/dirección (calle, número, colonia).
     *
     * @param cliente El objeto {@code Cliente} que contiene los datos actualizados.
     * Debe incluir un ID_USUARIO válido que exista en la base de datos.
     * @return {@code true} si la actualización fue exitosa y se modificó al menos una fila; 
     * {@code false} en caso contrario.
     * @throws PersistenciaException Si ocurre un error de sintaxis SQL, falla la conexión 
     * con el servidor de base de datos o hay una violación 
     * de restricciones (como un username duplicado).
     */
   public boolean actualizar(Cliente cliente) throws PersistenciaException {
    String sql = "UPDATE USUARIOS SET USERNAME = ?, PASSWORD = ?, NOMBRES = ?, APELLIDO_PATERNO = ?, " +
                 "APELLIDO_MATERNO = ?, FECHA_NACIMIENTO = ?, CALLE = ?, NUMERO = ?, COLONIA = ? " +
                 "WHERE ID_USUARIO = ?";
    
    try (Connection conn = conexion.crearConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setString(1, cliente.getUsername());
        ps.setString(2, cliente.getPassword()); 
        ps.setString(3, cliente.getNombres());
        ps.setString(4, cliente.getApellidoPaterno());
        ps.setString(5, cliente.getApellidoMaterno());
        
       
        if (cliente.getFechaNacimiento() != null) {
            ps.setDate(6, new java.sql.Date(cliente.getFechaNacimiento().getTime()));
        } else {
            ps.setNull(6, java.sql.Types.DATE);
        }
        
        ps.setString(7, cliente.getCalle());
        ps.setString(8, cliente.getNumero());
        ps.setString(9, cliente.getColonia());
        ps.setInt(10, cliente.getIdUsuario()); 
        
        int filasAfectadas = ps.executeUpdate();
        return filasAfectadas > 0;
        
    } catch (SQLException ex) {
       
        throw new PersistenciaException("Error SQL al actualizar: " + ex.getMessage(), ex);
    }
}

}