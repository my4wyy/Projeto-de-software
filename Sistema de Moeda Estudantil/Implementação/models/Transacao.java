package br.com.demo.regescweb.models;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.Properties;
import javax.mail.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date data;
    private String tipo;
    private int quantidade;
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "origem_id")
    private Pessoa origem;

    @ManyToOne(optional = true)
    private Aluno destino;

    public Transacao(int quantidade, String descricao, Pessoa origem, Aluno destino) {
        this.data = new Date();
        this.tipo = "Transferência";
        this.quantidade = quantidade;
        this.descricao = descricao;
        this.origem = origem;
        this.destino = destino;
    }

    public void registrarTransacao() {
        if (origem instanceof Professor) {
            Professor professorOrigem = (Professor) origem;
            if (professorOrigem.getConta() != null) {
                professorOrigem.getConta().setSaldo(professorOrigem.getConta().getSaldo() - quantidade);
                professorOrigem.getConta().adicionarTransacao(this);
            }
        } else if (origem instanceof Aluno) {
            Aluno alunoOrigem = (Aluno) origem;
            if (alunoOrigem.getConta() != null) {
                alunoOrigem.getConta().setSaldo(alunoOrigem.getConta().getSaldo() - quantidade);
                alunoOrigem.getConta().adicionarTransacao(this);
            }
        }

        if (destino != null && destino.getConta() != null) {
            destino.getConta().setSaldo(destino.getConta().getSaldo() + quantidade);
            destino.getConta().adicionarTransacao(this);
        }
    }

    public void enviarEmailAluno(Aluno aluno, Professor professor, int quantidade, String motivo) {
        String destinatario = aluno.getEmail();
        String assunto = "Você recebeu moedas!";
        String corpo = "Olá " + aluno.getNome() + ",\n\n" +
                "Você recebeu " + quantidade + " moedas de " + professor.getNome() + ".\n" +
                "Motivo: " + motivo + "\n\n" +
                "Agora, seu saldo total é: " + aluno.getConta().getSaldo() + " moedas.\n\n" +
                "Atenciosamente,\nEquipe Moeda Estudantil";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "localhost");
        properties.put("mail.smtp.port", "25");
        properties.put("mail.smtp.auth", "false");
        properties.put("mail.smtp.starttls.enable", "false");

        Session session = Session.getInstance(properties, null);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("admMoedaEstudantil@email.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(assunto);
            message.setText(corpo);

            Transport.send(message);
            System.out.println("E-mail enviado para o aluno: " + destinatario);
        } catch (MessagingException e) {
            System.err.println("Falha ao enviar e-mail: " + e.getMessage());
        }
    }
}
