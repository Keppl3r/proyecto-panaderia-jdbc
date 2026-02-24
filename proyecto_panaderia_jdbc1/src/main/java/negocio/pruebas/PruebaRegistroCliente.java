package negocio.pruebas;

import negocio.BOs.IClienteBO;
import negocio.fabrica.FabricaBOs;
import persistencia.dominio.Cliente;
import persistencia.dominio.Telefono;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de prueba para validar el flujo completo de registro de nuevos clientes.
 * <p>
 * Esta prueba de integración verifica la capacidad del sistema para persistir un 
 * perfil de cliente completo, evaluando:
 * <ul>
 * <li>El uso de la {@code FabricaBOs} para la obtención de servicios de negocio.</li>
 * <li>La correcta asignación de datos personales, credenciales y domicilio.</li>
 * <li>La persistencia de colecciones relacionadas (asociación {@code Telefono} - {@code Cliente}).</li>
 * <li>La conversión de formatos de fecha mediante {@code Date.valueOf}.</li>
 * <li>El retorno del objeto persistido con su ID generado por la base de datos.</li>
 */
public class PruebaRegistroCliente {

    public static void main(String[] args) {
        System.out.println("--- Prueba Registro Cliente ---");

        try {
            IClienteBO clienteBO = FabricaBOs.obtenerClienteBO();

            Cliente cliente = new Cliente();
            cliente.setUsername("prueba_registro");
            cliente.setPassword("mipassword");
            cliente.setNombres("Pedro");
            cliente.setApellidoPaterno("Lopez");
            cliente.setApellidoMaterno("Garcia");
            cliente.setFechaNacimiento(Date.valueOf("2000-05-15"));
            cliente.setCalle("Reforma");
            cliente.setNumero("123");
            cliente.setColonia("Centro");

            List<Telefono> telefonos = new ArrayList<>();
            Telefono tel = new Telefono();
            tel.setEtiqueta("CELULAR");
            tel.setNumero("6441234567");
            telefonos.add(tel);
            cliente.setTelefonos(telefonos);

            Cliente registrado = clienteBO.registrarCliente(cliente);
            System.out.println("Cliente registrado con ID: " + registrado.getIdUsuario());
            System.out.println("Username: " + registrado.getUsername());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
