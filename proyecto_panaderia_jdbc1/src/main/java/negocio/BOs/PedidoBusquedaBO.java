/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.excepciones.NegocioException;
import persistencia.DAOs.IPedidoExpressDAO;
import persistencia.DAOs.IPedidoProgramadoDAO;
import persistencia.dominio.Pedido;
import persistencia.dominio.PedidoExpress;
import persistencia.dominio.PedidoProgramado;
import persistencia.excepciones.PersistenciaException;

public class PedidoBusquedaBO implements IPedidoBusquedaBO {

    private IPedidoProgramadoDAO pedidoProgramadoDAO;
    private IPedidoExpressDAO pedidoExpressDAO;

    public PedidoBusquedaBO(IPedidoProgramadoDAO pedidoProgramadoDAO, IPedidoExpressDAO pedidoExpressDAO) {
        this.pedidoProgramadoDAO = pedidoProgramadoDAO;
        this.pedidoExpressDAO = pedidoExpressDAO;
    }

    @Override
    public List<Pedido> buscarPorTelefono(String telefono) {
        List<Pedido> resultados = new ArrayList<>();
        try {
            // Pedidos programados
            List<PedidoProgramado> programados = pedidoProgramadoDAO.obtenerPorTelefono(telefono);
            if (programados != null) {
                resultados.addAll(programados);
            }

            // Pedidos express
            List<PedidoExpress> express = pedidoExpressDAO.obtenerPorTelefono(telefono);
            if (express != null) {
                resultados.addAll(express);
            }

       } catch (PersistenciaException ex) {
            try {
                Logger.getLogger(PedidoBusquedaBO.class.getName())
                        .log(Level.SEVERE, "Error al buscar pedidos por teléfono", ex);
                throw new NegocioException("Error al buscar pedidos por teléfono", ex);
            } catch (NegocioException ex1) {
                Logger.getLogger(PedidoBusquedaBO.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }


    
        return resultados;
    }

    @Override
    public Pedido buscarPorFolio(String folio) throws NegocioException {
        try {
            // Solo los pedidos express tienen folio
            PedidoExpress pedido = pedidoExpressDAO.obtenerPorFolio(folio);
            if (pedido != null) {
                return pedido;
            } else {
                // Si no existe regresa null o lanzar excepción
                return null;
            }
         } catch (PersistenciaException ex) {
            throw new NegocioException("Error al buscar pedido por folio", ex);
        }
    }

    @Override
    public List<Pedido> buscarPorRangoFechas(Timestamp inicio, Timestamp fin) throws NegocioException {
        List<Pedido> resultados = new ArrayList<>();
        try {
            // Pedidos programados
            List<PedidoProgramado> programados = pedidoProgramadoDAO.obtenerPorRangoFechas(inicio, fin);
            if (programados != null) {
                resultados.addAll(programados);
            }

            // Pedidos express
            List<PedidoExpress> express = pedidoExpressDAO.obtenerPorRangoFechas(inicio, fin);
            if (express != null) {
                resultados.addAll(express);
            }

         } catch (PersistenciaException ex) {
            throw new NegocioException("Error al buscar pedidos por rango de fechas", ex);
        }

        return resultados;
    }
}