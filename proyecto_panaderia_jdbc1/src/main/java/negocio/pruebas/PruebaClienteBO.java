
     package negocio.pruebas;

     import negocio.BOs.*;
     import persistencia.DAOs.*;
     import persistencia.conexion.*;
     
     /**
     * Clase de prueba para validar la funcionalidad de la capa de negocio de Clientes.
     * <p>
     * Esta clase ejecuta un escenario de prueba manual para el método {@code existeCliente}.
     * Realiza las siguientes acciones:
     * <ul>
     * <li>Instancia la cadena de dependencias (Conexión -> DAO -> BO).</li>
     * <li>Verifica la existencia de un cliente con un ID conocido (ID: 1).</li>
     * <li>Verifica el manejo de casos negativos con un ID inexistente (ID: 999).</li>
     * </ul>
     * </p>
     * * Nota: Se utiliza principalmente en la etapa de desarrollo para asegurar que la 
     * comunicación con el servidor de base de datos sea correcta.
     */
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