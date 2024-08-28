package main;

import enums.TipoDisciplina;
import models.*;
import persistence.DataBase;

import java.io.IOException;
import java.util.Scanner;

public class MainApp {
    private static DataBase db = new DataBase();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarDados();

        while (true) {
            System.out.println("Bem-vindo ao Sistema de Matrículas");
            System.out.println("1. Login");
            System.out.println("2. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao == 1) {
                login();
            } else if (opcao == 2) {
                salvarDados();
                break;
            } else {
                System.out.println("Opção inválida.");
            }
        }
    }

    private static void login() {
        System.out.print("Nome de usuário: ");
        String nomeUsuario = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        Aluno aluno = db.buscarAlunoPorNomeUsuario(nomeUsuario);

        if (aluno != null && aluno.fazerLogin(nomeUsuario, senha)) {
            menuAluno(aluno);
        } else {
            System.out.println("Nome de usuário ou senha incorretos.");
        }
    }

    private static void menuAluno(Aluno aluno) {
        while (true) {
            System.out.println("\nBem-vindo, " + aluno.getNome());
            System.out.println("1. Inscrever-se no Semestre");
            System.out.println("2. Visualizar Disciplinas");
            System.out.println("3. Matricular em Disciplina");
            System.out.println("4. Cancelar Matrícula");
            System.out.println("5. Gerar Currículo");
            System.out.println("6. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    aluno.inscreverSemestre();
                    break;
                case 2:
                    visualizarDisciplinas();
                    break;
                case 3:
                    matricularDisciplina(aluno);
                    break;
                case 4:
                    cancelarMatricula(aluno);
                    break;
                case 5:
                    aluno.gerarCurriculo();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void visualizarDisciplinas() {
        System.out.println("\nDisciplinas Disponíveis:");
        for (Disciplina disciplina : db.getDisciplinas()) {
            String tipo = disciplina.getTipo() == TipoDisciplina.OBRIGATORIA ? "Obrigatória" : "Optativa";
            System.out.println("Código: " + disciplina.getCodigo() + " - " + disciplina.getNome() + " (" + disciplina.getVagasDisponiveis() + " vagas) - " + tipo);
        }
    }

    private static void matricularDisciplina(Aluno aluno) {
        System.out.print("Digite o código da disciplina: ");
        String codigo = scanner.nextLine();

        Disciplina disciplina = db.buscarDisciplinaPorCodigo(codigo);

        if (disciplina != null) {
            aluno.matricularDisciplina(disciplina);
        } else {
            System.out.println("Disciplina não encontrada.");
        }
    }

    private static void cancelarMatricula(Aluno aluno) {
        System.out.print("Digite o código da disciplina para cancelar a matrícula: ");
        String codigo = scanner.nextLine();

        Disciplina disciplina = db.buscarDisciplinaPorCodigo(codigo);

        if (disciplina != null) {
            aluno.cancelarMatricula(disciplina);
        } else {
            System.out.println("Disciplina não encontrada.");
        }
    }

    private static void inicializarDados() {
        if (db.getAlunos().isEmpty()) {
            Aluno aluno1 = new Aluno("aluno1", "senha1", "João", "20201234", "Engenharia de Software");
            Aluno aluno2 = new Aluno("aluno2", "senha2", "Maria", "20205678", "Engenharia de Software");
            db.adicionarAluno(aluno1);
            db.adicionarAluno(aluno2);
        }

        if (db.getDisciplinas().isEmpty()) {
            Disciplina disciplina1 = new Disciplina("COMP101", "Programação I", 4, TipoDisciplina.OBRIGATORIA, 2);
            Disciplina disciplina2 = new Disciplina("COMP102", "Programação II", 4, TipoDisciplina.OBRIGATORIA, 2);
            Disciplina disciplina3 = new Disciplina("COMP103", "Inteligência Artificial", 4, TipoDisciplina.OPTATIVA, 2);
            db.adicionarDisciplina(disciplina1);
            db.adicionarDisciplina(disciplina2);
            db.adicionarDisciplina(disciplina3);
        }
    }

    private static void salvarDados() {
        try {
            db.salvarDados();
            System.out.println("Dados salvos com sucesso.");
        } catch (IOException e) {
            System.out.println("Erro ao salvar os dados: " + e.getMessage());
        }
    }
}
