package com.ada.cliente.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponse {
    private UUID id;
    private String nome;
    private String documento;
    private String cep;
    private String logradouro;
    private String bairro;
    private String numero;
    private String complemento;
}
