/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.excepciones;

/**
 * Excepción personalizada para representar errores en las reglas de negocio.
 * <p>
 * Se utiliza para encapsular fallos que no son técnicos (como errores de SQL),
 * sino lógicos, tales como violaciones de políticas de venta, validaciones de 
 * usuario fallidas o estados de pedido inválidos. 
 * </p>
 * <p>
 * Permite que la capa de presentación reciba mensajes amigables y controlados 
 * para mostrar al usuario final.
 * </p>
 * * @author Jazmin
 */
public class NegocioException extends Exception{
    /**
     * Construye una nueva excepción sin un mensaje detallado.
     */
    public NegocioException() {
    }
    /**
     * Construye una nueva excepción con un mensaje específico que describe el error.
     * Este mensaje suele ser el que se muestra en las alertas de la interfaz (JavaFX).
     * * @param message Descripción del error de negocio.
     */
    public NegocioException(String message) {
        super(message);
    }
    /**
     * Construye una nueva excepción con un mensaje detallado y la causa original.
     * <p>
     * Útil para el "Exception Wrapping", donde se captura una excepción técnica 
     * (ej. {@code SQLException}) y se envuelve en una de negocio para no exponer 
     * detalles técnicos a la vista.
     * </p>
     * * @param message Descripción del error.
     * @param cause El error técnico original que provocó la falla.
     */
    public NegocioException(String message, Throwable cause) {
        super(message, cause);
    }
    
    

    
}