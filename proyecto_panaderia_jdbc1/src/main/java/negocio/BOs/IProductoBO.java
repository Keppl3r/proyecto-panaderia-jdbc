/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio.BOs;

import java.util.List;
import negocio.excepciones.NegocioException;
import persistencia.dominio.Producto;

/**
 *
 * @author Adrian Mendoza
 */
public interface IProductoBO {
    
    public List<Producto> obtenerProductoDisponibles()throws NegocioException;
    public Producto obtenerPorId(int idProducto)throws NegocioException;
    
}