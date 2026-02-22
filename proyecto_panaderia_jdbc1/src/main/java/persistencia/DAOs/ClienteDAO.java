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
                 SELECT ID_CLIENTE, NOMBRE_COMPLETO, DOMICILIO, FECHA_NACIMIENTO, EDAD, ESTADO
                             FROM CLIENTES
                             WHERE ID_CLIENTE = ? AND ESTADO = 'ACTIVO'
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
         public boolean existeClienteActivo(int idCliente) throws PersistenciaException {
             String sql = "SELECT COUNT(*) FROM CLIENTES WHERE ID_USUARIO = ? AND ESTADO = 'ACTIVO'";

             try (Connection conn = conexion.crearConexion();
                  PreparedStatement ps = conn.prepareStatement(sql)) {

                 ps.setInt(1, idCliente);

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
             // Cliente
             cliente.setNombres(rs.getString("NOMBRES"));
             cliente.setApellidoPaterno(rs.getString("APELLIDO_PATERNO"));
             cliente.setApellidoMaterno(rs.getString("APELLIDO_MATERNO"));
             cliente.setFechaNacimiento(rs.getDate("FECHA_NACIMIENTO"));
             cliente.setCalle(rs.getString("CALLE"));
             cliente.setNumero(rs.getString("NUMERO"));
             cliente.setColonia(rs.getString("COLONIA"));

             return cliente;
         }
     }