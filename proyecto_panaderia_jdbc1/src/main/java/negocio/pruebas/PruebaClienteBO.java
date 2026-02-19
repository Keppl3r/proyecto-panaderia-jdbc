
     package negocio.pruebas;

     import negocio.BOs.*;
     import persistencia.DAOs.*;
     import persistencia.conexion.*;

     public class PruebaClienteBO {

         public static void main(String[] args) {
             System.out.println("Probando ClienteBO");

             try {
                 IConexionBD conexion = new ConexionBD();
                 IClienteDAO clienteDAO = new ClienteDAO(conexion);
                 IClienteBO clienteBO = new ClienteBO(clienteDAO);

                 
                 boolean existe = clienteBO.existeCliente(1);
                 if (existe) {
                     System.out.println("Cliente 1 existe");
                 } else {
                     System.out.println("Cliente 1 no existe");
                 }

            
                 boolean noExiste = clienteBO.existeCliente(999);
                 if (noExiste) {
                     System.out.println("Cliente 999 existe ");
                 } else {
                     System.out.println("Cliente 999 no existe ");
                 }

             } catch (Exception e) {
                 System.out.println("Error: " + e.getMessage());
             }
         }
     }