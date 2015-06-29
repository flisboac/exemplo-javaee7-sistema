
package com.flaviolisboa.sistema.views.usuarios;

import com.flaviolisboa.sistema.excecoes.SistemaException;
import com.flaviolisboa.sistema.negocio.usuarios.Usuario;
import com.flaviolisboa.sistema.negocio.usuarios.UsuarioDao;
import com.flaviolisboa.sistema.utils.FacesUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;


@ManagedBean
@ViewScoped
public class UsuarioViewBean implements Serializable {
    
    /*
     * DI
     * --
     */
    
    @EJB
    private UsuarioDao dao;
    
    /*
     * CAMPOS
     * ------
     */
    
    private Usuario entidade;
    private List<Usuario> entidades;
    private boolean inserindo;
    private boolean excluindo;
    private boolean editando;
    
    /*
     * MÉTODOS DE CICLO DE VIDA
     * ------------------------
     * Os dois métodos abaixo são chamados em eventos distintos do ciclo de vida
     * do bean:
     * - `init` (anotado com (@PostConstruct) é chamado após o bean ter sido
     *   construído, ou quando o container CDI achar que o objeto deve ser
     *   inicializado. É preferível que qualquer inicialização não-trivial seja
     *   feita em um @PostConstruct, principalmente quando esta inicialização
     *   envolve objetos injetados (e.g. chamada de métodos de um DLO).
     * - `quit` (anotado com @PreDestroy`) é chamado quando o bean está para ser
     *   descartado pelo container, ou quando seu tempo de vida está no fim. É
     *   usado para realizar a finalização de quaisquer recursos, se necessário.
     */
    
    @PostConstruct
    public void init() {
        FacesUtils.mensagem(FacesMessage.SEVERITY_INFO, null, "Novo bean", "Iniciando novo ViewBean...");
        doLimpar();
        doListar();
    }

    @PreDestroy
    public void quit() {
        FacesUtils.mensagem(FacesMessage.SEVERITY_INFO, null, "Saindo do bean", "Terminando novo ViewBean...");
    }
    
    /*
     * AÇÕES
     * -----
     */
    
    public void doLimpar() {
        this.entidade = dao.instanciar(Usuario.class);
        this.entidades = new ArrayList<>();
        this.inserindo = false;
        this.excluindo = false;
        this.editando = false;
    }
    
    public void doListar() {
        try {
            this.entidades = dao.listarTodos();
            
        } catch (SistemaException ex) {
            FacesUtils.mensagem(FacesMessage.SEVERITY_INFO, null, "ERRO!", ex.getLocalizedMessage());
        }
    }
    
    /*
     * OUTCOMES
     * --------
     * Estes são actions, que podem ou não retornar uma String indicando a
     * próxima página ou tela.
     */
    
    public String listar() {
        onListar();
        doListar();
        return null;
    }
    
    public String inserir() {
        try {
            dao.inserir(entidade);
            FacesUtils.mensagem(FacesMessage.SEVERITY_INFO, null, getMensagemAcaoRealizada(), null);
            
        } catch (SistemaException ex) {
            FacesUtils.mensagem(FacesMessage.SEVERITY_INFO, null, "ERRO!", ex.getLocalizedMessage());
        }
        
        return listar();
    }
    
    public String editar() {
        try {
            dao.alterar(entidade);
            FacesUtils.mensagem(FacesMessage.SEVERITY_INFO, null, getMensagemAcaoRealizada(), null);
            
        } catch (SistemaException ex) {
            FacesUtils.mensagem(FacesMessage.SEVERITY_INFO, null, "ERRO!", ex.getLocalizedMessage());
        }
        
        return listar();
    }
    
    public String excluir() {
        try {
            dao.excluir(entidade);
            FacesUtils.mensagem(FacesMessage.SEVERITY_INFO, null, getMensagemAcaoRealizada(), null);
            
        } catch (SistemaException ex) {
            FacesUtils.mensagem(FacesMessage.SEVERITY_INFO, null, "ERRO!", ex.getLocalizedMessage());
        }
        
        return listar();
    }
    
    public String persistir() {
        if (isInserindo()) {
            return inserir();
            
        } else if (isEditando()) {
            return editar();
            
        } else if (isExcluindo()) {
            return excluir();
        }
        
        return cancelar();
    }
    
