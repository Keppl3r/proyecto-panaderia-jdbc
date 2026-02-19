package persistencia.pruebas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import persistencia.DAOs.*;
import persistencia.conexion.*;
import persistencia.dominio.*;

/**
 * @author Adrian Mendoza
 */
public class PruebaClienteDAO {

    public static void main(String[] args) {
        System.out.println("=== Probando ClienteDAO ===\n");

        try {
            IConexionBD conexion = new ConexionBD();

            // DIAGNOSTICO: verificar que hay en las tablas
            System.out.println("--- DIAGNOSTICO DE LA BASE DE DATOS ---");
            try (Connection conn = conexion.crearConexion()) {
                System.out.println("Conexion exitosa a la BD\n");

                // Ver todos los usuarios
                try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM USUARIOS");
                     ResultSet rs = ps.executeQuery()) {
                    System.out.println("TABLA USUARIOS:");
                    int count = 0;
                    while (rs.next()) {
                        count++;
                        System.out.println("  ID_USUARIO=" + rs.getInt("ID_USUARIO")
                                + ", USERNAME=" + rs.getString("USERNAME")
                                + ", ROL=" + rs.getString("ROL"));
                    }
                    if (count == 0) System.out.println("  (tabla vacia)");
                }

                System.out.println();

                // Ver todos los clientes
                try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM CLIENTES");
                     ResultSet rs = ps.executeQuery()) {
                    System.out.println("TABLA CLIENTES:");
                    int count = 0;
                    while (rs.next()) {
                        count++;
                        System.out.println("  ID_USUARIO=" + rs.getInt("ID_USUARIO")
                                + ", NOMBRES=" + rs.getString("NOMBRES")
                                + ", ESTADO='" + rs.getString("ESTADO") + "'");
                    }
                    if (count == 0) System.out.println("  (tabla vacia)");
                }

                System.out.println();

                // Ver valores distintos de ESTADO
                try (PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT ESTADO FROM CLIENTES");
                     ResultSet rs = ps.executeQuery()) {
                    System.out.println("VALORES DISTINTOS DE ESTADO EN CLIENTES:");
                    while (rs.next()) {
                        System.out.println("  '" + rs.getString("ESTADO") + "'");
                    }
                }
            }

            System.out.println("\n--- PRUEBA DEL DAO ---");
            IClienteDAO clienteDAO = new ClienteDAO(conexion);

            // Buscar cliente con ID 1
            Cliente cliente = clienteDAO.buscarPorId(1);
            if (cliente != null) {
                System.out.println("Cliente encontrado:");
                System.out.println("  ID: " + cliente.getIdUsuario());
                System.out.println("  Nombre: " + cliente.getNombres());
                System.out.println("  Apellido P: " + cliente.getApellidoPaterno());
                System.out.println("  Estado: " + cliente.getEstado());
                System.out.println("  Username: " + cliente.getUsername());
            } else {
                System.out.println("buscarPorId(1) retorno NULL");
                System.out.println("  -> Verifica que exista un CLIENTE con ID_USUARIO=1 y ESTADO='ACTIVO'");
            }

            // Verificar cliente activo
            boolean activo = clienteDAO.existeClienteActivo(1);
            System.out.println("existeClienteActivo(1) = " + activo);

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
