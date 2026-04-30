package com.ada.emprestimo.dto;

import com.ada.emprestimo.model.TipoAmortizacao;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmprestimoRequest {

    @NotNull(message = "O campo clienteId é obrigatório e deve ser um UUID válido")
    private UUID clienteId;

    @NotNull(message = "O campo valorTotal é obrigatório")
    @DecimalMin(value = "100.00", message = "O valor total deve ser no mínimo R$ 100,00")
    @DecimalMax(value = "10000000.00", message = "O valor total deve ser no máximo R$ 10.000.000,00")
    private BigDecimal valorTotal;

    @NotNull(message = "O campo quantidadeParcelas é obrigatório")
    @Min(value = 1, message = "A quantidade de parcelas deve ser no mínimo 1 mês")
    @Max(value = 480, message = "A quantidade de parcelas deve ser no máximo 480 meses")
    private Integer quantidadeParcelas;

    @NotNull(message = "O campo tipoAmortizacao é obrigatório e deve ser SAC ou PRICE")
    private TipoAmortizacao tipoAmortizacao;
}
