
package com.flaviolisboa.sistema.utils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class FacesUtils {

    public static void mensagem(FacesMessage.Severity severidade, String id, String sumario, String descricao) {
        FacesMessage mensagem = new FacesMessage(severidade, sumario, descricao);
        FacesContext.getCurrentInstance().addMessage(id, mensagem);
    }
}
