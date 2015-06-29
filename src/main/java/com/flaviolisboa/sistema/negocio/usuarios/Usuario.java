
package com.flaviolisboa.sistema.negocio.usuarios;

import com.flaviolisboa.sistema.constantes.Constantes;
import com.flaviolisboa.sistema.entidades.Entidade;
import com.flaviolisboa.sistema.gruposValidacao.Identidade;
import com.flaviolisboa.sistema.gruposValidacao.Identificacao;
import com.flaviolisboa.sistema.gruposValidacao.Integridade;
import com.flaviolisboa.sistema.gruposValidacao.Interno;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.GroupSequence;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@GroupSequence({Identificacao.class, Identidade.class, Integridade.class, Usuario.class})
@Entity
@Table(name = "usuario", uniqueConstraints = {
    @UniqueConstraint(name = "uq_usuario", columnNames = {"email"}) // É desejável que as identidades da entidade tenham uniqueConstraints correspondentes no banco
        
}, indexes = {
    @Index(name = "ix_usuario_nome", columnList = "nome") // À partir do Java EE 7, é possível definir índices. Muito útil.
})
@SequenceGenerator(name = "sq_usuario", sequenceName = "sq_usuario", allocationSize = 1, initialValue = 1)
@NamedQueries({
    @NamedQuery(name = "Usuario.buscarPorIdentidade", query = "select o from Usuario o where o.email = :email")
})
public class Usuario implements Entidade<Long> {

    @NotNull(groups = {Identificacao.class})
    @Id
    @GeneratedValue(generator = "sq_usuario", strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @NotNull(message = "Informe um e-mail.", groups = {Identidade.class})
    @Size(min = Constantes.Email.TamanhoMinimo, max = Constantes.Email.Tamanho, message = "E-mail deve estar entre {min} e {max} caracter(es)..", groups = {Identidade.class})
    @Pattern(regexp = Constantes.Email.Regexp, message = "Formato de e-mail inválido.", groups = {Identidade.class})
    @Column(name = "email", length = Constantes.Email.Tamanho, nullable = false)
    private String email;
    
    @NotNull(message = "Informe um nome.", groups = {Integridade.class})
    @Size(min = Constantes.Email.TamanhoMinimo, max = Constantes.Email.Tamanho, message = "Nome deve estar entre {min} e {max} caracter(es).", groups = {Integridade.class})
    @Column(name = "nome", length = Constantes.Nome.Tamanho, nullable = false)
    private String nome;
    
    @NotNull(groups = {Interno.class})
    @Past(groups = {Interno.class})
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_criacao", nullable = false)
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
