package br.com.demo.regescweb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import br.com.demo.regescweb.dao.TransacaoDAO;
import br.com.demo.regescweb.dao.VantagemDAO;
import br.com.demo.regescweb.dao.ProfessorDAO;
import br.com.demo.regescweb.dao.AlunoDAO;
import br.com.demo.regescweb.dao.ContaDAO;
import br.com.demo.regescweb.models.Transacao;
import br.com.demo.regescweb.models.Vantagem;
import br.com.demo.regescweb.models.Professor;
import br.com.demo.regescweb.models.ResgatarVantagemRequest;
import br.com.demo.regescweb.models.Aluno;
import br.com.demo.regescweb.models.Conta;
import br.com.demo.regescweb.models.EmpresaParceira;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/transacoes")
public class TransacaoController {

    private static final Logger logger = LoggerFactory.getLogger(VantagemController.class);


    @Autowired
    private TransacaoDAO transacaoDAO;

    @Autowired
    private VantagemDAO vantagemDAO;

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
        contaDAO.atualizar(contaProfessor);
        contaDAO.atualizar(contaAluno);

        Transacao transacao = new Transacao(quantidade, motivo, professorOrigem, alunoDestino);

        contaProfessor.adicionarTransacao(transacao);
        contaAluno.adicionarTransacao(transacao);
        transacaoDAO.salvar(transacao);

        System.out.println("Transação concluída com sucesso.");

        transacao.enviarEmailAluno(alunoDestino, professorOrigem, quantidade, motivo);

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

    @PostMapping("/resgatar-vantagem")
public ResponseEntity<?> resgatarVantagem(@RequestBody ResgatarVantagemRequest requestBody) {
    logger.info("Iniciando resgate de vantagem para aluno ID: {} e vantagem ID: {}", 
        requestBody.getAlunoId(), requestBody.getVantagemId());

    try {
        logger.debug("Buscando aluno no banco de dados.");
        Aluno aluno = alunoDAO.buscarPorId(requestBody.getAlunoId());
        if (aluno == null) {
            logger.warn("Aluno com ID {} não encontrado.", requestBody.getAlunoId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aluno não encontrado.");
        }

        logger.debug("Buscando vantagem no banco de dados.");
        Vantagem vantagem = vantagemDAO.buscarPorId(requestBody.getVantagemId());
        if (vantagem == null) {
            logger.warn("Vantagem com ID {} não encontrada.", requestBody.getVantagemId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vantagem não encontrada.");
        }

        logger.debug("Verificando saldo do aluno.");
        Conta contaAluno = aluno.getConta();
        if (contaAluno.getSaldo() < vantagem.getCusto()) {
            logger.warn("Saldo insuficiente para aluno ID {}. Saldo atual: {}, Custo da vantagem: {}", 
                requestBody.getAlunoId(), contaAluno.getSaldo(), vantagem.getCusto());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Saldo insuficiente.");
        }

        logger.debug("Atualizando saldo do aluno ID {}.", requestBody.getAlunoId());
        contaAluno.setSaldo(contaAluno.getSaldo() - vantagem.getCusto());
        contaDAO.atualizar(contaAluno);

        logger.debug("Criando transação de resgate de vantagem.");
        Transacao transacao = new Transacao(
            vantagem.getCusto(),
            "Resgate de vantagem: " + vantagem.getDescricao(),
            aluno,
            null
        );
        transacaoDAO.salvar(transacao);

        logger.debug("Gerando código do cupom para a vantagem.");
        String codigoCupom = vantagem.gerarCodigo();

        logger.debug("Enviando e-mail com o cupom para o aluno ID {}.", requestBody.getAlunoId());
        vantagem.enviarEmailCupom(aluno, codigoCupom);

        logger.debug("Enviando e-mail de notificação para a empresa parceira.");
        EmpresaParceira empresaParceira = vantagem.getEmpresaParceira();
        if (empresaParceira != null) { 
            vantagem.enviarEmailParceiro(empresaParceira, codigoCupom);
        } else {
            logger.warn("Nenhuma empresa parceira associada à vantagem ID {}.", requestBody.getVantagemId());
        }

        logger.info("Resgate de vantagem concluído com sucesso para aluno ID {}.", requestBody.getAlunoId());
        return ResponseEntity.status(HttpStatus.CREATED).body("Vantagem resgatada com sucesso!");
    } catch (Exception e) {
        logger.error("Erro ao resgatar vantagem para aluno ID {}: {}", 
            requestBody.getAlunoId(), e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno no servidor.");
    }
}

    
    }
    

