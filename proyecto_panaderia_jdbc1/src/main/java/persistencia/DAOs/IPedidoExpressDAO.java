/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;

import persistencia.dominio.PedidoExpress;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author Jazmin
 */
public interface IPedidoExpressDAO {
    
    
    public PedidoExpress crear(PedidoExpress pedido) throws PersistenciaException;
    public int generarNumPedido()throws PersistenciaException;
    
}
