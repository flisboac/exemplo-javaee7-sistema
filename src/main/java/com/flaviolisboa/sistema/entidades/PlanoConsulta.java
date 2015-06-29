package com.flaviolisboa.sistema.entidades;

import com.flaviolisboa.sistema.constantes.Ordenacao;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.persistence.metamodel.Attribute;

public final class PlanoConsulta {

    private final Set<String> fetches = new HashSet<>();
    private final Set<String> loads = new HashSet<>();
    private final Set<Object> marcadores = new HashSet<>();
    private final Map<Object, Object> dicas = new HashMap<>();
    private final Map<String, Ordenacao> ordersBy = new LinkedHashMap<>();
    
    public static PlanoConsulta fetch(String nomeGrafo) {
        PlanoConsulta retorno = new PlanoConsulta();
        retorno.fetches.add(nomeGrafo);
        return retorno;
    }
    
    public PlanoConsulta addFetch(String nomeGrafo) {
        this.fetches.add(nomeGrafo);
        return this;
    }

    public Set<String> getFetches() {
        return fetches;
    }
    
    public static PlanoConsulta load(String nomeGrafo) {
        PlanoConsulta retorno = new PlanoConsulta();
        retorno.loads.add(nomeGrafo);
        return retorno;
    }
    
    public PlanoConsulta addLoad(String nomeGrafo) {
        this.loads.add(nomeGrafo);
        return this;
    }

    public Set<String> getLoads() {
        return loads;
    }
    
    public static PlanoConsulta marcador(Object marcador) {
        PlanoConsulta retorno = new PlanoConsulta();
        retorno.marcadores.add(marcador);
        return retorno;
    }
    
    public PlanoConsulta addMarcador(Object marcador) {
        this.marcadores.add(marcador);
        return this;
    }

    public Set<Object> getMarcadores() {
        return marcadores;
    }
    
    public static PlanoConsulta dica(Object chave, Object valor) {
        PlanoConsulta retorno = new PlanoConsulta();
        retorno.dicas.put(chave, valor);
        return retorno;
    }
    
    public PlanoConsulta addGrafo(Object chave, Object valor) {
        this.dicas.put(chave, valor);
        return this;
    }

    public Map<Object, Object> getDicas() {
        return dicas;
    }
    
    public static PlanoConsulta orderBy(Attribute<?, ?> chave, Ordenacao valor) {
        PlanoConsulta retorno = new PlanoConsulta();
        retorno.ordersBy.put(chave.getName(), valor);
        return retorno;
    }
    
    public PlanoConsulta addOrderBy(Attribute<?, ?> chave, Ordenacao valor) {
        this.ordersBy.put(chave.getName(), valor);
        return this;
    }
    
    public static PlanoConsulta orderBy(String chave, Ordenacao valor) {
        PlanoConsulta retorno = new PlanoConsulta();
        retorno.ordersBy.put(chave, valor);
        return retorno;
    }
    
    public PlanoConsulta addOrderBy(String chave, Ordenacao valor) {
        this.ordersBy.put(chave, valor);
        return this;
    }

    public Map<String, Ordenacao> getOrdersBy() {
        return ordersBy;
    }
    
    public static PlanoConsulta agregar(PlanoConsulta... planos) {
        PlanoConsulta retorno = new PlanoConsulta();
        for (PlanoConsulta plano : planos) {
            retorno.fetches.addAll(plano.fetches);
            retorno.loads.addAll(plano.loads);
            retorno.marcadores.addAll(plano.marcadores);
            retorno.dicas.putAll(plano.dicas);
            retorno.ordersBy.putAll(plano.ordersBy);
        }
        return retorno;
    }
}
