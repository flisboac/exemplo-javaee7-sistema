package com.flaviolisboa.sistema.negocio.usuarios;

import com.flaviolisboa.sistema.entidades.Dao;
import javax.ejb.Local;

@Local
public interface UsuarioDao extends Dao<Long, Usuario> {

}
