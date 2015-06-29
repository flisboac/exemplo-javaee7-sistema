package com.flaviolisboa.sistema.entidades;

import com.flaviolisboa.sistema.constantes.Ordenacao;
import com.flaviolisboa.sistema.excecoes.ConsultaNaoImplementadaException;
import com.flaviolisboa.sistema.excecoes.PersistenciaException;
import com.flaviolisboa.sistema.excecoes.SistemaException;
import com.flaviolisboa.sistema.utils.JpaUtils;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.metamodel.Attribute;
import org.apache.commons.beanutils.BeanUtils;

public abstract class EntidadeDaoAbstractJpaBean<Id extends Serializable, T extends Entidade<Id>> implements Dao<Id, T> {

    protected Class<T> classeEntidade;
    protected Class<Id> classeId;

    public EntidadeDaoAbstractJpaBean(Class<T> classeEntidade, Class<Id> classeId) {
        this.classeEntidade = classeEntidade;
        this.classeId = classeId;
    }

    protected abstract EntityManager getEntityManager();

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<T> getClasseEntidade() {
        return this.classeEntidade;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<Id> getClasseId() {
        return this.classeId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <TipoEntidade extends T> TipoEntidade instanciar(Class<TipoEntidade> classeEntidade) throws SistemaException {
        try {
            return classeEntidade.newInstance();

        } catch (InstantiationException | IllegalAccessException ex) {
            throw new SistemaException(ex);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <TipoEntidade extends T> TipoEntidade clonar(TipoEntidade entidade) throws SistemaException {
        Class<? extends TipoEntidade> classeRealEntidade = (Class<? extends TipoEntidade>) entidade.getClass();
        TipoEntidade retorno = null;
        try {
            if (classeRealEntidade.getMethod("clone") != null) {
                Method metodo = classeRealEntidade.getMethod("clone");
                retorno = (TipoEntidade) metodo.invoke(entidade);
            } else {
                retorno = (TipoEntidade) BeanUtils.cloneBean(entidade);
            }
        } catch (NoSuchMethodException 
                | SecurityException 
                | IllegalAccessException 
                | IllegalArgumentException 
                | InvocationTargetException
                | InstantiationException ex) {
            throw new SistemaException(ex);
        }
        return retorno;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <TipoEntidade extends T> void inserir(TipoEntidade entidade, PlanoConsulta... planos) throws SistemaException, PersistenciaException {
        try {
            getEntityManager().persist(entidade);
            
        } catch (PersistenceException ex) {
            throw new PersistenciaException(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <TipoEntidade extends T> void alterar(TipoEntidade entidade, PlanoConsulta... planos) throws SistemaException, PersistenciaException {
        try {
            if (!getEntityManager().contains(entidade)) {
                getEntityManager().merge(entidade);
            }
            
        } catch (PersistenceException ex) {
            throw new PersistenciaException(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <TipoEntidade extends T> void excluir(TipoEntidade entidade, PlanoConsulta... planos) throws SistemaException, PersistenciaException {
        try {
            if (!getEntityManager().contains(entidade)) {
                entidade = buscarPorIdentidade(entidade);
            }
            getEntityManager().remove(entidade);
            
        } catch (PersistenceException ex) {
            throw new PersistenciaException(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void excluirPorId(Id id, PlanoConsulta... planos) throws SistemaException, PersistenciaException {
        try {
            T entidade = getEntityManager().find(classeEntidade, id);
            if (entidade != null) {
                getEntityManager().remove(entidade);
            }
            
        } catch (PersistenceException ex) {
            throw new PersistenciaException(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T buscarPorId(Id id, PlanoConsulta... planos) throws SistemaException, PersistenciaException {
        try {
            PlanoConsulta plano = PlanoConsulta.agregar(planos);
            String jpql = String.format("select o from %s o where o.id = :id", JpaUtils.getEntityName(classeEntidade));
            TypedQuery<T> query = getEntityManager().createQuery(jpql, classeEntidade);
            query.setParameter("id", id);
            aplicarDicas(query, plano);
            return JpaUtils.getSingleResult(query);
            
        } catch (PersistenceException ex) {
            throw new PersistenciaException(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <TipoEntidade extends T> TipoEntidade buscarPorIdentidade(TipoEntidade entidade, PlanoConsulta... planos) throws SistemaException, PersistenciaException {
        try {
            Class<TipoEntidade> classeRealEntidade = (Class<TipoEntidade>) entidade.getClass();
            TypedQuery<TipoEntidade> query = JpaUtils.createStandardizedNamedQuery(getEntityManager(), "buscarPorIdentidade", entidade, classeRealEntidade);
            
            if (query == null) {
                throw new ConsultaNaoImplementadaException();
            }
            
            aplicarDicas(query, PlanoConsulta.agregar(planos));
            return JpaUtils.getSingleResult(query, classeRealEntidade);
            
        } catch (IllegalAccessException 
                | InvocationTargetException 
                | NoSuchMethodException 
                | PersistenceException ex) {
            throw new PersistenciaException(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPersistidoPorId(Id id, PlanoConsulta... planos) throws SistemaException, PersistenciaException {
        try {
            PlanoConsulta plano = PlanoConsulta.agregar(planos);
            String jpql = String.format("select case when count(o) then true else false end from %s o where o.id = :id", JpaUtils.getEntityName(classeEntidade));
            Query query = getEntityManager().createQuery(jpql);
            query.setParameter("id", id);
            aplicarDicas(query, plano);
            return JpaUtils.getSingleResult(query, Boolean.class);
            
        } catch (PersistenceException ex) {
            throw new PersistenciaException(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <TipoEntidade extends T> boolean isPersistidoPorIdentidade(TipoEntidade entidade, PlanoConsulta... planos) throws SistemaException, PersistenciaException {
        try {
            Query query = JpaUtils.createStandardizedNamedQuery(getEntityManager(), "isPersistidoPorIdentidade", entidade);
            
            if (query == null) {
                return buscarPorIdentidade(entidade) != null;
            }
            
            aplicarDicas(query, PlanoConsulta.agregar(planos));
            return JpaUtils.getSingleResult(query, Boolean.class);
            
        } catch (IllegalAccessException 
                | InvocationTargetException 
                | NoSuchMethodException 
                | PersistenceException ex) {
            throw new PersistenciaException(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> listarTodos(PlanoConsulta... planos) throws SistemaException, PersistenciaException {
        try {
            PlanoConsulta plano = PlanoConsulta.agregar(planos);
            String jpql = String.format("select o from %s o", JpaUtils.getEntityName(classeEntidade));
            jpql = prepararJpqlOrderBy(jpql, plano);
            TypedQuery<T> query = getEntityManager().createQuery(jpql, classeEntidade);
            aplicarDicas(query, plano);
            return query.getResultList();
            
        } catch (PersistenceException ex) {
            throw new PersistenciaException(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Number contarTodos(PlanoConsulta... planos) throws SistemaException, PersistenciaException {
        try {
            PlanoConsulta plano = PlanoConsulta.agregar(planos);
            String jpql = String.format("select count(o) from %s o", JpaUtils.getEntityName(classeEntidade));
            TypedQuery<T> query = getEntityManager().createQuery(jpql, classeEntidade);
            aplicarDicas(query, plano);
            return JpaUtils.getSingleResult(query, Number.class);
            
        } catch (PersistenceException ex) {
            throw new PersistenciaException(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <TipoEntidade extends T> TipoEntidade atualizar(TipoEntidade entidade, String... campos) throws SistemaException, PersistenciaException {
        TipoEntidade entidadePersistida = entidade;
        boolean copiaNecessaria = false;
        
        if (!getEntityManager().contains(entidadePersistida)) {
            entidadePersistida = (TipoEntidade) buscarPorId(entidade.getId());
        }
        
        if (entidadePersistida == entidade) {
            
        }
        
        return entidade;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <TipoEntidade extends T> TipoEntidade atualizar(TipoEntidade entidade, Attribute<TipoEntidade, ?>... campos) throws SistemaException, PersistenciaException {
        String[] strCampos = new String[campos.length];
        for (int i = 0; i < strCampos.length; ++i) {
            strCampos[i] = campos[i].getName();
        }
        return atualizar(entidade, strCampos);
    }

    protected String prepararJpqlOrderBy(String jpql, PlanoConsulta plano) {
        
        if (!plano.getOrdersBy().isEmpty()) {
            String sep = " ";
            StringBuilder sb = new StringBuilder();
            sb.append(jpql);
            
            for (String campo : plano.getOrdersBy().keySet()) {
                Ordenacao valor = plano.getOrdersBy().get(campo);
                sb.append(sep).append(campo).append(" ").append(valor.getValorJpa());
                sep = ", ";
            }
        }
        
        return jpql;
    }
    
    protected void aplicarDicas(Query query, PlanoConsulta plano) {
        
        if (!plano.getLoads().isEmpty()) {
            EntityGraph<?> grafo = getEntityManager().getEntityGraph(plano.getLoads().iterator().next());
            query.setHint("javax.persistence.loadgraph", grafo);
        }
        
        if (!plano.getFetches().isEmpty()) {
            EntityGraph<?> grafo = getEntityManager().getEntityGraph(plano.getFetches().iterator().next());
            query.setHint("javax.persistence.fetchgraph", grafo);
        }
        
        for (Object chave : plano.getDicas().keySet()) {
            Object valor = plano.getDicas().get(chave);
            query.setHint(chave.toString(), valor);
        }
    }
}
