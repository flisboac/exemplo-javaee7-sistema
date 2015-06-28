
package com.flaviolisboa.sistema.usuarios;

import com.flaviolisboa.sistema.constantes.Constantes;
import com.flaviolisboa.sistema.entidades.Entidade;
import java.util.Date;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Usuario implements Entidade<Long> {

    private Long id;
    
    @NotNull(message = "Informe um e-mail.")
    @Size(min = Constantes.Email.TamanhoMinimo, max = Constantes.Email.Tamanho, message = "E-mail deve estar entre {min} e {max} caracter(es)..")
    @Pattern(regexp = Constantes.Email.Regexp, message = "Formato de e-mail inválido.")
    private String email;
    
    @NotNull(message = "Informe um nome.")
    @Size(min = Constantes.Email.TamanhoMinimo, max = Constantes.Email.Tamanho, message = "Nome deve estar entre {min} e {max} caracter(es).")
    private String nome;
    
    @Past(message = "Deve ser uma data anterior à data atual.")
    private Date dataCriacao;
    
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.email);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuario other = (Usuario) obj;
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", email=" + email + '}';
    }

}
