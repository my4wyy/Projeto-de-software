package br.com.demo.regescweb.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Entity
public class Vantagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String descricao;

    @JsonProperty("foto")
    private String foto;
    
    @JsonProperty("custo")
    private int custo;

    @ManyToOne
    @JoinColumn(name = "empresa_parceira_id")  
    @JsonBackReference
    private EmpresaParceira empresaParceira;

    public Vantagem() {}

    public Vantagem(String descricao, String foto, int custo, EmpresaParceira empresaParceira) {
        this.descricao = descricao;
        this.foto = foto;
        this.custo = custo;
        this.empresaParceira = empresaParceira;
    }

    public String gerarCodigo() {
        return "VAN" + (int) (Math.random() * 10000);
    }

    public void enviarEmailCupom(Aluno aluno, String codigo) {
        String assunto = "Seu Cupom Está Aqui!";
        String corpo = "Olá " + aluno.getNome() + ",\n\nSeu código de cupom é: " + codigo + "\n\nAgora, seu saldo total é: " + aluno.getConta().getSaldo() + " moedas.\n\n" +
        "Atenciosamente,\nEquipe Moeda Estudantil";
        enviarEmail(aluno.getEmail(), assunto, corpo)
        ;
        
    }

    public void enviarEmailParceiro(EmpresaParceira parceiro, String codigo) {
        String assunto = "Uma Vantagem Foi resgatada!";
        String corpo = "Olá " + parceiro.getNome() + ",\n\nO código do cupom é: " + codigo;
        enviarEmail(parceiro.getEmail(), assunto, corpo);
    }

    private void enviarEmail(String destinatario, String assunto, String corpo) {
       
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
            System.out.println("E-mail enviado para: " + destinatario);
        } catch (MessagingException e) {
            System.err.println("Falha ao enviar e-mail: " + e.getMessage());
        }
    }
    
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getCusto() {
        return custo;
    }

    public void setCusto(int custo) {
        this.custo = custo;
    }

    public EmpresaParceira getEmpresaParceira() {
        return empresaParceira;
    }

    public void setEmpresaParceira(EmpresaParceira empresaParceira) {
        this.empresaParceira = empresaParceira;
    }
}
