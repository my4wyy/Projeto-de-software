package br.com.demo.regescweb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import br.com.demo.regescweb.dao.TransacaoDAO;
import br.com.demo.regescweb.dao.ProfessorDAO;
import br.com.demo.regescweb.dao.AlunoDAO;
import br.com.demo.regescweb.dao.ContaDAO;
import br.com.demo.regescweb.models.Transacao;
import br.com.demo.regescweb.models.Professor;
import br.com.demo.regescweb.models.Aluno;
import br.com.demo.regescweb.models.Conta;

@RestController
@RequestMapping("/api/transacoes")
public class TransacaoController {

    @Autowired
    private TransacaoDAO transacaoDAO;

    @Autowired
    private ProfessorDAO professorDAO;

    @Autowired
    private AlunoDAO alunoDAO;

    @Autowired
    private ContaDAO contaDAO;

    @PostMapping("/enviar-moedas")
    public ResponseEntity<?> enviarMoedas(
            @RequestParam int quantidade,
            @RequestParam String motivo,
            @RequestParam Long alunoId,
            HttpServletRequest request) {

        try {
            System.out.println("Iniciando transferência de moedas...");

            // Obtendo o usuário logado a partir do atributo definido no filtro JWT
            String professorEmail = (String) request.getAttribute("clienteLogado");
            if (professorEmail == null) {
                System.out.println("Usuário não autenticado.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
            }

            Professor professorOrigem = professorDAO.buscarPorEmail(professorEmail);
            if (professorOrigem == null) {
                System.out.println("Professor não encontrado.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Professor não encontrado");
            }
            System.out.println("Professor encontrado: " + professorOrigem.getNome());

            Aluno alunoDestino = alunoDAO.buscarPorId(alunoId);
            if (alunoDestino == null) {
                System.out.println("Aluno não encontrado.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aluno não encontrado");
            }
            System.out.println("Aluno encontrado: " + alunoDestino.getNome());

            Conta contaProfessor = professorOrigem.getConta();
            if (contaProfessor.getSaldo() < quantidade) {
                System.out.println("Saldo insuficiente. Saldo atual: " + contaProfessor.getSaldo());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Saldo insuficiente");
            }

            Conta contaAluno = alunoDestino.getConta();
            System.out.println("Atualizando saldos...");
            contaProfessor.setSaldo(contaProfessor.getSaldo() - quantidade);
            contaAluno.setSaldo(contaAluno.getSaldo() + quantidade);
            System.out.println(contaAluno.getSaldo());
            System.out.println(contaProfessor.getSaldo());

        contaDAO.atualizar(contaProfessor);
        contaDAO.atualizar(contaAluno);

        
        Transacao transacao = new Transacao(quantidade, motivo, professorOrigem, alunoDestino);
        

        contaProfessor.adicionarTransacao(transacao);
        contaAluno.adicionarTransacao(transacao);

        transacaoDAO.salvar(transacao);

            System.out.println("Transação concluída com sucesso.");
            return ResponseEntity.status(HttpStatus.CREATED).body("Moedas enviadas com sucesso!");

        } catch (Exception e) {
            System.err.println("Erro ao processar transação:");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno no servidor");
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarTransacoes() {
        return ResponseEntity.ok(transacaoDAO.buscarTodas());
    }
}
