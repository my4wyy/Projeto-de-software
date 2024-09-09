package models;

import enums.TipoDisciplina;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Disciplina implements Serializable {
    private String codigo;
    private String nome;
    private int creditos;
    private TipoDisciplina tipo;
    private List<Disciplina> preRequisitos;
    private int vagasDisponiveis;
    private List<Aluno> alunosMatriculados;

    public Disciplina(String codigo, String nome, int creditos, TipoDisciplina tipo, int vagasDisponiveis) {
        this.codigo = codigo;
        this.nome = nome;
        this.creditos = creditos;
        this.tipo = tipo;
        this.preRequisitos = new ArrayList<>();
        this.vagasDisponiveis = vagasDisponiveis;
        this.alunosMatriculados = new ArrayList<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public int getCreditos() {
        return creditos;
    }

    public TipoDisciplina getTipo() {
        return tipo;
    }

    public int getVagasDisponiveis() {
        return vagasDisponiveis;
    }

    public void matricularAluno(Aluno aluno) {
        if (vagasDisponiveis > 0) {
            alunosMatriculados.add(aluno);
            vagasDisponiveis--;
        }
    }

    public void cancelarMatriculaAluno(Aluno aluno) {
        alunosMatriculados.remove(aluno);
        vagasDisponiveis++;
    }

    public void encerrarInscricoes() {
        System.out.println("Inscrições encerradas para a disciplina: " + nome);
    }

    public List<Aluno> getAlunosMatriculados() {
        return alunosMatriculados;
}
}
