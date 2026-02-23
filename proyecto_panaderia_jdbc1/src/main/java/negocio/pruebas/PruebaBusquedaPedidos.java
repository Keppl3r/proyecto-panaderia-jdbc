/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.pruebas;

import java.sql.Timestamp;
import java.util.List;
import negocio.BOs.PedidoBusquedaBO;
import persistencia.DAOs.IPedidoExpressDAO;
import persistencia.DAOs.IPedidoProgramadoDAO;
import persistencia.DAOs.PedidoExpressDAO;
import persistencia.DAOs.PedidoProgramadoDAO;
import persistencia.conexion.ConexionBD;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.Pedido;

/**
 * Prueba simple de busquedas de pedidos
 * @author Adrian Mendoza
 */

public class PruebaBusquedaPedidos {

    public static void main(String[] args) {
        try {
            // Crear conexión
            IConexionBD conexion = new ConexionBD();

            // Crear DAOs
            IPedidoProgramadoDAO pedidoPDAO = new PedidoProgramadoDAO(conexion);
            IPedidoExpressDAO pedidoEDAO   = new PedidoExpressDAO(conexion);

            // Crear BO
            PedidoBusquedaBO pedidoBusqueda = new PedidoBusquedaBO(pedidoPDAO, pedidoEDAO);

            // Buscar por telefono
            String telefono = "6451234567";
            List<Pedido> pedidosPorTelefono = pedidoBusqueda.buscarPorTelefono(telefono);
            System.out.println("Pedidos por teléfono:");
            for (Pedido p : pedidosPorTelefono) {
                System.out.println(p);
            }

            // Buscar por folio solo aplica en pedidos express
            String folio = "EXP-2001";
            Pedido pedidoPorFolio = pedidoBusqueda.buscarPorFolio(folio);
            System.out.println("\nPedido por folio:");
            System.out.println(pedidoPorFolio);

            // Buscar por rango de fechas
            Timestamp inicio = Timestamp.valueOf("2026-01-01 00:00:00");
            Timestamp fin    = new Timestamp(System.currentTimeMillis());
            List<Pedido> pedidosPorFecha = pedidoBusqueda.buscarPorRangoFechas(inicio, fin);
            System.out.println("\nPedidos por rango de fechas:");
            for (Pedido p : pedidosPorFecha) {
                System.out.println(p);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}



