package com.ada.cliente.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequest {

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 4, max = 150, message = "O nome deve ter entre 4 e 150 caracteres")
    private String nome;

    @NotBlank(message = "O documento é obrigatório")
    @Pattern(regexp = "^[0-9]{11}$|^[0-9]{14}$", message = "O documento deve ter 11 dígitos (CPF) ou 14 dígitos (CNPJ)")
    private String documento;

    @NotBlank(message = "O CEP é obrigatório")
    @Pattern(regexp = "^[0-9]{8}$", message = "O CEP deve ter exatamente 8 dígitos numéricos")
    private String cep;

    @NotBlank(message = "O número é obrigatório")
    @Size(max = 20, message = "O número deve ter no máximo 20 caracteres")
    private String numero;

    @Size(max = 50, message = "O complemento deve ter no máximo 50 caracteres")
    private String complemento;
}
