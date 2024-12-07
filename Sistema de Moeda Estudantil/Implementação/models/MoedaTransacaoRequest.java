package br.com.demo.regescweb.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoedaTransacaoRequest {
    private Long alunoId;
    private int quantidadeMoedas;
}
