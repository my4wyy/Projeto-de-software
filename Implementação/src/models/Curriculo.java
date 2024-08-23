package models;

import java.util.List;
import java.util.Map;

public class Curriculo {
    private Aluno aluno;
    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    private List<Disciplina> disciplinasCursadas;
    public List<Disciplina> getDisciplinasCursadas() {
        return disciplinasCursadas;
    }

    public void setDisciplinasCursadas(List<Disciplina> disciplinasCursadas) {
        this.disciplinasCursadas = disciplinasCursadas;
    }

    private Map<Disciplina, String> notas;

    public Curriculo(Aluno aluno, List<Disciplina> disciplinasCursadas, Map<Disciplina, String> notas) {
        this.aluno = aluno;
        this.disciplinasCursadas = disciplinasCursadas;
        this.notas = notas;
    }

    public Map<Disciplina, String> getNotas() {
        return notas;
    }

    public void setNotas(Map<Disciplina, String> notas) {
        this.notas = notas;
    }

    public void gerarCurriculo() {
    }
}
