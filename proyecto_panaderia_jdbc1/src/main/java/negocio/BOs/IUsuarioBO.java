package negocio.BOs;

import negocio.excepciones.NegocioException;
import persistencia.dominio.Usuario;

public interface IUsuarioBO {

    Usuario autenticar(String username, String password) throws NegocioException;
}
