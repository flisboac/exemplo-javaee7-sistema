
package com.flaviolisboa.sistema.negocio.usuarios;

import com.flaviolisboa.sistema.constantes.Constantes;
import com.flaviolisboa.sistema.entidades.EntidadeDaoAbstractJpaBean;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name= "usuarioDao")
public class UsuarioDaoJpaBean extends EntidadeDaoAbstractJpaBean<Long, Usuario> implements UsuarioDao {

    @PersistenceContext(unitName = Constantes.NomePersistenceUnitPadrao)
    private EntityManager entityManager;
    
    public UsuarioDaoJpaBean() {
        super(Usuario.class, Long.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    
    @Override
    public <T extends Usuario> T instanciar(Class<T> classeEntidade) {
        T retorno = super.instanciar(classeEntidade);
        retorno.setDataCriacao(new Date());
        return retorno;
    }
}