    public String cancelar() {
        return listar();
    }
    
    /*
     * HOOKS DE EVENTOS
     * ----------------
     */
    
    public void onListar() {
        doLimpar();
        this.inserindo = false;
        this.excluindo = false;
        this.editando = false;
        //FacesUtils.mensagem(FacesMessage.SEVERITY_INFO, null, getMensagemAcaoSendoRealizada(), "Listando? " + isListando());
    }
    
    public void onVisualizar() {
        this.inserindo = false;
        this.excluindo = false;
        this.editando = false;
        //FacesUtils.mensagem(FacesMessage.SEVERITY_INFO, null, getMensagemAcaoSendoRealizada(), "Visualizando? " + isVisualizando());
    }
    
    public void onIncluir() {
        this.inserindo = true;
        this.excluindo = false;
        this.editando = false;
        //FacesUtils.mensagem(FacesMessage.SEVERITY_INFO, null, getMensagemAcaoSendoRealizada(), "Incluindo? " + isInserindo());
    }
    
    public void onEditar() {
        this.inserindo = false;
        this.excluindo = false;
        this.editando = true;
        //FacesUtils.mensagem(FacesMessage.SEVERITY_INFO, null, getMensagemAcaoSendoRealizada(), "Editando? " + isEditando());
    }
    
    public void onExcluir() {
        this.inserindo = false;
        this.excluindo = true;
        this.editando = false;
        //FacesUtils.mensagem(FacesMessage.SEVERITY_INFO, null, getMensagemAcaoSendoRealizada(), "Excluindo? " + isExcluindo());
    }
    
    /*
     * PROPRIEDADES
     * ------------
     */
    
    public String getMensagemAcaoSendoRealizada() {
        if (isInserindo()) {
            return "Novo Usuário";
            
        } else if (isEditando()) {
            return "Editando Usuário (ID: " + this.entidade.getId() + ")";
            
        } else if (isExcluindo()) {
            return "Excluindo Usuário (ID: " + this.entidade.getId() + ")";
            
        } else if (isVisualizando()) {
            return "Visualizando Usuário (ID: " + this.entidade.getId() + ")";
            
        } else if (isListando()) {
            return "Listando Usuários";
        }
        
        return "";
    }
        
    public String getMensagemAcaoRealizada() {
        if (isInserindo()) {
            return "Usuário inserido";
            
        } else if (isEditando()) {
            return "Usuário alterado (ID: " + this.entidade.getId() + ")";
            
        } else if (isExcluindo()) {
            return "Usuário excluído (ID: " + this.entidade.getId() + ")";
            
        } else if (isVisualizando()) {
            return "Usuário visualizado (ID: " + this.entidade.getId() + ")";
            
        } else if (isListando()) {
            return "Usuários listados";
        }
        
        return "";
    }
    
    public boolean isSelecionado() {
         return this.entidade != null && this.entidade.getId() != null;
    }
    
    public boolean isInserindo() {
        return this.entidade != null && !isSelecionado() && inserindo;
    }
    
    public boolean isEditando() {
        return isSelecionado() && editando;
    }
    
    public boolean isExcluindo() {
        return isSelecionado() && excluindo;
    }
    
    public boolean isVisualizando() {
        return isSelecionado() && !isInserindo() && !isEditando() && !isExcluindo();
    }
    
    public boolean isListando() {
        return !(isVisualizando() || isInserindo() || isEditando() || isExcluindo());
    }
    
    public boolean isPreparadoParaEdicao() {
        return isInserindo() || isEditando() || isVisualizando();
    }
    
    public boolean isRealizandoEdicao() {
        return isInserindo() || isEditando();
    }
    
    /*
     * GETTERS E SETTERS
     * -----------------
     */
    
    public Usuario getEntidade() {
        return entidade;
    }

    public void setEntidade(Usuario entidade) {
        //FacesUtils.mensagem(FacesMessage.SEVERITY_INFO, null, "Selecionado!", "Usuário selecionado.");
        this.entidade = entidade;
    }

    public List<Usuario> getEntidades() {
        return entidades;
    }

    public void setEntidades(List<Usuario> entidades) {
        this.entidades = entidades;
    }
    
}
