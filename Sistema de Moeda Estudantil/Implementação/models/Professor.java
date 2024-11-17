package br.com.demo.regescweb.models;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Professor extends Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String departamento;

    public Professor() {}

    public Professor(String nome, String endereco, String email, String senha, String cpf, Conta conta, Instituicao instituicao, String departamento, int quantidade) {
        super(nome, endereco, email, senha, cpf, conta, instituicao);
        this.departamento = departamento;
    }

    public void enviarMoedas(Aluno aluno, int quantidade, String motivo) {
        if (getConta().getSaldo() >= quantidade) {
            getConta().setSaldo(getConta().getSaldo() - quantidade);
            aluno.getConta().setSaldo(aluno.getConta().getSaldo() + quantidade);
            Transacao transacao = new Transacao(quantidade, motivo, this, aluno);
            transacao.registrarTransacao();
        }
    }

    // Getters e Setters
    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

}
