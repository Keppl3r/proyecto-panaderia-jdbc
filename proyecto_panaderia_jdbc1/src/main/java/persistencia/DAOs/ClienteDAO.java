 package persistencia.DAOs;

     import java.sql.*;
     import persistencia.conexion.IConexionBD;
     import persistencia.dominio.Cliente;
     import persistencia.excepciones.PersistenciaException;

     /**
      * DAO para Cliente 
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
                         return extraerInformacionCliente(rs);
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
                     if (rs.next()) {
                         return rs.getInt(1) > 0;
                     }
                 }

             } catch (SQLException ex) {
                 throw new PersistenciaException("Error al verificar cliente activo", ex);
             }

             return false;
         }

         
         private Cliente extraerInformacionCliente(ResultSet rs) throws SQLException {
             Cliente cliente = new Cliente();

             // Usuario 
             cliente.setIdUsuario(rs.getInt("ID_USUARIO"));
             cliente.setUsername(rs.getString("USERNAME"));
             cliente.setPassword(rs.getString("PASSWORD"));
             cliente.setRol(rs.getString("ROL"));

             // Cliente
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