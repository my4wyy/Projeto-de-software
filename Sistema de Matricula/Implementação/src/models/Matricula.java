package models;

import java.io.Serializable;
import java.util.Date;

public class Matricula implements Serializable{
    private Aluno aluno;
    private Disciplina disciplina;
    private Date dataMatricula;

    public Matricula(Aluno aluno, Disciplina disciplina) {
        this.aluno = aluno;
        this.disciplina = disciplina;
        this.dataMatricula = new Date();
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Date getDataMatricula() {
        return dataMatricula;
    }

    public void setDataMatricula(Date dataMatricula) {
        this.dataMatricula = dataMatricula;
    }

    public void efetuarMatricula() {
        aluno.matricularDisciplina(disciplina);
    }

    public void cancelarMatricula() {
        aluno.cancelarMatricula(disciplina);
    }
}
