package com.flaviolisboa.sistema.entidades;

import com.flaviolisboa.sistema.constantes.Ordenacao;
import com.flaviolisboa.sistema.excecoes.PersistenciaException;
import com.flaviolisboa.sistema.excecoes.SistemaException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.metamodel.Attribute;

/**
 * Interface mínima para qualquer bean de persistência de dados relacionado à
 * uma {@link Entidade entidade}.
 * 
 * <p>Beans que implementam esta interface fazem parte da camada de
 * persistência do sistema: apenas estes beans fazem a comunicação, gravação e
 * leitura de dados do banco de dados ou de qualquer outro ambiente de
 * persistência (e.g. arquivos de texto como XML ou JSON, banco de dados
 * não-relacional).
 * </p>
 * 
 * <p>A única tarefa da camada de persistência é a de conversão e
 * persistência dos dados. Não é aconselhada a vaidação dos dados nesta camada,
 * pois esta tarefa cabe às camadas superiores (e.g. camadas de negócio, camadas
 * de visão e controle).
 * </p>
 * 
 * @author flisboac
 * @param <Id> O tipo do ID da entidade.
 * @param <T> O tipo da entidade.
 */
public interface Dao<Id extends Serializable, T extends Entidade<Id>> extends Serializable {

    /**
     * Retorna a classe da entidade.
     * 
     * <p>Independentemente da existência ou não subclasses/especializações de
     * {@code T}, este método sempre retornará a classe da entidade raíz
     * {@code T}.
     * </p>
     * 
     * @return A classe da entidade raíz.
     */
    Class<T> getClasseEntidade();
    
    /**
     * Retorna a classe do ID da entidade.
     * 
     * <p>A entidade raíz define o tipo do ID, e esta definição não pode ser
     * mudada pelas subclasses; isto se faz necessário para manter a
     * consistência na consulta e gravação dos dados para uma determinada
     * árvore de entidades. Por este motivo, este método sempre retornará um
     * único tipo de ID, que é o tipo especificado na entidade raíz.
     * </p>
     * 
     * @return A classe de ID da entidade raíz.
     */
    Class<Id> getClasseId();
    
    /**
     * Instancia um objeto com o tipo da entidade.
     * 
     * <p>O parâmetro {@code classeEntidade} é necessário para casos em que uma
     * entidade tenha especializações (ou seja, sub-classes, e.g.
     * {@code AutenticacaoSenha} e {@code AutenticacaoFacebook} são entidades
     * {@code Autenticacao} gerenciadas pelo mesmo conjunto de DAOs/DLOs
     * {@code AutenticacaoDao} e {@code AutenticacaoDlo}).
     * </p>
     * 
     * @param <TipoEntidade> O tipo da entidade, para caso de especializações da 
     * entidade raíz.
     * @param classeEntidade A classe/interface da entidade a ser instanciada.
     * @return Uma nova instância de {@code tipoEntidade}.
     */
    <TipoEntidade extends T> TipoEntidade instanciar(Class<TipoEntidade> classeEntidade) throws SistemaException;
    
    /**
     * Clona uma entidade, se possível.
     * 
     * @param <TipoEntidade> O tipo da entidade, para caso de especializações da 
     * entidade raíz.
     * @param entidade A entidade a ser clonada.
     * @return Umclone da entidade passada.
     * @throws SistemaException Em caso de erro ou impossibilidade de clonar.
     */
    <TipoEntidade extends T> TipoEntidade clonar(TipoEntidade entidade) throws SistemaException;
    
    /**
     * Insere uma nova entidade no ambiente de persistência.
     * 
     * <p>Note que esta é uma inserção: para que este método execute sem erros,
     * a entidade a ser gravada <em>não</em> deve existir no ambiente de
     * persistência .
     * </p>
     * 
     * @param <TipoEntidade> O tipo da entidade, para caso de especializações da 
     * entidade raíz.
     * @param entidade A entidade a ser inserida.
     * @param planos
     */
    <TipoEntidade extends T> void inserir(TipoEntidade entidade, PlanoConsulta... planos) throws SistemaException, PersistenciaException;
    
