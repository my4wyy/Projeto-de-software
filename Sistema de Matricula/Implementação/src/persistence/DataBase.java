package persistence;

import models.Aluno;
import models.Disciplina;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private static final String ALUNOS_FILE = "alunos.dat";
    private static final String DISCIPLINAS_FILE = "disciplinas.dat";

    private List<Aluno> alunos;
    private List<Disciplina> disciplinas;

    public DataBase() {
        try {
            alunos = (List<Aluno>) FileManager.loadFromFile(ALUNOS_FILE);
        } catch (IOException | ClassNotFoundException e) {
            alunos = new ArrayList<>();
        }

        try {
            disciplinas = (List<Disciplina>) FileManager.loadFromFile(DISCIPLINAS_FILE);
        } catch (IOException | ClassNotFoundException e) {
            disciplinas = new ArrayList<>();
        }
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void salvarDados() throws IOException {
        FileManager.saveToFile(ALUNOS_FILE, alunos);
        FileManager.saveToFile(DISCIPLINAS_FILE, disciplinas);
    }

    public void adicionarAluno(Aluno aluno) {
        alunos.add(aluno);
    }

    public void adicionarDisciplina(Disciplina disciplina) {
        disciplinas.add(disciplina);
    }

    public Aluno buscarAlunoPorNomeUsuario(String nomeUsuario) {
        for (Aluno aluno : alunos) {
            if (aluno.getNomeUsuario().equals(nomeUsuario)) {
                return aluno;
            }
        }
        return null;
    }

    public Disciplina buscarDisciplinaPorCodigo(String codigo) {
        for (Disciplina disciplina : disciplinas) {
            if (disciplina.getCodigo().equals(codigo)) {
                return disciplina;
            }
        }
        return null;
    }
}
