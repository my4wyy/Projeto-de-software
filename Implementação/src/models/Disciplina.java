package models;

import enums.TipoDisciplina;
import java.util.List;

public class Disciplina {
    private String codigo;
    private String nome;
    private int creditos;
    private TipoDisciplina tipo;
    private List<Disciplina> preRequisitos;
    private int vagasDisponiveis;
    private List<Aluno> alunosMatriculados;
    private Professor professorResponsavel;

    public Disciplina(String codigo, String nome, int creditos, TipoDisciplina tipo, int vagasDisponiveis) {
        this.codigo = codigo;
        this.nome = nome;
        this.creditos = creditos;
        this.tipo = tipo;
        this.vagasDisponiveis = vagasDisponiveis;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public TipoDisciplina getTipo() {
        return tipo;
    }

    public void setTipo(TipoDisciplina tipo) {
        this.tipo = tipo;
    }

    public List<Disciplina> getPreRequisitos() {
        return preRequisitos;
    }

    public void setPreRequisitos(List<Disciplina> preRequisitos) {
        this.preRequisitos = preRequisitos;
    }

    public int getVagasDisponiveis() {
        return vagasDisponiveis;
    }

    public void setVagasDisponiveis(int vagasDisponiveis) {
        this.vagasDisponiveis = vagasDisponiveis;
    }

    public List<Aluno> getAlunosMatriculados() {
        return alunosMatriculados;
    }

    public void setAlunosMatriculados(List<Aluno> alunosMatriculados) {
        this.alunosMatriculados = alunosMatriculados;
    }

    public Professor getProfessorResponsavel() {
        return professorResponsavel;
    }

    public void setProfessorResponsavel(Professor professorResponsavel) {
        this.professorResponsavel = professorResponsavel;
    }

    public void matricularAluno(Aluno aluno) {
    }

    public void cancelarMatriculaAluno(Aluno aluno) {
    }

    public int verificarVagas() {
        return 0;
    }

    public void encerrarInscricoes() {
    }
}