    /**
     * Atualiza os dados de uma entidade no ambiente de persistência.
     * 
     * <p>Note que esta é uma alteração: para que este modo execute sem erros,
     * a entidade a ser gravada <em>deve</em> existir no ambiente de
     * persistência.
     * </p>
     * 
     * @param <TipoEntidade> O tipo da entidade, para caso de especializações da 
     * entidade raíz.
     * @param entidade A entidade a ser alterada.
     * @param planos
     */
    <TipoEntidade extends T> void alterar(TipoEntidade entidade, PlanoConsulta... planos) throws SistemaException, PersistenciaException;
    
    /**
     * Exclui uma entidade pela sua identidade.
     * 
     * <p>Note que esta é uma exclusão: para que este modo execute sem erros,
     * a entidade a ser excluída <em>deve</em> existir no ambiente de
     * persistência.
     * </p>
     * 
     * @param <TipoEntidade> O tipo da entidade, para caso de especializações da 
     * entidade raíz.
     * @param entidade A entidade a ser excluída.
     * @param planos
     */
    <TipoEntidade extends T> void excluir(TipoEntidade entidade, PlanoConsulta... planos) throws SistemaException, PersistenciaException;
    
    /**
     * Exclui uma entidade pela sua identificação no ambiente de persistência.
     * 
     * <p>Note que esta é uma exclusão: para que este modo execute sem erros,
     * a identificação <em>deve</em> existir no ambiente de persistência.
     * </p>
     * 
     * @param id 
     * @param planos 
     */
    void excluirPorId(Id id, PlanoConsulta... planos) throws SistemaException, PersistenciaException;
    
    /**
     * Busca uma entidade como ID passado no ambiente de persistência.
     * 
     * <p>Note que esta é uma consulta: para que este modo execute sem erros,
     * a identificação <em>deve</em> existir no ambiente de persistência.
     * </p>
     * 
     * @param id O ID da entidade a ser carregada.
     * @param planos
     * @return A entidade, ou {@code null} se nenhuma entidade com o ID passado
     * existe.
     */
    T buscarPorId(Id id, PlanoConsulta... planos) throws SistemaException, PersistenciaException;
    
    /**
     * Busca uma entidade por sua identidade no ambiente de persistência.
     * 
     * <p>Note que esta é uma consulta: para que este modo execute sem erros,
     * a entidade <em>deve</em> existir no ambiente de persistência.
     * </p>
     * 
     * @param <TipoEntidade> O tipo da entidade, para caso de especializações da 
     * entidade raíz.
     * @param entidade
     * @param planos
     * @return 
     */
    <TipoEntidade extends T> TipoEntidade buscarPorIdentidade(TipoEntidade entidade, PlanoConsulta... planos) throws SistemaException, PersistenciaException;
    
    /**
     * Verifica se uma entidade como ID passado existe no ambiente de
     * persistência.
     * 
     * <p>Este método pode ser menos custoso que
     * {@link #buscarPorId(java.io.Serializable) } caso apenas se queira
     * verificar se o objeto existe ou não no banco de dados.
     * </p>
     * 
     * <p>Note que esta é uma consulta: para que este modo execute sem erros,
     * a identificação <em>deve</em> existir no ambiente de persistência.
     * </p>
     * 
     * @param id
     * @param planos
     * @return 
     */
    boolean isPersistidoPorId(Id id, PlanoConsulta... planos) throws SistemaException, PersistenciaException;
    
    /**
     * Verifica se uma entidade existe no ambiente de persistência.
     * 
     * <p>Este método pode ser menos custoso que
     * {@link #buscarPorIdentidade(com.flaviolisboa.sistema.entidades.Entidade) }
     * caso apenas se queira verificar se o objeto existe ou não no banco de
     * dados.
     * </p>
     * 
     * <p>Note que esta é uma consulta: para que este modo execute sem erros,
     * a entidade <em>deve</em> existir no ambiente de persistência.
     * </p>
     * 
     * @param <TipoEntidade>
     * @param entidade
     * @param planos
     * @return 
     */
    <TipoEntidade extends T> boolean isPersistidoPorIdentidade(TipoEntidade entidade, PlanoConsulta... planos) throws SistemaException, PersistenciaException;
    
