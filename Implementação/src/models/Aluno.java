package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import enums.TipoUsuario;

public class Aluno extends Usuario implements Serializable {
    private String nome;
    private String matricula;
    private String curso;
    private List<Matricula> matriculas;
    private Administrador administrador;

    public Aluno(String nomeUsuario, String senha, String nome, String matricula, String curso) {
        super(nomeUsuario, senha, TipoUsuario.ALUNO);
        this.nome = nome;
        this.matricula = matricula;
        this.curso = curso;
        this.matriculas = new ArrayList<>();
        this.administrador = new Administrador("admin", "admin123", "Admin", "0001", "Administração"); // Admin associado
    }

    public String getNome() {
        return nome;
    }

    public void inscreverSemestre() {
        System.out.println(nome + " foi inscrito no novo semestre.");
    }

    public void matricularDisciplina(Disciplina disciplina) {
        if (disciplina.getVagasDisponiveis() > 0) {
            Matricula matricula = new Matricula(this, disciplina);
            matriculas.add(matricula);
            disciplina.matricularAluno(this);
            System.out.println("Matriculado na disciplina: " + disciplina.getNome());

            // Notificação de cobrança
            administrador.cobrarDisciplinas(this);

        } else {
            System.out.println("Não há vagas disponíveis na disciplina: " + disciplina.getNome());
        }
    }

    public void cancelarMatricula(Disciplina disciplina) {
        for (Matricula matricula : matriculas) {
            if (matricula.getDisciplina().equals(disciplina)) {
                matriculas.remove(matricula);
                disciplina.cancelarMatriculaAluno(this);
                System.out.println("Matrícula cancelada na disciplina: " + disciplina.getNome());
                return;
            }
        }
        System.out.println("Matrícula não encontrada para a disciplina: " + disciplina.getNome());
    }

    public List<Matricula> getMatriculas() {
        return matriculas;
    }

    public void verificarVagas(Disciplina disciplina) {
        System.out.println("Vagas disponíveis na disciplina " + disciplina.getNome() + ": " + disciplina.getVagasDisponiveis());
    }

    public Curriculo gerarCurriculo() {
        Curriculo curriculo = new Curriculo(this);
        System.out.println("Currículo gerado para " + nome);
        return curriculo;
    }
}
