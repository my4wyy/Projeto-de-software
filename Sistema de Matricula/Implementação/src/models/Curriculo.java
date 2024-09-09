package models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Curriculo {
    private Aluno aluno;
    private List<Disciplina> disciplinasCursadas;
    private Map<Disciplina, String> notas;

    public Curriculo(Aluno aluno) {
        this.aluno = aluno;
        this.notas = new HashMap<>();
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public List<Disciplina> getDisciplinasCursadas() {
        return disciplinasCursadas;
    }

    public void setDisciplinasCursadas(List<Disciplina> disciplinasCursadas) {
        this.disciplinasCursadas = disciplinasCursadas;
    }

    public Map<Disciplina, String> getNotas() {
        return notas;
    }

    public void setNotas(Map<Disciplina, String> notas) {
        this.notas = notas;
    }

    public void gerarCurriculo() {
        System.out.println("Gerando currículo para o aluno " + aluno.getNome());
        for (Disciplina disciplina : disciplinasCursadas) {
            System.out.println("Disciplina: " + disciplina.getNome() + " - Nota: " + notas.get(disciplina));
        }
    }
}
