/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.pruebas;
 import persistencia.DAOs.*;
     import persistencia.conexion.*;
     import persistencia.dominio.*;
/**
 ** Punto de entrada principal para ejecutar la prueba de integración del flujo de pedidos.
     * <p>
     * Este método orquestador simula el comportamiento de la capa de presentación realizando
     * las siguientes etapas:
     * </p>
     * <ul>
     * <li><b>Inicialización:</b> Obtiene la implementación de la lógica de negocio desde {@link FabricaBOs}.</li>
     * <li><b>Construcción del Carrito:</b> Instancia una lista de {@link DetallePedido} con productos de prueba.</li>
     * <li><b>Transferencia de Datos:</b> Configura un {@link PedidoProgramadoNuevoDTO} estableciendo 
     * un cliente, los detalles y una fecha de entrega programada (3 horas a futuro).</li>
     * <li><b>Ejecución:</b> Invoca el método {@code programarPedido} y captura la respuesta o posibles errores.</li>
     * </ul>
     * * @param args Argumentos de línea de comandos (no utilizados en esta implementación).
     * @see FabricaBOs#obtenerPedidoProgramadoBO()
     * @see IPedidoProgramadoBO#programarPedido(PedidoProgramadoNuevoDTO)
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
