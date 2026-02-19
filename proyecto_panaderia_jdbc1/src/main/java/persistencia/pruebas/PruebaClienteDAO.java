/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.pruebas;
 import persistencia.DAOs.*;
     import persistencia.conexion.*;
     import persistencia.dominio.*;
/**
 *
 * @author Adrian Mendoza
 */
public class PruebaClienteDAO {
    
         public static void main(String[] args) {
             System.out.println("Probando ClienteDAO");

             try {
                 IConexionBD conexion = new ConexionBD();
                 IClienteDAO clienteDAO = new ClienteDAO(conexion);

                 // Buscar cliente existente
                 Cliente cliente = clienteDAO.buscarPorId(1);
                 if (cliente != null) {
                     System.out.println("Cliente encontrado");
                     System.out.println("Nombre: " + cliente.getNombres());
                 } else {
                     System.out.println("Cliente no encontrado");
                 }

                 // Verificar cliente activo
                 boolean activo = clienteDAO.existeClienteActivo(1);
                 if (activo) {
                     System.out.println("Cliente activo");
                 } else {
                     System.out.println("Cliente inactivo");
                 }

             } catch (Exception e) {
                 System.out.println("Error: " + e.getMessage());
             }
         }
}