    /**
     * Lista todas as entidades existentes no ambiente de persistência.
     * 
     * @param planos
     * @return Uma lista com todas as entidades encontradas.
     */
    List<T> listarTodos(PlanoConsulta... planos) throws SistemaException, PersistenciaException;
    
    /**
     * Retorna o número total de entidades persistidas.
     * 
     * @param planos
     * @return
     * @throws SistemaException
     * @throws PersistenciaException 
     */
    Number contarTodos(PlanoConsulta... planos) throws SistemaException, PersistenciaException;
    
    /*
     * Lista todas as entidades existentes no ambiente de persistência.
     * 
     * <p>{@code ordenacao} informa como a consulta será ordenada.
     * {@code indiceInicial} e {@code indiceFinal} demarcam os limites de
     * índice para os elementos incluídos no resultado. A lista retornada
     * terá tamanho inferior ou igual a {@code indices[1] - indices[0]} itens.
     * </p>
     * 
     * <p>Valores nulos não são válidos para os índices. Sendo este o caso, o
     * comportamento do método é indefinido.
     * </p>
     * 
     * <p>O método não retornará uma lista nula. Caso seja necessário indicar
     * um ou mais erros, ou caso não seja possível realizar a consulta, uma
     * exceção deverá ser lançada.
     * </p>
     * 
     * @param indiceInicial
     * @param indiceFinal
     * @param planos
     * @return Uma lista com todas as entidades encontradas.
     */
    //List<T> paginar(Number indiceInicial, Number indiceFinal, PlanoConsulta... planos) throws SistemaException, PersistenciaException;
    
    /**
     * Completa os valores das propriedades com os nomes informados.
     * 
     * <p>Em uma operação normal, este método realiza uma query/pesquisa ao
     * ambiente de persistência usando os nomes dos campos relativos às
     * propriedades listadas em {@code campos}. O método então <em>deve
     * apenas</em> atualizar estas propriedades, deixando os outros valores
     * intactos. O objeto retornado deve ser o mesmo recebido, para permitir
     * chamadas de método encadeadas (<em>chained calls</em>).
     * </p>
     * 
     * <p>Este método <em>não</em> é um método de persistência, e <em>nunca</em>
     * deve persistir nenhum dado da entidade passada. É Para isto que métodos
     * como {@link #inserir(com.flaviolisboa.sistema.entidades.Entidade) } e
     * {@link #alterar(com.flaviolisboa.sistema.entidades.Entidade) } existem.
     * </p>
     * 
     * @param <TipoEntidade> O tipo da entidade, para caso de especializações da 
     * entidade raíz.
     * @param entidade A entidade a ser completada.
     * @param campos Os nomes das propriedades a serem completadas.
     * @return O <em>mesmo</em> objeto de entrada {@code entidade} atualizado,
     * ou {@code null} caso não tenha sido possível completar o objeto.
     */
    <TipoEntidade extends T> TipoEntidade atualizar(TipoEntidade entidade, String... campos) throws SistemaException, PersistenciaException;
    
    /**
     * Completa os valores das propriedades com os atributos JPA informados.
     * 
     * <p>Este método tem a mesma semântica do {@link #atualizar(com.flaviolisboa.sistema.entidades.Entidade, javax.persistence.metamodel.Attribute...) },
     * com a diferença de que é voltado à backends baseados em JPA, para tirar
     * proveito dos benefícios de verificação sintática no uso das propriedades
     * em tempo de compilação (e.g. auxiliar nas refatorações ao indicar erros
     * à referências a propriedades em tempo de compilação). Caso a camada de
     * </p>
     * 
     * @param <TipoEntidade> O tipo da entidade, para caso de especializações da 
     * entidade raíz.
     * @param entidade A entidade a ser completada.
     * @param campos Os nomes das propriedades a serem completadas.
     * @return O <em>mesmo</em> objeto de entrada {@code entidade} atualizado,
     * ou {@code null} caso não tenha sido possível completar o objeto.
     */
    @SuppressWarnings("varargs")
    <TipoEntidade extends T> TipoEntidade atualizar(TipoEntidade entidade, Attribute<TipoEntidade, ?>... campos) throws SistemaException, PersistenciaException;
    
}
