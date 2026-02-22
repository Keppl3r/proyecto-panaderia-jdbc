/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import java.util.List;
import negocio.excepciones.NegocioException;
import persistencia.dominio.DetallePedido;
import persistencia.dominio.PedidoExpress;

/**
 *
 * @author Jazmin
 */
public interface IPedidoExpressBO {
    
    public PedidoExpress programarPedidoExpress(List<DetallePedido> detalles) throws NegocioException;
    
}
