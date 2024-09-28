package br.com.demo.regescweb.models;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date dataPedido;

    @ManyToOne
    @JoinColumn(name = "automovel_id")
    private Automovel automovel;

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

   
    public Pedido() {
    }

    public Pedido(Date dataPedido, Automovel automovel) {
        this.dataPedido = dataPedido;
        this.automovel = automovel;
        this.status = StatusPedido.PENDENTE; 
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(Date dataPedido) {
        this.dataPedido = dataPedido;
    }

    public Automovel getAutomovel() {
        return automovel;
    }

    public void setAutomovel(Automovel automovel) {
        this.automovel = automovel;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }
    
    public void avaliarPedido(Cliente cliente) {
        if (automovel != null) {
            automovel.alugar(cliente); 
            this.status = StatusPedido.APROVADO; 
    }
}
}
