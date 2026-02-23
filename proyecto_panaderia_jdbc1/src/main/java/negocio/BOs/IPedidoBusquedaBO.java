/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import java.sql.Timestamp;
import java.util.List;
import negocio.excepciones.NegocioException;
import persistencia.dominio.Pedido;

/**
 *
 * @author jazmin
 */
public interface IPedidoBusquedaBO {

    List<Pedido> buscarPorTelefono(String telefono) throws NegocioException;;

    Pedido buscarPorFolio(String folio) throws NegocioException;;

    List<Pedido> buscarPorRangoFechas(Timestamp inicio, Timestamp fin) throws NegocioException;;

}
